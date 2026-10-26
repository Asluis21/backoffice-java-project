package lab.db_studio.helper;

import java.sql.SQLException;
import java.util.regex.Pattern;

import lab.db_studio.exceptions.UnkownEngineException;
import lab.db_studio.service.Engine;
import lab.db_studio.service.JavascriptEngine;
import lab.db_studio.service.SQLEngine;

public class CodeAnalyzer {
    
    
    private static final Pattern SQL_START_PATTERN = Pattern.compile(
        "(?is)^\\s*(SELECT\\b|INSERT\\s+INTO\\b|UPDATE\\b|DELETE\\s+FROM\\b|" +
        "CREATE\\s+(TABLE|DATABASE|INDEX|VIEW)\\b|ALTER\\s+TABLE\\b|" +
        "DROP\\s+(TABLE|DATABASE|INDEX|VIEW)\\b)"
    );

    private static final Pattern JS_START_PATTERN = Pattern.compile(
        "(?s)^\\s*(const\\b|let\\b|var\\b|function\\b|class\\b|" +
        "import\\b|export\\b|async\\b|(?:if|for|while|switch|try)\\s*\\(|" +
        "\\(.*\\)\\s*=>|[A-Za-z_$][\\w$]*\\s*=>)"
    );

    private static final Pattern SQL_PATTERN = Pattern.compile(
        "(?is)\\b(SELECT|INSERT\\s+INTO|UPDATE\\s+\\w+\\s+SET|" +
        "DELETE\\s+FROM|CREATE\\s+(TABLE|DATABASE|INDEX|VIEW)|" +
        "ALTER\\s+TABLE|DROP\\s+(TABLE|DATABASE|INDEX|VIEW)|" +
        "FROM|WHERE|JOIN|GROUP\\s+BY|ORDER\\s+BY)\\b"
    );

    private static final Pattern JS_PATTERN = Pattern.compile(
        "(?s)(\\b(function|const|let|var|class|return|import|export|" +
        "async|await|new|throw|typeof|instanceof)\\b|=>|" +
        "\\bconsole\\s*\\.\\s*(log|error|warn|info)\\s*\\(|" +
        "\\bdocument\\s*\\.|\\bwindow\\s*\\.|" +
        "\\b(if|else|for|while|switch|try|catch)\\s*\\(|" +
        "\\b(true|false|null|undefined)\\b)"
    );

    public static Engine verifyCode(String code) throws SQLException{
        
        String cleanCode = code.strip();

        boolean isJS = JS_START_PATTERN.matcher(cleanCode).find();
        boolean isSQL = SQL_START_PATTERN.matcher(cleanCode).find();

        if(isJS && !isSQL){

            return JavascriptEngine.getInstance();
        }
        
        if(isSQL && !isJS){
            
            return SQLEngine.getInstance();
        }

        isJS = JS_PATTERN.matcher(cleanCode).find();
        isSQL = SQL_PATTERN.matcher(cleanCode).find();

        if(isJS && !isSQL){

            return JavascriptEngine.getInstance();
        }
        
        if(isSQL && !isJS){
            
            return  SQLEngine.getInstance();
        }

        throw new UnkownEngineException("Cannot identify the language");
    }
}
