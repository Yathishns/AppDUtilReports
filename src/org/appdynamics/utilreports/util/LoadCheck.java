/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.appdynamics.utilreports.util;

import org.appdynamics.utilreports.resources.AppDUtilReportS;
import org.appdynamics.appdrestapi.RESTAccess;
import org.appdynamics.appdrestapi.data.*;
import org.appdynamics.utilreports.conf.QInfo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;
import java.util.logging.Level;

import java.util.logging.Logger;


/**
 *
 * @author gilbert.solorzano
 */
public class LoadCheck implements Runnable{
    private static Logger logger=Logger.getLogger(LoadCheck.class.getName());
    private String name,appId, header,sheetName,appName;
    private int queryIndex, metricIndex;
    private RESTAccess access;
    private ArrayList<CheckAll> checks=new ArrayList<CheckAll>();
    private ArrayList<QInfo> base = new ArrayList<QInfo>();//This is going to be the list of names that we got. 
    
    public LoadCheck(String name, RESTAccess access){this.name=name;this.access=access;}
    public LoadCheck(String name, RESTAccess access,ArrayList<CheckAll> checks){this.name=name;this.access=access;this.checks=checks;}
    
    @Override
    public void run(){
        logger.log(Level.INFO, new StringBuilder().append("LoadCheck running ").append(name).toString());
        //setQInfoForMetricType();
        sortChecks();
        
        for(int i=0; i < checks.size(); i++){
             checks.get(i).setBase(base);
             checks.get(i).run();
             base=checks.get(i).getBase();
         }
         
    }
    
    public void setQInfoForMetricType(){
        base = new ArrayList<QInfo>();
        setMetricTypeFromName();
        switch(metricIndex){
            case 5:
                //logger.log(Level.INFO,"Getting BTS");
                BusinessTransactions bts = access.getBTSForApplication(appId);
                QInfo qInfo;
                for(BusinessTransaction bt:bts.getBusinessTransactions()){
                    //logger.log(Level.INFO,new StringBuilder().append("Found BT ").append(bt.getName()).toString());
                    if(!bt.getName().equals("_APPDYNAMICS_DEFAULT_TX_")) base.add(new QInfo(bt.getName(),bt.getTierName()));
                }
                break;
            case 1:
                MetricItems exitPoints = access.getBaseMetricListPath(appId, "Backends");
                for(MetricItem mt: exitPoints.getMetricItems()){
                    //logger.log(Level.INFO,new StringBuilder().append("Found BT ").append(mt.getName()).toString());
                    base.add(new QInfo(mt.getName()));
                }
                break;
            default:
                    MetricItems eum = access.getBaseMetricListPath(appId, getEUMPath());   
                    //logger.log(Level.INFO,new StringBuilder().append("Looking for EUM path ").append(getEUMPath()).toString());
                        for(MetricItem mt: eum.getMetricItems()){
                            base.add(new QInfo(mt.getName()));
                        }
                    break;
          }

        
    }
    
    public void sortChecks(){
        Collections.sort(checks,new Comparator<CheckAll>(){
            @Override
            public int compare (final CheckAll chk1, final CheckAll chk2){
                if(chk1.getHours() >chk2.getHours()) return 1;
                if(chk1.getHours() < chk2.getHours()) return -1;
                return 0;
            }
        });
        
    }
    public void setMetricTypeFromName(){
        if(AppDUtilReportS.BT_CHECK.equals(name)){ metricIndex=5;header=AppDUtilReportS.BUSINESS_TRANSACTION;sheetName=AppDUtilReportS.BT_CHECK;}
        if(AppDUtilReportS.BE_CHECK.equals(name)){ metricIndex=1;header=AppDUtilReportS.BACKENDS;sheetName=AppDUtilReportS.BE_CHECK;}
        if(AppDUtilReportS.EUM_AJAX_CHECK.equals(name)){ metricIndex=0;header=getEUMName();sheetName=getEUMSheetName();}
        if(AppDUtilReportS.EUM_BASE_PAGE_CHECK.equals(name)){ metricIndex=22;header=getEUMName();sheetName=getEUMSheetName();}
        if(AppDUtilReportS.EUM_IFRAME_CHECK.equals(name)){ metricIndex=57;header=getEUMName();sheetName=getEUMSheetName();}

    }
    
    public String getEUMPath(){
        if(metricIndex == 57) return "End User Experience|Iframes";
        if(metricIndex == 22) return "End User Experience|Base Pages";
        return "End User Experience|AJAX Requests";
    }
    
    public String getEUMName(){
        if(metricIndex == 57) return "Iframe Names";
        if(metricIndex == 22) return "Base Page Names";
        return "AJAX Request Names";
    }
    
    public String getEUMSheetName(){
        if(metricIndex == 57) return AppDUtilReportS.EUM_IFRAME_CHECK;
        if(metricIndex == 22) return AppDUtilReportS.EUM_BASE_PAGE_CHECK;
        return AppDUtilReportS.EUM_AJAX_CHECK;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    
    public int getQueryIndex() {
        return queryIndex;
    }

    public void setQueryIndex(int queryIndex) {
        this.queryIndex = queryIndex;
    }

    public int getMetricIndex() {
        return metricIndex;
    }

    public void setMetricIndex(int metricIndex) {
        this.metricIndex = metricIndex;
    }

    public RESTAccess getAccess() {
        return access;
    }

    public void setAccess(RESTAccess access) {
        this.access = access;
    }

    public ArrayList<CheckAll> getChecks() {
        return checks;
    }

    public void setChecks(ArrayList<CheckAll> checks) {
        this.checks = checks;
    }

    public ArrayList<QInfo> getBase() {
        return base;
    }

    public void setBase(ArrayList<QInfo> base) {
        this.base = base;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }
    
    
}
