package lab.db_studio.entity;

import lab.db_studio.service.Engine;

/**
 * Dbstudio
 */
public class Dbstudio{

    public Engine actions;

    public Dbstudio(){
        
    }
 
    public Dbstudio(Engine actions){
        this.actions = actions;
    }
}