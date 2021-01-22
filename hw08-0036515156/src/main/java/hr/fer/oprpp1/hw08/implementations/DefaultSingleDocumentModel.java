package hr.fer.oprpp1.hw08.implementations;

import java.nio.file.Path;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import hr.fer.oprpp1.hw08.interfaces.SingleDocumentListener;
import hr.fer.oprpp1.hw08.interfaces.SingleDocumentModel;

/**
 * Class DefaultSingleDocumentModel represents an implementation of a SingleDocumentModel with all functionalities
 * @author leokiparje
 *
 */

public class DefaultSingleDocumentModel implements SingleDocumentModel {
	
	/*
	 * Path of given document
	 */
	private Path path;
	
	/*
	 * Text editor of the document
	 */
	private JTextArea editor;
	
	/*
	 * Boolean value that represents if document is modified or not
	 */
	private boolean modified = false;
	
	/*
	 * List of document listeners
	 */
	private List<SingleDocumentListener> listeners = new ArrayList<>();
	
	/*
	 * Basic Constructor
	 */
	public DefaultSingleDocumentModel(Path path, String content) {
		this.path = path;
		this.editor = new JTextArea(content);
		
		this.editor.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void insertUpdate(DocumentEvent e) {
				update();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				update();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				update();
			}
			
		});
	}
	
	/*
	 * Method that calls the modificationNotify method and delegates that method job of informing listeners that change has been made.
	 * Method also sets document to be modified.
	 */
	private void update() {
		setModified(true);
		modificationNotify();
	}

	@Override
	public JTextArea getTextComponent() {
		return editor;
	}

	@Override
	public Path getFilePath() {
		return path;
	}

	@Override
	public void setFilePath(Path path) {
		
		Objects.requireNonNull(path, "Given file path can't be null.");
		this.path = path;
		
		setModified(false);
		modificationNotify();
		
		for (SingleDocumentListener l : listeners) {
			l.documentFilePathUpdated(this);
		}
	}

	@Override
	public boolean isModified() {
		return modified ? true : false;
	}

	@Override
	public void setModified(boolean modified) {
		this.modified = modified;
		modificationNotify();
	}

	@Override
	public void addSingleDocumentListener(SingleDocumentListener l) {
		listeners.add(l);
	}

	@Override
	public void removeSingleDocumentListener(SingleDocumentListener l) {
		listeners.remove(l);
	}

	/*
	 * Method notifies all the listeners of the change
	 */
	private void modificationNotify() {
		for (SingleDocumentListener l : listeners) {
			l.documentModifyStatusUpdated(this);
		}
	}
}

































