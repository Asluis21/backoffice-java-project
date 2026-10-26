import java.util.List;

import lab.db_studio.entity.Dbstudio;
import lab.db_studio.entity.Result;
import lab.db_studio.helper.CodeAnalyzer;
import lab.db_studio.helper.ResultPrinter;

public class App {
    public static void main(String[] args) throws Exception {

        List<String> statements = List.of(
            "CREATE TABLE users (id INT PRIMARY KEY, name VARCHAR(50))", 
            "INSERT INTO users VALUES (1, 'Ana'), (2, 'Luis'), (3, 'Maria')", 
            "SELECT * FROM users",
            "SELECT * FROM nope");

        
        try {

            for (String input : statements) {
                
                Dbstudio dbstudio = new Dbstudio(CodeAnalyzer.verifyCode(input));
                
                Result sqlResult = dbstudio.actions.execute(input);
    
                if(sqlResult.hasResultSet()){
    
                    ResultPrinter.printInConsole(sqlResult.getResultSet());
                }else{
                    System.out.println(sqlResult.getRowsAffected());
                }
            }


        } catch (Exception e) {
            
            System.out.println(e.getMessage());
        }

    }
}
