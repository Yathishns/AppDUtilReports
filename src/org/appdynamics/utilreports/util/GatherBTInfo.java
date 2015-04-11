/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.appdynamics.utilreports.util;

import org.appdynamics.utilreports.actions.*;
import org.appdynamics.utilreports.conf.BTInfo;
import org.appdynamics.appdrestapi.RESTAccess;
import org.appdynamics.appdrestapi.data.*;
import org.appdynamics.appdrestapi.util.MyCalendar;
import org.appdynamics.utilreports.logging.JLog_Formatter;

import java.util.HashMap;
import java.util.ArrayList;
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
public class GatherBTInfo extends GatherInfo implements Runnable{
    private static Logger logger=Logger.getLogger(GatherBTInfo.class.getName());
    private RESTAccess access;
    private String appId;
    private long minBT;
    private long minBT24;
    private long minBT48;
    private long start;
    private long end;
    private ArrayList<BTInfo> btInfoList = new ArrayList<BTInfo>();
    private HashMap<BusinessTransaction,Long> normalBT=new HashMap<BusinessTransaction,Long>();
    private HashMap<BusinessTransaction,Long> _4HourBT=new HashMap<BusinessTransaction,Long>();
    private HashMap<BusinessTransaction,Long> _24HourBT=new HashMap<BusinessTransaction,Long>();
    private HashMap<BusinessTransaction,Long> _48HourBT=new HashMap<BusinessTransaction,Long>();
    private HashMap<BusinessTransaction,Long> allOthersBT=new HashMap<BusinessTransaction,Long>();
    
    static {
        setupLogger();
    }
    
    private Calendar cal;
    
    public GatherBTInfo(RESTAccess access, String appId, long minBT){
        super();
        this.access=access;
        this.appId=appId;
        this.minBT=minBT;
        this.minBT24=minBT*2;
        this.minBT48=minBT*3;
    }
    
    public GatherBTInfo(RESTAccess access, String appId, long minBT, long minBT24, long minBT48){
        super();
        this.access=access;
        this.appId=appId;
        this.minBT=minBT;
        this.minBT24=minBT24;
        this.minBT48=minBT48;
    }
    
    @Override
    public void run(){
        logger.log(Level.INFO,new StringBuilder().append("The value of run is ").append(run).toString());
        if(run == 0){run4Hours();}
        if(run == 1){run24Hours();}
        if(run == 2) {run48Hours();}
        run++;
        //printData();
    }
    
    @Override
    public void run2(){
        //run24Hours();
        run48Hours();
    }
    
    public void run4Hours(){
        long _timeS=Calendar.getInstance().getTimeInMillis();
        BusinessTransactions businessTS = access.getBTSForApplication(appId);
        
        cal = MyCalendar.getCalendar();
        end = cal.getTimeInMillis();end++;
        cal.add(Calendar.HOUR, -4);
        start = cal.getTimeInMillis();
        
        
        logger.log(Level.INFO,new StringBuilder().append("Starting collection of last 4 hours BT request counts.").toString());
        for(BusinessTransaction bt: businessTS.getBusinessTransactions()){
            if(!bt.getName().equals("_APPDYNAMICS_DEFAULT_TX_"))btInfoList.add(new BTInfo(bt));
        }
        
        ThreadExecutor threadExec=new ThreadExecutor(threadNumN);
         for(BTInfo bt:btInfoList){
             BTExecutor btExec=new BTExecutor(bt,access,appId,start,end);
             threadExec.getExecutor().execute(btExec);
         }
         
         threadExec.getExecutor().shutdown();
         threadExec.shutdown();
         
         //logger.log(Level.INFO,new StringBuilder().append("Size of btInfoList is ").append(btInfoList.size()).toString());
         Iterator<BTInfo> tempBTs=btInfoList.iterator();
 
         
         while(tempBTs.hasNext()){
             BTInfo bt = tempBTs.next();
             if(bt.getBt().getName().equals("_APPDYNAMICS_DEFAULT_TX_")){
                 allOthersBT.put(bt.getBt(),bt.getValue());
                 tempBTs.remove();
             }else{
                 if(bt.getValue() > minBT){
                     normalBT.put(bt.getBt(),bt.getValue());
                     tempBTs.remove();
                 }else{
                        if(bt.getValue() == -1){
                            _48HourBT.put(bt.getBt(), bt.getValue());
                            tempBTs.remove();
                        }
                    }
             }

         }
         long _endT=(Calendar.getInstance().getTimeInMillis()-_timeS)/1000;
         logger.log(Level.INFO,new StringBuilder().append("Completed collection of last 4 hours BT request counts in ")
                 .append(_endT).append(" seconds.").toString());
    }
    
    public void run24Hours(){
         cal.add(Calendar.HOUR, -20);
         start = cal.getTimeInMillis();
         
         logger.log(Level.INFO,new StringBuilder().append("Starting collection of last 24 hours BT request counts.").toString());
         
         long _timeS=Calendar.getInstance().getTimeInMillis();
         
         ThreadExecutor threadExec=new ThreadExecutor(threadNumM);
         for(BTInfo bt:btInfoList){
             BTExecutor btExec=new BTExecutor(bt,access,appId,start,end);
             threadExec.getExecutor().execute(btExec);
         }
         
         threadExec.getExecutor().shutdown();
         threadExec.shutdown();
         
         
         Iterator<BTInfo> tempBTs=btInfoList.iterator();
 
         
         while(tempBTs.hasNext()){
             BTInfo bt = tempBTs.next();

            if(bt.getValue() >= minBT24){
                _4HourBT.put(bt.getBt(),bt.getValue());
                tempBTs.remove();
            }else{
                if(bt.getValue() == -1){
                    _48HourBT.put(bt.getBt(), bt.getValue());
                    tempBTs.remove();
                }
            }
            
         }
         long _endT=(Calendar.getInstance().getTimeInMillis()-_timeS)/1000;
         logger.log(Level.INFO,new StringBuilder().append("Completed collection of last 24 hours BT request counts in ")
                 .append(_endT).append(" seconds.").toString());
    }
    
