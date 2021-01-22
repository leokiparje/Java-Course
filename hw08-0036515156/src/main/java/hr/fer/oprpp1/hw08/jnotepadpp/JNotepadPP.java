package hr.fer.oprpp1.hw08.jnotepadpp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.Collator;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import hr.fer.oprpp1.hw08.implementations.DefaultMultipleDocumentModel;
import hr.fer.oprpp1.hw08.interfaces.MultipleDocumentListener;
import hr.fer.oprpp1.hw08.interfaces.MultipleDocumentModel;
import hr.fer.oprpp1.hw08.interfaces.SingleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.local.FormLocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.local.ILocalizationListener;
import hr.fer.oprpp1.hw08.jnotepadpp.local.LocalizationProvider;

/**
 * Class JNotepadPP represents a notepad with all important functionalities. JNotepadPP has implemented a localization for three
 * languages, Croatian, English and German. It is implemented as a document with multitple tabbs which can be added or removed.
 * @author leokiparje
 *
 */

public class JNotepadPP extends JFrame {

	private static final long serialVersionUID = -2032486975952746222L;
	
	/*
	 * Model refrence of a notepad
	 */
	private MultipleDocumentModel notepad;
	
	/*
	 * Copied text
	 */
	private String copiedText;
	
	/*
	 * JPanel that represents statusBar shown on the bottom of the notepad
	 */
	private JPanel statusBar;
	
	/*
	 * TimerThread which represents a real time clock
	 */
	protected TimerThread timerThread;
	
	/*
	 * Actions used in a notepad, action name is self explanatory
	 */
	private Action exitAction;
	private Action openDocumentAction;
	private Action saveAction;
	private Action saveAsAction;
	private Action createNewDocumentAction;
	private Action removeDocumentAction;
	private Action copyAction;
	private Action cutAction;
	private Action pasteAction;
	private Action statisticalInfoAction;
	
	private Action hr;
	private Action en;
	private Action de;
	
	private Action toUpperCase;
	private Action toLowerCase;
	private Action invertCase;
	
	private Action ascendingAction;
	private Action descendingAction;
	
	private Action unique;
	
	/*
	 * JMenuBar represents a menu bar
	 */
	private JMenuBar menuBar;
	
	/*
	 * JMenu represents a tools menu
	 */
	private JMenu toolsMenu;
	
	/*
	 * Provider for localization
	 */
	private FormLocalizationProvider flp;
	
