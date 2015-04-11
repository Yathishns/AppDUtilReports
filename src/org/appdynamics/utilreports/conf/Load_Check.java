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
 * 
 *         <load-check name="eum-ajax-check" enabled="true">
                <check name="last-4-hours" hours="4" >50</check>
                <check name="last-24-hours" hours="24">100</check>
                <check name="last-48-hours" hours="48">150</check>
            </load-check>
        * 
 */
public class Load_Check {
    private boolean enabled;
    private String name;
    private ArrayList<CheckXML> checks=new ArrayList<CheckXML>();

    
    public Load_Check(){}

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<CheckXML> getChecks() {
        return checks;
    }

    public void setChecks(ArrayList<CheckXML> checks) {
        this.checks = checks;
    }

    

    
    @Override
    public String toString(){
        StringBuilder bud = new StringBuilder();
        bud.append(AppDUtilReportS.L3).append(AppDUtilReportS.LOAD_CHECK);
        bud.append(AppDUtilReportS.L4).append(AppDUtilReportS.NAME).append(AppDUtilReportS.VE).append(name);
        bud.append(AppDUtilReportS.L4).append(AppDUtilReportS.ENABLED).append(AppDUtilReportS.VE).append(enabled);
        for(CheckXML check:checks)bud.append(check.toString());
        return bud.toString();
    }
    
}
