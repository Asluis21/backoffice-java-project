package lab.db_studio.service;

import java.sql.SQLException;

import lab.db_studio.entity.Result;

/**
 * Actions
 */
public interface Engine {

    Result execute(String code) throws SQLException;
}