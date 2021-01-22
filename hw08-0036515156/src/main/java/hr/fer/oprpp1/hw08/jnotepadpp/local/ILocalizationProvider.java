package hr.fer.oprpp1.hw08.jnotepadpp.local;

/**
 * Interface ILocalizationProvider represents a localization provider
 * @author leokiparje
 *
 */

public interface ILocalizationProvider {
	
	/*
	 * Method adds a new localization listener
	 */
	void addLocalizationListener(ILocalizationListener listener);
	
	/*
	 * Method removes a given localization listener
	 */
	void removeLocalizationListener(ILocalizationListener listener);
	
	/*
	 * Method returns String value of a given key
	 */
	String getString(String key);
	
	/*
	 * Method returns currently set language
	 */
	String getCurrentLanguage();
}
