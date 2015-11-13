/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.appdynamics.markH.data;

import org.appdynamics.utilreports.conf.*;

import java.util.ArrayList;


/**
 *
 * @author gilbert.solorzano
 */
public class MarkHList {
    private ServerConfig serverConfig;
    private ArrayList<ApplicationCheck> apps = new ArrayList<ApplicationCheck>();
    
    public MarkHList(){}

    public ServerConfig getServerConfig() {
        return serverConfig;
    }

    public void setServerConfig(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }

    public ArrayList<ApplicationCheck> getApps() {
        return apps;
    }

    public void setApps(ArrayList<ApplicationCheck> apps) {
        this.apps = apps;
    }
    
    
    
}
