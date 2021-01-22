package hr.fer.oprpp1.hw08.interfaces;

/**
 * Interface That represents a multiple document listener 
 * @author leokiparje
 *
 */
public interface MultipleDocumentListener {
	
	/*
	 * Method is called when tab on tabbed pane changes
	 */
	void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel);
	
	/*
	 * Method is used when document is added
	 */
	void documentAdded(SingleDocumentModel model);
	
	/*
	 * Method is called when document is removed
	 */
	void documentRemoved(SingleDocumentModel model);
}