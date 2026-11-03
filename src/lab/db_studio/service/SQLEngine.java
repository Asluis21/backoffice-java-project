package lab.db_studio.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import lab.db_studio.entity.SQLResult;
import lab.db_studio.exceptions.SqlValidationException;

public class SQLEngine implements Engine{

    private static final SQLEngine INSTANCE = new SQLEngine(); 
    private final Connection connection;

    @Override
    public SQLResult execute(String code) {
        
        SQLResult sqlResult = new SQLResult();


        for(String st : splitStatements(code)){

            PreparedStatement ps;

            try {
               ps  = connection.prepareStatement(st); 
            } catch (SQLException e) {
                throw new SqlValidationException(e.getMessage(), e);
            }

            sqlResult = new SQLResult();

            try(ps) {
                
                if (ps.execute()) {
                    
                    ResultSet rs = ps.getResultSet();
        
                    ResultSetMetaData metaData = rs.getMetaData();
                    int columnsCount = metaData.getColumnCount();
        
                    sqlResult.setColumns(getColumns(columnsCount, metaData));
                    sqlResult.setResultSet(formateResultSet(rs, sqlResult.getColumns()));
        
        
                }else{
                    sqlResult.setExecuteRowsAffected(ps.getUpdateCount());
                }
            } catch (SQLException e) {
                sqlResult.setErrorResult(e.getMessage());
                return sqlResult;
            }

        }

        return sqlResult;
    }

    private SQLEngine(){

        try {
            this.connection = DriverManager.getConnection("jdbc:h2:mem:studio;DB_CLOSE_DELAY=-1", "sa", "");
        } catch (SQLException e) {
            throw new RuntimeException("Error at creating a SQL Engine", e);
        }
    }

    public static SQLEngine getInstance() {
        return INSTANCE;
    }

    private String[] getColumns(int totalColumns, ResultSetMetaData metaData) throws SQLException {
        
        if (totalColumns <= 0) return new String[0];

        String[] columns = new String[totalColumns];

        for (int i = 1; i <= totalColumns; i++) {
            columns[i-1] = metaData.getColumnLabel(i);       
        } 

        return columns;
    }

    private List<Map<String, Object>> formateResultSet(ResultSet rs, String[] columns) throws SQLException{
        
        List<Map<String, Object>> formatResult = new ArrayList<>();
        
        while (rs.next()) {
            
            Map<String, Object> row = new LinkedHashMap<>();
    
            for (int i = 1; i <= columns.length; i++) {
                row.put(columns[i-1], rs.getObject(i));
            }

            formatResult.add(row);

        }

        return formatResult;
    }


    private List<String> splitStatements(String code){

        List<String> result = new ArrayList<>();
        StringBuilder cur = new StringBuilder();

        boolean inLine = false, inBlock = false, inSingle = false, inDouble = false;

        for(int i = 0; i < code.length(); i++){
            char c = code.charAt(i);
            char next = i + 1 < code.length() ? code.charAt(i+1) : '\0';

            if (inLine)         { if (c == '\n') inLine = false; }
            else if (inBlock)   { if (c == '*' && next == '/') { inBlock = false; cur.append(c); c = next; i++;} }
            else if (inSingle)  { if (c == '\'') inSingle = false; } 
            else if (inDouble)  { if (c == '"') inDouble = false; } 
            else if (c == '-' && next =='-' ) inLine = true;
            else if (c == '/' && next == '*') inBlock = true;
            else if (c == '\'') inSingle = true;
            else if (c == '"')  inDouble = true;
            else if (c == ';') {
                if(!cur.toString().isBlank()) result.add(cur.toString().trim());
                cur.setLength(0);
                continue;
            }

            cur.append(c);
        }

        if(!cur.toString().isBlank()) result.add(cur.toString().trim());

        return result;
    }
    
}
