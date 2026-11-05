package lab.db_studio.service;

import lab.db_studio.entity.Result;

/**
 * Actions
 */
public interface Engine {

    Result execute(String code);
}