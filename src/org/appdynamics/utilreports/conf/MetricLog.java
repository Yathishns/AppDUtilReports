/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.appdynamics.utilreports.conf;

import org.appdynamics.utilreports.resources.AppDUtilReportS;

/**
 *
 * @author gilbert.solorzano
 * <server-log maxNumber="" maxSize="">{FULL_PATH}</server-log>
 * 
 */
public class MetricLog {
    private int maxNumber;
    private int maxSize;
    private String fullPath;
    private String type;
    
    public MetricLog(){}

    public int getMaxNumber() {
        return maxNumber;
    }

    public void setMaxNumber(int maxNumber) {
        this.maxNumber = maxNumber;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public String getFullPath() {
        return fullPath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    
    @Override
    public String toString(){
        StringBuilder bud = new StringBuilder();
        bud.append(AppDUtilReportS.L2).append(AppDUtilReportS.SERVER_LOG);
        bud.append(AppDUtilReportS.L3).append(AppDUtilReportS.TYPE).append(AppDUtilReportS.VE).append(type);
        bud.append(AppDUtilReportS.L3).append(AppDUtilReportS.FULL_PATH).append(AppDUtilReportS.VE).append(fullPath);
        bud.append(AppDUtilReportS.L3).append(AppDUtilReportS.MAX_NUMBER).append(AppDUtilReportS.VE).append(maxNumber);
        bud.append(AppDUtilReportS.L3).append(AppDUtilReportS.MAX_SIZE).append(AppDUtilReportS.VE).append(maxSize);
        return bud.toString();
    }
}
