/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.appdynamics.utilreports.actions;

import org.appdynamics.utilreports.conf.BTInfo;
import org.appdynamics.appdrestapi.RESTAccess;
import org.appdynamics.appdrestapi.data.*;
import org.appdynamics.utilreports.logging.JLog_Formatter;

import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author gilbert.solorzano
 * 
 * This is going to be used to execute on all of the BT
 */
public class BTExecutor implements Runnable{
    private static Logger logger=Logger.getLogger(BTExecutor.class.getName());
    private static RESTAccess access;
    
    static {
        setupLogger();
    }
    
    private BTInfo btInfo;
    private String appId;
    private long start;
    private long end;
    
    public BTExecutor(BTInfo btInfo, RESTAccess access, String appId, long start, long end){
        this.access=access;
        this.btInfo=btInfo;
        this.appId=appId;
        this.start=start;
        this.end=end;
        
    }
    
    @Override
    public void run(){
        long value = 0;
        try{
                 value = getRollUpValue(access.getRESTBTMetricQuery(5, appId, btInfo.getBt().getTierName(), btInfo.getBt().getName(), start, end, true));
                 if(value == -1){
                     logger.log(Level.WARNING,new StringBuilder().append("Query returned null for ")
                             .append(btInfo.getBt().getName()).append(" the old value was ").append(btInfo.getValue()).toString());
                 }
        }catch(Exception e){
            logger.log(Level.WARNING,new StringBuilder().append("Exception running ").append(btInfo.toString()).toString());
        }
        //logger.log(Level.INFO, new StringBuilder().append("\n\tCompleted work on ").append(btInfo.getBt().getName()).append(" value of ").append(value).append(" compared to ").append(minBT).toString());
        btInfo.setValue(value);
    }
    
    private long getRollUpValue(MetricDatas mds){
        
        if(mds != null && mds.getSingleRollUpMetricValue() != null){
            return mds.getSingleRollUpMetricValue().getValue();
        }

        return -1;
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
