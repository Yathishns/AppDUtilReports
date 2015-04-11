/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.appdynamics.utilreports.util;
import org.appdynamics.appdrestapi.RESTAccess;

import java.util.ArrayList;

/**
 *
 * @author gilbert.solorzano
 */
public class LoadCheckSet {
    private String application;
    private ArrayList<LoadCheck> loadChecks=new ArrayList<LoadCheck>();
    private RESTAccess access;
    
    public LoadCheckSet(String application){this.application=application;}
    
    
    
}
