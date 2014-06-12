/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.appdynamics.utilreports.conf;

import org.appdynamics.utilreports.resources.AppDUtilReportS;

/**
 *
 * @author gilbert.solorzano
 */
public class ServerConfig {
    private Controller controller;
    private Account account;
    private MetricLog metricLog, serverLog;
    
    public ServerConfig(){}


    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public MetricLog getMetricLog() {
        return metricLog;
    }

    public void setMetricLog(MetricLog metricLog) {
        this.metricLog = metricLog;
    }

    public MetricLog getServerLog() {
        return serverLog;
    }

    public void setServerLog(MetricLog serverLog) {
        this.serverLog = serverLog;
    }
    
    @Override
    public String toString(){
        StringBuilder bud = new StringBuilder();
        bud.append(AppDUtilReportS.L1).append(AppDUtilReportS.SERVER_CONFIG);
        bud.append(controller.toString());//L2
        bud.append(account.toString());
        bud.append(serverLog.toString());

        return bud.toString();
    }
    
}
