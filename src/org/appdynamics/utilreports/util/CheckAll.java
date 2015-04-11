/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.appdynamics.utilreports.util;

import org.appdynamics.appdrestapi.RESTAccess;
import org.appdynamics.appdrestapi.data.MetricDatas;
import org.appdynamics.appdrestapi.data.MetricData;
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
public class CheckAll  implements Runnable{
    private static Logger logger=Logger.getLogger(CheckAll.class.getName());
    private String name;
    private int hours,minCount,queryIndex;
    private long start,end;
    private String appId;
    private RESTAccess access;
    
    private ArrayList<QInfo> retries=new ArrayList<QInfo>(),base=new ArrayList<QInfo>(), myList=new ArrayList<QInfo>(),newBase=new ArrayList<QInfo>();
    
    
    public CheckAll(String name, int hours, int minCount, int queryIndex, RESTAccess access, String appId){
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
        setTimeRange();
        switch(queryIndex){
                case 5: 
                    getAllRollUpValues(access.getRESTBTMetricQuery(5, appId, "*", "*", start, end, true));
                    break;
                case 1:
                    getAllRollUpValues(access.getRESTBackendMetricQuery(1, appId, "*", start, end, true));         
                    break;
                case 0:
                    getAllRollUpValues(access.getRESTEUMMetricQuery(0, appId, "*", start, end, true)); //
                    break;
                case 22:
                    getAllRollUpValues(access.getRESTEUMMetricQuery(22, appId, "*", start, end, true));// 22
                    break;
                case 57:
                    getAllRollUpValues(access.getRESTEUMMetricQuery(57, appId, "*", start, end, true)); //57
                    break;
                    
         }
        
        
            try{Thread.sleep(130);}catch(Exception e){}

       
        
       
        
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
    
    private void getAllRollUpValues(MetricDatas mds){
        
        if(mds == null) return; // We did no get anything back, don't continue
        //logger.log(Level.INFO,new StringBuilder().append("We are continuing ").append(mds).toString());
        // We expect base to have the info
        QInfo qInfo,qInfo1;
        
        for(MetricData md_: mds.getMetric_data()){
            boolean fnd=false;

            qInfo1=getQInfoFromPath(md_.getMetricPath());
            if(qInfo1 != null ){
                if(md_.getMetricValues().size() > 0){
                    // 
                    //logger.log(Level.INFO,new StringBuilder().append("MetricData: ").append(" ").append(md_).toString());
                    if(!md_.hasNoValues()) qInfo1.setValue(md_.getSingleValue().getCount());
                    if(qInfo1.getValue() > minCount){
                        // This one is good
                        qInfo1.setPassed(true);
                        myList.add(qInfo1);
                    }else{
                       newBase.add(qInfo1);
                    }
                }
            }else{
                    // We did not get a value
                    newBase.add(qInfo1);
             }
        }
        //logger.log(Level.INFO,new StringBuilder().append("We are continuing2 ").append(mds).toString());
        Iterator<QInfo> iterBase = base.iterator();
        //This cleans up myList
        while(iterBase.hasNext()){
            qInfo = iterBase.next();
            Iterator<QInfo> myIterBase=myList.iterator();
            while(myIterBase.hasNext()){
                qInfo1=myIterBase.next();
                if(qInfo.getName().equals(qInfo1.getName()) && qInfo.getTierName().equals(qInfo1.getTierName())){
                    // If we already passed
                    if(qInfo.isPassed()) myIterBase.remove();
                }
            }
            
            myIterBase=newBase.iterator();
            while(myIterBase.hasNext()){
                qInfo1=myIterBase.next();
                if(qInfo.getName().equals(qInfo1.getName()) && qInfo.getTierName().equals(qInfo1.getTierName())){
                    // If we already passed
                    myIterBase.remove();
                }
            }
            
        }
        
       iterBase = base.iterator();
        //This updates the base.
        while(iterBase.hasNext()){
            qInfo = iterBase.next();
            Iterator<QInfo> myIterBase=myList.iterator();
            boolean fnd=false;
            while(myIterBase.hasNext()){
                
                qInfo1=myIterBase.next();
                if(qInfo.getName().equals(qInfo1.getName()) && qInfo.getTierName().equals(qInfo1.getTierName())){
                    fnd=true;
                    // If we already passed
                    qInfo.setPassed(qInfo1.isPassed());
                    qInfo.setOldValue(qInfo.getValue());
                    qInfo.setValue(qInfo1.getValue());
                }
            }
            
        }
        
        iterBase = myList.iterator();
        //This updates the base.
        while(iterBase.hasNext()){
            qInfo = iterBase.next();
            Iterator<QInfo> myIterBase=base.iterator();
            boolean fnd=false;
            while(myIterBase.hasNext()){
                qInfo1=myIterBase.next();
                if( qInfo.getName().equals(qInfo1.getName()) && qInfo.getTierName().equals(qInfo1.getTierName()) ){
                    fnd=true;
                }
            }
            if(!fnd) base.add(qInfo);
        }
        
        iterBase = newBase.iterator();
        //This updates the base.
        while(iterBase.hasNext()){
            qInfo = iterBase.next();
            Iterator<QInfo> myIterBase=base.iterator();
            boolean fnd=false;
            while(myIterBase.hasNext()){
                qInfo1=myIterBase.next();
                if( qInfo.getName().equals(qInfo1.getName()) && qInfo.getTierName().equals(qInfo1.getTierName()) ){
                    fnd=true;
                }
            }
            if(!fnd) base.add(qInfo);
        }
       
    }
    
    private QInfo getQInfoFromPath(String metricPath){
        //logger.log(Level.INFO,new StringBuilder().append("MetricPath  ").append(metricPath).append(" is ").toString());
        QInfo qInfo=null;
        String[] path=metricPath.split("\\|");
        switch(queryIndex){
                case 5: 
                    if(path.length > 3){
                        qInfo = new QInfo(path[3],path[2]);
                    }
                    break;
                case 1:
                    if(path.length > 2){
                        qInfo = new QInfo(path[1],"");
                    }       
                    break;
                case 0:
                    if(path.length > 2){
                        qInfo = new QInfo(path[2],"");
                    }
                    break;
                case 22:
                    if(path.length > 2){
                        qInfo = new QInfo(path[2],"");
                    }
                    break;
                case 57:
                    if(path.length > 2){
                        qInfo = new QInfo(path[2],"");
                    }
                    break;
                    
         }

        //logger.log(Level.INFO,new StringBuilder().append("The name ").append(qInfo.getName()).toString());
        return qInfo;
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
