/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.appdynamics.markH.file;

import org.appdynamics.markH.resources.MarkNodeS;
import org.appdynamics.markH.data.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.Set;
import java.util.HashSet;
import org.appdynamics.utilreports.conf.Account;
import org.appdynamics.utilreports.conf.Controller;
import org.appdynamics.utilreports.conf.MetricLog;
import org.appdynamics.utilreports.conf.ServerConfig;
import org.appdynamics.utilreports.resources.AppDUtilReportS;

/**
 *
 * @author gilbert.solorzano
 */
public class ReadConfig {
    private MarkHList report;
    
    public ReadConfig(){}
    public ReadConfig(String filePath){init(filePath);}
    
    
    private void init(String filePath){
        BufferedInputStream configFile = null;
        //metricLog.info("Starting the logging on metric ");
        try{
            report = new MarkHList();
            
            configFile = new BufferedInputStream(new FileInputStream(filePath));
            
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(configFile);
           

            
            report.setServerConfig(setServerInfo((Element)doc.getElementsByTagName(AppDUtilReportS.SERVER_CONFIG).item(0)));
            setApplications(doc.getElementsByTagName(MarkNodeS.APPLICATION));
            // We need to set the applications


        }catch(Exception e){
            System.out.println(e.getMessage());
        }finally{
            try{ configFile.close();}catch(Exception e){}
        }
        
        
    }
    
    private void setApplications(NodeList appList) throws Exception{
        
        for(int i=0; i < appList.getLength(); i++){
            ApplicationCheck chk = new ApplicationCheck();
            Element app = (Element)appList.item(i);
            // .item(i).getAttributes().getNamedItem(AppDUtilReportS.FORMAT).getNodeValue()
            
            chk.setMinimumCount(stringToInt(app.getAttributes().getNamedItem(MarkNodeS.MIN_COUNT).getNodeValue(),1));
            chk.setHoursToCheck(stringToInt(app.getAttributes().getNamedItem(MarkNodeS.HOURS_TO_CHECK).getNodeValue(),4));
            chk.setName(app.getAttributes().getNamedItem(MarkNodeS.NAME).getNodeValue());
            NodeList tiers = app.getElementsByTagName(MarkNodeS.TIER);
            for(int a=0; a < tiers.getLength(); a++){
                Element tier = (Element) tiers.item(a);
                String myType = tier.getAttributes().getNamedItem(MarkNodeS.CHECK_TYPE).getNodeValue();
                String myValue = tier.getAttributes().getNamedItem(MarkNodeS.NAME_VALUE).getNodeValue();
                TierCheck chkTier = new TierCheck(myValue,getType(myType));
                NodeList nodes = tier.getElementsByTagName(MarkNodeS.NODE);
                for(int b=0; b < nodes.getLength(); b++){
                    Element node = (Element)nodes.item(b);
                    String myTypeN = node.getAttributes().getNamedItem(MarkNodeS.CHECK_TYPE).getNodeValue();
                    String myValueN = node.getAttributes().getNamedItem(MarkNodeS.NAME_VALUE).getNodeValue();
                    NodeCheck chkNode = new NodeCheck(myValueN,getType(myTypeN));
                    chkTier.getNodeChecks().add(chkNode);
                }
                chk.getTierChecks().add(chkTier);
            }
            report.getApps().add(chk);
        }
        
    }
    
    
    
    private ServerConfig setServerInfo(Element serverXML) throws Exception{
        ServerConfig serverConfig = new ServerConfig();
        // serverConfigXML.getElementsByTagName(ExportS.INTERVAL).item(0).getTextContent();
        Account account = new Account();
        account.setAccount(serverXML.getElementsByTagName(AppDUtilReportS.ACCOUNT).item(0).getTextContent());
        account.setUser(serverXML.getElementsByTagName(AppDUtilReportS.USER).item(0).getTextContent());
        account.setPasswd(serverXML.getElementsByTagName(AppDUtilReportS.PASSWORD).item(0).getTextContent());
        serverConfig.setAccount(account);
        
        Controller controller=new Controller();
        controller.setFqdn(serverXML.getElementsByTagName(AppDUtilReportS.CONTROLLER).item(0).getTextContent());
        controller.setPort(serverXML.getElementsByTagName(AppDUtilReportS.CONTROLLER).item(0).getAttributes().getNamedItem(AppDUtilReportS.PORT).getTextContent());
        controller.setUseSSL(getBoolean(serverXML.getElementsByTagName(AppDUtilReportS.CONTROLLER).item(0).getAttributes().getNamedItem(AppDUtilReportS.USESSL).getTextContent()));
        serverConfig.setController(controller);
        
        MetricLog serverLog=new MetricLog();
        serverLog.setType("Server log");
        serverLog.setFullPath(serverXML.getElementsByTagName(AppDUtilReportS.SERVER_LOG).item(0).getTextContent());
        serverLog.setMaxNumber(stringToInt(serverXML.getElementsByTagName(AppDUtilReportS.SERVER_LOG).item(0).getAttributes().getNamedItem(AppDUtilReportS.MAX_NUMBER).getTextContent(),5));
        serverLog.setMaxSize(stringToInt(serverXML.getElementsByTagName(AppDUtilReportS.SERVER_LOG).item(0).getAttributes().getNamedItem(AppDUtilReportS.MAX_SIZE).getTextContent(),20));
        
        
        serverConfig.setServerLog(serverLog);
        
        return serverConfig;
    }
    
    public CheckType getType(String type){
        int indexId=0;
        for(int i=0; i < MarkNodeS.SEACH_TYPES.length; i++) if(type.equals(MarkNodeS.SEACH_TYPES[i])) indexId=i;
        switch(indexId){
            case 0:
                return CheckType.CONTAINS;
                
            case 1:
                return CheckType.STARTSWITH;
            case 2:
                return CheckType.ENDSWITH;
            case 4:
                return CheckType.ANY;
            case 5:    
                return CheckType.REGEX;
        }
        return CheckType.CONTAINS;
    }
    
    public boolean getBoolean(String value){
        return Boolean.valueOf(value).booleanValue();
    }
    
    public int stringToInt(String value, int defaultVal){
        
        try{
            return new Integer(value).intValue();
        }catch(Exception e){}
        
        return defaultVal;
    }

    public MarkHList getReport() {
        return report;
    }

    public void setReport(MarkHList report) {
        this.report = report;
    }
    
    
    
}
