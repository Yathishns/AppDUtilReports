/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.appdynamics.utilreports.resources;

import org.appdynamics.utilreports.resources.AppDUtilReportS;

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
 * 
 * For now we are going to keep simple
 * 
 *  -c | --cfg
 *  -h | --help
 *  -v | --version
 * 
 * 
 */
public class HCLoadOptions {
    private static Logger logger=Logger.getLogger(HCLoadOptions.class.getName());
    private static Options options=new Options();
    private String[] arguments;
    private CommandLine cmdLine;
    
    public HCLoadOptions(String[] arguments){
        this.arguments=arguments;
        init();
    }
    
    
    public static void init(){
        Option cfgFile = OptionBuilder.withLongOpt(AppDUtilReportS.CFG_FILE_L)
                                .withArgName( AppDUtilReportS.CFG_FILE_S )
                                .hasArg()
                                .withDescription( AppDUtilReportS.CFG_FILE_D )
                                .create( AppDUtilReportS.CFG_FILE_S );
        options.addOption(cfgFile);
                
        options.addOption(AppDUtilReportS.HELP_S, AppDUtilReportS.HELP_L, AppDUtilReportS.HELP_A, AppDUtilReportS.HELP_D);
        options.addOption(AppDUtilReportS.VERSION_S, AppDUtilReportS.VERSION_L, AppDUtilReportS.VERSION_A, AppDUtilReportS.VERSION_D);
        
        
        /*
        Option help = OptionBuilder.withLongOpt(AppDUtilReportS.HELP_L).withArgName( AppDUtilReportS.HELP_S )
                                .hasArg()
                                .withDescription(  AppDUtilReportS.HELP_D )
                                .create( AppDUtilReportS.HELP_S );
        options.addOption(help);
        
        
        Option version = OptionBuilder.withLongOpt(AppDUtilReportS.VERSION_L).withArgName( AppDUtilReportS.VERSION_S )
                                .hasArg()
                                .withDescription( AppDUtilReportS.VERSION_D )
                                .create( AppDUtilReportS.VERSION_S );
        options.addOption(version);
        */
    }
    
    public boolean parse(){
            CommandLineParser parser = new GnuParser();
        try {
            // parse the command line arguments
            cmdLine = parser.parse( options, arguments );
            
            //Optional options
            if(cmdLine.hasOption(AppDUtilReportS.HELP_L) || cmdLine.hasOption(AppDUtilReportS.HELP_S)){
                AppDUtilReportS.HELP_FND=true;
                return true;
            }

            if(cmdLine.hasOption(AppDUtilReportS.VERSION_L) || cmdLine.hasOption(AppDUtilReportS.VERSION_S)){
                AppDUtilReportS.VERSION_FND=true;  
                return true;
            }

            /* Required */
            if(!cmdLine.hasOption(AppDUtilReportS.CFG_FILE_L) || !cmdLine.hasOption(AppDUtilReportS.CFG_FILE_S)){
                logger.log(Level.INFO, AppDUtilReportS.OPTION_ERROR_1 + " 1");
                return false;
            }else{ AppDUtilReportS.CFG_FILE_V=cmdLine.getOptionValue(AppDUtilReportS.CFG_FILE_S);}


        }
        catch( ParseException exp ) {
            // oops, something went wrong
            logger.log(Level.SEVERE, new StringBuilder().append("Parsing failed.  Reason: ").append(exp.getMessage()).append("\n\n").toString() );
            return false;
        }
        return true;
    }
    
    public void printHelp(){
        
        new HelpFormatter().printHelp(AppDUtilReportS.USAGE_V, options);
    }
}
