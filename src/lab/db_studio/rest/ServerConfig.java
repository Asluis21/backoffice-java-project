package lab.db_studio.rest;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

public class ServerConfig {

    private static final int PORT = 8080;
    private HttpServer httpServer;

    private ServerConfig() throws IOException {
        httpServer = HttpServer.create(
            new InetSocketAddress(PORT), 0
        );
    }

    private static class Holder {
        private static final ServerConfig INSTANCE;

        static {
            try {
                INSTANCE = new ServerConfig();
            } catch (IOException e) {
                throw new ExceptionInInitializerError(e);
            }
        }
    }

    public static ServerConfig initServer() {
        return Holder.INSTANCE;
    }

    public ServerConfig addContext(String context) {
        httpServer.createContext(context);
        return this;
    }
}