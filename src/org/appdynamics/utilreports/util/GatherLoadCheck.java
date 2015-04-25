/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.appdynamics.utilreports.util;


import org.appdynamics.utilreports.conf.Load_Check_Set;
import org.appdynamics.utilreports.conf.Load_Check;
import org.appdynamics.utilreports.conf.CheckXML;
import org.appdynamics.utilreports.resources.AppDUtilReportS;
import org.appdynamics.appdrestapi.RESTAccess;
import org.appdynamics.appdrestapi.data.Applications;
import org.appdynamics.appdrestapi.data.Application;

import java.util.ArrayList;

import java.util.logging.Logger;
import java.util.logging.Level;
import org.appdynamics.utilreports.actions.BKExecutor;
import org.appdynamics.utilreports.actions.ThreadExecutor;
import org.appdynamics.utilreports.conf.QInfo;


/**
 *
 * @author gilbert.solorzano
 * 
 * This is going to collect the data for a load check.
 * 
 * GatherBTInfo btInfo = new GatherBTInfo(access, appId, minBT);
        GatherBKInfo bkInfo = new GatherBKInfo(access,appId,minBK);
        
        GatherEUMInfo eum0 = new GatherEUMInfo(access, appId, minBT, 0);
        GatherEUMInfo eum1 = new GatherEUMInfo(access, appId, minBT, 1);
        GatherEUMInfo eum2 = new GatherEUMInfo(access, appId, minBT, 2);
 * 
 */
public class GatherLoadCheck {
    private static Logger logger=Logger.getLogger(GatherLoadCheck.class.getName());
    private Applications apps;
    private ArrayList<Thread> execThreads=new ArrayList<Thread>();
    private ArrayList<GatherInfo> infoList=new ArrayList<GatherInfo>();
    private ArrayList<LoadCheck> loadCheckList = new ArrayList<LoadCheck>();
    private GatherBTInfo bt;
    private GatherBKInfo bk;
    private ArrayList<GatherEUMInfo> eum=new ArrayList<GatherEUMInfo>();
    private Load_Check_Set lc;
    private RESTAccess access;
    
    
    public GatherLoadCheck(RESTAccess access,Load_Check_Set lc ){this.lc=lc;this.access=access;}
    
    public void init(){
        setApplications();
        logger.log(Level.INFO,"Initializing collections");
        
        if(lc.getApplication().equals("@@All")){
            //We are going to check for all
            for(Application app:apps.getApplications()){
            logger.log(Level.INFO,new StringBuilder().append("Working with the apps collections for ").append(app.getName()).toString());
                for(Load_Check lcItem: lc.getHcLoadCheck()){
                    if(lcItem.isEnabled()){

                        LoadCheck lCheck=new LoadCheck(lcItem.getName(),access);
                        lCheck.setAppId(Integer.toString(app.getId()));
                        lCheck.setAppName(app.getName());
                        lCheck.setMetricTypeFromName();
                        for(CheckXML chkXML:lcItem.getChecks()){
                            CheckAll chk = new CheckAll(chkXML.getName(),chkXML.getHours(),chkXML.getMin(),lCheck.getMetricIndex(),access,lCheck.getAppId());
                            lCheck.getChecks().add(chk);
                        }

                        loadCheckList.add(lCheck);
                    }
                }
                
            }
            logger.log(Level.INFO,new StringBuilder().append("Total number of checks ").append(loadCheckList.size()).toString());
        }else{
            Application app=getApplication(lc.getApplication());
            for(Load_Check lcItem: lc.getHcLoadCheck()){
                
                if(lcItem.isEnabled()){

                    LoadCheck lCheck=new LoadCheck(lcItem.getName(),access);
                    lCheck.setAppId(Integer.toString(app.getId()));
                    lCheck.setAppName(app.getName());
                    lCheck.setMetricTypeFromName();
                    for(CheckXML chkXML:lcItem.getChecks()){
                        CheckAll chk = new CheckAll(chkXML.getName(),chkXML.getHours(),chkXML.getMin(),lCheck.getMetricIndex(),access,lCheck.getAppId());
                        lCheck.getChecks().add(chk);
                    }

                    loadCheckList.add(lCheck);
                }
            }
        }
        
        ThreadExecutor threadExec=new ThreadExecutor(1);
         for(LoadCheck lc:loadCheckList){
             threadExec.getExecutor().execute(lc);
         }
         
         threadExec.getExecutor().shutdown();
         threadExec.shutdown();
   

    }

    public ArrayList<LoadCheck> getLoadCheckList() {
        return loadCheckList;
    }

    public void setLoadCheckList(ArrayList<LoadCheck> loadCheckList) {
        this.loadCheckList = loadCheckList;
    }
    
    private void setApplications(){
        apps=access.getApplications();
    }
    
    private Application getApplication(String name){
        Application _app=null;
        for(Application app:apps.getApplications()){
            if(app.getName().equals(name)) _app=app;
            if(name.equals(Integer.toString(app.getId()))) _app=app;
        }
        
        return _app;
    }
    
    public String printData(){
        StringBuilder msg = new StringBuilder();
        if(bt != null) msg.append(bt.printData());
        if(bk != null) msg.append(bk.printData());
        for(GatherEUMInfo eu : eum){ if(eu != null) msg.append(eu.printData());}
        return msg.toString();
    }

    public GatherBTInfo getBt() {
        return bt;
    }

    public void setBt(GatherBTInfo bt) {
        this.bt = bt;
    }

    public GatherBKInfo getBk() {
        return bk;
    }

    public void setBk(GatherBKInfo bk) {
        this.bk = bk;
    }

    public ArrayList<GatherEUMInfo> getEum() {
        return eum;
    }

    public void setEum(ArrayList<GatherEUMInfo> eum) {
        this.eum = eum;
    }

    public Load_Check_Set getLc() {
        return lc;
    }

    public void setLc(Load_Check_Set lc) {
        this.lc = lc;
    }
    
    
    
}
