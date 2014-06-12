/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.appdynamics.utilreports.conf;

import org.appdynamics.utilreports.resources.AppDUtilReportS;

import java.util.ArrayList;

/**
 *
 * @author gilbert.solorzano
 */
public class HC_Load_Checks {
    private Outputs outputs;
    private ArrayList<Load_Check_Set> loadCheckSet=new ArrayList<Load_Check_Set>();
    
    public HC_Load_Checks(){}

    public Outputs getOutputs() {
        return outputs;
    }

    public void setOutputs(Outputs outputs) {
        this.outputs = outputs;
    }

    public ArrayList<Load_Check_Set> getLoadCheckSet() {
        return loadCheckSet;
    }

    public void setLoadCheckSet(ArrayList<Load_Check_Set> loadCheckSet) {
        this.loadCheckSet = loadCheckSet;
    }
    
    @Override
    public String toString(){
        StringBuilder bud = new StringBuilder();
        bud.append(AppDUtilReportS.L1).append(AppDUtilReportS.HC_LOAD_CHECK);
        bud.append(outputs.toString());//L2
        for(Load_Check_Set lcs: loadCheckSet)bud.append(lcs.toString());
        return bud.toString();
    }
    
}
