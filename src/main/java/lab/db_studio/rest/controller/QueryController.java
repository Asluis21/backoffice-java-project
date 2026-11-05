package lab.db_studio.rest.controller;

import lab.db_studio.rest.HttpMethods;
import lab.db_studio.rest.annotation.Route;

public class QueryController{
    
    public final static String CONTEXT_PATH = "/api/query";

    @Route (path = "/api/query/info", method = HttpMethods.GET)
    public String info() {        
        return "I'm working";
    }


    
    
    
}
