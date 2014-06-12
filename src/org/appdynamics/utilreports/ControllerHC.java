/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.appdynamics.utilreports;

import org.appdynamics.utilreports.util.*;
import org.appdynamics.utilreports.resources.*;
import org.appdynamics.utilreports.conf.*;

import org.appdynamics.appdrestapi.RESTAccess;
import org.appdynamics.appdrestapi.data.*;


import java.util.logging.Logger;
import java.util.logging.Level;

import java.util.HashMap;
import java.util.Calendar;
import java.util.Set;
import java.util.Iterator;

/**
 *
 * @author gilbert.solorzano
 */
public class ControllerHC {
    private static Logger logger=Logger.getLogger(ControllerHC.class.getName());
        private static int minBT=50;
        private static int minBK=45;    
        
        
    public static void main(String[] args){
        
        HCLoadOptions opts = new HCLoadOptions(args);
        boolean CP = opts.parse();

        if(!CP){
            //We had an exception
            opts.printHelp();
            System.exit(1);
        }
        
        if(AppDUtilReportS.VERSION_FND){System.out.println(AppDUtilReportS.VERSION_V); System.exit(0);}
        if(AppDUtilReportS.HELP_FND){opts.printHelp();System.exit(0);}
        
        
        logger.log(Level.INFO,new StringBuilder().append("Parsing XML file ").append(AppDUtilReportS.CFG_FILE_V).append(" ")
                .append(Calendar.getInstance().getTime().toString()).toString());
        ReadConfig cfg = new ReadConfig(AppDUtilReportS.CFG_FILE_V);
        
        ProcessXMLConfig process=new ProcessXMLConfig(cfg.getReport());
        try{
            process.init();

        }catch(Exception e){
            logger.log(Level.SEVERE,e.getMessage());
        }
        
  
    }
    
    
}
