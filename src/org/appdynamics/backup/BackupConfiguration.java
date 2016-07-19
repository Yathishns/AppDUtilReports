/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.appdynamics.backup;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import org.appdynamics.backup.resources.BackupConfigS;
import org.appdynamics.backup.util.BackupConfigurationOptions;

import org.appdynamics.appdrestapi.RESTAccess2;
import org.appdynamics.appdrestapi.data.*;
import org.appdynamics.appdrestapi.resources.s;

import java.util.Calendar;
import java.util.HashMap;
import java.util.logging.Logger;
import java.util.logging.Level;

import java.text.SimpleDateFormat;
/**
 *
 * @author gilbert.solorzano
 * 
 * This is going 
 * 
 */
public class BackupConfiguration {
    private static Logger logger=Logger.getLogger(BackupConfiguration.class.getName());
    
    public BackupConfiguration(){
        // Its up to the developer to setup the needed variables.
    }
    
    public static void main(String[] args){
        // controller host (-h), controller port (-P), use ssl (-s) , account (-a), user (-u), passwd (-p), directory (-D)
        BackupConfigurationOptions bkOptions = new BackupConfigurationOptions(args);
        boolean continueExec = bkOptions.parse();
        
        if(!continueExec){
            logger.log(Level.SEVERE,"The appropriate options were not provided, exiting..");
            System.exit(1);
        }
        
        init();
    }
    
