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
 *  <check name="last-48-hours" hours="48">150</check>
 * 
 */
public class CheckXML {
    private String name;
    private int hours, min;
    
    public CheckXML(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }
    
    @Override
    public String toString(){
        StringBuilder bud = new StringBuilder();
        bud.append(AppDUtilReportS.L4).append(AppDUtilReportS.CHECK);
        bud.append(AppDUtilReportS.L5).append(AppDUtilReportS.NAME).append(AppDUtilReportS.VE).append(name);
        bud.append(AppDUtilReportS.L5).append(AppDUtilReportS.HOURS).append(AppDUtilReportS.VE).append(hours);
        bud.append(AppDUtilReportS.L5).append(AppDUtilReportS.MIN_COUNT).append(AppDUtilReportS.VE).append(min);
        
        return bud.toString();
    }
    
}
