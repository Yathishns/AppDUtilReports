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
public class Resource {
    private String type,format,name;
    
    public Resource(){}

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
        bud.append(LicenseCheckS.TYPE).append(LicenseCheckS.E_).append(type).append(", ").append(LicenseCheckS.FORMAT)
                .append(LicenseCheckS.E_).append(format).append(",").append(LicenseCheckS.NAME).append(LicenseCheckS.E_).append(name);
        return bud.toString();
    }
}
