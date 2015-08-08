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
public class InfoCheck {
    private Resource base, email,rest;
    private boolean _email,_rest;
    private int hours;
    
    public InfoCheck(){}

    public Resource getBase() {
        return base;
    }

    public boolean isEmail() {
        return _email;
    }

    public void setEmail(boolean _email) {
        this._email = _email;
    }

    public boolean isRest() {
        return _rest;
    }

    public void setRest(boolean _rest) {
        this._rest = _rest;
    }
    
    public void setBase(Resource base) {
        this.base = base;
    }

    public Resource getEmail() {
        return email;
    }

    public void setEmail(Resource email) {
        this.email = email;
    }

    public Resource getRest() {
        return rest;
    }

    public void setRest(Resource rest) {
        this.rest = rest;
    }

    

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }
    
    
    
    @Override
    public String toString(){
        StringBuilder bud = new StringBuilder();
        bud.append(LicenseCheckS.L0).append(LicenseCheckS.BASE).append(LicenseCheckS.E_);
        bud.append(base).append(LicenseCheckS.L0);
        bud.append(LicenseCheckS.NOTFICATIONS).append(LicenseCheckS.E_);
        if(email != null) bud.append(email);
        if(rest != null) bud.append(rest);
        bud.append(LicenseCheckS.L0).append(LicenseCheckS.CHECK_LAST_HOURS).append(LicenseCheckS.E_).append(hours);
        return bud.toString();
    }
    
}

/*
   <info-check>
        <output type="email" format="gilbert.solorzano@appdynamics.com"/>
        <input type="file" format="csv">baseFile.csv</input>
    </info-check> 
*/