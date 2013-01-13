package proxy;

import gnu.cajo.*;
import java.io.IOException;
import java.util.concurrent.*;
import javax.swing.JComponent;
import cajo.sdk.*;

/**
 * An example proxy
 */
public class ExampleServiceProxy extends AbstractController {
	private interface IExampleServiceProxy {
		Future<String> getMessage(String message) throws Exception;
	}

	private final IExampleServiceProxy proxy;

	public ExampleServiceProxy(Object serviceRef) {
		super(serviceRef);
		description = "";
		addDescriptor("getMessage", "",
				new String[][]{{"java.lang.String", ""}}, new String[]{
						"java.lang.String", ""},
				new String[][]{REMOTEEXCEPTION});
		proxy = proxy(serviceRef, IExampleServiceProxy.class);
	}

	@Override
	public void init(Cajo cajo) {
		super.init(cajo);
	}

	public String doGetMessage(String message) throws Exception {
		Future<String> future = proxy.getMessage(message);
		String result = null;
		result = future.get(1, java.util.concurrent.TimeUnit.SECONDS);
		return result;
	}

	@Override
	public JComponent getView() throws IOException {
		return null;
	}
}
