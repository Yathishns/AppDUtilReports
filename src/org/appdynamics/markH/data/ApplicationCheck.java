/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.appdynamics.markH.data;

import java.util.ArrayList;
/**
 *
 * @author gilbert.solorzano
 */
public class ApplicationCheck {
    private ArrayList<TierCheck> tierChecks=new ArrayList<TierCheck>();
    private int hoursToCheck,minimumCount;
    private String name;
    
    public ApplicationCheck(){}

    public ArrayList<TierCheck> getTierChecks() {
        return tierChecks;
    }

    public void setTierChecks(ArrayList<TierCheck> tierChecks) {
        this.tierChecks = tierChecks;
    }

    public int getHoursToCheck() {
        return hoursToCheck;
    }

    public void setHoursToCheck(int hoursToCheck) {
        this.hoursToCheck = hoursToCheck;
    }

    public int getMinimumCount() {
        return minimumCount;
    }

    public void setMinimumCount(int minimumCount) {
        this.minimumCount = minimumCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    
    
    @Override
    public String toString(){
        StringBuilder bud = new StringBuilder();
        bud.append("Application name ").append(name).append(" hours to check ").append(hoursToCheck).append(" and minimum count is ").append(minimumCount).append("\n");
        for(TierCheck tier: tierChecks) bud.append(tier.toString());
        return bud.toString();
    }
    
}
