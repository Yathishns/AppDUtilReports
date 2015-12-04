/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.appdynamics.licenseCheck;

import org.appdynamics.licenseCheck.action.SendMail;
import org.appdynamics.licenseCheck.util.*;
import org.appdynamics.licenseCheck.resources.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.*;
import org.appdynamics.licenseCheck.action.SendEvent;
import org.appdynamics.utilreports.logging.JLog_Formatter;


/**
 *
 * @author gilbert.solorzano
 */
public class LicenseCheck {
    private static Logger logger=Logger.getLogger(LicenseCheck.class.getName());   
    public static void main(String[] args){
        long start = Calendar.getInstance().getTimeInMillis();
        

        
        setupLogger();
        
        LCOptions lcOptions = new LCOptions(args);
        if( ! lcOptions.parse()){
            lcOptions.printHelp();
            System.exit(1);
        }
        
        if(LicenseCheckS.HELP_FND){System.out.println(LicenseCheckS.HELP_V);System.exit(0);}
        if(LicenseCheckS.VERSION_FND){System.out.println(LicenseCheckS.VERSION_V);System.exit(0);}
        
       
        logger.log(Level.INFO,"Attempting to parse the configuration file {0}",LicenseCheckS.CFG_FILE_V);
        
        
        
        ReadConfig cfg = new ReadConfig(LicenseCheckS.CFG_FILE_V);
        ProcessXMLConfig xmlConfig = new ProcessXMLConfig(cfg.getReport());
        ArrayList<BaseEntry> base=null;
        
        if(LicenseCheckS.MAIL_FND){
            // We are testing the mail portion
            logger.log(Level.INFO,"Found the mail");
            logger.log(Level.INFO,cfg.getReport().getInfoCheck().toString());
            logger.log(Level.INFO,cfg.getReport().getMailInfo().toString());
            SendMail _mail = new SendMail(cfg.getReport().getMailInfo(),cfg.getReport().getInfoCheck().getEmail().getName(),"Test of email");
            logger.log(Level.INFO,"Done sending out the email.");
            System.exit(0);
        }
        
        if(LicenseCheckS.BASE_FILE_FND){
            
            
            // Now we save the file to 
            try{
                xmlConfig.init();
                base = xmlConfig.createBase();
                java.io.FileWriter file = new java.io.FileWriter(new java.io.File(LicenseCheckS.BASE_FILE_V));
                for(BaseEntry b:base) {
                    file.write(b.toString());
                }
                file.close();
            }catch(Exception e){
                logger.log(Level.SEVERE, "Exception occurred: {0}", e.getMessage()); System.exit(1);
            }
            System.exit(0);
        }
        
        try{base= ReadBase.getBaseObjects(cfg.getReport().getInfoCheck().getBase().getName());}
        catch(Exception e){logger.log(Level.SEVERE, "Exception occurred: {0}", e.getMessage()); System.exit(1);}
        
        Messages msgs=null;
        xmlConfig = new ProcessXMLConfig(cfg.getReport(),base);
        
        try{
            xmlConfig.init();
            msgs=xmlConfig.checkTheLicense();
        }catch(Exception e){logger.log(Level.SEVERE, "Exception occurred: {0}", e.getMessage()); System.exit(1);}
        
         
        
        logger.log(Level.INFO,msgs.toString());
        if(cfg.getReport().getInfoCheck().isEmail()){ 
            new SendMail(cfg.getReport().getMailInfo(),cfg.getReport().getInfoCheck().getEmail().getName(),msgs.toString());
        }
        if(cfg.getReport().getInfoCheck().isRest()){
            SendEvent.send(xmlConfig.getAccess(), cfg.getReport().getInfoCheck().getRest().getName(), msgs.toString());
        }
        logger.log(Level.INFO,"\nDone");
        
        long end=(Calendar.getInstance().getTimeInMillis() - start)/1000;
        logger.log(Level.INFO,"Completed the execution in {0} seconds.",end);
    }
    
    public static void setupLogger(){
        Handler fileH;
        Formatter formatter;
        try{
            fileH = new FileHandler(LicenseCheckS.LOG_FILE, 1024*1024*32,1,true);
            formatter = new JLog_Formatter();
            //logger.getHandlers()[0].setFormatter(formatter);
            logger.addHandler(fileH);
            
            fileH.setFormatter(formatter);
            fileH.setLevel(Level.ALL);
            logger.setLevel(Level.ALL);
            
        }catch(Exception e){logger.log(Level.SEVERE,new StringBuilder().append("Exception e: ").append(e.getMessage()).toString());}
    }
    
}
