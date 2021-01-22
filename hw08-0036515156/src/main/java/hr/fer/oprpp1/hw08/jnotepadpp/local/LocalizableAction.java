package hr.fer.oprpp1.hw08.jnotepadpp.local;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

/**
 * Class LocalizableAction represents a action that is localized
 * @author leokiparje
 *
 */

public class LocalizableAction extends AbstractAction {
	
	private static final long serialVersionUID = 1L;
	
	/*
	 * ILocaliazationProvider provider
	 */
	private ILocalizationProvider provider;

	/*
	 * Basic constrcutor
	 */
	public LocalizableAction(String key, ILocalizationProvider lp) {
		
		this.provider = lp;
		String value = provider.getString(key);
		putValue(Action.NAME, value);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
	}

}
