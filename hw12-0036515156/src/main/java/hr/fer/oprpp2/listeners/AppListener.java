package hr.fer.oprpp2.listeners;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Class AppListener is a servlet context listener. On initialization it sets start time attribute.
 * @author leokiparje
 *
 */

@WebListener
public class AppListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		
		sce.getServletContext().setAttribute("started",System.currentTimeMillis());
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {}

}
