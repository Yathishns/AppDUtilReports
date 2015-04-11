/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.appdynamics.utilreports;

import org.appdynamics.utilreports.util.*;
import org.appdynamics.utilreports.resources.*;
import org.appdynamics.utilreports.conf.*;
import org.appdynamics.utilreports.logging.JLog_Formatter;


import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Calendar;


/**
 *
 * @author gilbert.solorzano
 * 
 * This is going to be a very simple executable, we just need to get the xml
 * file, then we execute the commands within it.
 * 
 */
public class ControllerHC {
    private static Logger logger=Logger.getLogger(ControllerHC.class.getName());   
                
    public static void main(String[] args){
        long start = Calendar.getInstance().getTimeInMillis();
        HCLoadOptions opts = new HCLoadOptions(args);
        boolean CP = opts.parse();

        if(!CP){
            //We had an exception
            opts.printHelp();
            System.exit(1);
        }
        setupLogger();
        if(AppDUtilReportS.VERSION_FND){System.out.println(AppDUtilReportS.VERSION_V); System.exit(0);}
        if(AppDUtilReportS.HELP_FND){opts.printHelp();System.exit(0);}
        
        
        logger.log(Level.INFO,new StringBuilder().append("Parsing XML file ")
                .append(AppDUtilReportS.CFG_FILE_V).append(".").toString());
        
        ReadConfig cfg = new ReadConfig(AppDUtilReportS.CFG_FILE_V);
        //logger.log(Level.INFO,"Processing excel file...");
        ProcessXMLConfig process=new ProcessXMLConfig(cfg.getReport());
        try{
            process.init();

        }catch(Exception e){
            logger.log(Level.SEVERE,e.getMessage());
        }
        logger.log(Level.INFO,new StringBuilder().append("Total time ").append((Calendar.getInstance().getTimeInMillis()-start)/1000).toString());
  
    }
    
    
    public static void setupLogger(){
        Handler fileH;
        Formatter formatter;
        try{
            fileH = new FileHandler("./ControllerHC.log", 1024*1024*32,1,true);
            formatter = new JLog_Formatter();
            //logger.getHandlers()[0].setFormatter(formatter);
            logger.addHandler(fileH);
            
            fileH.setFormatter(formatter);
            fileH.setLevel(Level.ALL);
            logger.setLevel(Level.ALL);
            
        }catch(Exception e){logger.log(Level.SEVERE,new StringBuilder().append("Exception e: ").append(e.getMessage()).toString());}
    }
    
    
}
