/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.appdynamics.utilreports.util;


import org.appdynamics.appdrestapi.RESTAccess;
import org.appdynamics.appdrestapi.data.*;
import org.appdynamics.appdrestapi.util.MyCalendar;
import org.appdynamics.utilreports.actions.*;
import org.appdynamics.utilreports.conf.QInfo;
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
public class GatherEUMInfo extends GatherInfo implements Runnable{
    private static Logger logger=Logger.getLogger(GatherEUMInfo.class.getName());
    private RESTAccess access;
    private String appId;
    private long min;
    private long min24;
    private long min48;
    private long start;
    private long end;
    
    private int type;
    private ArrayList<QInfo> eumInfoList=new ArrayList<QInfo>();
    private HashMap<String,Long> normalEUM=new HashMap<String,Long>();
    private HashMap<String,Long> _4HourEUM=new HashMap<String,Long>();
    private HashMap<String,Long> _24HourEUM=new HashMap<String,Long>();
    private HashMap<String,Long> _48HourEUM=new HashMap<String,Long>();  
        
    static {
        setupLogger();
    }
    
    private Calendar cal;
    
    public GatherEUMInfo(RESTAccess access,String appId, long min, int type){
        super();
        this.access=access;
        this.appId=appId;
        this.min=min;
        this.min24=min*2;
        this.min48=min*3;
        setType(type);
        
    }
    
    public GatherEUMInfo(RESTAccess access, String appId, long min, long min24, long min48, int type){
        super();
        this.access=access;
        this.appId=appId;
        this.min=min;
        this.min24=min24;
        this.min48=min48;
        setType(type);
    }
    
    @Override
    public void run(){
        logger.log(Level.INFO,new StringBuilder().append("The value of run is ").append(run).toString());
        if(run == 0){run4Hours(); }
        if(run == 1){run24Hours();}
        if(run == 2) {run48Hours();}
         run++;
    }
    
    @Override
    public void run2(){
        //run24Hours();
        run48Hours();
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        if(type >= 0 && type <= 2){this.type = type;}
        else{type=0;}
    }
    
    public String getPath(){
        if(type == 2) return "End User Experience|Iframes";
        if(type == 1) return "End User Experience|Base Pages";
        return "End User Experience|AJAX Requests";
    }
    
    public String getName(){
        if(type == 2) return "Iframes";
        if(type == 1) return "Base Pages";
        return "AJAX Requests";
    }
    
    public int getIndex(){
        if(type == 2) return 57;
        if(type == 1) return 22;
        return 0;
    }
    
    public void run4Hours(){
        logger.log(Level.INFO,new StringBuilder().append("Starting query for ").append(getName()).append(" ").toString());
        
        long _timeS=Calendar.getInstance().getTimeInMillis();
        
        MetricItems eum = access.getBaseMetricListPath(appId, getPath());
        if(eum == null){
            logger.log(Level.SEVERE,new StringBuilder().append("EUM path for ").append(getPath()).append(" return null.").toString());
            return;
        }
         
         cal = MyCalendar.getCalendar();
         end = cal.getTimeInMillis();
         cal.add(Calendar.HOUR, -4);
         start = cal.getTimeInMillis();
         
         logger.log(Level.INFO,new StringBuilder().append("Starting collection of last 4 hours ").append(getName())
                 .append(" request counts ").toString());
         
         for(MetricItem mt: eum.getMetricItems()){
             eumInfoList.add(new QInfo(mt.getName()));
         }
         
         ThreadExecutor threadExec=new ThreadExecutor(threadNumN);
         for(QInfo eu:eumInfoList){
             EUMExecutor btExec=new EUMExecutor(eu,access,appId,start,end,getIndex());
             threadExec.getExecutor().execute(btExec);
         }
         
         threadExec.getExecutor().shutdown();
         threadExec.shutdown();
         
        
         Iterator<QInfo> tempBKs=eumInfoList.iterator();
         
         while(tempBKs.hasNext()){
             QInfo bt = tempBKs.next();
 
            if(bt.getValue() > min){
                normalEUM.put(bt.getName(),bt.getValue());
                tempBKs.remove();
            }else{
                if(bt.getValue() == -1){
                    _48HourEUM.put(bt.getName(), bt.getValue());
                    tempBKs.remove();
                }
            }
             

         }

         long _endT=(Calendar.getInstance().getTimeInMillis()-_timeS)/1000;
         
         logger.log(Level.INFO,new StringBuilder().append("Completed collection of last 4 hours ").append(getName())
                 .append(" request counts in ").append(_endT).append(" seconds.").toString());
    }
    
