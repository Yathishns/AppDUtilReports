/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.appdynamics.markH.data;
import java.util.regex.*;
/**
 *
 * @author gilbert.solorzano
 * 
 * The node check 
 */
public class NodeCheck {
    protected CheckType check;
    protected String checkString;
    protected Pattern p;
    
    public NodeCheck(String checkString, CheckType check){
        this.check=check;
        this.checkString=checkString;
        if(check.equals(CheckType.REGEX)) p=Pattern.compile(checkString);
    }
    
    public boolean check(String name){
        boolean valid=false;
        
        if(name == null) return valid;
        
        switch(check){
            case CONTAINS:
                valid=name.contains(checkString);
                break;
            case STARTSWITH:
                valid=name.startsWith(checkString);
                break;
            case ENDSWITH:
                valid=name.endsWith(checkString);
                break;
            case ANY:
                valid=true;
                break;
            case REGEX:
                valid=p.matcher(name).matches();
                break;              
        }
        
        return valid;
    }
    
    @Override
    public String toString(){
        StringBuilder bud = new StringBuilder();
        bud.append("\t\tNode check type '").append(check).append("' and check value '").append(checkString).append("'.\n");
        return bud.toString();
    }
    
}
