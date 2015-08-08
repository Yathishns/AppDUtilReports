/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.appdynamics.licenseCheck.util;

/**
 *
 * @author gilbert.solorzano
 */
public class AppCheck {
    private String application, tier;
    private Integer machineNumber, applicationNumber;

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getTier() {
        return tier;
    }

    public void setTier(String tier) {
        this.tier = tier;
    }

    public Integer getMachineNumber() {
        return machineNumber;
    }

    public void setMachineNumber(Integer machineNumber) {
        this.machineNumber = machineNumber;
    }

    public Integer getApplicationNumber() {
        return applicationNumber;
    }

    public void setApplicationNumber(Integer applicationNumber) {
        this.applicationNumber = applicationNumber;
    }
    
    @Override 
    public String toString(){
        StringBuilder bud = new StringBuilder();
        bud.append(application).append(",").append(tier).append(",").append(applicationNumber).append(",").append(machineNumber).append("\n");
        return bud.toString();
    }
    
}
