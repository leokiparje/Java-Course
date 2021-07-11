package hr.fer.oprpp2.webserver.workers;

import hr.fer.oprpp2.webserver.IWebWorker;
import hr.fer.oprpp2.webserver.RequestContext;

/**
 * HomeWorker class represents a worker with job to output home screen with proper background color.
 * @author leokiparje
 *
 */

public class HomeWorker implements IWebWorker {

	@Override
	public void processRequest(RequestContext context) throws Exception {
		
		String bgcolor = context.getPersistentParameter("bgcolor");
		if (bgcolor!=null) {
			context.setTemporaryParameter("background",bgcolor);
		}else {
			context.setTemporaryParameter("background","7F7F7F");
		}
		context.getDispatcher().dispatchRequest("/private/pages/home.smscr");
	}

}
