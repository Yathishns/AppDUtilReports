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
public class Account {
    private String user;
    private String passwd;
    private String account;
    
    public Account(){}

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
    
    @Override
    public String toString(){
        StringBuilder bud=new StringBuilder();
        bud.append(AppDUtilReportS.L2).append(AppDUtilReportS.ACCOUNT).append(AppDUtilReportS.VE).append(account);
        bud.append(AppDUtilReportS.L3).append(AppDUtilReportS.USER).append(AppDUtilReportS.VE).append(user);
        bud.append(AppDUtilReportS.L3).append(AppDUtilReportS.PASSWORD).append(AppDUtilReportS.VE).append(passwd);
        return bud.toString();
    }
}
