/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.appdynamics.utilreports.util;

import org.appdynamics.appdrestapi.RESTAccess;
import org.appdynamics.appdrestapi.data.MetricDatas;
import org.appdynamics.appdrestapi.data.MetricData;
import org.appdynamics.appdrestapi.data.Tiers;
import org.appdynamics.appdrestapi.data.Tier;
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
        bud.append("Time range for  ").append(name).append(" is ");
        bud.append(" \n\t\tend time is : " ).append(cal.getTime()).append(" and start time is ");
        end=cal.getTimeInMillis();
        cal.add(Calendar.HOUR, (hours * -1) );
        start = cal.getTimeInMillis();
        bud.append(cal.getTime());
         logger.log(Level.INFO,bud.toString());
    }
    
    
    private MetricDatas getBTsByTier(){
        MetricDatas mds_=null;
        Tiers tiers = access.getTiersForApplication(appId);
        switch(queryIndex){
                case 5: 
                    //getAllRollUpValues(access.getRESTBTMetricQuery(5, appId, "*", "*", start, end, true));
                    for(Tier tier:tiers.getTiers()){
                        MetricDatas mds = access.getRESTBTMetricQuery(5, appId, tier.getName(), "*", start, end, true);
                        logger.log(Level.INFO,new StringBuilder().append("Tier ").append(tier.getName()).append(" ").append(mds.hasNoValues()).toString());
                        if(mds_ == null){
                            mds_=mds;
                        }else{
                            mds_.getMetric_data().addAll(mds.getMetric_data());
                        }
                        try{Thread.sleep(300);}catch(Exception e){}
                    }
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
        
        
        return mds_;
    }
    
    private void setupNothingReturned(){
        if(base.isEmpty())base.add(new QInfo("Nothing returned", "Nothing retunred"));
    }
    
    private void getAllRollUpValues(MetricDatas mds){
        
        if(mds == null){logger.log(Level.INFO,new StringBuilder().append("Nothing was returned, exiting.").toString()); setupNothingReturned();return;} // We did no get anything back, don't continue
        if(mds.hasNoValues()){
            //mds=getBTsByTier();
            if(mds.hasNoValues()){ logger.log(Level.INFO,new StringBuilder().append("Nothing was returned when checking tiers, exiting.").toString()); setupNothingReturned();return;}
        }
        
        //logger.log(Level.INFO,mds.toString());
        // We expect base to have the info
        QInfo qInfo,qInfo1;
        
        for(MetricData md_: mds.getMetric_data()){
            boolean fnd=false;
            //logger.log(Level.INFO,new StringBuilder().append("MetricData: ").append(md_.getMetricPath()).append(" ").append(md_.hasNoValues()).toString());
            qInfo1=getQInfoFromPath(md_.getMetricPath());
            if(qInfo1 != null ){
                if(md_.getMetricValues().size() > 0){
                    // 
                    //logger.log(Level.INFO,new StringBuilder().append("MetricData: ").append(" ").append(md_).toString());
                    if(!md_.hasNoValues()) qInfo1.setValue(md_.getSingleValue().getSum());
                    if(qInfo1.getValue() > minCount){
                        // This one is good
                        qInfo1.setPassed(true);
                        myList.add(qInfo1);
                    }else{
                       newBase.add(qInfo1);
                    }
                }else{
                    logger.log(Level.INFO,new StringBuilder().append("\nNo metrics gathered for ").append(md_.getMetricPath()).toString());
                }
            }
        }
        logger.log(Level.INFO,new StringBuilder().append("First run size of base is  ").append(base.size())
                .append(" , ").append(" size of mine ").append(myList.size()).append(", size of newBase ")
                .append(newBase.size()).toString());
        
        Iterator<QInfo>  outerIterator;
        Iterator<QInfo> interIterator;
        
        
        
        /* ----- Done with the first run ---- */
        
        outerIterator = base.iterator();
        /* This cleans up myList that my have an object that already passed */
        while(outerIterator.hasNext()){
            qInfo = outerIterator.next();
            interIterator=myList.iterator();
            while(interIterator.hasNext()){
                qInfo1=interIterator.next();
                if(qInfo.getName().equals(qInfo1.getName()) && qInfo.getTierName().equals(qInfo1.getTierName())){
                    // If we already passed, so remove the inter
                    if(qInfo.isPassed()) interIterator.remove();
                }
            }
            
            // If we did not a full detail on an entry but we already passed then just forget it.
            interIterator=newBase.iterator();
            while(interIterator.hasNext()){
                qInfo1=interIterator.next();
                if(qInfo.getName().equals(qInfo1.getName()) 
                        && qInfo.getTierName().equals(qInfo1.getTierName())){
                    // If we already passed
                    interIterator.remove();
                }
            }
            
        }
        
        logger.log(Level.INFO,new StringBuilder().append("Second run size of base is  ").append(base.size())
                .append(" , ").append(" size of mine ").append(myList.size()).append(", size of newBase ")
                .append(newBase.size()).toString());
        
        /* This updates the base because this might be our first run, so we are adding anything that is new. */
        outerIterator = myList.iterator(); 
        while(outerIterator.hasNext()){
            qInfo = outerIterator.next();
            interIterator=base.iterator();
            boolean fnd=false;
            while(interIterator.hasNext()){
                qInfo1=interIterator.next();
                if( qInfo.getName().equals(qInfo1.getName()) 
                        && qInfo.getTierName().equals(qInfo1.getTierName()) ){
                    fnd=true;
                }
            }
            if(!fnd) base.add(qInfo);
        }
        
        // This is adding the new object from the new base if they don't exist
        outerIterator = newBase.iterator(); 
        while(outerIterator.hasNext()){
            qInfo = outerIterator.next();
            interIterator=base.iterator();
            boolean fnd=false;
            while(interIterator.hasNext()){
                qInfo1=interIterator.next();
                if( qInfo.getName().equals(qInfo1.getName()) 
                        && qInfo.getTierName().equals(qInfo1.getTierName()) ){
                    fnd=true;
                }
            }
            if(!fnd) base.add(qInfo);
        }
        
        logger.log(Level.INFO,new StringBuilder().append("Third run size of base is  ").append(base.size())
                .append(" , ").append(" size of mine ").append(myList.size()).append(", size of newBase ")
                .append(newBase.size()).toString());
        
       
        /* This updates the base with the new values, we will set the object as passed if it has passed  */
        outerIterator = base.iterator();
        while(outerIterator.hasNext()){
            qInfo = outerIterator.next();
            interIterator=myList.iterator();
            boolean fnd=false;
            while(interIterator.hasNext()){
                
                qInfo1=interIterator.next();
                if(qInfo.getName().equals(qInfo1.getName()) 
                        && qInfo.getTierName().equals(qInfo1.getTierName())){
                    fnd=true;
                    // If we already passed
                    qInfo.setPassed(qInfo1.isPassed());
                    qInfo.setOldValue(qInfo.getValue());
                    qInfo.setValue(qInfo1.getValue());
                }
            }
            
        }
        logger.log(Level.INFO,new StringBuilder().append("Final run size of base is  ").append(base.size())
                .append(" , ").append(" size of mine ").append(myList.size()).append(", size of newBase ")
                .append(newBase.size()).toString());
       
    }
    
    private QInfo getQInfoFromPath(String metricPath){
        //logger.log(Level.INFO,new StringBuilder().append("MetricPath  ").append(metricPath).append(" is ").toString());
        QInfo qInfo=null;
        String[] path=metricPath.split("\\|");
        switch(queryIndex){
                case 5: 
                    if(path.length > 3){
                        qInfo = new QInfo(path[3],path[2]);
                    }else{
                        logger.log(Level.WARNING,new StringBuilder().append("Failed to part the metric name for query index 5 ").append(metricPath).toString());
                    }
                    break;
                case 1:
                    if(path.length > 2){
                        qInfo = new QInfo(path[1],"");
                    }else{
                        logger.log(Level.WARNING,new StringBuilder().append("Failed to part the metric name for query index 1 ").append(metricPath).toString());
                    }       
                    break;
                case 0:
                    if(path.length > 2){
                        qInfo = new QInfo(path[2],"");
                    }else{
                        logger.log(Level.WARNING,new StringBuilder().append("Failed to part the metric name for query index 0 ").append(metricPath).toString());
                    }
                    break;
                case 22:
                    if(path.length > 2){
                        qInfo = new QInfo(path[2],"");
                    }else{
                        logger.log(Level.WARNING,new StringBuilder().append("Failed to part the metric name for query index 22 ").append(metricPath).toString());
                    }
                    break;
                case 57:
                    if(path.length > 2){
                        qInfo = new QInfo(path[2],"");
                    }else{
                        logger.log(Level.WARNING,new StringBuilder().append("Failed to part the metric name for query index 57 ").append(metricPath).toString());
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
