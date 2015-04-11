/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.appdynamics.backup.util;

import org.appdynamics.backup.resources.BackupConfigS;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.HelpFormatter;


import java.util.logging.Logger;
import java.util.logging.Level;


/**
 *
 * @author gilbert.solorzano
 */
public class BackupConfigurationOptions {
    private static Logger logger=Logger.getLogger(BackupConfigurationOptions.class.getName());
    private static Options options=new Options();
    private String[] arguments;
    private CommandLine cmdLine;
    
    public BackupConfigurationOptions(String[] arguments){
        this.arguments=arguments;
        init();
    }
    
    
    public static void init(){
        Option controller = OptionBuilder.withLongOpt(BackupConfigS.CONTROLLER_L)
                                .withArgName( BackupConfigS.CONTROLLER_S )
                                .hasArg()
                                .withDescription( BackupConfigS.CONTROLLER_D )
                                .create( BackupConfigS.CONTROLLER_S );
        
        options.addOption(controller);
        Option port = OptionBuilder.withLongOpt(BackupConfigS.PORT_L).withArgName( BackupConfigS.PORT_S )
                                .hasArg()
                                .withDescription(  BackupConfigS.PORT_D )
                                .create( BackupConfigS.PORT_S );
        options.addOption(port);
        Option account = OptionBuilder.withLongOpt(BackupConfigS.ACCOUNT_L).withArgName( BackupConfigS.ACCOUNT_S )
                                .hasArg()
                                .withDescription( BackupConfigS.ACCOUNT_D )
                                .create( BackupConfigS.ACCOUNT_S );
        options.addOption(account);
        Option username = OptionBuilder.withLongOpt(BackupConfigS.USERNAME_L).withArgName( BackupConfigS.USERNAME_S )
                                .hasArg()
                                .withDescription( BackupConfigS.USERNAME_D )
                                .create( BackupConfigS.USERNAME_S );
        options.addOption(username);
        
        Option passwd = OptionBuilder.withLongOpt(BackupConfigS.PASSWD_L).withArgName( BackupConfigS.PASSWD_S )
                                .hasArg()
                                .withDescription( BackupConfigS.PASSWD_D )
                                .create( BackupConfigS.PASSWD_S );
        options.addOption(passwd);
        
        options.addOption(BackupConfigS.SSL_S, BackupConfigS.SSL_L, BackupConfigS.SSL_A, BackupConfigS.SSL_D);
        
        Option dir = OptionBuilder.withLongOpt(BackupConfigS.DIRECTORY_L).withArgName( BackupConfigS.DIRECTORY_S )
                                .hasArg()
                                .withDescription( BackupConfigS.DIRECTORY_D )
                                .create( BackupConfigS.DIRECTORY_S );
        options.addOption(dir);
        
    }
    
    
    public boolean parse(){
        CommandLineParser parser = new GnuParser();
        boolean printHelp=false;
        try{
            cmdLine = parser.parse(options, arguments);
            
           /* Required */
            if(!cmdLine.hasOption(BackupConfigS.USERNAME_L) || !cmdLine.hasOption(BackupConfigS.USERNAME_S)){
                logger.log(Level.INFO, new StringBuilder().append(BackupConfigS.OPTION_ERROR_1).append(" ").append(BackupConfigS.USERNAME_L).toString());
                return false;
            }else{ BackupConfigS.USERNAME_V=cmdLine.getOptionValue(BackupConfigS.USERNAME_S);}

            if(!cmdLine.hasOption(BackupConfigS.PASSWD_L) || !cmdLine.hasOption(BackupConfigS.PASSWD_S)){
                logger.log(Level.INFO, new StringBuilder().append(BackupConfigS.OPTION_ERROR_1).append(" ").append(BackupConfigS.PASSWD_L).toString());
                return false;
            }else{ BackupConfigS.PASSWD_V=cmdLine.getOptionValue(BackupConfigS.PASSWD_S);}
            
            if(!cmdLine.hasOption(BackupConfigS.CONTROLLER_L) || !cmdLine.hasOption(BackupConfigS.CONTROLLER_S)){
                logger.log(Level.INFO, new StringBuilder().append(BackupConfigS.OPTION_ERROR_1).append(" ").append(BackupConfigS.CONTROLLER_L).toString());
                return false;
            }else{ BackupConfigS.CONTROLLER_V=cmdLine.getOptionValue(BackupConfigS.CONTROLLER_S);}
            
            if(!cmdLine.hasOption(BackupConfigS.PORT_L) || !cmdLine.hasOption(BackupConfigS.PORT_S)){
                logger.log(Level.INFO, new StringBuilder().append(BackupConfigS.OPTION_ERROR_1).append(" ").append(BackupConfigS.PORT_L).toString());
                return false;
            }else{ BackupConfigS.PORT_V=cmdLine.getOptionValue(BackupConfigS.PORT_S);}

            //Optional options
            if(cmdLine.hasOption(BackupConfigS.DIRECTORY_L) || cmdLine.hasOption(BackupConfigS.DIRECTORY_S)){
                BackupConfigS.DIRECTORY_V=cmdLine.getOptionValue(BackupConfigS.DIRECTORY_S);
            }
            
             if(cmdLine.hasOption(BackupConfigS.ACCOUNT_L) || cmdLine.hasOption(BackupConfigS.ACCOUNT_S)){
                BackupConfigS.ACCOUNT_V=cmdLine.getOptionValue(BackupConfigS.ACCOUNT_S);
             }
             
             if(cmdLine.hasOption(BackupConfigS.SSL_L) || cmdLine.hasOption(BackupConfigS.SSL_S)){
                BackupConfigS.SSL_V=true;
             }
            
        }catch(ParseException pex){
            logger.log(Level.SEVERE,new StringBuilder().append("Parsing failed. Reason: ").append(pex.getMessage()).toString());
            return false;
        }
        return true;
    }

    public static Logger getLogger() {
        return logger;
    }

    public static void setLogger(Logger logger) {
        BackupConfigurationOptions.logger = logger;
    }

    public static Options getOptions() {
        return options;
    }

    public static void setOptions(Options options) {
        BackupConfigurationOptions.options = options;
    }

    public String[] getArguments() {
        return arguments;
    }

    public void setArguments(String[] arguments) {
        this.arguments = arguments;
    }

    public CommandLine getCmdLine() {
        return cmdLine;
    }

    public void setCmdLine(CommandLine cmdLine) {
        this.cmdLine = cmdLine;
    }
    
    
}
