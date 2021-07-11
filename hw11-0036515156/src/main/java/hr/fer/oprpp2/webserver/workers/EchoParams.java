package hr.fer.oprpp2.webserver.workers;

import hr.fer.oprpp2.webserver.IWebWorker;
import hr.fer.oprpp2.webserver.RequestContext;

/**
 * EchoParams class represents a class with job to output all the given parameters in a request.
 * @author leokiparje
 *
 */

public class EchoParams implements IWebWorker {

	@Override
	public void processRequest(RequestContext context) throws Exception {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("""
				<!DOCTYPE html>
				<html>
					<head>
						<title>Echo Params worker</title>
					</head>
					<body>
						<table>
							<thead>
							<tr>
							<th>Naziv</th>
							<th>Vrijednost</th>
							</tr>
							</thead>
							<tbody>
							""");
		for (String paramName : context.getParameterNames()) {
			sb.append("""
					<tr>
					<td>
					""");
			sb.append(paramName);
			sb.append("""
					</td>
					<td>
					""");
			sb.append(context.getParameter(paramName));
			sb.append("</td");
		}
		sb.append("""
							</tbody>
						</table>
					</body>
				</html>
				""");
		context.write(sb.toString());
	}

}
