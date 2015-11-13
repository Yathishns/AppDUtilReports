/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.appdynamics.markH.util;

/**
 *
 * @author gilbert.solorzano
 */
public class MarkedNode {
    private Integer id,count;
    private String name,tierName;
    private boolean hadValue;
    
    public MarkedNode(){}
    
    public MarkedNode(Integer id, Integer count, String name, String tierName){
        this.id=id;
        this.count=count;
        this.name=name;
        this.tierName=tierName;
        hadValue=true;
    }
    
    
    public MarkedNode(Integer id, boolean hadValue, String name, String tierName){
        this.id=id;
        this.name=name;
        this.tierName=tierName;
        this.hadValue=hadValue;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTierName() {
        return tierName;
    }

    public void setTierName(String tierName) {
        this.tierName = tierName;
    }

    public boolean isHadValue() {
        return hadValue;
    }

    public void setHadValue(boolean hadValue) {
        this.hadValue = hadValue;
    }
    
    
}
