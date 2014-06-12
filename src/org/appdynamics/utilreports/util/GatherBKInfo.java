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
public class GatherBKInfo extends GatherInfo implements Runnable{
    private static Logger logger=Logger.getLogger(GatherBKInfo.class.getName());
    private RESTAccess access;
    private String appId;
    private long minBK;
    private long minBK24;
    private long minBK48;
    private long start;
    private long end;
    private HashMap<String,Long> normalBK=new HashMap<String,Long>();
    private HashMap<String,Long> _4HourBK=new HashMap<String,Long>();
    private HashMap<String,Long> _24HourBK=new HashMap<String,Long>();
    private HashMap<String,Long> _48HourBK=new HashMap<String,Long>();    
    private Calendar cal;
    
    
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
        run4Hours();
        run24Hours();
        run48Hours();
        //printData();
    }
    
    public void run4Hours(){
        
        logger.log(Level.INFO,new StringBuilder().append("Starting query for backends ").toString());
        
        MetricItems exitPoints = access.getBaseMetricListPath(appId, "Backends");

        //gatherInfo.run();
         
         cal = Calendar.getInstance();
         end = cal.getTimeInMillis();
         cal.add(Calendar.HOUR, -4);
         start = cal.getTimeInMillis();
         
         logger.log(Level.INFO,new StringBuilder().append("Starting collection of last 4 hours Backend request counts ").toString());
         for(MetricItem mt: exitPoints.getMetricItems()){
             long value = 0;
             try{
                 //Thread.sleep(1000);
                 value = getRollUpValue(access.getRESTBackendMetricQuery(1, appId, mt.getName(), start, end, true));
             }catch(Exception e){
                 logger.log(Level.WARNING,new StringBuilder().append("Exception running ").append(mt.toString()).toString());
             }
             
             if(value > minBK){
                 normalBK.put(mt.getName(), value);
             }else{
                 _4HourBK.put(mt.getName(), value);
             }
         }
    }
    
    public void run24Hours(){
        Iterator<String> bkIter=_4HourBK.keySet().iterator();
         cal.add(Calendar.HOUR, -20);
         start = cal.getTimeInMillis();
         
         logger.log(Level.INFO,new StringBuilder().append("Starting collection of last 24 hours Backend request counts ").toString());
         while(bkIter.hasNext()){
             String mt = bkIter.next();
             long value = 0;
             try{
                 //Thread.sleep(1300);
                 value = getRollUpValue(access.getRESTBackendMetricQuery(1, appId, mt, start, end, true));
             }catch(Exception e){
                 logger.log(Level.WARNING,new StringBuilder().append("Exception running ").append(mt.toString()).toString());
             }
             
             if(value < (minBK24)){
                 _24HourBK.put(mt, value);
                 bkIter.remove();
             }
         }
         
         
    }
    
    public void run48Hours(){
        Iterator<String> bkIter=_24HourBK.keySet().iterator();
         cal.add(Calendar.HOUR, -24);
         start = cal.getTimeInMillis();
         logger.log(Level.INFO,new StringBuilder().append("Starting collection of last 48 hours Backend request counts ").toString());
         
         while(bkIter.hasNext()){
             String mt = bkIter.next();
             long value = 0;
             try{
                 //Thread.sleep(1300);
                 value = getRollUpValue(access.getRESTBackendMetricQuery(1, appId, mt, start, end, true));
             }catch(Exception e){
                 logger.log(Level.WARNING,new StringBuilder().append("Exception running ").append(mt.toString()).toString());
             }
             
             if(value < (minBK48)){
                 _48HourBK.put(mt, value);
                 bkIter.remove();
             }
         }
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
    
    
}
