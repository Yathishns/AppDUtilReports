/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.appdynamics.licenseCheck.util;

import org.appdynamics.licenseCheck.resources.LicenseCheckS;

import java.util.ArrayList;
/**
 *
 * @author gilbert.solorzano
 */
public class Messages {
    ArrayList<String> newObjects=new ArrayList<String>();
    ArrayList<String> tooManyAgents=new ArrayList<String>();
    ArrayList<String> tooFewAgents=new ArrayList<String>();
    
    public Messages(){}

    public ArrayList<String> getNewObjects() {
        return newObjects;
    }

    public void setNewObjects(ArrayList<String> newObjects) {
        this.newObjects = newObjects;
    }

    public ArrayList<String> getTooManyAgents() {
        return tooManyAgents;
    }

    public void setTooManyAgents(ArrayList<String> tooManyAgents) {
        this.tooManyAgents = tooManyAgents;
    }

    public ArrayList<String> getTooFewAgents() {
        return tooFewAgents;
    }

    public void setTooFewAgents(ArrayList<String> tooFewAgents) {
        this.tooFewAgents = tooFewAgents;
    }
    
    public boolean somethingToSend(){
        if(!newObjects.isEmpty() || !tooManyAgents.isEmpty() || !tooFewAgents.isEmpty()) return true;
        return false;
    }
    
    @Override
    public String toString(){
        StringBuilder bud =new StringBuilder();
        if(!newObjects.isEmpty()){
            bud.append("New objects:\n");
            for(String val: newObjects) bud.append(val);
        }
        
        if(!tooManyAgents.isEmpty()){
            bud.append("\nToo many agents:\n");
            for(String val: tooManyAgents)bud.append(val); 
        }
        
        if(!tooFewAgents.isEmpty()){
            bud.append("\nMissing agents:\n");
            for(String val: tooFewAgents)bud.append(val);    
        }
        
        return bud.toString();
    }
    
}
