package hr.fer.oprpp1.hw08.interfaces;

import java.nio.file.Path;

/**
 * Interface MultipleDocumentModel represents and interface with methods used for tabbed pane model
 * @author leokiparje
 *
 */
public interface MultipleDocumentModel extends Iterable<SingleDocumentModel> {
	
	/*
	 * Method is called when new document is created
	 */
	SingleDocumentModel createNewDocument();
	
	/*
	 * Method returns currently present document tabbed pane
	 */
	SingleDocumentModel getCurrentDocument();
	
	/*
	 * Method is called when loading document from computer
	 */
	SingleDocumentModel loadDocument(Path path);
	
	/*
	 * Method is called when saving document
	 */
	void saveDocument(SingleDocumentModel model, Path newPath);
	
	/*
	 * Method is called when closing document
	 */
	void closeDocument(SingleDocumentModel model);
	
	/*
	 * Method registers a new MultipleDocumentListener 
	 */
	void addMultipleDocumentListener(MultipleDocumentListener l);
	
	/*
	 * Method removes a MultipleDocumentListener
	 */
	void removeMultipleDocumentListener(MultipleDocumentListener l);
	
	/*
	 * Method returns number of document in a tabbed pane
	 */
	int getNumberOfDocuments();
	
	/*
	 * Method returns document on the given index
	 */
	SingleDocumentModel getDocument(int index);
}
