import java.util.List;

import lab.db_studio.entity.Dbstudio;
import lab.db_studio.entity.Result;
import lab.db_studio.helper.CodeAnalyzer;
import lab.db_studio.helper.ResultPrinter;

public class App {
    public static void main(String[] args) throws Exception {

        List<String> statements = List.of(
            "CREATE TABLE users (id INT PRIMARY KEY, name VARCHAR(50), lastname VARCHAR(50))", 
            "INSERT INTO users VALUES (1, 'Ana', 'Paz'), (2, 'Luis', 'Gutierrez'), (3, 'Maria', 'Almeida')", 
            "SELECT * FROM users",
            "SELECT * FROM nope",
            "Hello world"
        );

        List<String> jsStatements = List.of(
            "const q = 'SELECT * FROM users';",
            "console.log('select from where')",
            "x = 5;",
            "-- return all rows\nSELECT * FROM users"
        );

        for (String input : jsStatements){

            try {
                
                new Dbstudio(CodeAnalyzer.verifyCode(input));

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }

        
        for (String input : statements) {
            
            try {
                
                Dbstudio dbstudio = new Dbstudio(CodeAnalyzer.verifyCode(input));
                
                Result sqlResult = dbstudio.actions.execute(input);
    
                if(sqlResult.hasResultSet()){
    
                    ResultPrinter.printInConsole(sqlResult.getResultSet());
                }else{
                    System.out.println(sqlResult.getRowsAffected());
                }
    
    
            } catch (Exception e) {
                
                System.out.println(e.getMessage());
            }
        }

    }
}
