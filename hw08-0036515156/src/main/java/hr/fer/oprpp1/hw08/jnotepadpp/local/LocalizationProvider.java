package hr.fer.oprpp1.hw08.jnotepadpp.local;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * LocalizationProvider represents a localization provider
 * @author leokiparje
 *
 */

public class LocalizationProvider extends AbstractLocalizationProvider {
	
	/*
	 * String language represents language
	 */
	private String language;
	
	/*
	 * ResourceBundle represents a bundle
	 */
	private ResourceBundle bundle;
	
	/*
	 * Static refrence to a singleton provider
	 */
	private static LocalizationProvider provider;
	
	/*
	 * Basic constructor
	 */
	private LocalizationProvider() {
		language = "en";
		bundle = ResourceBundle.getBundle("hr.fer.oprpp1.hw08.jnotepadpp.local.prijevodi", Locale.forLanguageTag(language));
	}
	
	/*
	 * Method returns static singleton provider refrence
	 */
	public static LocalizationProvider getInstance() {
		
		if (provider==null) provider = new LocalizationProvider();
		
		return provider;
	}
	
	/*
	 * Method sets a given language to be the current one
	 */
	public void setLanguage(String language) {
		this.language = language;
		bundle = ResourceBundle.getBundle("hr.fer.oprpp1.hw08.jnotepadpp.local.prijevodi", Locale.forLanguageTag(language));
		fire();
	}

	@Override
	public String getString(String key) {
		return bundle.getString(key);
	}

	@Override
	public String getCurrentLanguage() {
		return language;
	}
}



























