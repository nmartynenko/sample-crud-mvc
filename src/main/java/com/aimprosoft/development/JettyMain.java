package com.aimprosoft.development;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.webapp.WebAppContext;

public class JettyMain {

    public static void main(String[] args) throws Exception {
        Server server = new Server();

        ServerConnector connector = new ServerConnector(server);
        connector.setPort(8080);
        server.addConnector(connector);

        WebAppContext root = new WebAppContext("src/main/webapp", "/");
        server.setHandler(root);

        server.start();
        server.join();
    }
}
