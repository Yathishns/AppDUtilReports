/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.appdynamics.utilreports.actions;

import org.appdynamics.utilreports.conf.QInfo;
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
 */
public class BKExecutor  implements Runnable{
    private static Logger logger=Logger.getLogger(BKExecutor.class.getName());
    private static RESTAccess access;
    
    static {
        setupLogger();
    }
    
    private QInfo qInfo;
    private String appId;
    private long start;
    private long end;
    private long minBT;
    
    public BKExecutor(QInfo qInfo, RESTAccess access, String appId, long start, long end){
        this.access=access;
        this.qInfo=qInfo;
        this.appId=appId;
        this.start=start;
        this.end=end;
        this.minBT=minBT;
        
    }
    
    @Override
    public void run(){
        long value = 0;
        try{
                 value = getRollUpValue(access.getRESTBackendMetricQuery(1, appId, qInfo.getName(), start, end, true));
                 if(value == -1){
                     logger.log(Level.WARNING,new StringBuilder().append("Query returned null for ")
                             .append(qInfo.getName()).append(" the old value was ").append(qInfo.getValue()).toString());
                 }
        }catch(Exception e){
            logger.log(Level.WARNING,new StringBuilder().append("Exception running ").append(qInfo).toString());
        }
        
        qInfo.setValue(value);
    }
    
    private long getRollUpValue(MetricDatas mds){
        
        if(mds != null && mds.getSingleRollUpMetricValue() != null)
            return mds.getSingleRollUpMetricValue().getValue();
        
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
