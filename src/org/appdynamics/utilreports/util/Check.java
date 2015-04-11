/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.appdynamics.utilreports.util;

import org.appdynamics.appdrestapi.RESTAccess;
import org.appdynamics.appdrestapi.data.MetricDatas;
import org.appdynamics.utilreports.conf.QInfo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.HashMap;

import java.util.logging.Logger;
import java.util.logging.Level;

/**
 *
 * @author gilbert.solorzano
 * 
 *  
 * 
 */
public class Check  implements Runnable{
    private static Logger logger=Logger.getLogger(Check.class.getName());
    private String name;
    private int hours,minCount,queryIndex;
    private long start,end;
    private String appId;
    private RESTAccess access;
    
    private ArrayList<QInfo> retries=new ArrayList<QInfo>(),base=new ArrayList<QInfo>(), myList=new ArrayList<QInfo>(),newBase=new ArrayList<QInfo>();
    
    
    public Check(String name, int hours, int minCount, int queryIndex, RESTAccess access, String appId){
        this.name=name;
        this.hours=hours;
        this.minCount=minCount;
        this.queryIndex=queryIndex;
        this.access=access;
        this.appId=appId;
    }


    /*
     *  First we are going to execute on the base list, 
     * 
     */
    @Override
    public void run(){
        logger.log(Level.INFO, new StringBuilder().append("Check running ").append(name).append(" queryNumber ").append(queryIndex).toString());
        long value=-2L;
        QInfo qInfo;
        setTimeRange();
        Iterator<QInfo> qIter=base.iterator();
        while(qIter.hasNext()){
            qInfo=qIter.next();
            value=-2L;
            switch(queryIndex){
                case 5: 
                    value = getRollUpValue(access.getRESTBTMetricQuery(5, appId, qInfo.getTierName(), qInfo.getName(), start, end, true));
                    break;
                case 1:
                   value = getRollUpValue(access.getRESTBackendMetricQuery(1, appId, qInfo.getName(), start, end, true));         
                    break;
                case 0:
                    value = getRollUpValue(access.getRESTEUMMetricQuery(0, appId, qInfo.getName(), start, end, true)); //
                    break;
                case 22:
                    value = getRollUpValue(access.getRESTEUMMetricQuery(22, appId, qInfo.getName(), start, end, true));// 22
                    break;
                case 57:
                    value = getRollUpValue(access.getRESTEUMMetricQuery(57, appId, qInfo.getName(), start, end, true)); //57
                    break;
                    
            }
            try{Thread.sleep(130);}catch(Exception e){}
            //logger.log(Level.INFO, new StringBuilder().append("\tValue of  ").append(qInfo.getName()).append(" query is ").append(value).toString());
            qInfo.setValue(value);
            if(value >= 0){
                    if(value > minCount){
                        myList.add(qInfo);
                        qIter.remove();
                    }
            }else{
                retries.add(qInfo);
                qIter.remove();
            }
        } // of while
        
       
        // another to make sure we get the data.
        
        qIter = retries.iterator();
        while(qIter.hasNext()){
            qInfo=qIter.next();
            value=-2L;
            switch(queryIndex){
                case 5: 
                    value = getRollUpValue(access.getRESTBTMetricQuery(5, appId, qInfo.getTierName(), qInfo.getName(), start, end, true));
                    break;
                case 1:
                   value = getRollUpValue(access.getRESTBackendMetricQuery(1, appId, qInfo.getName(), start, end, true));         
                    break;
                case 0:
                    value = getRollUpValue(access.getRESTEUMMetricQuery(0, appId, qInfo.getName(), start, end, true)); //
                    break;
                case 22:
                    value = getRollUpValue(access.getRESTEUMMetricQuery(22, appId, qInfo.getName(), start, end, true));// 22
                    break;
                case 57:
                    value = getRollUpValue(access.getRESTEUMMetricQuery(57, appId, qInfo.getName(), start, end, true)); //57
                    break;
                    
            }
            try{Thread.sleep(500);}catch(Exception e){}
            qInfo.setValue(value);
            if(value >= 0){
                    if(value > minCount){
                        myList.add(qInfo);
                        qIter.remove();
                    }
            }else{
                base.add(qInfo);
            }
        }
        logger.log(Level.INFO, new StringBuilder().append("Completed the check for ").append(name).append(" queryNumber ").append(queryIndex).append(" with the base of ").append(base.size()).toString());
    }
    
    /*
     * This is just going set start and end.
     */
    public void setTimeRange(){
        Calendar cal = Calendar.getInstance();
        StringBuilder bud = new StringBuilder();
        bud.append("The end time is : " ).append(cal.getTime()).append(" and start time is ");
        end=cal.getTimeInMillis();
        cal.add(Calendar.HOUR, (hours * -1) );
        start = cal.getTimeInMillis();
        bud.append(cal.getTime());
         logger.log(Level.INFO,new StringBuilder().append("Time range for  ").append(name).append(" is ").append(bud.toString()).toString());
    }
    
    private long getRollUpValue(MetricDatas mds){
        
        if(mds != null && mds.getSingleRollUpMetricValue() != null)
            return mds.getSingleRollUpMetricValue().getValue();
        
        return -1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMinCount() {
        return minCount;
    }

    public void setMinCount(int minCount) {
        this.minCount = minCount;
    }

    public int getQueryIndex() {
        return queryIndex;
    }

    public void setQueryIndex(int queryIndex) {
        this.queryIndex = queryIndex;
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

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public RESTAccess getAccess() {
        return access;
    }

    public void setAccess(RESTAccess access) {
        this.access = access;
    }

    public ArrayList<QInfo> getRetries() {
        return retries;
    }

    public void setRetries(ArrayList<QInfo> retries) {
        this.retries = retries;
    }

    public ArrayList<QInfo> getBase() {
        return base;
    }

    public void setBase(ArrayList<QInfo> base) {
        this.base = base;
    }

    public ArrayList<QInfo> getMyList() {
        return myList;
    }

    public void setMyList(ArrayList<QInfo> myList) {
        this.myList = myList;
    }
    
    

    
}