    public static void init(){
        StringBuilder autoExp=new StringBuilder(),customMatch=new StringBuilder(),healthRules=new StringBuilder();
        // First we get the information to connect
        RESTAccess2 access = new RESTAccess2(BackupConfigS.CONTROLLER_V,BackupConfigS.PORT_V,
                BackupConfigS.SSL_V,BackupConfigS.USERNAME_V,BackupConfigS.PASSWD_V,BackupConfigS.ACCOUNT_V);
        //access.setDebugLevel(3);
        String fileDateName = getDateName();
        // We get the applications in the controller
        Applications apps = access.getApplications();
        String appVal;
        
        for(Application app : apps.getApplications()){
             appVal = access.getApplicationExportById(app.getId());
             logger.info(new StringBuilder().append("Working on export of application: ").append(app.getName()).toString());
             writeFile(app.getName(),fileDateName,appVal,0);
             sleepIt(1000);
        }
        
        /*
         For every app we need to get the tier, then request the information per tier.
        */
        String name;
        for(Application app: apps.getApplications()){
            Tiers tiers = access.getTiersForApplication(app.getId());

            name=new StringBuilder().append(app.getName()).append("_Global_Auto").toString();
            appVal=access.getRESTExportOfAuto(app.getName());
            writeFile(name,fileDateName,appVal,3);
            //Level|type|App|[Tier]|File
            autoExp.append("Application|Auto|").append(app.getName()).append("||").append(name).append(BackupConfigS.XML_END).append("\n");
            // Let's get the app stuff for <AppName>_Global_Auto.xml and <AppName>_Global_POJO.xml
            int count=0;
            for(int i=0 ; i < 1;i++){
                String val=s.JAVA_CUSTOM_MATCHES[i];
                name=new StringBuilder().append(app.getName()).append("_").append(val).toString();
                appVal=access.getRESTCustomJavaExport(count, app.getName(), null);
                if(appVal != null && appVal.length() > 100){ 
                    writeFile(name,fileDateName,appVal,3);
                    customMatch.append("Application|JAVA_CUSTOM|").append(val).append("|").append(app.getName()).append("||").append(name).append(BackupConfigS.XML_END).append("\n");
                }
                count+=2;
                sleepIt(1000);
            }
            
            count=0;
            for(int i=0 ; i < 1;i++){
                String val= s.DOTNET_CUSTOM_MATCHES[i];
                name=new StringBuilder().append(app.getName()).append("_").append(val).toString();
                appVal=access.getRESTCustomDotNetExport(count, app.getName(), null);
                if(appVal != null && appVal.length() > 100){
                    writeFile(name,fileDateName,appVal,3);
                    customMatch.append("Application|DOTNET_CUSTOM|").append(val).append("|").append(app.getName()).append("||").append(name).append(BackupConfigS.XML_END).append("\n");
                }
                count+=2;
                sleepIt(1000);
            }
            
            
            for(Tier tier:tiers.getTiers()){
                   logger.info(new StringBuilder().append("Working on auto  ").append(app.getName()).append(" ").append(tier.getName()).toString());
                    name=new StringBuilder().append(app.getName()).append("_").append(tier.getName()).append("_Auto").toString();
                    appVal=access.getRESTExportOfAuto(app.getName(),tier.getName(),null);
                    writeFile(name,fileDateName,appVal,3);
                    autoExp.append("Tier|Auto|").append(app.getName()).append("|").append(tier.getName()).append("|").append(name).append(BackupConfigS.XML_END).append("\n");
                    // Let's get the app stuff for <AppName>_Global_Auto.xml and <AppName>_Global_POJO.xml
                    count=0;
                    for(int i=0 ; i < 1;i++){
                        String val= s.JAVA_CUSTOM_MATCHES[i];
                        name=new StringBuilder().append(app.getName()).append("_").append(tier.getName()).append("_").append(val).toString();
                        appVal=access.getRESTCustomJavaExport(count, app.getName(),tier.getName(), null);
                        if(appVal != null && appVal.length() > 100){
                            writeFile(name,fileDateName,appVal,3);
                            customMatch.append("Tier|JAVA_CUSTOM|").append(val).append("|").append(app.getName()).append("|").append(tier.getName()).append("|").append(name).append(BackupConfigS.XML_END).append("\n");
                        }
                        count+=2;
                        sleepIt(1000);
                    }

                    count=0;
                    for(int i=0 ; i < 1;i++){
                        String val= s.DOTNET_CUSTOM_MATCHES[i];
                        name=new StringBuilder().append(app.getName()).append("_").append(tier.getName()).append("_").append(val).toString();
                        appVal=access.getRESTCustomDotNetExport(count, app.getName(),tier.getName(), null);
                        if(appVal != null && appVal.length() > 100){
                            writeFile(name,fileDateName,appVal,3);
                             customMatch.append("Tier|DOTNET_CUSTOM|").append(val).append("|").append(app.getName()).append("|").append(tier.getName()).append("|").append(name).append(BackupConfigS.XML_END).append("\n");
                        }
                        count+=2;
                        sleepIt(1000);
                    }
                    count=0;
                    for(int i=0 ; i < 1;i++){
                        String val= s.NODEJS_CUSTOM_MATCHES[i];
                        name=new StringBuilder().append(app.getName()).append("_").append(tier.getName()).append("_").append(val).toString();
                        //appVal=access.getRESTCustomDotNetExport(count, app.getName(),tier.getName(), null);
                        if(appVal != null && appVal.length() > 100){
                            writeFile(name,fileDateName,appVal,3);
                             customMatch.append("Tier|DOTNET_CUSTOM|").append(val).append("|").append(app.getName()).append("|").append(tier.getName()).append("|").append(name).append(BackupConfigS.XML_END).append("\n");
                        }
                        count+=2;
                        sleepIt(1000);
                    }
                    count=0;
                    for(int i=0 ; i < 1;i++){
                        String val= s.PHP_CUSTOM_MATCHES[i];
                        name=new StringBuilder().append(app.getName()).append("_").append(tier.getName()).append("_").append(val).toString();
                        //appVal=access.getRESTCustomDotNetExport(count, app.getName(),tier.getName(), null);
                        if(appVal != null && appVal.length() > 100){
                            writeFile(name,fileDateName,appVal,3);
                             customMatch.append("Tier|DOTNET_CUSTOM|").append(val).append("|").append(app.getName()).append("|").append(tier.getName()).append("|").append(name).append(BackupConfigS.XML_END).append("\n");
                        }
                        count+=2;
                        sleepIt(1000);
                    }
            }
        }
        
        // Extract HealthRules
        for(Application app: apps.getApplications()){
            name=new StringBuilder().append(app.getName()).append("_HealthRules").toString();
            appVal=access.getRESTHealthRuleExportAll(app.getName());
            //AppName|File
            writeFile(name,fileDateName,appVal,2);
            healthRules.append(app.getName()).append("|").append(name).append(BackupConfigS.XML_END).append("\n");
            sleepIt(1000);
        }
        
        writeFile("CUSTOMMATCH_PROPS",fileDateName,customMatch.toString(),3);
        writeFile("AUTOMATCH_PROPS",fileDateName,autoExp.toString(),3);
        writeFile("HEALTHRULES_PROPS",fileDateName,healthRules.toString(),2);
        
        int i = 0;
        int limit=300;
        while(limit > i){
            i++;
            //logger.info(new StringBuilder().append("Working on dashboard index: ").append(i).append(".").toString());
            Dashboard rep = access.getDashboardExportById(i);
            if(rep.isExists()){
                 logger.info(new StringBuilder().append("Working on export of dashboard: ").append(rep.getName()).toString());
                writeFile(rep.getName(),fileDateName,rep.getValue(),1);
                sleepIt(1000);
            }
        }
                
    }
    
