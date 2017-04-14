/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.appdynamics.licenseCheck.util;

import org.appdynamics.utilreports.util.*;
import org.appdynamics.utilreports.conf.*;


import org.appdynamics.appdrestapi.RESTAccess;
import org.appdynamics.appdrestapi.data.*;
import org.appdynamics.crypto.*;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.ArrayList;
import java.util.Calendar;
import org.appdynamics.licenseCheck.resources.LicenseCheckS;

/**
 *
 * @author gilbert.solorzano
 * 
 * This is going to get the AppHCReport and process it.
 * 
 * First thing is to get the server information.
 * 
 */
public class ProcessXMLConfig {
    private static Logger logger=Logger.getLogger(ProcessXMLConfig.class.getName());
    private AppLCheck appDHC;
    private RESTAccess access;
    private long start,end;
    private ArrayList<BaseEntry> loadChecks=new ArrayList<BaseEntry>();
    
    public ProcessXMLConfig(AppLCheck appDHC){
        this.appDHC=appDHC;
    }
    
    public ProcessXMLConfig(AppLCheck appDHC, ArrayList<BaseEntry> loadChecks){
        this.appDHC=appDHC;
        this.loadChecks=loadChecks;
    }
    
    public void init() throws Exception{

        
        // If the method of decryption needs to change just update the DeCrypt class
        String pwd=org.appdynamics.utilreports.conf.DeCrypt.decrypt(appDHC.getServerConfig().getAccount().getPasswd());
        ServerConfig srv=appDHC.getServerConfig();
        access = new RESTAccess(srv.getController().getFqdn(),srv.getController().getPort(),
                srv.getController().isUseSSL(),srv.getAccount().getUser(),pwd,srv.getAccount().getAccount());
        Calendar cal = Calendar.getInstance();
        end=cal.getTimeInMillis();
        cal.add(Calendar.HOUR, (-1 * appDHC.getInfoCheck().getHours()));
        logger.log(Level.INFO,"Created connection");
        
        // Now that we have our base + the notifications we need to add our info
    }
    
    public String printData(){
        return null;
    }

    public RESTAccess getAccess() {
        return access;
    }

    public void setAccess(RESTAccess access) {
        this.access = access;
    }
    
    

    public ArrayList<BaseEntry> createBase(){
        logger.log(Level.INFO,"Getting applications");
        Applications apps = access.getApplications();
        ArrayList<BaseEntry> base=new ArrayList<BaseEntry>();
        
        
        if(apps != null){
            logger.log(Level.INFO,"Iterating through the list of applications");
            for(Application app: apps.getApplications()){  
                //LicenseCheckS.DEBUG=false;
               // if(app.getName().startsWith("BDR")) LicenseCheckS.DEBUG=true;
                // Here we grab the node level metrics for the agent and machine agent
                MetricDatas appMetrics = access.getAgentTierAppMetricQuery(0, app.getName(), "*", start, end,true);
                MetricDatas machineMetrics = access.getAgentTierAppMetricQuery(1, app.getName(), "*", start, end,true);

                // Just one additional check
                if(appMetrics == null){
                    try{Thread.sleep(1500);}catch(Exception e){}
                    appMetrics = access.getAgentTierAppMetricQuery(0, app.getName(), "*", start, end,true);
                }
                
                if(appMetrics != null){
                          
                            for(MetricData mds:appMetrics.getMetric_data()){
                                  //if(LicenseCheckS.DEBUG) logger.log(Level.INFO,mds.toString());
                                    String[] path_=parse(mds.getMetricPath()); //Application Infrastructure Performance|1stTier|Agent|App|Availability
                                    if(path_ != null && path_.length == 5){
                                        
                                        if(mds.hasNoValues() != true){ base.add(new BaseEntry(app.getName(),path_[1],new Long(mds.getSingleValue().getValue()).intValue() ));}
                                        else{ base.add(new BaseEntry(app.getName(),path_[1],0 ));}
                                    }
                            }

                }
                
                if(machineMetrics == null){
                     try{Thread.sleep(1500);}catch(Exception e){}
                    machineMetrics = access.getAgentTierAppMetricQuery(1, app.getName(), "*", start, end,true);
                }
                
                if(machineMetrics != null){
                    //logger.log(Level.INFO,"Iterating through tier level info for machine agents");
                    for(MetricData mds:machineMetrics.getMetric_data()){
                          //if(LicenseCheckS.DEBUG) logger.log(Level.INFO,mds.toString());
                                    String[] path_=parse(mds.getMetricPath()); //Application Infrastructure Performance|1stTier|Agent|App|Availability
                                    if(path_ != null && path_.length == 5){
                                        
                                        if(mds.hasNoValues() != true){ 
                                            for(BaseEntry b:base){
                                                if(b.getApplication().equalsIgnoreCase(app.getName()) && b.getTier().equalsIgnoreCase(path_[1])) 
                                                    b.setMachine(new Long(mds.getSingleValue().getValue()).intValue() );
                                            }
                                        }
                                        
                                    }
                    }
                }
                    
                
            }
        }

        
        return base;
    }
    
