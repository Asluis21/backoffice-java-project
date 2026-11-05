package lab.db_studio.rest;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import lab.db_studio.rest.annotation.Route;
import lab.db_studio.rest.util.HttpListStatus;

public class Router implements HttpHandler{

    private Map<HttpMethodPath, Method> paths = new HashMap<>();

    public  Router() {
        findRouter();
    }

    private void findRouter(){
        Reflections reflections = new Reflections(
            new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forClassLoader())
                .setScanners(Scanners.MethodsAnnotated) 
        );

        Set<Method> methods = reflections.getMethodsAnnotatedWith(Route.class); 

        for(Method m : methods){

            Route route = m.getAnnotation(Route.class);

            HttpMethods httpMethod = route.method(); 
            String path            = route.path(); 
            

            if(path != null){
                
                paths.put(new HttpMethodPath(httpMethod, path), m);
            }
        }
        
    }


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        
        String path = exchange.getRequestURI().getPath();
        HttpMethods httpMethod = HttpMethods.valueOf(exchange.getRequestMethod());

        Method foundPath = paths.get(new HttpMethodPath(httpMethod, path));
        
        if(foundPath == null){
            
        }

        for(Entry<HttpMethodPath, Method> entry : paths.entrySet()){
            
            HttpMethodPath httpMethodPath = entry.getKey();

            if(httpMethodPath.path().equals(path)){

                if (httpMethodPath.method().equals(httpMethod)) {
                    
                    try {
                        Method methodTarget = entry.getValue();
                        Class<?> classController = methodTarget.getDeclaringClass();
    
                        Object classInstance = classController.getDeclaredConstructor().newInstance();
    
                        writeBody(exchange, HttpListStatus.OK, methodTarget.invoke(classInstance).toString());
                        return;
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                        writeBody(exchange, HttpListStatus.INTERNAL_SERVER_ERROR, e.getCause().getMessage());
                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                        writeBody(exchange, HttpListStatus.INTERNAL_SERVER_ERROR);
                        return;
                    }
                    
                }else{

                    writeBody(exchange, HttpListStatus.WRONG_METHOD);    
                    return;
                
                }
            }
        }
        writeBody(exchange, HttpListStatus.ENDPOINT_NOT_FOUND);        
    }

    private void writeBody(HttpExchange exchange, HttpStatus httpStatus, String body) throws IOException{
        
        int code = httpStatus.code();
        byte[] messageByte = body.getBytes(StandardCharsets.UTF_8);

        exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=UTF-8");
        exchange.sendResponseHeaders(code, messageByte.length);
        
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(messageByte);
        }
    }
   
    private void writeBody(HttpExchange exchange, HttpStatus httpStatus) throws IOException{
        
        int code = httpStatus.code();
        byte[] messageByte = httpStatus.message().getBytes(StandardCharsets.UTF_8);

        exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=UTF-8");
        exchange.sendResponseHeaders(code, messageByte.length);
        
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(messageByte);
        }
    }
}
