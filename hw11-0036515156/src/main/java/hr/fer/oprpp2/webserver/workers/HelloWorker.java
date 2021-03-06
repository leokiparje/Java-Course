package hr.fer.oprpp2.webserver.workers;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import hr.fer.oprpp2.webserver.IWebWorker;
import hr.fer.oprpp2.webserver.RequestContext;

/**
 * HelloWorker class represents a worker with job to output hello screen.
 * @author leokiparje
 *
 */

public class HelloWorker implements IWebWorker {
	
	@Override
	public void processRequest(RequestContext context) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date now = new Date();
		context.setMimeType("text/html");
		String name = context.getParameter("name");
		try {
			context.write("<html><body>");
			context.write("<h1>Hello!!!</h1>");
			context.write("<p>Now is: "+sdf.format(now)+"</p>");
			if(name==null || name.trim().isEmpty()) {
				context.write("<p>You did not send me your name!</p>");
			} else {
				context.write("<p>Your name has "+name.trim().length()+" letters.</p>");
			}
			context.write("</body></html>");
		} catch(IOException ex) {
			ex.printStackTrace();
		}
	}
}




































