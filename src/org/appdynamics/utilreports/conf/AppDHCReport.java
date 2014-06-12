/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.appdynamics.utilreports.conf;

import org.appdynamics.utilreports.resources.AppDUtilReportS;

/**
 *
 * @author gilbert.solorzano
 */
public class AppDHCReport {
    private ServerConfig serverConfig;
    private HC_Load_Checks hcLoadChecks;
    
    public AppDHCReport(){}

    public ServerConfig getServerConfig() {
        return serverConfig;
    }

    public void setServerConfig(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }

    public HC_Load_Checks getHcLoadChecks() {
        return hcLoadChecks;
    }

    public void setHcLoadChecks(HC_Load_Checks hcLoadChecks) {
        this.hcLoadChecks = hcLoadChecks;
    }
    
    @Override
    public String toString(){
        StringBuilder bud = new StringBuilder();
        bud.append(AppDUtilReportS.APPD_HC_REPORT);
        bud.append(serverConfig.toString()); // L1
        bud.append(hcLoadChecks.toString());
        return bud.toString();
    }
}
