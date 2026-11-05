package lab.db_studio.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lab.db_studio.exceptions.FailedOperationException;
import lab.db_studio.exceptions.InvalidResponseException;

public abstract class Result {
    
    private boolean isSuccess = true;
    private boolean hasResultSet = true;
    private String errorCause;

    private List<Map<String, Object>> resultSet = new ArrayList<>();
    private String[] columns;
    private int executeRowsAffected;

    public Result() {}

    public void setResultSet(List<Map<String, Object>> rs){
        this.resultSet = rs;
        hasResultSet = true;
    }

    public void setExecuteRowsAffected(int rowAffected){
        this.hasResultSet = false;
        resultSet = null;
        this.executeRowsAffected = rowAffected;
    }

    public List<Map<String, Object>> getResultSet() {
        if (!isSuccess) throw new FailedOperationException(errorCause);
        if (!hasResultSet) throw new InvalidResponseException("No result set available");
        return this.resultSet;
    }

    public String getRowsAffected(){
        if (!isSuccess) throw new InvalidResponseException(errorCause);
        if (hasResultSet) throw new InvalidResponseException("Any columns have been affected");
        return this.executeRowsAffected + " row(s) affected";
    }

    public void setErrorResult(String cause){
        this.errorCause = cause;
        this.isSuccess = false;
    }

    public boolean getIsSuccess(){
        return isSuccess;
    }

    public Boolean hasResultSet(){
        return this.hasResultSet;
    }

    public void setColumns(String[] columns){
        this.columns = columns;
        hasResultSet = true;
    }

    public String[] getColumns(){
        return columns;
    }
}

