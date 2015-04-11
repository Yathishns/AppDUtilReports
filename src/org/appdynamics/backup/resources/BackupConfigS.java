/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.appdynamics.backup.resources;

/**
 *
 * @author gilbert.solorzano
 */
public class BackupConfigS {
    /**  Options **/
    public static final String CONTROLLER_L="controller";
    public static final String CONTROLLER_S="c";
    public static final String CONTROLLER_D="This is going to be the FQDN of the controller, for example: appdyn.saas.appdynamics.com";
    public static final boolean CONTROLLER_R=true;
    public static final boolean CONTROLLER_A=true;
    
    public static final String PORT_L="port";
    public static final String PORT_S="P";
    public static final String PORT_D="The is going to be the port that is used by the controller.";
    public static final boolean PORT_R=true;
    public static final boolean PORT_A=true;
    
    public static final String ACCOUNT_L="account";
    public static final String ACCOUNT_S="a";
    public static final String ACCOUNT_D="If controller is multi-tenant add the account";
    public static final boolean ACCOUNT_R=false;
    public static final boolean ACCOUNT_A=false;
    
    public static final String USERNAME_L="username";
    public static final String USERNAME_S="u";
    public static final String USERNAME_D="The user name to use for the connection";
    public static final boolean USERNAME_R=true;
    public static final boolean USERNAME_A=true;
    
    public static final String PASSWD_L="passwd";
    public static final String PASSWD_S="p";
    public static final String PASSWD_D="The password to use for the connection";
    public static final boolean PASSWD_R=true;
    public static final boolean PASSWD_A=true;
    
    public static final String SSL_L="ssl";
    public static final String SSL_S="s";
    public static final String SSL_D="Use SSL with connection";
    public static final boolean SSL_R=false;
    public static final boolean SSL_A=false;
    
    public static final String DIRECTORY_L="directory";
    public static final String DIRECTORY_S="D";
    public static final String DIRECTORY_D="The base directory where to create the files";
    public static final boolean DIRECTORY_R=false;
    public static final boolean DIRECTORY_A=false;
    
    public final static String HTTP="http://";
    public final static String HTTPS="https://";
    public final static String COLON=":";
    
    public final static String P="/";
    public static String DIRECTORY_V=".";
    public static String DIRECTORY_Name_V="AppD";
    public static String SIMPLE_DATE_FORMAT="yyyyMMdd";
    public static String DIRECTORY_PREFIX_EXPORTS="AppDExports_";
    public static String DIRECTORY_PREFIX_DASHBOARDS="Dashboards_";
    public static String DIRECTORY_PREFIX_HEALTHRULES="HealthRules_";
    public static String DIRECTORY_PREFIX_MATCHRULES="MatchRules_";
    public static String XML_END=".xml";
    public static String _U="_";
    
    public static final String OPTION_ERROR_1="A required parameter was not found. Please view the help menu for required parameters.";
    public static String USERNAME_V=null;
    public static String PASSWD_V=null;
    public static String CONTROLLER_V=null;
    public static String ACCOUNT_V="customer1";
    
    public static String PORT_V=null;
    public static int DEBUG_V=0;
    public static boolean SSL_V=false;
    
    
}
