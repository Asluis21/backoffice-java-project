package lab.db_studio.rest.controller;

import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import lab.db_studio.rest.HttpMethods;
import lab.db_studio.rest.annotation.Route;

public class QueryController{
    
    public final static String CONTEXT_PATH = "/api/query";

    @Route (path = "/api/query/info", method = HttpMethods.GET)
    public void info(HttpExchange exchange) throws IOException {
        
        String response = "I'm working";

        exchange.sendResponseHeaders(200, response.getBytes().length);

        try (OutputStream os = exchange.getResponseBody()) {
            
            os.write(response.getBytes());
        }

    }


    
    
    
}
