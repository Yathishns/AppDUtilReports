/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.appdynamics.utilreports.conf;

import org.appdynamics.utilreports.resources.AppDUtilReportS;

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


/**
 *
 * @author gilbert.solorzano
 */
public class ReadConfig {
    
    private AppDHCReport report;
    
    public ReadConfig(){}
    public ReadConfig(String filePath){init(filePath);}
    
    private void init(String filePath){
        BufferedInputStream configFile = null;
        //metricLog.info("Starting the logging on metric ");
        try{
            report = new AppDHCReport();
            
            configFile = new BufferedInputStream(new FileInputStream(filePath));
            
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(configFile);
           
            //Element serverConfigXML = (Element)doc.getElementsByTagName(ExportS.SERVER_CONFIG).item(0);
            
            report.setServerConfig(setServerInfo((Element)doc.getElementsByTagName(AppDUtilReportS.SERVER_CONFIG).item(0)));
            report.setHcLoadChecks(setHCLoadChecks((Element)doc.getElementsByTagName(AppDUtilReportS.HC_LOAD_CHECK).item(0)));

            //appDMetricExConf.setMetricCollections(setMetricExportInfo((Element)doc.getElementsByTagName(ExportS.METRIC_COLLECTIONS).item(0)));
            
            //appDMetricExConf.setMetricOutput(setMetricOutput((Element)doc.getElementsByTagName(ExportS.METRIC_OUTPUT).item(0)));

        }catch(Exception e){
            System.out.println(e.getMessage());
        }finally{
            try{ configFile.close();}catch(Exception e){}
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
    
    private HC_Load_Checks setHCLoadChecks(Element hcLoadChecksXML) throws Exception{
        HC_Load_Checks checks = new HC_Load_Checks();
        Outputs outs = new Outputs();
        Load_Check_Set checkSet = new Load_Check_Set();
        
        Element outsXML=(Element)hcLoadChecksXML.getElementsByTagName(AppDUtilReportS.OUTPUTS).item(0);
        Element checksXML=(Element)hcLoadChecksXML.getElementsByTagName(AppDUtilReportS.LOAD_CHECK_SET).item(0);
        checkSet.setApplication(checksXML.getAttribute(AppDUtilReportS.APPLICATION));
        // We need to iterate through them
        for(int i=0; i < outsXML.getElementsByTagName(AppDUtilReportS.OUTPUT).getLength(); i++){
            Output output = new Output();
            output.setFormat(outsXML.getElementsByTagName(AppDUtilReportS.OUTPUT).item(i).getAttributes().getNamedItem(AppDUtilReportS.FORMAT).getNodeValue());
            output.setType(outsXML.getElementsByTagName(AppDUtilReportS.OUTPUT).item(i).getAttributes().getNamedItem(AppDUtilReportS.TYPE).getNodeValue());
            output.setFile(outsXML.getElementsByTagName(AppDUtilReportS.OUTPUT).item(i).getTextContent());
            outs.getOutpus().add(output);
        }
        
       
        for(int i=0; i < checksXML.getElementsByTagName(AppDUtilReportS.LOAD_CHECK).getLength(); i++){
            Element loadC = (Element)checksXML.getElementsByTagName(AppDUtilReportS.LOAD_CHECK).item(i);
            
            Load_Check lc = new Load_Check();
            lc.setName(loadC.getAttributes().getNamedItem(AppDUtilReportS.NAME).getNodeValue());
            lc.setEnabled(getBoolean(loadC.getAttributes().getNamedItem(AppDUtilReportS.ENABLED).getNodeValue()));
            for(int a=0; a < loadC.getElementsByTagName(AppDUtilReportS.CHECK).getLength();a++){
                
                Element check_ = (Element)loadC.getElementsByTagName(AppDUtilReportS.CHECK).item(a);
                CheckXML check = new CheckXML();
                check.setName(check_.getAttribute(AppDUtilReportS.NAME));
                check.setHours(stringToInt(check_.getAttribute(AppDUtilReportS.HOURS),AppDUtilReportS.DEF_MIN));
                check.setMin(stringToInt(check_.getNodeValue(),AppDUtilReportS.DEF_MIN));
                lc.getChecks().add(check);
            }
            checkSet.getHcLoadCheck().add(lc);
            //System.out.println(lc.toString());
        }
        //System.out.println(checkSet.getApplication());
        //System.out.println(checksXML.getAttributes().getNamedItem(AppDUtilReportS.APPLICATION).getNodeValue());
        /*
        for(int i=0; i < checksXML.getElementsByTagName(AppDUtilReportS.LOAD_CHECK).getLength(); i++){
            Element loadC = (Element)checksXML.getElementsByTagName(AppDUtilReportS.LOAD_CHECK).item(i);
            Load_Check lc = new Load_Check();
            lc.setName(loadC.getAttributes().getNamedItem(AppDUtilReportS.NAME).getNodeValue());
            lc.setEnabled(getBoolean(loadC.getAttributes().getNamedItem(AppDUtilReportS.ENABLED).getNodeValue()));
            lc.setMin(stringToInt(loadC.getElementsByTagName(AppDUtilReportS.MIN_COUNT).item(0).getTextContent().toString(),AppDUtilReportS.DEF_MIN));
            lc.setMin24(stringToInt(loadC.getElementsByTagName(AppDUtilReportS.MIN24_COUNT).item(0).getTextContent().toString(),AppDUtilReportS.DEF_MIN24));
            lc.setMin48(stringToInt(loadC.getElementsByTagName(AppDUtilReportS.MIN48_COUNT).item(0).getTextContent().toString(),AppDUtilReportS.DEF_MIN48));
            checkSet.getHcLoadCheck().add(lc);
            //System.out.println(lc.toString());
            
        }
        */
        
        checks.getLoadCheckSet().add(checkSet);
        checks.setOutputs(outs);
        return checks;
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

    public AppDHCReport getReport() {
        return report;
    }

    public void setReport(AppDHCReport report) {
        this.report = report;
    }
    
    
}