    public Messages checkTheLicense(){
        Messages msgs=new Messages();
        ArrayList<BaseEntry> newBase = createBase();
        java.util.Iterator<BaseEntry> baseIter = loadChecks.iterator();
        // First we are going to look for the entries that we know of
        logger.log(Level.INFO,"Iterating through base list");
        while(baseIter.hasNext()){
            
            BaseEntry obj=baseIter.next();
            java.util.Iterator<BaseEntry> newBaseIter = newBase.iterator();
            boolean fnd=false;
            logger.log(Level.INFO,"Working on {0}",obj);
            while(newBaseIter.hasNext()){
                BaseEntry b = newBaseIter.next();
                 //logger.log(Level.INFO,"\tChecking against {0}",b);
                if(b.getApplication().equalsIgnoreCase(obj.getApplication()) && b.getTier().equalsIgnoreCase(obj.getTier())){
                    fnd=true;
                    //logger.log(Level.INFO,"\t\tChecking against {0}",b);
                    if(b.getNumber() > obj.getNumber()) msgs.getTooManyAgents().add(getMessage(0,b,obj));
                    //logger.log(Level.INFO,"\t\tChecking machine");
                    if(b.getMachine() > obj.getMachine()) msgs.getTooManyAgents().add(getMessage(1,b,obj));
                    //logger.log(Level.INFO,"\t\tChecking app");
                    if(b.getNumber() < obj.getNumber()) msgs.getTooFewAgents().add(getMessage(0,b,obj));
                    //logger.log(Level.INFO,"\t\tChecking machine 2");
                    if(b.getMachine() < obj.getMachine()) msgs.getTooFewAgents().add(getMessage(1,b,obj));
                    //logger.log(Level.INFO,"\t\tRemoving information");
                    newBaseIter.remove(); // We found it get rid of it
                }
            }
            //logger.log(Level.INFO,"\tChecking if found.");
            if(!fnd){
                // We did not find it then 
                msgs.getTooFewAgents().add(getMessage(2,obj,obj));
            }
            //logger.log(Level.INFO,"\tRemove the file.");
            // We don't need this
            baseIter.remove();
        }
        
        // Now we reverse it, with 
        logger.log(Level.INFO,"Iterating through new list");
        for(BaseEntry b:newBase){
            msgs.getNewObjects().add(getMessage(3,b,b));
        }
        
        
        return msgs;
    }
    
    public String getMessage(int msgId, BaseEntry _new, BaseEntry _Base){
        String msg="";
        switch(msgId){
            case 0:
                   msg=new StringBuilder().append("Application (").append(_new.getApplication())
                           .append(") and tier (").append(_new.getTier()).append("): Found that controller has ")
                           .append(_new.getNumber()).append(" app agent/s but we expect ")
                           .append(_Base.getNumber()).append(".").append(System.lineSeparator()).toString();
                   break;
            case 1:
                   msg=new StringBuilder().append("Application (").append(_new.getApplication())
                           .append(") and tier (").append(_new.getTier()).append("): Found that controller has ")
                           .append(_new.getNumber()).append(" machine agent/s but we expect ")
                           .append(_Base.getNumber()).append(".").append(System.lineSeparator()).toString();
                   break;
            case 2:
                   msg = new StringBuilder().append("Application (").append(_new.getApplication())
                           .append(") and tier (").append(_new.getTier()).append(") is expected to have ").append(_new.getNumber())
                           .append(" agent/s: Found that the controller does not have these authorized agents")
                           .append(System.lineSeparator()).toString();
                    break;
            case 3:
                   msg = new StringBuilder().append("Application (").append(_new.getApplication())
                           .append(") and tier (").append(_new.getTier()).append(") with ").append(_new.getNumber()).append(" app agent/s and ")
                           .append(_new.getMachine()).append(" machine agent/s ")
                           .append(": Found that the controller has these additional entries which is more than we expected.")
                           .append(System.lineSeparator()).toString();
                    
                    break;
        }
        
        return msg;
    }
    
    public String  addDateToFile(String fileName){
        return null;
    }
    
    public  String[] parse(String metricPath){
        
        if(metricPath != null){
            return metricPath.split("\\|");
        }
        return null;
    }
}
