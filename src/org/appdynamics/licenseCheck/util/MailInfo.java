/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.appdynamics.licenseCheck.util;

import org.appdynamics.licenseCheck.resources.LicenseCheckS;



/**
 *
 * @author gilbert.solorzano
 */
public class MailInfo {
    private String mailUser,mailPasswd,mailHost,mailPort;
    private boolean mailAuth;
    
    public MailInfo(){}
    
    
    public MailInfo(boolean mailAuth,String mailUser,String mailPasswd, String mailHost, String mailPort){
        this.mailAuth=mailAuth;
        this.mailUser=mailUser;
        this.mailPasswd=mailPasswd;
        this.mailPort=mailPort;
        this.mailHost=mailHost;
    }

    public String getMailUser() {
        return mailUser;
    }

    public void setMailUser(String mailUser) {
        this.mailUser = mailUser;
    }

    public String getMailPasswd() {
        return mailPasswd;
    }

    public void setMailPasswd(String mailPasswd) {
        this.mailPasswd = mailPasswd;
    }

    public String getMailHost() {
        return mailHost;
    }

    public void setMailHost(String mailHost) {
        this.mailHost = mailHost;
    }

    public String getMailPort() {
        return mailPort;
    }

    public void setMailPort(String mailPort) {
        this.mailPort = mailPort;
    }

    public boolean isMailAuth() {
        return mailAuth;
    }

    public void setMailAuth(boolean mailAuth) {
        this.mailAuth = mailAuth;
    }
    
    @Override
    public String toString(){
        StringBuilder bud = new StringBuilder();
        bud.append(LicenseCheckS.L0).append("Mail auth ").append(mailAuth);
        bud.append(LicenseCheckS.L0).append("Mail user ").append(mailUser);
        bud.append(LicenseCheckS.L0).append("Mail host ").append(mailHost);
        bud.append(LicenseCheckS.L0).append("Mail port ").append(mailPort);
        return bud.toString();
    }
}

/*
    
    <mail-info>
        <mail-auth>false</mail-auth>
        <mail-user>appd@appdynamics.com</mail-user>
        <mail-passwd></mail-passwd>
        <mail-host>localhost</mail-host>
        <mail-port>25</mail-port>
    </mail-info>
    
*/