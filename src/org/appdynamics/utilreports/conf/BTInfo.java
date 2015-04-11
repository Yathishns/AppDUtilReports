/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.appdynamics.utilreports.conf;

import org.appdynamics.appdrestapi.data.BusinessTransaction;

/**
 *
 * @author gilbert.solorzano
 */
public class BTInfo {
    private long value=-2;
    private BusinessTransaction bt;
    private int btMinCheck;
    private boolean ok;
    
    public BTInfo(BusinessTransaction bt){
        this.bt=bt;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public BusinessTransaction getBt() {
        return bt;
    }

    public void setBt(BusinessTransaction bt) {
        this.bt = bt;
    }

    public int getBtMinCheck() {
        return btMinCheck;
    }

    public void setBtMinCheck(int btMinCheck) {
        this.btMinCheck = btMinCheck;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }
    
    
}
