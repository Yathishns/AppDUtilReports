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
public class GatherBTInfo extends GatherInfo implements Runnable{
    private static Logger logger=Logger.getLogger(GatherBTInfo.class.getName());
    private RESTAccess access;
    private String appId;
    private long minBT;
    private long minBT24;
    private long minBT48;
    private long start;
    private long end;
    private HashMap<BusinessTransaction,Long> normalBT=new HashMap<BusinessTransaction,Long>();
    private HashMap<BusinessTransaction,Long> _4HourBT=new HashMap<BusinessTransaction,Long>();
    private HashMap<BusinessTransaction,Long> _24HourBT=new HashMap<BusinessTransaction,Long>();
    private HashMap<BusinessTransaction,Long> _48HourBT=new HashMap<BusinessTransaction,Long>();
    private HashMap<BusinessTransaction,Long> allOthersBT=new HashMap<BusinessTransaction,Long>();
    
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
        run4Hours();
        run24Hours();
        run48Hours();
        //printData();
    }
    
    public void run4Hours(){
        logger.log(Level.INFO,new StringBuilder().append("Starting query for business transactions ").toString());
        BusinessTransactions businessTS = access.getBTSForApplication(appId);
        cal = Calendar.getInstance();
         end = cal.getTimeInMillis();
         cal.add(Calendar.HOUR, -4);
         start = cal.getTimeInMillis();
         
         logger.log(Level.INFO,new StringBuilder().append("Starting collection of last 4 hours BT request counts ").toString());
         for(BusinessTransaction bt: businessTS.getBusinessTransactions()){
             long value = 0;
             try{
                 value = getRollUpValue(access.getRESTBTMetricQuery(5, appId, bt.getTierName(), bt.getName(), start, end, true));
             }catch(Exception e){
                 logger.log(Level.WARNING,new StringBuilder().append("Exception running ").append(bt.toString()).toString());
             }
             
             if(value > minBT){
                 normalBT.put(bt,value);
             }else{
                 if(bt.getName().equals("_APPDYNAMICS_DEFAULT_TX_")){
                     allOthersBT.put(bt, value);
                 }else{
                     _4HourBT.put(bt, value);
                 }
                 
             }
         }
    }
    
    public void run24Hours(){
         cal.add(Calendar.HOUR, -20);
         start = cal.getTimeInMillis();
         
         logger.log(Level.INFO,new StringBuilder().append("Starting collection of last 24 hours BT request counts ").toString());
         //Set<BusinessTransaction> tempBTs=_4HourBT.keySet();
         Iterator<BusinessTransaction> tempBTs=_4HourBT.keySet().iterator();
         while(tempBTs.hasNext()){
             BusinessTransaction bt=tempBTs.next();
             long value = 0;
             try{
                 value = getRollUpValue(access.getRESTBTMetricQuery(5, appId, bt.getTierName(), bt.getName(), start, end, true));
             }catch(Exception e){
                 logger.log(Level.WARNING,new StringBuilder().append("Exception running ").append(bt.toString()).toString());
             }
             
             if(value < minBT24){
                 _24HourBT.put(bt, value);
                 tempBTs.remove();
             }
         }
    }
    
    public void run48Hours(){
        
         cal.add(Calendar.HOUR, -24);
         start = cal.getTimeInMillis();
         logger.log(Level.INFO,new StringBuilder().append("Starting collection of last 48 hours BT request counts ").toString());
         Iterator<BusinessTransaction> tempBTs=_24HourBT.keySet().iterator();
         while(tempBTs.hasNext()){
             BusinessTransaction bt = tempBTs.next();
             long value = 0;
             try{
                 value = getRollUpValue(access.getRESTBTMetricQuery(5, appId, bt.getTierName(), bt.getName(), start, end, true));
             }catch(Exception e){
                 logger.log(Level.WARNING,new StringBuilder().append("Exception running ").append(bt.toString()).toString());
             }
             
             
             if(value < minBT48){
                 _48HourBT.put(bt, value);
                 tempBTs.remove();
             }
         }
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
   
    
    
    
}
