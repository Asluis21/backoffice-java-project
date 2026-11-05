package lab.db_studio.rest;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;


public class ServerConfig {
    
    private static final int PORT = 8080;
    private HttpServer httpServer;

    private ServerConfig() throws IOException {
        this.httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
    }

    public static ServerConfig initServer() throws IOException{
        ServerConfig sc = new ServerConfig()
            .addContext("/api", new Router());

        sc.httpServer.start();

        return sc;
    }

    private ServerConfig addContext(String path, HttpHandler handler){
        httpServer.createContext(path, handler);
        return this;
    }


}
