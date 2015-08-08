/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.appdynamics.licenseCheck.util;


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
import org.appdynamics.licenseCheck.resources.LicenseCheckS;


/**
 *
 * @author gilbert.solorzano
 */
public class ReadConfig {
    
    private AppLCheck report;
    
    public ReadConfig(){}
    public ReadConfig(String filePath){init(filePath);}
    
    private void init(String filePath){
        BufferedInputStream configFile = null;
        //metricLog.info("Starting the logging on metric ");
        try{
            report = new AppLCheck();
            
            configFile = new BufferedInputStream(new FileInputStream(filePath));
            
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(configFile);

            report.setServerConfig(setServerInfo((Element)doc.getElementsByTagName(AppDUtilReportS.SERVER_CONFIG).item(0)));

            report.setInfoCheck(setInfoCheck((Element)doc.getElementsByTagName(LicenseCheckS.INFO_CHECK).item(0)));

            report.setMailInfo(setMailInfo((Element)doc.getElementsByTagName(LicenseCheckS.MAIL_INFO).item(0)));

        }catch(Exception e){
            System.out.println(e.getMessage());
        }finally{
            try{ configFile.close();}catch(Exception e){}
        }
        
        
    }
    
    private MailInfo setMailInfo(Element mailXML) throws Exception{
        MailInfo info = new MailInfo();
        info.setMailAuth(Boolean.parseBoolean(mailXML.getElementsByTagName(LicenseCheckS.MAIL_AUTH).item(0).getTextContent()) );
        info.setMailUser(mailXML.getElementsByTagName(LicenseCheckS.MAIL_USER).item(0).getTextContent());
        info.setMailPasswd(mailXML.getElementsByTagName(LicenseCheckS.MAIL_PASSWD).item(0).getTextContent());
        info.setMailHost(mailXML.getElementsByTagName(LicenseCheckS.MAIL_HOST).item(0).getTextContent());
        info.setMailPort(mailXML.getElementsByTagName(LicenseCheckS.MAIL_PORT).item(0).getTextContent());
        return info;
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
        
        return serverConfig;
    }
    
    private InfoCheck setInfoCheck(Element hcLoadChecksXML) throws Exception{
        InfoCheck checks = new InfoCheck();
        //System.out.println(hcLoadChecksXML.getAttributes().getNamedItem(LicenseCheckS.CHECK_LAST_HOURS));
        checks.setHours(new Integer(hcLoadChecksXML.getAttributes().getNamedItem(LicenseCheckS.CHECK_LAST_HOURS).getNodeValue()));
        Element outsXML;
        for(int i = 0; i < hcLoadChecksXML.getElementsByTagName(LicenseCheckS.OUTPUT).getLength(); i++){
                outsXML=(Element)hcLoadChecksXML.getElementsByTagName(LicenseCheckS.OUTPUT).item(0);

                Resource output = new Resource();
                    output.setFormat(outsXML.getAttributes().getNamedItem(LicenseCheckS.FORMAT).getNodeValue());
                    output.setType(outsXML.getAttributes().getNamedItem(LicenseCheckS.TYPE).getNodeValue());
                    output.setName(outsXML.getTextContent());
                if(output.getType().equalsIgnoreCase("email")){
                    checks.setEmail(output);
                    checks.setRest(true);
                }else{
                    checks.setRest(output);
                    checks.setRest(true);
                }
        }
        /*
        Element outsXML=(Element)hcLoadChecksXML.getElementsByTagName(LicenseCheckS.OUTPUT).item(0);
        
        Resource output = new Resource();
            output.setFormat(outsXML.getAttributes().getNamedItem(LicenseCheckS.FORMAT).getNodeValue());
            output.setType(outsXML.getAttributes().getNamedItem(LicenseCheckS.TYPE).getNodeValue());
            output.setName(outsXML.getTextContent());
        */
            
        outsXML = (Element)hcLoadChecksXML.getElementsByTagName(LicenseCheckS.INPUT).item(0);
        Resource input = new Resource();
            input.setFormat(outsXML.getAttributes().getNamedItem(LicenseCheckS.FORMAT).getNodeValue());
            input.setType(outsXML.getAttributes().getNamedItem(LicenseCheckS.TYPE).getNodeValue());
            input.setName(outsXML.getTextContent());
         checks.setBase(input);
         
        
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

    public AppLCheck getReport() {
        return report;
    }

    public void setReport(AppLCheck report) {
        this.report = report;
    }


    
}
