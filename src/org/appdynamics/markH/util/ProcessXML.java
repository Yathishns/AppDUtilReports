/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.appdynamics.markH.util;

import org.appdynamics.markH.data.*;


import org.appdynamics.appdrestapi.RESTAccess;
import org.appdynamics.appdrestapi.data.*;
import org.appdynamics.appdrestapi.util.MyCalendar;
import org.appdynamics.utilreports.conf.*;
import org.appdynamics.markH.data.ApplicationCheck;


import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

/**
 *
 * @author gilbert.solorzano
 */
public class ProcessXML {
    private static Logger logger=Logger.getLogger(ProcessXML.class.getName());
    private MarkHList appDHC;
    private RESTAccess access;
    
    public ProcessXML(MarkHList appDHC){this.appDHC=appDHC;}
    
    
    public void init() throws Exception{

        
        // If the method of decryption needs to change just update the DeCrypt class
        String pwd=DeCrypt.decrypt(appDHC.getServerConfig().getAccount().getPasswd());
        ServerConfig srv=appDHC.getServerConfig();
        access = new RESTAccess(srv.getController().getFqdn(),srv.getController().getPort(),
                srv.getController().isUseSSL(),srv.getAccount().getUser(),pwd,srv.getAccount().getAccount());
        logger.log(Level.INFO,"Parsing XML objects");
        
        // Now what we have to do for each application pull the nodes and then the tiers
        for(ApplicationCheck app:appDHC.getApps()){
            processApplication(app);
        }
        
    }
    
    public void processApplication(ApplicationCheck app){
        // 
        Nodes nodes = access.getNodesForApplication(app.getName());
        Tiers tiers = access.getTiersForApplication(app.getName());
        Set<Integer> markedNodes = new HashSet<Integer>();
        Set<Node> nodesToCheck = new HashSet<Node>();
        
        if(nodes != null && tiers != null){
            ArrayList<String> tierList = new ArrayList<String>();
           
            for(TierCheck tierChk:app.getTierChecks()){
                for(Tier tier: tiers.getTiers()){
                        if(tierChk.check(tier.getName())) tierList.add(Integer.toString(tier.getId()));
                }
            }
            
            for(Node node:nodes.getNodes()){
                if(tierList.contains(Integer.toString(node.getTierId()))){
                    for(TierCheck tierChk:app.getTierChecks()){
                        for(NodeCheck nodeChk:tierChk.getNodeChecks()){
                            if(nodeChk.check(node.getName())) 
                                nodesToCheck.add(node); //markedNodes.add(node.getId());
                        }
                    }
                }
            }
            //nodesToCheck=sortNodes(nodesToCheck);
            markedNodes=checkUpTime(nodesToCheck,app);
            processNodes(markedNodes);
        }else{
            logger.log(Level.SEVERE,"Error occurred retrieving the nodes and tiers from the controller, exiting");
        }
    }
    
    /*
    private Set<Node>  sortNodes(Set<Node> nodes){
        ArrayList<Node> _nodes = new ArrayList<Node>(nodes);
        Collections.sort(_nodes, new Comparator<Node>(){
           public int compare(Node s1, Node s2){
             return s1.getTierName().compareTo(s2.getTierName());
           }
        });
        
        Set map = new HashSet();
        map.addAll(_nodes);
        return map;
    }
    */
    
    private void processNodes(Set<Integer> nodes){
        int start1=0;
         int maxSend=15;
         StringBuilder sendBuff=new StringBuilder();
         for(Integer val:nodes){
             if(start1 > 0){
                 sendBuff.append(",").append(val);
             }else{
                 sendBuff.append(val);
             }
             start1++;
             if(start1 > maxSend){
                 logger.log(Level.INFO,"Marking the following node ids: " + sendBuff.toString());
                 access.postRESTMarkNodeHistorical(sendBuff.toString());
                 sendBuff=new StringBuilder();
                 start1=0;
             }
         }
    }
    
    private Set<Integer> checkUpTime(Set<Node> nodes, ApplicationCheck app){
        Set<Integer> markedNodes = new HashSet<Integer>();
        Calendar cal = MyCalendar.getCalendar();//Calendar.getInstance();
        cal.set(Calendar.MINUTE, 0);
        long end = cal.getTimeInMillis();
        cal.add(Calendar.HOUR, (-1*app.getHoursToCheck()));
        long start = cal.getTimeInMillis();
        
        for(Node node:nodes){
            MetricDatas metrics = access.getRESTMetricQuery(0, app.getName(), node.getTierName(),node.getName(), start, end);
            if(metrics != null){
                        if(metrics.hasNoValues() != true && metrics.getSingleRollUpMetricValue().getCount() < app.getMinimumCount()){
                                       logger.log(Level.INFO,new StringBuilder().append("Setting historical because of lack of count for node ").append(node.getName())
                                               .append(" , with id ").append(node.getId()).append(" located in tier ").append(node.getTierName()).append(".").toString());
                                       markedNodes.add(node.getId());
                                       
                        }else{
                            if(metrics.hasNoValues()){ 
     
                                logger.log(Level.INFO,new StringBuilder().append("Setting historical because of no metrics returned during time range ")
                                        .append(node.getName()).append(" , with id ").append(node.getId()).append(" located in tier ").append(node.getTierName()).append(".").toString());
                                markedNodes.add(node.getId());
                            }
                        }
            }else{
                logger.log(Level.INFO,new StringBuilder().append("The REST query did not provide any metrics for ").append(node.getName())
                        .append(" , with id ").append(node.getId()).append(" located in tier ").append(node.getTierName()).append(".").toString());
            }
        }
        
        return markedNodes;
    }
    
}
