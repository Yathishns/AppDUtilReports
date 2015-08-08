/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.appdynamics.licenseCheck.util;

import java.util.ArrayList;

/**
 *
 * @author gilbert.solorzano
 */
public class AccountCheck {
    private ArrayList<AppCheck> base=new ArrayList<AppCheck>();
    private ArrayList<AppCheck> restCheck=new ArrayList<AppCheck>();
    
    public AccountCheck(){}

    public ArrayList<AppCheck> getBase() {
        return base;
    }

    public void setBase(ArrayList<AppCheck> base) {
        this.base = base;
    }

    public ArrayList<AppCheck> getRestCheck() {
        return restCheck;
    }

    public void setRestCheck(ArrayList<AppCheck> restCheck) {
        this.restCheck = restCheck;
    }
    
    
}
