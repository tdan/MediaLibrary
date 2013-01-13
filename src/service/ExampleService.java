package service;

import java.io.*;
import cajo.sdk.*;

/**
 * An example service
 */
public final class ExampleService extends AbstractService {
	public ExampleService(String name, ObjectOutputStream log) throws Exception {
		super("proxy.ExampleServiceProxy", name, log); // controller.ExampleController
		// is the proxy for this
		// service
		description = "An example service";
		// Allow remote calls to the getMessage method
		addDescriptor("getMessage", "",
				new String[][]{{"java.lang.String", ""}}, new String[]{
						"java.lang.String", ""},
				new String[][]{REMOTEEXCEPTION});
		startup();
	}

	public String getMessage(String message) {
		if (message == null)
			return "";
		return message;
	}

	@Override
	public String toString() {
		return "ExampleService";
	}
}
