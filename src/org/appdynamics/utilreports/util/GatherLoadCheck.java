/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.appdynamics.utilreports.util;


import org.appdynamics.utilreports.conf.Load_Check_Set;
import org.appdynamics.utilreports.conf.Load_Check;
import org.appdynamics.utilreports.resources.AppDUtilReportS;
import org.appdynamics.appdrestapi.RESTAccess;

import java.util.ArrayList;

import java.util.logging.Logger;
import java.util.logging.Level;


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
    private ArrayList<Thread> execThreads=new ArrayList<Thread>();
    private GatherBTInfo bt;
    private GatherBKInfo bk;
    private ArrayList<GatherEUMInfo> eum=new ArrayList<GatherEUMInfo>();
    private Load_Check_Set lc;
    private RESTAccess access;
    
    
    public GatherLoadCheck(RESTAccess access,Load_Check_Set lc ){this.lc=lc;this.access=access;}
    
    public void init(){
        logger.log(Level.INFO,"Initializing collections");
        for(Load_Check lcItem: lc.getHcLoadCheck()){
            if(lcItem.isEnabled()){
                
                if(lcItem.getName().equals(AppDUtilReportS.BT_CHECK)){ 
                    bt=new GatherBTInfo(access,lc.getApplication(),lcItem.getMin(),lcItem.getMin24(),lcItem.getMin48());
                    execThreads.add(new Thread(bt));
                }
                if(lcItem.getName().equals(AppDUtilReportS.BE_CHECK)){ 
                    bk=new GatherBKInfo(access,lc.getApplication(),lcItem.getMin(),lcItem.getMin24(),lcItem.getMin48());
                    execThreads.add(new Thread(bk));
                }
                if(lcItem.getName().equals(AppDUtilReportS.EUM_AJAX_CHECK)){   
                    GatherEUMInfo eum1=new GatherEUMInfo(access,lc.getApplication(),lcItem.getMin(),lcItem.getMin24(),lcItem.getMin48(),0);
                    eum.add(eum1); 
                    execThreads.add(new Thread(eum1));
                }
                if(lcItem.getName().equals(AppDUtilReportS.EUM_BASE_PAGE_CHECK)){ 
                   GatherEUMInfo eum1=new GatherEUMInfo(access,lc.getApplication(),lcItem.getMin(),lcItem.getMin24(),lcItem.getMin48(),1);
                    eum.add(eum1);
                    execThreads.add(new Thread(eum1));
                }
                if(lcItem.getName().equals(AppDUtilReportS.EUM_IFRAME_CHECK)){ 
                    GatherEUMInfo eum1=new GatherEUMInfo(access,lc.getApplication(),lcItem.getMin(),lcItem.getMin24(),lcItem.getMin48(),2);
                    eum.add(eum1);
                    execThreads.add(new Thread(eum1));
                }
                    
            }
        }
        
        for(Thread th: execThreads) th.start();
        
        
        try{
            Thread.sleep(2000);
            for(Thread th: execThreads) th.join();
            
        }catch(Exception e){
            logger.log(Level.WARNING, new StringBuilder().append("Exception ").append(e.getMessage()).toString());
        }
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
