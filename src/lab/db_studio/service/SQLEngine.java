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

    private static final SQLEngine INSTANCE = new SQLEngine(); 
    private final Connection connection;

    @Override
    public SQLResult execute(String code) {

        SQLResult sqlResult = new SQLResult();

        try (Statement statement = connection.createStatement()) {
            
            boolean hasResultSet = statement.execute(code);

            if (hasResultSet) {
                
                ResultSet rs = statement.getResultSet();

                ResultSetMetaData metaData = rs.getMetaData();
                int columnsCount = metaData.getColumnCount();

                sqlResult.setColumns(getColumns(columnsCount, metaData));
                sqlResult.setResultSet(formateResultSet(rs, sqlResult.getColumns()));


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

    public static SQLEngine getInstance() {
        return INSTANCE;
    }

    private String[] getColumns(int totalColumns, ResultSetMetaData metaData) throws SQLException {
        
        if (totalColumns <= 0) return new String[0];

        String[] columns = new String[totalColumns];

        for (int i = 0; i < totalColumns; i++) {
            columns[i] = metaData.getColumnLabel(i);       
        } 

        return columns;
    }

    private List<Map<String, Object>> formateResultSet(ResultSet rs, String[] columns) throws SQLException{
        
        List<Map<String, Object>> formatResult = new ArrayList<>();
        
        while (rs.next()) {
            
            Map<String, Object> row = new LinkedHashMap<>();
    
            for (int i = 0; i <= columns.length; i++) {
                row.put(columns[i], rs.getObject(i));
            }

            formatResult.add(row);

        }

        return formatResult;
    }
}
