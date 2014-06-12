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
public class Outputs {
    ArrayList<Output> outputs=new ArrayList<Output>();
    
    public Outputs(){}

    public ArrayList<Output> getOutpus() {
        return outputs;
    }

    public void setOutpus(ArrayList<Output> outpus) {
        this.outputs = outpus;
    }
    
    @Override
    public String toString(){
        StringBuilder bud = new StringBuilder();
        bud.append(AppDUtilReportS.L2).append(AppDUtilReportS.OUTPUTS);
        for(Output ou: outputs) bud.append(ou.toString());
        return bud.toString();
    }
}
