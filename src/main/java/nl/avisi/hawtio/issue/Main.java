package nl.avisi.hawtio.issue;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.webapp.WebAppContext;

import java.io.File;

public class Main {

    private static Server jettyServer;
    private static org.apache.camel.spring.Main camel;

    /**
     * Allow this route to be run as an application
     */
    public static void main(String[] args) throws Exception {
        startJetty();
        startCamel();
    }

    private static void startCamel() throws Exception {
        camel = new org.apache.camel.spring.Main();
        camel.setApplicationContextUri("classpath:camel-context.xml");
        camel.enableHangupSupport();
        camel.run();
    }

    private static void startJetty() throws Exception {
        WebAppContext hawtioWebapp = new WebAppContext();
        hawtioWebapp.setContextPath("/hawtio");
        hawtioWebapp.setWar(new File("target/webapps/hawtio-default-offline.war").getAbsolutePath());
        hawtioWebapp.setParentLoaderPriority(true);
        hawtioWebapp.setLogUrlOnStart(true);
        hawtioWebapp.setTempDirectory(new File("temp"));

        jettyServer = new Server(8180);
        ContextHandlerCollection contexts = new ContextHandlerCollection();
        contexts.setHandlers(new Handler[] { hawtioWebapp });
        jettyServer.setHandler(contexts);
        jettyServer.start();
    }
}