    public void run24Hours(){
        
         cal.add(Calendar.HOUR, -20);
         start = cal.getTimeInMillis();
         
         logger.log(Level.INFO,new StringBuilder().append("Starting collection of last 24 hours ").append(getName())
                 .append(" request counts ").toString());
         
         long _timeS=Calendar.getInstance().getTimeInMillis();
         
         ThreadExecutor threadExec=new ThreadExecutor(threadNumM);
         for(QInfo eu:eumInfoList){
             EUMExecutor btExec=new EUMExecutor(eu,access,appId,start,end,getIndex());
             threadExec.getExecutor().execute(btExec);
         }
         
         threadExec.getExecutor().shutdown();
         threadExec.shutdown();
         
         Iterator<QInfo> tempBKs=eumInfoList.iterator();
         
         while(tempBKs.hasNext()){
             QInfo bt = tempBKs.next();
 
            if(bt.getValue() > min24){
                _4HourEUM.put(bt.getName(),bt.getValue());
                tempBKs.remove();
            }else{
                if(bt.getValue() == -1){
                    _48HourEUM.put(bt.getName(), bt.getValue());
                    tempBKs.remove();
                }
            }
             

         }
         
         long _endT=(Calendar.getInstance().getTimeInMillis()-_timeS)/1000;
         logger.log(Level.INFO,new StringBuilder().append("Completed collection of last 24 hours ").append(getName())
                 .append(" request counts in ").append(_endT).append(" seconds.").toString());
    }
    
    public void run48Hours(){
        Iterator<String> eumIter=_24HourEUM.keySet().iterator();
         cal.add(Calendar.HOUR, -24);
         start = cal.getTimeInMillis();
         
         logger.log(Level.INFO,new StringBuilder().append("Starting collection of last 48 hours ").append(getName())
                 .append(" request counts ").toString());
         
         long _timeS=Calendar.getInstance().getTimeInMillis();
         
         ThreadExecutor threadExec=new ThreadExecutor(threadNum);
         for(QInfo eu:eumInfoList){
             EUMExecutor btExec=new EUMExecutor(eu,access,appId,start,end,getIndex());
             threadExec.getExecutor().execute(btExec);
         }
         
         threadExec.getExecutor().shutdown();
         threadExec.shutdown();
         
         Iterator<QInfo> tempBKs=eumInfoList.iterator();
         
         while(tempBKs.hasNext()){
             QInfo bt = tempBKs.next();
 
            if(bt.getValue() > min48){
                _24HourEUM.put(bt.getName(),bt.getValue());
                tempBKs.remove();
            }else{
                _48HourEUM.put(bt.getName(),bt.getValue());
                tempBKs.remove();
            }
             

         }

         long _endT=(Calendar.getInstance().getTimeInMillis()-_timeS)/1000;
         
         logger.log(Level.INFO,new StringBuilder().append("Completed collection of last 48 hours ").append(getName())
                 .append(" request counts in ").append(_endT).append(" seconds.").toString());
    }
    
    public String printData(){
        StringBuilder msg = new StringBuilder();
         msg.append("\nThe number of normal " + getName() + " is " + normalEUM.size());
         msg.append("\nThe number of " + getName() + " with very few entries in 4 hours " + _4HourEUM.size());

         for(String key: _4HourEUM.keySet()){
             StringBuilder bud = new StringBuilder();
             bud.append("\n" + getName() + " name = '").append(key).append("'|Number of call in 48 hours is ").append(_4HourEUM.get(key));
             msg.append(bud.toString());
         }
         
  
         msg.append("\n\nThe number of " + getName() + " with very few entries in 24 hours " + _24HourEUM.size());
         for(String key: _24HourEUM.keySet()){
             StringBuilder bud = new StringBuilder();
             bud.append("\n" + getName() + " name = '").append(key).append("'|Number of call in 48 hours is ").append(_24HourEUM.get(key));
             msg.append(bud.toString());
         }
         
         
         msg.append("\n\nThe number of " + getName() + " with very few entries in 48 hours " + _48HourEUM.size());
         for(String key: _48HourEUM.keySet()){
             StringBuilder bud = new StringBuilder();
             bud.append("\n" + getName() + " name = '").append(key).append("'|Number of call in 48 hours is ").append(_48HourEUM.get(key));
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

    public HashMap<String, Long> getNormalEUM() {
        return normalEUM;
    }

    public void setNormalEUM(HashMap<String, Long> normalEUM) {
        this.normalEUM = normalEUM;
    }

    public HashMap<String, Long> get4HourEUM() {
        return _4HourEUM;
    }

    public void set4HourEUM(HashMap<String, Long> _4HourEUM) {
        this._4HourEUM = _4HourEUM;
    }

    public HashMap<String, Long> get24HourEUM() {
        return _24HourEUM;
    }

    public void set24HourEUM(HashMap<String, Long> _24HourEUM) {
        this._24HourEUM = _24HourEUM;
    }

    public HashMap<String, Long> get48HourEUM() {
        return _48HourEUM;
    }

    public void set48HourEUM(HashMap<String, Long> _48HourEUM) {
        this._48HourEUM = _48HourEUM;
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
