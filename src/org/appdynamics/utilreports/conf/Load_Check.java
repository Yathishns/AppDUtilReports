/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.appdynamics.utilreports.conf;

import org.appdynamics.utilreports.resources.AppDUtilReportS;

/**
 *
 * @author gilbert.solorzano
 * 
 *         <bt-check enabled="true">
            <min-4hour-bt-count>50</min-4hour-bt-count>
            <min-24hour-bt-count>100</min-24hour-bt-count>
            <min-48hour-bt-count>150</min-48hour-bt-count>
        </bt-check>
        * 
 */
public class Load_Check {
    private boolean enabled;
    private String name;
    private int min;
    private int min24;
    private int min48;
    
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

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMin24() {
        return min24;
    }

    public void setMin24(int min24) {
        this.min24 = min24;
    }

    public int getMin48() {
        return min48;
    }

    public void setMin48(int min48) {
        this.min48 = min48;
    }
    
    @Override
    public String toString(){
        StringBuilder bud = new StringBuilder();
        bud.append(AppDUtilReportS.L3).append(AppDUtilReportS.LOAD_CHECK);
        bud.append(AppDUtilReportS.L4).append(AppDUtilReportS.NAME).append(AppDUtilReportS.VE).append(name);
        bud.append(AppDUtilReportS.L4).append(AppDUtilReportS.ENABLED).append(AppDUtilReportS.VE).append(enabled);
        bud.append(AppDUtilReportS.L4).append(AppDUtilReportS.MIN_COUNT).append(AppDUtilReportS.VE).append(min);
        bud.append(AppDUtilReportS.L4).append(AppDUtilReportS.MIN24_COUNT).append(AppDUtilReportS.VE).append(min24);
        bud.append(AppDUtilReportS.L4).append(AppDUtilReportS.MIN48_COUNT).append(AppDUtilReportS.VE).append(min48);
        
        return bud.toString();
    }
    
}