	/*
	 * Basic constructor
	 */
	public JNotepadPP() {
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setLocation(100,100);
		setSize(800,800);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				exitAction.actionPerformed(null);
			}
		});
		
		flp = new FormLocalizationProvider(LocalizationProvider.getInstance(), this);
		flp.addLocalizationListener(new ILocalizationListener() {
			
			@Override
			public void localizationChanged() {
				openDocumentAction.putValue(Action.NAME, flp.getString("open"));
				openDocumentAction.putValue(Action.SHORT_DESCRIPTION, flp.getString("openDesc"));
				saveAction.putValue(Action.NAME, flp.getString("save"));
				saveAction.putValue(Action.SHORT_DESCRIPTION, flp.getString("saveDesc"));
				saveAsAction.putValue(Action.NAME, flp.getString("saveAs"));
				saveAsAction.putValue(Action.SHORT_DESCRIPTION, flp.getString("saveAsDesc"));
				createNewDocumentAction.putValue(Action.NAME, flp.getString("create"));
				createNewDocumentAction.putValue(Action.SHORT_DESCRIPTION, flp.getString("createDesc"));
				removeDocumentAction.putValue(Action.NAME, flp.getString("remove"));
				removeDocumentAction.putValue(Action.SHORT_DESCRIPTION, flp.getString("removeDesc"));
				copyAction.putValue(Action.NAME, flp.getString("copy"));
				copyAction.putValue(Action.SHORT_DESCRIPTION, flp.getString("copyDesc"));
				cutAction.putValue(Action.NAME, flp.getString("cut"));
				cutAction.putValue(Action.SHORT_DESCRIPTION, flp.getString("cutDesc"));
				pasteAction.putValue(Action.NAME, flp.getString("paste"));
				pasteAction.putValue(Action.SHORT_DESCRIPTION, "pasteDesc");
				statisticalInfoAction.putValue(Action.NAME, "info");
				statisticalInfoAction.putValue(Action.SHORT_DESCRIPTION, "infoDesc");
				
				JMenu file = (JMenu) menuBar.getComponents()[0];
				JMenu language = (JMenu) menuBar.getComponents()[1];
				JMenu tools = (JMenu) menuBar.getComponents()[2];
				file.setText(flp.getString("file"));
				language.setText(flp.getString("language"));
				tools.setText(flp.getString("tools"));
				
				JMenu changeCase = (JMenu) tools.getMenuComponent(0);
				changeCase.setText(flp.getString("changeCase"));
				
				toUpperCase.putValue(Action.NAME, flp.getString("upperCase"));
				toUpperCase.putValue(Action.SHORT_DESCRIPTION, flp.getString("upperCaseDesc"));
				toLowerCase.putValue(Action.NAME, flp.getString("lowerCase"));
				toLowerCase.putValue(Action.SHORT_DESCRIPTION, flp.getString("lowerCaseDesc"));
				invertCase.putValue(Action.NAME, flp.getString("invertCase"));
				invertCase.putValue(Action.SHORT_DESCRIPTION, flp.getString("invertCaseDesc"));
				
				JMenu sort = (JMenu) tools.getMenuComponent(1);
				sort.setText(flp.getString("sort"));
				
				ascendingAction.putValue(Action.NAME, flp.getString("ascending"));
				ascendingAction.putValue(Action.SHORT_DESCRIPTION, flp.getString("ascendingDesc"));
				descendingAction.putValue(Action.NAME, flp.getString("descending"));
				descendingAction.putValue(Action.SHORT_DESCRIPTION, flp.getString("descendingDesc"));
				
				unique.putValue(Action.NAME, flp.getString("unique"));
				unique.putValue(Action.SHORT_DESCRIPTION, flp.getString("uniqueDesc"));
				
				statusBarUpdated(flp.getString("length"), flp.getString("ln"), flp.getString("col"), flp.getString("sel"));
				
			}
		});
		
		initGUI();
	}
	
	/*
	 * Method called to initialize gui elements of the notepad
	 */
	private void initGUI() {
		
		setTitle("(unnamed) - JNotepad++");
		
		DefaultMultipleDocumentModel tabbedNotepad = new DefaultMultipleDocumentModel();
		notepad = tabbedNotepad;
		
		JLabel statusBarLeft = new JLabel(" length : 0                      ");	
		JLabel statusBarRight = new JLabel(" Ln : 1    Col : 1    Sel : 0");
		
		statusBar = new JPanel(new BorderLayout());
        statusBar.setPreferredSize(new Dimension(getWidth(), 25));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEADING, 5, 3));
        leftPanel.setOpaque(false);
        statusBar.add(leftPanel, BorderLayout.WEST);
        
        leftPanel.add(statusBarLeft);
        leftPanel.add(new SeparatorPanel(Color.GRAY, Color.WHITE));
        leftPanel.add(statusBarRight);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING, 5, 3));
        rightPanel.setOpaque(false);
        statusBar.add(rightPanel, BorderLayout.EAST);
        
        rightPanel.add(new SeparatorPanel(Color.GRAY, Color.white));
        
        final JLabel dateLabel = new JLabel();
        dateLabel.setHorizontalAlignment(JLabel.CENTER);
        rightPanel.add(dateLabel);

        final JLabel timeLabel = new JLabel();
        timeLabel.setHorizontalAlignment(JLabel.CENTER);
        rightPanel.add(timeLabel);
        
        timerThread = new TimerThread(dateLabel, timeLabel);
        timerThread.start();
        
        statusBar.setBorder(BorderFactory.createLineBorder(Color.black));
        
		getContentPane().setLayout(new BorderLayout());
		add(new JScrollPane(tabbedNotepad), BorderLayout.CENTER);
		add(statusBar, BorderLayout.SOUTH);
		
		notepad.createNewDocument();
		notepad.getCurrentDocument().getTextComponent().addCaretListener(new CaretListener() {
			
			@Override
			public void caretUpdate(CaretEvent e) {
				statusBarUpdated(flp.getString("length"), flp.getString("ln"), flp.getString("col"), flp.getString("sel"));
				changeToolsEnabled();
			}
		});
		
		createActions();
		initializeActions();
		createMenus();
		createToolbars();
		
		notepad.addMultipleDocumentListener(new MultipleDocumentListener() {
			
			@Override
			public void documentRemoved(SingleDocumentModel model) {
				statusBarUpdated(flp.getString("length"), flp.getString("ln"), flp.getString("col"), flp.getString("sel"));
				changeToolsEnabled();
			}
			
			@Override
			public void documentAdded(SingleDocumentModel model) {
				
				statusBarUpdated(flp.getString("length"), flp.getString("ln"), flp.getString("col"), flp.getString("sel"));
				changeToolsEnabled();
				model.getTextComponent().addCaretListener(new CaretListener() {
					
					@Override
					public void caretUpdate(CaretEvent e) {						
						statusBarUpdated(flp.getString("length"), flp.getString("ln"), flp.getString("col"), flp.getString("sel"));
						changeToolsEnabled();
					}
				});
				
				String filePath = notepad.getCurrentDocument().getFilePath()==null
						? "(unnamed)"
						: notepad.getCurrentDocument().getFilePath().toString();
				setTitle(filePath+" - JNotepadPP");
			}
			
			@Override
			public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
				
				
				changeToolsEnabled();
				statusBarUpdated(flp.getString("length"), flp.getString("ln"), flp.getString("col"), flp.getString("sel"));
				
				String filePath = currentModel.getFilePath()==null
						? "(unnamed)"
						: currentModel.getFilePath().toString();
				setTitle(filePath+" - JNotepadPP");		
				
			}
		});
		
	}

	/*
	 * Method createActions creates actions
	 */
	private void createActions() {
		
		exitAction = new AbstractAction() {
			
			private static final long serialVersionUID = 1L;
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				for (SingleDocumentModel document : notepad) {
					
					if (document.isModified()) {
						
						Object[] options = {flp.getString("optionYes"), flp.getString("optionNo"), flp.getString("optionCancel")};
						String question = flp.getString("fileNotSaved");
						String questionName = flp.getString("fileNotSavedTitle");
						
						int selected = JOptionPane.showOptionDialog(JNotepadPP.this,
								question,
								questionName,
								JOptionPane.YES_NO_CANCEL_OPTION,
								JOptionPane.WARNING_MESSAGE,
								null,
								options,
								options[0]
								);
						if (selected==JOptionPane.CANCEL_OPTION) {
							return;
						}else if(selected==JOptionPane.NO_OPTION) {
							continue;
						}else if (selected==JOptionPane.YES_OPTION) {
							
							if (document.getFilePath()==null) {							
								saveDialog(document);								
							}else {
								notepad.saveDocument(document, document.getFilePath());
							}
							
						}
						
					}
					
				}
				timerThread.setRunning(false);
				dispose();
			}
		};
		
		createNewDocumentAction = new AbstractAction() {

			private static final long serialVersionUID = 3985269009833090130L;

			@Override
			public void actionPerformed(ActionEvent e) {
				notepad.createNewDocument();
			}
			
		};
		
		removeDocumentAction = new AbstractAction() {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
			
				SingleDocumentModel current = notepad.getCurrentDocument();
				
				if (current.isModified()) {
					
					Object[] options = {flp.getString("optionYes"), flp.getString("optionNo")};
					String question = flp.getString("fileNotSaved");
					String questionName = flp.getString("fileNotSavedTitle");
					
					int selected = JOptionPane.showOptionDialog(JNotepadPP.this,
							question,
							questionName,
							JOptionPane.YES_NO_OPTION,
							JOptionPane.WARNING_MESSAGE,
							null,
							options,
							options[0]
							);
					if (selected==JOptionPane.YES_OPTION) {
						saveAction.actionPerformed(null);
					}
					
				}
				
				notepad.closeDocument(notepad.getCurrentDocument());
				
			}
			
		};
		
		saveAction = new AbstractAction() {

			private static final long serialVersionUID = 4162264944873895169L;

			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (notepad.getCurrentDocument().getFilePath()==null) {
					saveAsAction.actionPerformed(null);
					return;
				}
				
				SingleDocumentModel document = notepad.getCurrentDocument();
				
				notepad.saveDocument(document, document.getFilePath());
				setTitle(document.getFilePath().toString()+" - JNotepadPP");
				document.setModified(false);
			}
			
		};
		
		saveAsAction = new AbstractAction() {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				
				saveDialog(notepad.getCurrentDocument());
				String title = notepad.getCurrentDocument().getFilePath()==null ? "(unnamed)" : notepad.getCurrentDocument().getFilePath().toString();
				setTitle(title+" - JNotepadPP");
			}
			
		};
		
		openDocumentAction = new AbstractAction() {

			private static final long serialVersionUID = -1837268006030176790L;

			@Override
			public void actionPerformed(ActionEvent e) {
				
				JFileChooser fc = new JFileChooser();
				fc.setDialogTitle("Open file");
				if(fc.showOpenDialog(JNotepadPP.this)!=JFileChooser.APPROVE_OPTION) {
					return;
				}
				File fileName = fc.getSelectedFile();
				Path filePath = fileName.toPath();
				
				String notExist = flp.getString("notExist");
				String er = flp.getString("err");
				
				if(!Files.isReadable(filePath)) {
					JOptionPane.showMessageDialog(
							JNotepadPP.this, 
							notExist, 
							er, 
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				notepad.loadDocument(filePath);
				
			}
			
		}; 
		
		copyAction = new AbstractAction() {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				
				JTextArea editor = notepad.getCurrentDocument().getTextComponent();
				Document doc = editor.getDocument();
				int len = Math.abs(editor.getCaret().getDot()-editor.getCaret().getMark());
				if(len==0) return;
				int offset = Math.min(editor.getCaret().getDot(),editor.getCaret().getMark());
				
				try {
					copiedText = doc.getText(offset, len);
				} catch (BadLocationException e1) {
					e1.printStackTrace();
				}
				
			}
			
		};
		
		cutAction = new AbstractAction() {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				
				JTextArea editor = notepad.getCurrentDocument().getTextComponent();
				Document doc = editor.getDocument();
				int len = Math.abs(editor.getCaret().getDot()-editor.getCaret().getMark());
				if(len==0) return;
				int offset = Math.min(editor.getCaret().getDot(),editor.getCaret().getMark());
				
				try {
					copiedText = doc.getText(offset, len);
					doc.remove(offset, len);
				} catch (BadLocationException e1) {
					e1.printStackTrace();
				}
				
			}
			
		};
		
		pasteAction = new AbstractAction() {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				
				JTextArea editor = notepad.getCurrentDocument().getTextComponent();
				Document doc = editor.getDocument();
				
				try {
					doc.insertString(editor.getCaretPosition(), copiedText, null);
				} catch (BadLocationException e1) {
					e1.printStackTrace();
				}
				
			}
			
		};
		
		statisticalInfoAction = new AbstractAction() {

			private static final long serialVersionUID = -7901225759962283246L;

			@Override
			public void actionPerformed(ActionEvent e) {
				
				String content = notepad.getCurrentDocument().getTextComponent().getText();
				
				int numOfCharacters = content.length();
				
				int count = 0;
				for (int i=0;i<numOfCharacters;i++) {
					if (content.charAt(i)==' ' || content.charAt(i)=='\n' || content.charAt(i)=='\t') {
						continue;
					}
					count++;
				}
				
				String[] lines = content.split("\n");
				int numOfLines = lines.length;
				
				String info1 = flp.getString("info1");
				String info2 = flp.getString("info2");
				String info3 = flp.getString("info3");
				
				JOptionPane.showMessageDialog(
						JNotepadPP.this, 
						info1+" : "+numOfLines+"\n"+info2+" : "+numOfCharacters+"\n"+info3+" : "+count,
						"Info", 
						JOptionPane.INFORMATION_MESSAGE);
				
			}
			
		};
		
		hr = new AbstractAction() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				LocalizationProvider.getInstance().setLanguage("hr");
			}
		};
		
		en = new AbstractAction() {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				LocalizationProvider.getInstance().setLanguage("en");
			}
		};
		
		de = new AbstractAction() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				LocalizationProvider.getInstance().setLanguage("de");
			}
		};
		
		toUpperCase = new AbstractAction() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				
				JTextArea editor = notepad.getCurrentDocument().getTextComponent();				
				String selected = editor.getSelectedText();				
				editor.replaceSelection(selected.toUpperCase());
				
			}
		};
		
		toLowerCase = new AbstractAction() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				
				JTextArea editor = notepad.getCurrentDocument().getTextComponent();
				String selected = editor.getSelectedText();
				editor.replaceSelection(selected.toLowerCase());
				
			}
		};
		
		invertCase = new AbstractAction() {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				
				JTextArea editor = notepad.getCurrentDocument().getTextComponent();
				String selected = editor.getSelectedText();
				
				StringBuilder sb = new StringBuilder();
				
				for (int i=0;i<selected.length();i++) {
					if (Character.isUpperCase(selected.charAt(i))) {
						sb.append(Character.toLowerCase(selected.charAt(i)));
					}else if(Character.isLowerCase(selected.charAt(i))) {
						sb.append(Character.toUpperCase(selected.charAt(i)));
					}else {
						sb.append(selected.charAt(i));
					}
				}
				editor.replaceSelection(sb.toString());
				
			}
		};
		
		ascendingAction = new AbstractAction() {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				
				sort(true);
				
			}
		};
		
		descendingAction = new AbstractAction() {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				
				sort(false);
				
			}
		};
		
		unique = new AbstractAction() {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				
				JTextArea editor = notepad.getCurrentDocument().getTextComponent();
				int endPosition = editor.getSelectionEnd();
				int startPosition = editor.getSelectionStart();	
				
				Document document = editor.getDocument();
				
				int lineStart = 0;
				int lineEnd = 0;
				try {
					lineStart = editor.getLineOfOffset(startPosition);
					lineEnd = editor.getLineOfOffset(endPosition);
				}catch(Exception ex) {
					ex.printStackTrace();
				}
				
				String[] lines = editor.getText().split("\n|\r");
				
				StringBuilder sb = new StringBuilder();
				
				if (lineEnd>0 && document.getEndPosition().getOffset()==(editor.getCaret().getDot()+1)) lineEnd--;
				
				for (int i=lineStart;i<lineEnd;i++) {
					if (lines[i]==null) continue;
					String original = lines[i];
					for (int j=i+1;j<lineEnd;j++) {
						if (lines[j]==null) continue;
						if (original.equals(lines[j])) {
							lines[j]=null;
						}
					}
				}
				String original = lines[lineEnd];
				for (int i=lineStart;i<lineEnd;i++) {
					if (lines[i]==null) continue;
					if (lines[i].equals(original)) {
						lines[lineEnd] = null;
						break;
					}
				}
				
				for (int i=lineStart;i<lineStart+lines.length;i++) {
					if (lines[i]!=null) {
						sb.append(lines[i]);
						sb.append("\n");
					}
				}
				sb.setLength(sb.length()-1);
				
				try {
					editor.replaceRange(sb.toString(), editor.getLineStartOffset(lineStart), editor.getLineEndOffset(lineEnd));
				}catch(Exception ex) {
					ex.printStackTrace();
				}
				
			}
		};
		
	}
	
	/*
	 * Method statusBarUpdated is called whenever statusBar is updated
	 */
	private void statusBarUpdated(String length, String ln, String col, String sel) {
		
		JPanel left  = (JPanel) statusBar.getComponents()[0];
		
		JLabel leftLabel = (JLabel) left.getComponents()[0];
		JLabel rightLabel = (JLabel) left.getComponents()[2];
		
		JTextArea editor = notepad.getCurrentDocument().getTextComponent();
		
		int line = 0;
		int column = 0;
		
		int selected = editor.getSelectedText()==null ? 0 : editor.getSelectedText().length();
		
		try {
			int caretPosition = editor.getCaretPosition();
			line = editor.getLineOfOffset(caretPosition);
			column = caretPosition - editor.getLineStartOffset(line);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		leftLabel.setText(" "+length+" : "+editor.getText().length()+"                      ");
		rightLabel.setText(" "+ln+" : "+(line+1)+"    "+col+" : "+(column+1)+"    "+sel+" : "+selected);
	}
	
	/*
	 * Method is called whenever a selection in notepad is made to signal the tools menu items to be enabled for use
	 */
	private void changeToolsEnabled() {
		
		JTextArea editor = notepad.getCurrentDocument().getTextComponent();

		int selected = editor.getSelectedText()==null ? 0 : editor.getSelectedText().length();
		
		if (selected>0) {
			toolsMenu.getMenuComponent(0).setEnabled(true);
			toolsMenu.getMenuComponent(1).setEnabled(true);
			toolsMenu.getMenuComponent(2).setEnabled(true);
		}else {
			toolsMenu.getMenuComponent(0).setEnabled(false);
			toolsMenu.getMenuComponent(1).setEnabled(false);
			toolsMenu.getMenuComponent(2).setEnabled(false);
		}
		
	}
	
	/*
	 * Method is called whenever a save operation is called
	 */
	private void saveDialog(SingleDocumentModel model) {
		
		String message = flp.getString("notSaved");
		String warning = flp.getString("warning");
		
		JFileChooser fc = new JFileChooser();
		fc.setDialogTitle("Save file");
		if(fc.showSaveDialog(JNotepadPP.this)!=JFileChooser.APPROVE_OPTION) {
			JOptionPane.showMessageDialog(JNotepadPP.this,
					message,
					warning,
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		File directory = fc.getSelectedFile();
		Path directoryPath = directory.toPath();
		
		for (SingleDocumentModel doc : notepad) {
			if (doc.getFilePath()==null) continue;
			if (doc.getFilePath().equals(notepad.getCurrentDocument().getFilePath())) {
				JOptionPane.showMessageDialog(JNotepadPP.this,
						flp.getString("alreadyOpened"), 
						flp.getString("warning"), JOptionPane.WARNING_MESSAGE);
				return;
			}
		}
		
		if (Files.exists(directoryPath)) {
			
			Object[] options = {flp.getString("optionYes"), flp.getString("optionNo")};
			String question = flp.getString("override");
			String questionName = flp.getString("saveAs");
			
			int selected = JOptionPane.showOptionDialog(JNotepadPP.this,
					question,
					questionName,
					JOptionPane.YES_NO_OPTION,
					JOptionPane.WARNING_MESSAGE,
					null,
					options,
					options[0]
					);
			if(selected==JOptionPane.NO_OPTION) return;
		}
		notepad.saveDocument(model, directoryPath);
		JOptionPane.showMessageDialog(JNotepadPP.this,
				flp.getString("overrideDone"),
				"Info",
				JOptionPane.INFORMATION_MESSAGE);
		
	}
	
	/*
	 * Method is called for sorting selected text ascendingly or descendingly
	 */
	private void sort(boolean ascending) {
		
		JTextArea editor = notepad.getCurrentDocument().getTextComponent();
		Document document = editor.getDocument();
		int endPosition = editor.getSelectionEnd();
		int startPosition = editor.getSelectionStart();				
		
		int lineStart = 0;
		int lineEnd = 0;
		try {
			lineStart = editor.getLineOfOffset(startPosition);
			lineEnd = editor.getLineOfOffset(endPosition);
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		String[] lines = editor.getText().split("\n|\r");
		
		Collator collator = Collator.getInstance(new Locale(flp.getCurrentLanguage()));
		
		StringBuilder sb = new StringBuilder();
		
		if (lineEnd>0 && document.getEndPosition().getOffset()==(editor.getCaret().getDot()+1)) lineEnd--;
		
		for (int i=lineStart;i<=lineEnd;i++) {
			
			List<String> list = Arrays.asList(lines[i].split("\\s+"));
			if (ascending) {
				list.sort(collator);
			}else {
				list.sort(collator.reversed());
			}
			for (String s : list) {
				sb.append(s+" ");
			}
			sb.setLength(sb.length()-1);
			sb.append("\n");
		}
		try {
			editor.replaceRange(sb.toString(), editor.getLineStartOffset(lineStart), editor.getLineEndOffset(lineEnd));
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
	}
	
	/*
	 * Method is called after createActions method to initialize actions
	 */
	private void initializeActions() {
		
		exitAction.putValue(Action.NAME, "Exit");
		exitAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control shift X"));
		exitAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_X); 
		exitAction.putValue(Action.SHORT_DESCRIPTION, "Exit application."); 
		
		openDocumentAction.putValue(Action.NAME, "Open");
		openDocumentAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control O")); 
		openDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_O); 
		openDocumentAction.putValue(Action.SHORT_DESCRIPTION, "Used to open existing file from disk");
		
		createNewDocumentAction.putValue(Action.NAME, "Create new document");
		createNewDocumentAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control shift C"));
		createNewDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_1);
		createNewDocumentAction.putValue(Action.SHORT_DESCRIPTION, "Creates new document");
		
		saveAction.putValue(Action.NAME, "Save");
		saveAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control S"));
		saveAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_3);
		saveAction.putValue(Action.SHORT_DESCRIPTION, "Saves document");
		
		saveAsAction.putValue(Action.NAME, "Save as");
		saveAsAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control shift S"));
		saveAsAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_2);
		saveAsAction.putValue(Action.SHORT_DESCRIPTION, "Saves document to the chosen location");
		
		removeDocumentAction.putValue(Action.NAME, "Remove document");
		removeDocumentAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control R"));
		removeDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_3);
		removeDocumentAction.putValue(Action.SHORT_DESCRIPTION, "Removes the current document");
		
		copyAction.putValue(Action.NAME, "Copy");
		copyAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control C"));
		copyAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_4);
		copyAction.putValue(Action.SHORT_DESCRIPTION, "Copy selected text");
		
		cutAction.putValue(Action.NAME, "Cut");
		cutAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control X"));
		cutAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_5);
		cutAction.putValue(Action.SHORT_DESCRIPTION, "Cut selected text");
		
		pasteAction.putValue(Action.NAME, "Paste");
		pasteAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control V"));
		pasteAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_6);
		pasteAction.putValue(Action.SHORT_DESCRIPTION, "Paste text");
		
		statisticalInfoAction.putValue(Action.NAME, "Info");
		statisticalInfoAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control I"));
		statisticalInfoAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_7);
		statisticalInfoAction.putValue(Action.SHORT_DESCRIPTION, "Info about the document");
		
		hr.putValue(Action.NAME, "Hrvatski");
		hr.putValue(Action.SHORT_DESCRIPTION, "Promijena jezika u hrvatski");
		
		en.putValue(Action.NAME, "English");
		en.putValue(Action.SHORT_DESCRIPTION, "Change language to english");
		
		de.putValue(Action.NAME, "Deutsche");
		de.putValue(Action.SHORT_DESCRIPTION, "Sprache auf Deutsch ändern");
		
		toUpperCase.putValue(Action.NAME, "to uppercase");
		toUpperCase.putValue(Action.SHORT_DESCRIPTION, "Change selected text to uppercase");
		
		toLowerCase.putValue(Action.NAME, "to lowercase");
		toLowerCase.putValue(Action.SHORT_DESCRIPTION, "Change slected text to lowercase");
		
		invertCase.putValue(Action.NAME, "invert case");
		invertCase.putValue(Action.SHORT_DESCRIPTION, "Change case of selected text");
		
		ascendingAction.putValue(Action.NAME, "ascending sort");
		ascendingAction.putValue(Action.SHORT_DESCRIPTION, "Sort the selected text ascendingly");
		
		descendingAction.putValue(Action.NAME, "descending sort");
		descendingAction.putValue(Action.SHORT_DESCRIPTION, "Sort the sleceted text descendingly");
		
		unique.putValue(Action.NAME, "unique");
		unique.putValue(Action.SHORT_DESCRIPTION, "Leaves only first occurrrence of a line if there is more");
		
	}
	
	/*
	 * Method is called to create menus of a notepad
	 */
	private void createMenus() {
		menuBar = new JMenuBar();
		
		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);

		fileMenu.add(new JMenuItem(openDocumentAction));
		fileMenu.add(new JMenuItem(createNewDocumentAction));
		fileMenu.add(new JMenuItem(saveAction));
		fileMenu.add(new JMenuItem(saveAsAction));
		fileMenu.add(new JMenuItem(removeDocumentAction));
		fileMenu.addSeparator();
		fileMenu.add(new JMenuItem(copyAction));
		fileMenu.add(new JMenuItem(cutAction));
		fileMenu.add(new JMenuItem(pasteAction));
		fileMenu.addSeparator();
		fileMenu.add(new JMenuItem(statisticalInfoAction));
		
		JMenu languageMenu = new JMenu("Language");
		menuBar.add(languageMenu);
		
		languageMenu.add(hr);
		languageMenu.add(en);
		languageMenu.add(de);
		
		toolsMenu = new JMenu("Tools");
		menuBar.add(toolsMenu);
		
		JMenu changeCase = new JMenu("Change case");
		changeCase.add(new JMenuItem(toUpperCase));
		changeCase.add(new JMenuItem(toLowerCase));
		changeCase.add(new JMenuItem(invertCase));
		changeCase.setEnabled(false);
		
		toolsMenu.add(changeCase);
		
		JMenu sort = new JMenu("Sort");
		sort.add(new JMenuItem(ascendingAction));
		sort.add(new JMenuItem(descendingAction));
		sort.setEnabled(false);
		
		toolsMenu.add(sort);
		
		JMenuItem uniqueItem = new JMenuItem(unique);
		toolsMenu.add(uniqueItem);
		uniqueItem.setEnabled(false);
		
		this.setJMenuBar(menuBar);
	}
	
	/*
	 * Method is called to create toolbars
	 */
	private void createToolbars() {
		JToolBar toolBar = new JToolBar("Alati");
		toolBar.setFloatable(true);
		
		toolBar.add(new JButton(openDocumentAction));
		toolBar.add(new JButton(createNewDocumentAction));
		toolBar.add(new JButton(saveAction));
		toolBar.add(new JButton(saveAsAction));
		toolBar.add(new JButton(removeDocumentAction));
		toolBar.addSeparator();
		toolBar.add(new JButton(copyAction));
		toolBar.add(new JButton(cutAction));
		toolBar.add(new JButton(pasteAction));
		toolBar.addSeparator();
		toolBar.add(new JButton(statisticalInfoAction));
		
		this.getContentPane().add(toolBar, BorderLayout.PAGE_START);
	}
	
	/*
	 * Method main
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new JNotepadPP().setVisible(true);
			}
		});
	}
}




































































































































































