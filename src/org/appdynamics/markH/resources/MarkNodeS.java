/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.appdynamics.markH.resources;

/**
 *
 * @author gilbert.solorzano
 */
public class MarkNodeS {
    
    public static String CONTROLLER_V="familysearch.saas.appdynamics.com";
    public static String CONTROLLER_PORT_V="443";
    public static String CONTROLLER_USER_V="tam";
    public static String CONTROLLER_USER_PASS_V="";
    public static boolean CONTROLLER_SSL_V=true;
    public static String CONTROLLER_ACCOUNT_V="familysearch";
    public static String APPLICATION_NAME="53";
    public static int INTERVAL_V=-4;
    
    public static String[] TIERS_CONTAINS_V={};
    public static String[] NODES_CONTAINS_V={"cf_","awsebs","ip"}; //if(path_[3].startsWith("cf_") || path_[3].startsWith("awsebs") || path_[3].startsWith("ip")
    
    
    public static String[] SEACH_TYPES={"contains","startsWith","endsWith","regex","any"};
    public static final String APPLICATION="application";
    public static String TIER="tier";
    public static String NODE="node";
    public static final String MARK_HISTORICAL="mark_historical";
    public static final String HOURS_TO_CHECK="hours_to_check";
    public static final String MIN_COUNT="min_count";
    public static final String CHECK_TYPE="check_type";
    public static final String NAME_VALUE="name_value";
    public static final String NAME="name";
    
    
}
