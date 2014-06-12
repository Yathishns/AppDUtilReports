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
public class Output {
    private String type;
    private String format;
    private String file;
    
    public Output(){}

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
    
    
    
    @Override
    public String toString(){
        StringBuilder bud = new StringBuilder();
        bud.append(AppDUtilReportS.L3).append(AppDUtilReportS.OUTPUT);
        bud.append(AppDUtilReportS.L4).append(AppDUtilReportS.TYPE).append(AppDUtilReportS.VE).append(type);
        bud.append(AppDUtilReportS.L4).append(AppDUtilReportS.FORMAT).append(AppDUtilReportS.VE).append(format);
        bud.append(AppDUtilReportS.L4).append(AppDUtilReportS.FULL_PATH).append(AppDUtilReportS.VE).append(file);
        return bud.toString();
    }
    
    
}
