/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.appdynamics.utilreports.util;

import org.appdynamics.appdrestapi.RESTAccess;
import org.appdynamics.appdrestapi.data.*;
import org.appdynamics.appdrestapi.util.MyCalendar;
import org.appdynamics.utilreports.conf.QInfo;
import org.appdynamics.utilreports.actions.*;
import org.appdynamics.utilreports.logging.JLog_Formatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Calendar;
import java.util.Iterator;

import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gilbert.solorzano
 */
public class GatherBKInfo extends GatherInfo implements Runnable{
    private static Logger logger=Logger.getLogger(GatherBKInfo.class.getName());
    private RESTAccess access;
    private String appId;
    private long minBK;
    private long minBK24;
    private long minBK48;
    private long start;
    private long end;
    private ArrayList<QInfo> bkInfoList=new ArrayList<QInfo>();
    private HashMap<String,Long> normalBK=new HashMap<String,Long>();
    private HashMap<String,Long> _4HourBK=new HashMap<String,Long>();
    private HashMap<String,Long> _24HourBK=new HashMap<String,Long>();
    private HashMap<String,Long> _48HourBK=new HashMap<String,Long>();    
    private Calendar cal;
    
    static {
        setupLogger();
    }
    
    public GatherBKInfo(RESTAccess access,String appId, long minBK){
        super();
        this.access=access;
        this.appId=appId;
        this.minBK=minBK;
        this.minBK24=minBK*2;
        this.minBK48=minBK*3;
        
    }
    
    public GatherBKInfo(RESTAccess access, String appId, long minBK, long minBK24, long minBK48){
        super();
        this.access=access;
        this.appId=appId;
        this.minBK=minBK;
        this.minBK24=minBK24;
        this.minBK48=minBK48;
    }
    
    @Override
    public void run(){
        logger.log(Level.INFO,new StringBuilder().append("The value of run is ").append(run).toString());
        if(run == 0){run4Hours(); }
        if(run == 1){run24Hours(); }
        if(run == 2) {run48Hours();}
        run++;
    }
    
    @Override
    public void run2(){
        //run24Hours();
        run48Hours();
    }
    
    public void run4Hours(){
        
        logger.log(Level.INFO,new StringBuilder().append("Starting query for backends and 4 hour check").toString());
        
        long _timeS=Calendar.getInstance().getTimeInMillis();
        
        MetricItems exitPoints = access.getBaseMetricListPath(appId, "Backends");

        //gatherInfo.run();
         
         cal = MyCalendar.getCalendar();
         end = cal.getTimeInMillis();
         cal.add(Calendar.HOUR, -4);
         start = cal.getTimeInMillis();
         
         for(MetricItem mt: exitPoints.getMetricItems()){
             bkInfoList.add(new QInfo(mt.getName()));
         }
         
         ThreadExecutor threadExec=new ThreadExecutor(threadNumN);
         for(QInfo bk:bkInfoList){
             BKExecutor btExec=new BKExecutor(bk,access,appId,start,end);
             threadExec.getExecutor().execute(btExec);
         }
         
         threadExec.getExecutor().shutdown();
         threadExec.shutdown();
         
         Iterator<QInfo> tempBKs=bkInfoList.iterator();
         
         while(tempBKs.hasNext()){
             QInfo bt = tempBKs.next();
 
            if(bt.getValue() > minBK){
                normalBK.put(bt.getName(),bt.getValue());
                tempBKs.remove();
            }else{
                if(bt.getValue() == -1){
                    _48HourBK.put(bt.getName(), bt.getValue());
                    tempBKs.remove();
                }
            }
             

         }

         long _endT=(Calendar.getInstance().getTimeInMillis()-_timeS)/1000;
         logger.log(Level.INFO,new StringBuilder().append("Completed query for backends and 4 hour check in ")
                 .append(_endT).append(" seconds.").toString());
    }
    
