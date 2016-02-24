/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.appdynamics.markH;

import org.appdynamics.markH.resources.*;
import org.appdynamics.markH.util.*;
import org.appdynamics.markH.file.*;

import org.appdynamics.appdrestapi.RESTAccess2;
import org.appdynamics.appdrestapi.data.*;
import org.appdynamics.utilreports.resources.AppDUtilReportS;

import java.util.ArrayList;
import java.util.logging.*;
import java.util.Calendar;
import org.appdynamics.utilreports.logging.JLog_Formatter;
import org.appdynamics.utilreports.resources.HCLoadOptions;


/**
 *
 * @author gilbert.solorzano
 *  -h help
 *  -A <String|Applications>
 *  -t <String|regex>
 *  -n <String|regex>
 *  -i <Integer|hours>
 *  -l <Boolean|log only>
 * 
 */
public class AppDMH {
    private static Logger logger=Logger.getLogger(AppDMH.class.getName());
    private static ArrayList<MarkedNode> markedNodes=new ArrayList<MarkedNode>();
    
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
        //if(AppDUtilReportS.VERSION_FND){System.out.println(AppDUtilReportS.VERSION_V); System.exit(0);}
        //if(AppDUtilReportS.HELP_FND){opts.printHelp();System.exit(0);}
        
        
        logger.log(Level.INFO,new StringBuilder().append("Parsing XML file ")
                .append(AppDUtilReportS.CFG_FILE_V).append(".").toString());
        
        
        ReadConfig cfg = new ReadConfig(AppDUtilReportS.CFG_FILE_V);
        ProcessXML process = new ProcessXML(cfg.getReport());
        try{
            process.init();
        }catch(Exception e){
            logger.log(Level.SEVERE,new StringBuilder().append("Exception occurred: ").append(e.getMessage()).toString());
        }
        
        logger.log(Level.INFO, "Completed the mark historical process in " + ((Calendar.getInstance().getTimeInMillis() - start)/1000) + " seconds.");
        
        
        
    }
    
    public static void setupLogger(){
        Handler fileH;
        Formatter formatter;
        try{
            fileH = new FileHandler("./MarkHistorical.log", 1024*1024*32,1,true);
            formatter = new JLog_Formatter();
            //logger.getHandlers()[0].setFormatter(formatter);
            logger.addHandler(fileH);
            
            fileH.setFormatter(formatter);
            fileH.setLevel(Level.ALL);
            logger.setLevel(Level.ALL);
            
        }catch(Exception e){logger.log(Level.SEVERE,new StringBuilder().append("Exception e: ").append(e.getMessage()).toString());}
    }
    
    public static String[] parse(String metricPath){
        
        if(metricPath != null){
            return metricPath.split("\\|");
        }
        return null;
    }
}
