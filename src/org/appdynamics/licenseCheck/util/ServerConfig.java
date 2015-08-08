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
public class ServerConfig {
    private Controller controller;
    private Account account;
  
    
    public ServerConfig(){}


    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }



    
    @Override
    public String toString(){
        StringBuilder bud = new StringBuilder();
        bud.append(AppDUtilReportS.L1).append(AppDUtilReportS.SERVER_CONFIG);
        bud.append(controller.toString());//L2
        bud.append(account.toString());
        return bud.toString();
    }
    
}