    public void run48Hours(){
        
         cal.add(Calendar.HOUR, -24);
         start = cal.getTimeInMillis();
         logger.log(Level.INFO,new StringBuilder().append("Starting collection of last 48 hours BT request counts.").toString());
  
         long _timeS=Calendar.getInstance().getTimeInMillis();
         
         /*
          *  We are going to multi-thread this portion, this means we need to know, this might go faster
          *  if we first create the BTs that belong in the 48hr bucket, then remove from the 24hr bucket
          * 
          * Concerns: the _48HourBT hash is going to be sent out as a static value
          * 
          */
         ThreadExecutor threadExec=new ThreadExecutor(threadNum);
         for(BTInfo bt:btInfoList){
             BTExecutor btExec=new BTExecutor(bt,access,appId,start,end);
             threadExec.getExecutor().execute(btExec);
         }
         
         threadExec.getExecutor().shutdown();
         threadExec.shutdown();
         
         
         Iterator<BTInfo> tempBTs=btInfoList.iterator();
 
         
         while(tempBTs.hasNext()){
             BTInfo bt = tempBTs.next();

            if(bt.getValue() >= minBT48){
                _24HourBT.put(bt.getBt(),bt.getValue());
                tempBTs.remove();
            }else{
                _48HourBT.put(bt.getBt(),bt.getValue());
                tempBTs.remove();
            }
            
         }
         

         long _endT=(Calendar.getInstance().getTimeInMillis()-_timeS)/1000;
         
         logger.log(Level.INFO,new StringBuilder().append("Completed collection of last 48 hours BT request counts in ")
                 .append(_endT).append(" seconds.").toString());
    }
    
    public String printData(){
        StringBuilder msg=new StringBuilder();
         msg.append("\nThe number of entries in normal are " + normalBT.size());
         
         msg.append("\nThe number of BTs with very few entries in 4 hour are " + _4HourBT.size());
         Iterator<BusinessTransaction> tempBTs=_4HourBT.keySet().iterator();
         while(tempBTs.hasNext()){
             BusinessTransaction bt = tempBTs.next();
             StringBuilder bud = new StringBuilder();
             bud.append("\nBT Name = '").append(bt.getName()).append("'|Internal Name = '").append(bt.getInternalName()).append("'|BT Id=").append(bt.getId())
                     .append("|Tier Name=").append(bt.getTierName()).append("|4 hours entries ")
                     .append(_4HourBT.get(bt));
             msg.append(bud.toString());
         }
         
 
         msg.append("\n\nThe number of BTs with very few entries in 24 hour are " + _24HourBT.size());
         tempBTs=_24HourBT.keySet().iterator();
         while(tempBTs.hasNext()){
             BusinessTransaction bt = tempBTs.next();
             StringBuilder bud = new StringBuilder();
             bud.append("\nBT Name = '").append(bt.getName()).append("'|Internal Name = '").append(bt.getInternalName()).append("'|BT Id=").append(bt.getId())
                     .append("|Tier Name=").append(bt.getTierName()).append("|24 hours entries ")
                     .append(_24HourBT.get(bt));
             msg.append(bud.toString());
         }
         
         
         msg.append("\n\nThe number of BTs with very few entries in 48 hour are " + _48HourBT.size());
         tempBTs=_48HourBT.keySet().iterator();
         while(tempBTs.hasNext()){
             BusinessTransaction bt = tempBTs.next();
             StringBuilder bud = new StringBuilder();
             bud.append("\nBT Name = '").append(bt.getName()).append("'|Internal Name = '").append(bt.getInternalName()).append("'|BT Id=").append(bt.getId())
                     .append("|Tier Name=").append(bt.getTierName()).append("|48 hours entries ")
                     .append(_48HourBT.get(bt));
             msg.append(bud.toString());
         }

         return msg.toString();
         
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public HashMap<BusinessTransaction, Long> getNormalBT() {
        return normalBT;
    }

    public void setNormalBT(HashMap<BusinessTransaction, Long> normalBT) {
        this.normalBT = normalBT;
    }

    public HashMap<BusinessTransaction, Long> get4HourBT() {
        return _4HourBT;
    }

    public void set4HourBT(HashMap<BusinessTransaction, Long> _4HourBT) {
        this._4HourBT = _4HourBT;
    }

    public HashMap<BusinessTransaction, Long> get24HourBT() {
        return _24HourBT;
    }

    public void set24HourBT(HashMap<BusinessTransaction, Long> _24HourBT) {
        this._24HourBT = _24HourBT;
    }

    public HashMap<BusinessTransaction, Long> get48HourBT() {
        return _48HourBT;
    }

    public void set48HourBT(HashMap<BusinessTransaction, Long> _48HourBT) {
        this._48HourBT = _48HourBT;
    }

    public HashMap<BusinessTransaction, Long> getAllOthersBT() {
        return allOthersBT;
    }

    public void setAllOthersBT(HashMap<BusinessTransaction, Long> allOthersBT) {
        this.allOthersBT = allOthersBT;
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