    public void run24Hours(){
         cal.add(Calendar.HOUR, -20);
         start = cal.getTimeInMillis();
         
         logger.log(Level.INFO,new StringBuilder().append("Starting collection of last 24 hours Backend request counts ").toString());
         
         long _timeS=Calendar.getInstance().getTimeInMillis();
         
         ThreadExecutor threadExec=new ThreadExecutor(threadNumM);
         for(QInfo bk:bkInfoList){
             BKExecutor btExec=new BKExecutor(bk,access,appId,start,end);
             threadExec.getExecutor().execute(btExec);
         }
         
         threadExec.getExecutor().shutdown();
         threadExec.shutdown();
         
  
         Iterator<QInfo> tempBKs=bkInfoList.iterator();
         
         while(tempBKs.hasNext()){
             QInfo bt = tempBKs.next();
 
            if(bt.getValue() > minBK24){
                _4HourBK.put(bt.getName(),bt.getValue());
                tempBKs.remove();
            }else{
                if(bt.getValue() == -1){
                    _48HourBK.put(bt.getName(), bt.getValue());
                    tempBKs.remove();
                }
            }
             

         }

         long _endT=(Calendar.getInstance().getTimeInMillis()-_timeS)/1000;
         logger.log(Level.INFO,new StringBuilder().append("Completed collection of last 24 hours Backend request counts in ")
                 .append(_endT).append(" seconds.").toString());
    }
    
    public void run48Hours(){

         cal.add(Calendar.HOUR, -24);
         start = cal.getTimeInMillis();
         logger.log(Level.INFO,new StringBuilder().append("Starting collection of last 48 hours Backend request counts.").toString());
         
         long _timeS=Calendar.getInstance().getTimeInMillis();
         
         ThreadExecutor threadExec=new ThreadExecutor(threadNum);
         for(QInfo bk:bkInfoList){
             BKExecutor btExec=new BKExecutor(bk,access,appId,start,end);
             threadExec.getExecutor().execute(btExec);
         }
         
         threadExec.getExecutor().shutdown();
         threadExec.shutdown();
         
         Iterator<QInfo> tempBKs=bkInfoList.iterator();
         
         while(tempBKs.hasNext()){
             QInfo bt = tempBKs.next();
 
            if(bt.getValue() > minBK24){
                _24HourBK.put(bt.getName(),bt.getValue());
                tempBKs.remove();
            }else{
                _48HourBK.put(bt.getName(),bt.getValue());
                tempBKs.remove();
            }
             

         }

         long _endT=(Calendar.getInstance().getTimeInMillis()-_timeS)/1000;
         logger.log(Level.INFO,new StringBuilder().append("Completed collection of last 48 hours Backend request counts in ")
                 .append(_endT).append(" seconds.").toString());
    }
    
    public String printData(){
        StringBuilder msg = new StringBuilder();
         msg.append("\n\nThe number of normal backends is " + normalBK.size());
         msg.append("\nThe number of Backends with very few entries in 4 hours " + _4HourBK.size());

         for(String key: _4HourBK.keySet()){
             StringBuilder bud = new StringBuilder();
             bud.append("\nBackend name = '").append(key).append("'|Number of call in 48 hours is ").append(_4HourBK.get(key));
             msg.append(bud.toString());
         }
         
        
         msg.append("\n\nThe number of Backends with very few entries in 24 hours " + _24HourBK.size());
         for(String key: _24HourBK.keySet()){
             StringBuilder bud = new StringBuilder();
             bud.append("\nBackend name = '").append(key).append("'|Number of call in 48 hours is ").append(_24HourBK.get(key));
             msg.append(bud.toString());
         }
         

         msg.append("\n\nThe number of Backends with very few entries in 48 hours " + _48HourBK.size());
         for(String key: _48HourBK.keySet()){
             StringBuilder bud = new StringBuilder();
             bud.append("\nBackend name = '").append(key).append("'|Number of call in 48 hours is ").append(_48HourBK.get(key));
             msg.append(bud.toString());
         }
         return msg.toString();
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public HashMap<String, Long> getNormalBK() {
        return normalBK;
    }

    public void setNormalBK(HashMap<String, Long> normalBK) {
        this.normalBK = normalBK;
    }

    public HashMap<String, Long> get4HourBK() {
        return _4HourBK;
    }

    public void set4HourBK(HashMap<String, Long> _4HourBK) {
        this._4HourBK = _4HourBK;
    }

    public HashMap<String, Long> get24HourBK() {
        return _24HourBK;
    }

    public void set24HourBK(HashMap<String, Long> _24HourBK) {
        this._24HourBK = _24HourBK;
    }

    public HashMap<String, Long> get48HourBK() {
        return _48HourBK;
    }

    public void set48HourBK(HashMap<String, Long> _48HourBK) {
        this._48HourBK = _48HourBK;
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
