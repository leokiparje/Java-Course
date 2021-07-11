package hr.fer.oprpp2.webserver.workers;

import hr.fer.oprpp2.webserver.IWebWorker;
import hr.fer.oprpp2.webserver.RequestContext;

/**
 * Class BgColorWorker represents a worker with job to add needed parameters
 * to request context so that background color of the screen can be changed.
 * @author leokiparje
 *
 */

public class BgColorWorker implements IWebWorker {

	@Override
	public void processRequest(RequestContext context) throws Exception {
		
		String bgcolor = context.getParameter("bgcolor");
		if (bgcolor!=null && bgcolor.length()==6 && bgcolor.matches("-?[0-9a-fA-F]+")) {
			context.setPersistentParameter("bgcolor",bgcolor);
			context.setTemporaryParameter("message","Color is updated");
			context.getDispatcher().dispatchRequest("/private/pages/color.smscr");
		}else {
			context.setTemporaryParameter("message","Color is not updated");
			context.getDispatcher().dispatchRequest("/private/pages/color.smscr");
		}
	}

}