    public static int writeFile(String appName, String dateName, String fileContent, int type){
        int retValue=0;
        //retValue = createChgDirectory(changeId);

        String baseDir1=new StringBuilder().append(BackupConfigS.DIRECTORY_V).append(BackupConfigS.P).append(BackupConfigS.DIRECTORY_Name_V).append(BackupConfigS.P).toString();
        
        File dir = new File(baseDir1);
        if(!dir.exists()) dir.mkdir();
        
        
        String baseDir=new StringBuilder().append(baseDir1).append(BackupConfigS.DIRECTORY_PREFIX_EXPORTS).append(dateName).toString();
        if(type==1) baseDir=new StringBuilder().append(baseDir1).append(BackupConfigS.DIRECTORY_PREFIX_DASHBOARDS).append(dateName).toString();
        if(type==2) baseDir=new StringBuilder().append(baseDir1).append(BackupConfigS.DIRECTORY_PREFIX_HEALTHRULES).append(dateName).toString();
        if(type==3) baseDir=new StringBuilder().append(baseDir1).append(BackupConfigS.DIRECTORY_PREFIX_MATCHRULES).append(dateName).toString();
        
        
        
        File dir1 = new File(baseDir);
        if(!dir1.exists()) dir1.mkdir();
        
        String fileName1 = new StringBuilder().append(baseDir).append(BackupConfigS.P)
                .append(appName).append(BackupConfigS.XML_END).toString();
        
        logger.log(Level.INFO, new StringBuilder().append("Writting the following application file ")
                .append(fileName1).append(".").toString());
        
        retValue=writeFile(fileName1,fileContent);
        
        return retValue;
    }
    
    private static int writeFile(String fileName, String fileContent){
        int retValue=0;
        BufferedWriter outF=null;
        try{
            File file = new File(fileName);
            outF=new BufferedWriter(new FileWriter(file));
            outF.write(fileContent);
            outF.flush();
            outF.close();
        }catch(Exception e){
            logger.severe(new StringBuilder()
                    .append("Exception occurred while attempting to write file: ")
                    .append(fileName).append("\n. With content : ").append(fileContent)
                    .append(".\n With exception: ").append(e.getMessage()).toString());
            retValue=1;
        }finally{
            try{
                if(outF != null) outF.close();
            }catch(Exception e){}
        }
        
        return retValue;
    }
    
    public static String getDateName(){
        SimpleDateFormat smf = new SimpleDateFormat(BackupConfigS.SIMPLE_DATE_FORMAT);
 
        return smf.format(Calendar.getInstance().getTime());
    }
    
    private static void sleepIt(long t){
        try{
            Thread.sleep(t);
        }catch(Exception e){}
    }
    
}
