package lab.db_studio.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import lab.db_studio.entity.SQLResult;

public class SQLEngine implements Engine{

    private final static SQLEngine INSTANCE = new SQLEngine(); 
    private final Connection connection;

    @Override
    public SQLResult execute(String code) throws SQLException {

        SQLResult sqlResult = new SQLResult();

        try (Statement statement = connection.createStatement()) {
            
            Boolean hasResultSet = statement.execute(code);

            if (hasResultSet) {
                
                sqlResult.setResultSet(formateResultSet(statement.getResultSet()));
            }else{
                sqlResult.setExecuteRowsAffected(statement.getUpdateCount());
            }

        } catch (SQLException e) {
            sqlResult.setErrorResult(e.getMessage());
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

    public static SQLEngine getInstance() throws SQLException{
        return INSTANCE;
    }

    private List<Map<String, Object>> formateResultSet(ResultSet rs) throws SQLException{
        
        ResultSetMetaData metaData = rs.getMetaData();
        int columnsCount = metaData.getColumnCount();
        
        List<String> columns = new ArrayList<>();
        
        String columnNameIteration;
        
        if (!rs.next()) {
            
            List<Map<String, Object>> formatResult = new ArrayList<>();

            Map<String, Object> row = new LinkedHashMap<>();
            
            for(int i = 1; i<= columnsCount; i++){
                row.put(metaData.getColumnLabel(i), "");
            }

            formatResult.add(row);
            
            return formatResult;

        }else{

            for(int i = 1; i<= columnsCount; i++){
                columnNameIteration = metaData.getColumnLabel(i);
                columns.add(columnNameIteration);
            }
        }


        System.out.println();

        List<Map<String, Object>> formatResult = new ArrayList<>();

        do {
            Map<String, Object> row = new LinkedHashMap<>();
            Object columnValue = null;
    
            for(String column : columns){
                columnValue = rs.getObject(column);
                row.put(column, columnValue);
            }
            
            formatResult.add(row);
            
        } while (rs.next());

        return formatResult;
    }
}
