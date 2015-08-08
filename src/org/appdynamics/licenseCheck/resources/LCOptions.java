/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.appdynamics.licenseCheck.resources;

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
public class LCOptions {
    private static Logger logger=Logger.getLogger(LCOptions.class.getName());
    private static Options options=new Options();
    private String[] arguments;
    private CommandLine cmdLine;
    
    
    public LCOptions(String[] args){arguments=args;init();}
    
    private void init(){
        Option cfgFile = OptionBuilder.withLongOpt(LicenseCheckS.CFG_FILE_L)
                                .withArgName( LicenseCheckS.CFG_FILE_S )
                                .hasArg()
                                .withDescription( LicenseCheckS.CFG_FILE_D )
                                .create( LicenseCheckS.CFG_FILE_S );
        options.addOption(cfgFile);
                
        Option baseFile = OptionBuilder.withLongOpt(LicenseCheckS.BASE_FILE_L)
                                .withArgName(LicenseCheckS.BASE_FILE_S)
                                .hasArg()
                                .withDescription(LicenseCheckS.BASE_FILE_D)
                                .create(LicenseCheckS.BASE_FILE_S);
        options.addOption(baseFile);
                

        
        options.addOption(LicenseCheckS.MAIL_S,LicenseCheckS.MAIL_L,LicenseCheckS.MAIL_A,LicenseCheckS.HELP_D);
        options.addOption(LicenseCheckS.HELP_S, LicenseCheckS.HELP_L, LicenseCheckS.HELP_A, LicenseCheckS.HELP_D);
        options.addOption(LicenseCheckS.VERSION_S, LicenseCheckS.VERSION_L, LicenseCheckS.VERSION_A, LicenseCheckS.VERSION_D);
    }
    
    public boolean parse(){
            CommandLineParser parser = new GnuParser();
        try {
            // parse the command line arguments
            cmdLine = parser.parse( options, arguments );
            
            //Optional options
            if(cmdLine.hasOption(LicenseCheckS.HELP_L) || cmdLine.hasOption(LicenseCheckS.HELP_S)){
                LicenseCheckS.HELP_FND=true;
                return true;
            }

            if(cmdLine.hasOption(LicenseCheckS.VERSION_L) || cmdLine.hasOption(LicenseCheckS.VERSION_S)){
                LicenseCheckS.VERSION_FND=true;  
                return true;
            }
            
            if(cmdLine.hasOption(LicenseCheckS.BASE_FILE_L) || cmdLine.hasOption(LicenseCheckS.BASE_FILE_S)){
                LicenseCheckS.BASE_FILE_FND=true;
                LicenseCheckS.BASE_FILE_V=cmdLine.getOptionValue(LicenseCheckS.BASE_FILE_S);
            }
            
            if(cmdLine.hasOption(LicenseCheckS.MAIL_L) || cmdLine.hasOption(LicenseCheckS.MAIL_S)){
                LicenseCheckS.MAIL_FND=true;
                LicenseCheckS.MAIL_V=cmdLine.getOptionValue(LicenseCheckS.MAIL_S);
            }

            /* Required */
            if(!cmdLine.hasOption(LicenseCheckS.CFG_FILE_L) || !cmdLine.hasOption(LicenseCheckS.CFG_FILE_S)){
                logger.log(Level.INFO, LicenseCheckS.OPTION_ERROR_1 + " 1");
                return false;
            }else{ LicenseCheckS.CFG_FILE_V=cmdLine.getOptionValue(LicenseCheckS.CFG_FILE_S);}


        }
        catch( ParseException exp ) {
            // oops, something went wrong
            logger.log(Level.SEVERE, new StringBuilder().append("Parsing failed.  Reason: ").append(exp.getMessage()).append("\n\n").toString() );
            return false;
        }
        return true;
    }
    
    public void printHelp(){
        
        new HelpFormatter().printHelp(LicenseCheckS.USAGE_V, options);
    }
    
}
