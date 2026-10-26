package lab.db_studio.service;

import lab.db_studio.entity.JSResult;

public class JavascriptEngine implements Engine{

    private static final JavascriptEngine instance = new JavascriptEngine(); 

    @Override
    public JSResult execute(String code) {
        
        System.out.println("You're using JS");
        return  null;
    }
    
    private JavascriptEngine(){}
    
    public static JavascriptEngine getInstance(){
        return instance;
    }
}
