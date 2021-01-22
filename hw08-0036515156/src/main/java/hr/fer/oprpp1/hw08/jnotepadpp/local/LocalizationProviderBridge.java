package hr.fer.oprpp1.hw08.jnotepadpp.local;

/**
 * Class LocalizationProviderBridge represents a bridge of a localiazation provider
 * @author leokiparje
 *
 */

public class LocalizationProviderBridge extends AbstractLocalizationProvider {

	/*
	 * Boolean connected variable
	 */
	private boolean connected;
	
	/*
	 * Listener
	 */
	private ILocalizationListener listener;
	
	/*
	 * Provider
	 */
	private ILocalizationProvider provider;
	
	/*
	 * Basic constructor
	 */
	public LocalizationProviderBridge(ILocalizationProvider provider) {
		this.provider = provider;
		connected = false;
	}
	
	/*
	 * Method disconnect disconnects the bridge when window is closed
	 */
	public void disconnect() {
		provider.removeLocalizationListener(listener);
		connected = false;
	}
	
	/*
	 * Method connect connects the bridge when window is opened
	 */
	public void connect() {
		
		if (!connected) {
			listener = new ILocalizationListener() {		
				@Override
				public void localizationChanged() {
					fire();
				}
			};
			
			provider.addLocalizationListener(listener);
			connected = true;
		}
		
	}
	
	/*
	 * Method returns string value of providers key
	 */
	public String getString(String key) { 
		return provider.getString(key);
	}

	@Override
	public String getCurrentLanguage() {
		return provider.getCurrentLanguage();
	}
}
