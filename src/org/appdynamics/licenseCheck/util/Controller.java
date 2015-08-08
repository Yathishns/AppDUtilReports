/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.appdynamics.licenseCheck.util;

import org.appdynamics.utilreports.conf.*;
import org.appdynamics.utilreports.resources.AppDUtilReportS;

/**
 *
 * @author gilbert.solorzano
 */
public class Controller {
    private String fqdn;
    private String port;
    private boolean useSSL;
    
    public Controller(){}

    public String getFqdn() {
        return fqdn;
    }

    public void setFqdn(String fqdn) {
        this.fqdn = fqdn;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public boolean isUseSSL() {
        return useSSL;
    }

    public void setUseSSL(boolean useSSL) {
        this.useSSL = useSSL;
    }
    
    @Override
    public String toString(){
        StringBuilder bud = new StringBuilder();
        bud.append(AppDUtilReportS.L2).append(AppDUtilReportS.CONTROLLER).append(AppDUtilReportS.VE).append(fqdn);
        bud.append(AppDUtilReportS.L3).append(AppDUtilReportS.PORT).append(AppDUtilReportS.VE).append(port);
        bud.append(AppDUtilReportS.L3).append(AppDUtilReportS.USESSL).append(AppDUtilReportS.VE).append(useSSL);
        return bud.toString();
    }
    
}
