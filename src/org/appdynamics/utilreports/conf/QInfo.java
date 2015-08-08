/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.appdynamics.utilreports.conf;

/**
 *
 * @author gilbert.solorzano
 */
public class QInfo {
    private String name;
    private String tierName;
    private String type;
    private long value=-2,oldValue=-2;
    private int attemptNum=0;
    private boolean passed=false;

    public QInfo(String name){
        this.name=name;
    }
    
    public QInfo(String name, String tierName){
        this.name=name;
        this.tierName=tierName;
    }
    
    public QInfo(String name, String tierName, String type){
        this.name=name;
        this.tierName=tierName;
        this.type=type;
    }

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }
    
    
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public String getTierName() {
        return tierName;
    }

    public void setTierName(String tierName) {
        this.tierName = tierName;
    }

    public int getAttemptNum() {
        return attemptNum;
    }

    public void setAttemptNum(int attemptNum) {
        this.attemptNum = attemptNum;
    }

    public long getOldValue() {
        return oldValue;
    }

    public void setOldValue(long oldValue) {
        this.oldValue = oldValue;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    
    
}
