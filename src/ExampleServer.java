import cajo.sdk.*;

/**
 * An example server
 */
public class ExampleServer extends AbstractServer {
    public static void main(String[] args) throws Exception {
	gnu.cajo.invoke.Remote.config("0.0.0.0", 1198, null, 0);
	cajo = new gnu.cajo.Cajo();
	new service.ExampleService("ExampleService", null);
    }
}
