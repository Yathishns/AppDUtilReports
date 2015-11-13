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
public class TierCheck extends NodeCheck{
    private ArrayList<NodeCheck> nodeChecks = new ArrayList<NodeCheck>();
    
    public TierCheck(String checkString, CheckType check){super(checkString,check);}

    public ArrayList<NodeCheck> getNodeChecks() {
        return nodeChecks;
    }

    public void setNodeChecks(ArrayList<NodeCheck> nodeChecks) {
        this.nodeChecks = nodeChecks;
    }
    
    public boolean checkNode(String name){
        
        for(NodeCheck node: nodeChecks){
            if(node.check(name)) return true;
        }
        return false;
    }
    
    @Override
    public String toString(){
        StringBuilder bud = new StringBuilder();
        bud.append("\tTier check type '").append(check).append("' and check value '").append(checkString).append("'.\n");
        for(NodeCheck node:nodeChecks)bud.append(node.toString());
        return bud.toString();
    }
    
}
