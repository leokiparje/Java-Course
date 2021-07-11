package hr.fer.oprpp2.webserver.workers;

import hr.fer.oprpp2.webserver.IWebWorker;
import hr.fer.oprpp2.webserver.RequestContext;

/**
 * SumWorker class represents a worker with job to take two arguments and adds them and outputs the result.
 * @author leokiparje
 *
 */

public class SumWorker implements IWebWorker {

	@Override
	public void processRequest(RequestContext context) throws Exception {
		
		int a=1;
		int b=2;
		
		try {
			a = Integer.parseInt(context.getParameter("a"));
		}catch(Exception e) {}
		try {
			b = Integer.parseInt(context.getParameter("b"));
		}catch(Exception e) {}
		
		String sum = (a+b)+"";
		context.setTemporaryParameter("zbroj",sum);
		context.setTemporaryParameter("varA",a+"");
		context.setTemporaryParameter("varB",b+"");
		if (Integer.parseInt(sum)%2==0) {
			context.setTemporaryParameter("imgName","han.png");
		}else {
			context.setTemporaryParameter("imgName","toretto.png");
		}
		context.getDispatcher().dispatchRequest("/private/pages/calc.smscr");
		
	}

}
