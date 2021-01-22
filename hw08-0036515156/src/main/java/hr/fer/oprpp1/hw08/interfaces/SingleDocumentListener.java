package hr.fer.oprpp1.hw08.interfaces;

/**
 * Interface SingleDocumentListener represents one listener on a single document
 * @author leokiparje
 *
 */
public interface SingleDocumentListener {
	
	/*
	 * Method is called when a modification in document has been made
	 */
	void documentModifyStatusUpdated(SingleDocumentModel model);
	
	/*
	 * Method is called when file path is updated
	 */
	void documentFilePathUpdated(SingleDocumentModel model);
}
