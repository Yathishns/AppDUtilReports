/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.appdynamics.utilreports.conf;

import org.appdynamics.utilreports.resources.AppDUtilReportS;

import java.util.ArrayList;


/**
 *
 * @author gilbert.solorzano
 */
public class Load_Check_Set {
    private ArrayList<Load_Check> hcLoadCheck=new ArrayList<Load_Check>();
    private String application;
    private String applicationName;
    
    public Load_Check_Set(){}

    public ArrayList<Load_Check> getHcLoadCheck() {
        return hcLoadCheck;
    }

    public void setHcLoadCheck(ArrayList<Load_Check> hcLoadCheck) {
        this.hcLoadCheck = hcLoadCheck;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }
    
    @Override
    public String toString(){
        StringBuilder bud = new StringBuilder();
        bud.append(AppDUtilReportS.L2).append(AppDUtilReportS.LOAD_CHECK_SET);
        bud.append(AppDUtilReportS.L3).append(AppDUtilReportS.APPLICATION).append(AppDUtilReportS.VE).append(application);
        for(Load_Check lc: hcLoadCheck)bud.append(lc.toString()); //L3
        return bud.toString();
    }
    
}
