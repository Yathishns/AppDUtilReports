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
public class BaseEntry {
    private String id,application, tier;
    private int number,machine;
    private boolean hasMachineAgent;
    
    public BaseEntry(){}
    
    public BaseEntry(String application, String tier){
        this.application=application;
        this.tier=tier;
        this.id=new StringBuilder().append(application).append(".").append(tier).toString();
        
    }
    
    public BaseEntry(String application, String tier, int number){
        this.application=application;
        this.tier=tier;
        this.id=new StringBuilder().append(application).append(".").append(tier).toString();
        this.number=number;
    }
    
    public BaseEntry(String application, String tier, int number, int machine){
        this.application=application;
        this.tier=tier;
        this.id=new StringBuilder().append(application).append(".").append(tier).toString();
        this.number=number;
        this.machine=machine;
    }

    public boolean isHasMachineAgent() {
        return hasMachineAgent;
    }

    public void setHasMachineAgent(boolean hasMachineAgent) {
        this.hasMachineAgent = hasMachineAgent;
    }

    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getMachine() {
        return machine;
    }

    public void setMachine(int machine) {
        this.machine = machine;
    }
    
    
    
    @Override
    public String toString(){
        StringBuilder bud = new StringBuilder();
        bud.append(application).append(",").append(tier).append(",").append(number).append(",").append(machine).append(System.lineSeparator());
        return bud.toString();
    }
    
    
}
