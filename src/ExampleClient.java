import gnu.cajo.invoke.Remote;

/**
 * An example client
 */
public class ExampleClient {
    public static void main(String[] args) throws Exception {
	/*
	 * if (System.getSecurityManager() == null)
	 * System.setSecurityManager(new SecurityManager() { public void
	 * checkPermission(java.security.Permission perm) { } }); int clientPort
	 * = 0; String clientHost = null; int localPort = 0; String localHost =
	 * java.net.InetAddress.getLocalHost().getHostAddress();
	 * Remote.config(localHost, localPort, clientHost, clientPort);
	 */

	Object service = java.rmi.Naming
		.lookup("//0.0.0.0:1198/ExampleService");
	Object proxy = Remote.invoke(service, "getController", null);
	Remote.invoke(proxy, "init", new gnu.cajo.Cajo());
	String message = (String) Remote.invoke(proxy, "doGetMessage",
		"Hello, World!");
	System.out.println(message);
	System.exit(0);
    }
}
