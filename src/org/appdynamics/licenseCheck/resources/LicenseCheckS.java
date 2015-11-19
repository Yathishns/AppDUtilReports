/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.appdynamics.licenseCheck.resources;

/**
 *
 * @author gilbert.solorzano
 */
public class LicenseCheckS {
    
    public static final int VERSION_MAJOR=0;
    public static final int VESION_MINOR=9;
    public static final int VERSION_BUILD=0;
    
    public static final String E_=" = ";
    public static final String L0="\n";
    public static final String SOLUTION_NAME="License Check";
    public static final String NAME="name";
    public static final String TYPE="type";
    public static final String EMAIL="email";
    public static final String NEW_APPLICATIONS="New Applications";
    public static final String EXCESSIVE_AGENTS="Excessive Agents";
    public static final String NOTFICATIONS="Notifications";
    public static final String BASE="Base";
    public static final String CHECK_LAST_HOURS="check-last-hours";
    
    /*
        <info-check>
        <output type="email" format="gilbert.solorzano@appdynamics.com"/>
        <input type="file" format="csv">baseFile.csv</input>
       </info-check> 
    */
    public static final String INFO_CHECK="info-check";
    public static final String OUTPUT="output";
    public static final String INPUT="input";
    public static final String FORMAT="format";
    public static final String FILE="file";
    
    /*
    mail-info
    */
    public static final String MAIL_INFO="mail-info";
    public static final String MAIL_AUTH="mail-auth";
    public static final String MAIL_USER="mail-user";
    public static final String MAIL_PASSWD="mail-passwd";
    public static final String MAIL_HOST="mail-host";
    public static final String MAIL_PORT="mail-port";
    
    public static final String HELP_L="help";
    public static final String HELP_S="h";
    public static final boolean HELP_R=false;
    public static final boolean HELP_A=false;
    public static final String HELP_D="Prints out the help of the tool";
    public static final String HELP_V="\n\tUsage java -cp \"execLib/*\" ";
    public static boolean HELP_FND=false;
    
    public static final String VERSION_L="version";
    public static final String VERSION_S="v";
    public static final boolean VERSION_R=false;
    public static final boolean VERSION_A=false;
    public static final String VERSION_D="Prints out the version of the tool";
    public static final String VERSION_V="1.0.1";
    public static boolean VERSION_FND=false;
    
    public static final String CFG_FILE_L="config";
    public static final String CFG_FILE_S="C";
    public static final String CFG_FILE_D="";
    public static final boolean CFG_FILE_R=false;
    public static final boolean CFG_FILE_A=true;
    public static String CFG_FILE_V;
    
    public static final String BASE_FILE_L="base";
    public static final String BASE_FILE_S="b";
    public static final String BASE_FILE_D="";
    public static final boolean BASE_FILE_R=false;
    public static final boolean BASE_FILE_A=true;
    public static String BASE_FILE_V;
    public static boolean BASE_FILE_FND=false;
    
    public static final String MAIL_L="mail_test";
    public static final String MAIL_S="m";
    public static final String MAIL_D="";
    public static final boolean MAIL_R=false;
    public static final boolean MAIL_A=false;
    public static String MAIL_V;
    public static boolean MAIL_FND=false;
    
    public static String LOG_FILE="LicenseCheck.log";
    
    
    public static final String OPTION_ERROR_1="A required parameter was not found. Please view the help menu for required parameters.";
    public static final String USAGE_V="\n\tUsage java -cp \"execLib/*\" org.appdynamics.licenseCheck.LicenseCheck [-h|-v] -c <cfg-file> [-b <new-base-file>]";
    public static  boolean DEBUG=false;
    
    public static final String REST_TYPE="LicenseEvent";
    public static final String REST_COMMENT="Automated check";
    
}
