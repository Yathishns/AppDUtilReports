/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.appdynamics.utilreports.util;


import org.appdynamics.appdrestapi.RESTAccess;
import org.appdynamics.appdrestapi.data.*;

import java.util.HashMap;
import java.util.Calendar;
import java.util.Iterator;

import java.util.logging.Logger;
import java.util.logging.Level;

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
    
    private HashMap<String,Long> normalEUM=new HashMap<String,Long>();
    private HashMap<String,Long> _4HourEUM=new HashMap<String,Long>();
    private HashMap<String,Long> _24HourEUM=new HashMap<String,Long>();
    private HashMap<String,Long> _48HourEUM=new HashMap<String,Long>();  
        
    
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
        run4Hours();
        run24Hours();
        run48Hours();
        //printData();
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
        MetricItems eum = access.getBaseMetricListPath(appId, getPath());
        
         
         cal = Calendar.getInstance();
         end = cal.getTimeInMillis();
         cal.add(Calendar.HOUR, -4);
         start = cal.getTimeInMillis();
         
         logger.log(Level.INFO,new StringBuilder().append("Starting collection of last 4 hours ").append(getName())
                 .append(" request counts ").toString());
         for(MetricItem mt: eum.getMetricItems()){
             long value = 0;
             try{
                 
                 value = getRollUpValue(access.getRESTEUMMetricQuery(getIndex(), appId, mt.getName(), start, end, true));
                 
             }catch(Exception e){
                 logger.log(Level.WARNING,new StringBuilder().append("Exception running ").append(mt.toString()).toString());
             }
             
             if(value > min){
                 normalEUM.put(mt.getName(), value);
             }else{
                 _4HourEUM.put(mt.getName(), value);
             }
         }
         
         
    }
    
    public void run24Hours(){
        Iterator<String> bkIter=_4HourEUM.keySet().iterator();
         cal.add(Calendar.HOUR, -20);
         start = cal.getTimeInMillis();
         
         logger.log(Level.INFO,new StringBuilder().append("Starting collection of last 24 hours ").append(getName())
                 .append(" request counts ").toString());
         while(bkIter.hasNext()){
             String mt = bkIter.next();
             long value = 0;
             try{
                 //Thread.sleep(1300);
                 value = getRollUpValue(access.getRESTEUMMetricQuery(getIndex(), appId, mt, start, end, true));
             }catch(Exception e){
                 logger.log(Level.WARNING,new StringBuilder().append("Exception running ").append(mt.toString()).toString());
             }
             
             if(value < (min24)){
                 _24HourEUM.put(mt, value);
                 bkIter.remove();
             }
         }
         
         
    }
    
    public void run48Hours(){
        Iterator<String> bkIter=_24HourEUM.keySet().iterator();
         cal.add(Calendar.HOUR, -24);
         start = cal.getTimeInMillis();
         
         logger.log(Level.INFO,new StringBuilder().append("Starting collection of last 48 hours ").append(getName())
                 .append(" request counts ").toString());
         while(bkIter.hasNext()){
             String mt = bkIter.next();
             long value = 0;
             try{
                 //Thread.sleep(1300);
                 value = getRollUpValue(access.getRESTEUMMetricQuery(getIndex(), appId, mt, start, end, true));
             }catch(Exception e){
                 logger.log(Level.WARNING,new StringBuilder().append("Exception running ").append(mt.toString()).toString());
             }
             
             if(value < min48){
                 _48HourEUM.put(mt, value);
                 bkIter.remove();
             }
         }
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
    
    
}
