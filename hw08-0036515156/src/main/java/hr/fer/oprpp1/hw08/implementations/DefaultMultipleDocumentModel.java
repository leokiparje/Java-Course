package hr.fer.oprpp1.hw08.implementations;

import java.io.InputStream;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import hr.fer.oprpp1.hw08.interfaces.MultipleDocumentListener;
import hr.fer.oprpp1.hw08.interfaces.MultipleDocumentModel;
import hr.fer.oprpp1.hw08.interfaces.SingleDocumentListener;
import hr.fer.oprpp1.hw08.interfaces.SingleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.JNotepadPP;

/**
 * Class DefaultMultipleDocumentModel represents an implementation of MultiPLeDocumentModel interface with all functionalities
 * @author leokiparje
 *
 */

public class DefaultMultipleDocumentModel extends JTabbedPane implements MultipleDocumentModel {

	private static final long serialVersionUID = -3314291155798460887L;
	
	/*
	 * List of SingleDocumentModels
	 */
	private List<SingleDocumentModel> models;
	
	/*
	 * Current SingleDocumentModel available in the notepad
	 */
	private SingleDocumentModel current;
	
	/*
	 * List of MultipleDocumentModel listeners
	 */
	private List<MultipleDocumentListener> listeners;
	
	/*
	 * Basic constructor
	 */
	public DefaultMultipleDocumentModel() {
		models = new ArrayList<>();
		listeners = new ArrayList<>();
		this.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		addChangeListener(l -> {
			notifyDocumentChanged(current, models.get(getSelectedIndex()));
		});
	}

	@Override
	public Iterator<SingleDocumentModel> iterator() {
		return models.iterator();
	}

	@Override
	public SingleDocumentModel createNewDocument() {
		return addNewDocument(null, "");
	}

	@Override
	public SingleDocumentModel getCurrentDocument() {
		return current;
	}

	@Override
	public SingleDocumentModel loadDocument(Path path) {
		
		Objects.requireNonNull(path, "Unable to load document because path is null.");
		
		for (SingleDocumentModel document : models) {
			if (document.getFilePath()==null) continue;
			if (document.getFilePath().equals(path)) {
				notifyDocumentChanged(current, document);
				return document;
			}
		}
		String content = null;
		try {
			content = Files.readString(path);
		}catch(Exception e) {
			System.out.println("Unable to read from file with given path : "+path.toString());
		}
		return addNewDocument(path, content);
	}

	@Override
	public void saveDocument(SingleDocumentModel model, Path newPath) {
		
		notifyDocumentChanged(current, model);
		
		for (SingleDocumentModel document : models) {
			if (model.equals(document) || document.getFilePath()==null) continue;
			if (document.getFilePath().equals(newPath)) {
				throw new RuntimeException("File with specified path is already opened.");
			}
		}
		
		if (newPath==null) {
			newPath = model.getFilePath();
		}else {
			current.setFilePath(newPath);
		}
		
		try {
			Files.write(newPath, model.getTextComponent().getText().getBytes());
		}catch(Exception e) {
			System.out.println("Unable to write new file.");
		}
		
	}

	@Override
	public void closeDocument(SingleDocumentModel model) {
		if (models.size()==1) {
			createNewDocument();
		}
		int index = models.indexOf(model)>0 ? models.indexOf(model)-1 : 0;
		setSelectedIndex(index);
		removeTabAt(models.indexOf(model));
		models.remove(model);
		current = models.get(index);
		notifyDocumentRemoved(model);
	}

	@Override
	public void addMultipleDocumentListener(MultipleDocumentListener l) {
		listeners.add(l);
	}

	@Override
	public void removeMultipleDocumentListener(MultipleDocumentListener l) {
		listeners.remove(l);
	}

	@Override
	public int getNumberOfDocuments() {
		return models.size();
	}

	@Override
	public SingleDocumentModel getDocument(int index) {
		return models.get(index);
	}
	
	/*
	 * Private method for adding a new document with given path
	 */
	private SingleDocumentModel addNewDocument(Path path, String content) {
		SingleDocumentModel document = new DefaultSingleDocumentModel(path, content);
		models.add(document);
		notifyNewDocument(document);
		notifyDocumentChanged(current, document);
		document.addSingleDocumentListener(new SingleDocumentListener() {
			
			@Override
			public void documentModifyStatusUpdated(SingleDocumentModel model) {
				changeIcon(model);
			}
			
			@Override
			public void documentFilePathUpdated(SingleDocumentModel model) {
				changeTitle(model);
				changeToolTip(model);
			}
		});
		addNewTab(document);
		return document;
	}
	
	/*
	 * Method notifies all document listeners that a new document was added
	 */
	private void notifyNewDocument(SingleDocumentModel model) {
		for (MultipleDocumentListener l : listeners) {
			l.documentAdded(model);
		}
	}
	
	/*
	 * Method notifies all documents that a document has changed
	 */
	private void notifyDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
		current = currentModel;
		for (MultipleDocumentListener l : listeners) {
			l.currentDocumentChanged(previousModel, currentModel);
		}
	}
	
	/*
	 * Method notifies all documents that a document was removed
	 */
	private void notifyDocumentRemoved(SingleDocumentModel model) {
		for (MultipleDocumentListener l : listeners) {
			l.documentRemoved(model);
		}
	}
	
	/*
	 * Method creates an icon
	 */
	private ImageIcon createIcon(String path) {
		byte[] bytes = null;
		try(InputStream is = JNotepadPP.class.getResourceAsStream(path)){
			if (is==null) throw new IllegalArgumentException("Wrong path to the image given.");
			bytes = is.readAllBytes();
		}catch(Exception e) {
			System.out.println(e);
		}
		return new ImageIcon(bytes);
	}
	
	/*
	 * Method changes title of a document
	 */
	private void changeTitle(SingleDocumentModel model) {
		setTitleAt(models.indexOf(model), model.getFilePath().getFileName().toString());
	}
	
	/*
	 * Method changes tooltup of a document
	 */
	private void changeToolTip(SingleDocumentModel model) {
		setToolTipTextAt(models.indexOf(model), model.getFilePath().toString());
	}
	
	/*
	 * Method changes icon of a document
	 */
	private void changeIcon(SingleDocumentModel model) {
		if (model.isModified()) {
			setIconAt(models.indexOf(model), createIcon("icons/red.png"));
		}else {
			setIconAt(models.indexOf(model), createIcon("icons/green.png"));
		}
	}
	
	/*
	 * Method adds a new tab to the tabbed pane
	 */
	private void addNewTab(SingleDocumentModel model) {
		String title = model.getFilePath()==null ? "unnamed" : model.getFilePath().getFileName().toString();
		ImageIcon icon = createIcon("icons/green.png");
		JScrollPane component = new JScrollPane(model.getTextComponent());
		String toolTip = model.getFilePath()==null ? "unnamed" : model.getFilePath().toAbsolutePath().toString();
		addTab(title, icon, component, toolTip);
		setSelectedComponent(component);
	}
}
































































































