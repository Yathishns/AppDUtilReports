/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.appdynamics.utilreports.util;

import org.appdynamics.appdrestapi.RESTAccess;

/**
 *
 * @author gilbert.solorzano
 */
public class QueryResult {
    protected int retriesLimit=3;
    protected int retries=0;
    protected boolean success=false;
    protected RESTAccess access;
    protected int queryType;
    
}
