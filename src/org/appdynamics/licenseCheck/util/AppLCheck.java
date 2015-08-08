/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.appdynamics.licenseCheck.util;


import org.appdynamics.utilreports.resources.AppDUtilReportS;

/**
 *
 * @author gilbert.solorzano
 */
public class AppLCheck {
    private ServerConfig serverConfig;
    private InfoCheck infoCheck;
    private MailInfo mailInfo;

    public MailInfo getMailInfo() {
        return mailInfo;
    }

    public void setMailInfo(MailInfo mailInfo) {
        this.mailInfo = mailInfo;
    }

    
    public AppLCheck(){}

    public InfoCheck getInfoCheck() {
        return infoCheck;
    }

    public void setInfoCheck(InfoCheck infoCheck) {
        this.infoCheck = infoCheck;
    }


    public ServerConfig getServerConfig() {
        return serverConfig;
    }

    public void setServerConfig(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }

    @Override
    public String toString(){
        StringBuilder bud = new StringBuilder();
        bud.append(AppDUtilReportS.APPD_HC_REPORT);
        bud.append(serverConfig); // L1
        bud.append(infoCheck);
        return bud.toString();
    }
}
