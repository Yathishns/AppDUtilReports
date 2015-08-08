/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.appdynamics.utilreports.resources;

/**
 *
 * @author gilbert.solorzano
 * 
 * <appD_HC_report>
    <server-config> 
        <controller Port="8090" useSSL="false">gsappdyn01</controller>
        <account>customer1</account>
        <user>gsadmin</user>
        <password>KF2I66CpFrNxlAN+AppDUtilReportS+BsL+8V7nKGLo5H</password>
        <server-log maxNumber="4" maxSize="30">/tmp/server.log</server-log>
    </server-config>
    
    <hc-load-checks>
        
        <outputs>
            <output type="stdout" format="pipe-delimieted"/>
        </outputs>       
        <load-check-set application="";
        <load-check name="bt-check" enabled="true">
            <min-count>50</min-count>
            <min24-count>100</min24-count>
            <min48-count>150</min48-count>
        </load-check>
 * 
 */
public class AppDUtilReportS {
    
    public static final String L1="\n    ";
    public static final String L2="\n\t";
    public static final String L3="\n\t    ";
    public static final String L4="\n\t\t";
    public static final String L5="\n\t\t    ";
    public static final String L6="\n\t\t\t";
    public static final String VE=" = ";
    public static final String _U="_";
    
    public static final int DEF_MIN=50;
    public static final int DEF_MIN24=100;
    public static final int DEF_MIN48=150;
    
    
    public static final String APPD_HC_REPORT="appD-HC-report";
    public static final String SERVER_CONFIG="server-config";
    public static final String CONTROLLER="controller";
    public static final String PORT="Port";
    public static final String USESSL="useSSL";
    public static final String ACCOUNT="account";
    public static final String USER="user";
    public static final String PASSWORD="password";
    public static final String SERVER_LOG="server-log";
    public static final String MAX_NUMBER="maxNumber";
    public static final String MAX_SIZE="maxSize";
    public static final String HC_LOAD_CHECK="hc-load-checks";
    public static final String OUTPUTS="outputs";
    public static final String OUTPUT="output";
    public static final String TYPE="type";
    public static final String FORMAT="format";
    public static final String LOAD_CHECK_SET="load-check-set";
    public static final String APPLICATION="application";
    public static final String LOAD_CHECKS="load-checks";
    public static final String LOAD_CHECK="load-check";
    public static final String NAME="name";
    public static final String ENABLED="enabled";
    public static final String CHECK="check";
    public static final String HOURS="hours";
    public static final String MIN_COUNT="min-count";
    public static final String MIN24_COUNT="min24-count";
    public static final String MIN48_COUNT="min48-count";
    public static final String FULL_PATH="full-path";
    
    public static final String OUTPUT_TYPE_STDOUT="stdout";
    public static final String OUTPUT_TYPE_FILE="file";
    public static final String OUTPUT_FORMAT_EXCEL="excel";
    public static final String OUTPUT_FORMAT_PIPEDELIMITED="pipe-delimited";
    
    public static final String BT_CHECK="bt-check";
    public static final String BE_CHECK="be-check";
    public static final String EUM_AJAX_CHECK="eum-ajax-check";
    public static final String EUM_BASE_PAGE_CHECK="eum-base-page-check";
    public static final String EUM_IFRAME_CHECK="eum-iframe-check";
    
    public static final String BUSINESS_TRANSACTION="Business Transaction Names";
    public static final String BACKENDS="Backend Nanes";
    
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
    
    public static final String DEBUG_L="debug";
    public static final String DEBUG_S="d";
    public static final String DEBUG_D="Debug level to set the calls at.";
    public static final boolean DEBUG_A=true;
    public static final boolean DEBUG_R=false;
    
    
    public static final String OPTION_ERROR_1="A required parameter was not found. Please view the help menu for required parameters.";
    
    
    public static String USERNAME_V=null;
    public static String PASSWD_V=null;
    public static String CONTROLLER_V=null;
    public static String ACCOUNT_V="Customer1";
    public static String PORT_V=null;
    public static int DEBUG_V=0;
    public static boolean SSL_V=false;
    public static boolean GRANULAR_V=false;
    public static double UPTIME_V=0.0;
    
    public static final String HELP_L="help";
    public static final String HELP_S="h";
    public static final boolean HELP_R=false;
    public static final boolean HELP_A=false;
    public static final String HELP_D="Prints out the help of the tool";
    public static final String HELP_V="\n\tUsage java -cp \"execLib/*\" ";
    public static boolean HELP_FND=false;
    
    public static final String USAGE_V="\n\tUsage java -cp \"execLib/*\" org.appdynamics.utilreports.ControllerHC [-h|-v] -c <cfg_file>";
    
    public static final String VERSION_L="version";
    public static final String VERSION_S="v";
    public static final boolean VERSION_R=false;
    public static final boolean VERSION_A=false;
    public static final String VERSION_D="Prints out the version of the tool";
    public static final String VERSION_V="1.0.1";
    public static boolean VERSION_FND=false;
   
    
    public static final String FILENAME_L="file";
    public static final String FILENAME_S="f";
    public static final String FILENAME_D="Optional : This is going to be the file name that is going to be created. Default is <AccountName>_LicenseCount.xlsx.";
    public static final boolean FILENAME_R=false;
    public static final boolean FILENAME_A=true;
    
    public static final String INTERVAL_L="interval";
    public static final String INTERVAL_S="i";
    public static final String INTERVAL_D="Optional : This is going to be the number of days we go back and run. Default is going back 7 days from midnight to midnight.  ";
    public static final boolean INTERVAL_R=false;
    public static final boolean INTERVAL_A=true;
    
    public static final String CFG_FILE_L="config";
    public static final String CFG_FILE_S="C";
    public static final String CFG_FILE_D="";
    public static final boolean CFG_FILE_R=false;
    public static final boolean CFG_FILE_A=true;
    public static String CFG_FILE_V;
    
    
    // Execl File
    public static final String TIME_RANGE="Time Range";
    public static final String APPLICATION_EQ="Application=";
    public static final String REQUEST_COUNTS="Request Counts";
    public static final String LAST_4_HOURS="Last 4 Hours";
    public static final String LAST_24_HOURS="Last 24 Hours";
    public static final String LAST_48_HOURS="Last 48 Hours";
    public static final String NORMAL="Normal Load Within 4hrs";
    
    
}
