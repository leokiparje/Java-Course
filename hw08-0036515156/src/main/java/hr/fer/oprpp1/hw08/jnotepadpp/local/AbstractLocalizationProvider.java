package hr.fer.oprpp1.hw08.jnotepadpp.local;

import java.util.ArrayList;
import java.util.List;

/**
 * Class AbstractLocalizationProvider implements localization provider interface and gives some functionality to it
 * @author leokiparje
 *
 */

public abstract class AbstractLocalizationProvider implements ILocalizationProvider {
	
	/*
	 * List of listeners
	 */
	private List<ILocalizationListener> listeners = new ArrayList<>();

	/*
	 * Empty constructor
	 */
	public AbstractLocalizationProvider() {}
	
	/*
	 * Method adds a listeners
	 */
	public void addLocalizationListener(ILocalizationListener listener) {
		listeners.add(listener);
	}
	
	/*
	 * Method removes a listener
	 */
	public void removeLocalizationListener(ILocalizationListener listener) {
		listeners.remove(listener);
	}
	
	/*
	 * Method is called to notify all the listeners that a change in localization has been made
	 */
	public void fire() {
		for (ILocalizationListener listener : listeners) {
			listener.localizationChanged();
		}
	}
}
