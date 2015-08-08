/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.appdynamics.licenseCheck.action;

import org.appdynamics.licenseCheck.util.MailInfo;
import org.appdynamics.crypto.*;
import org.appdynamics.utilreports.conf.*;

import java.util.logging.*;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
/**
 *
 * @author gilbert.solorzano
 */
public class SendMail {
    private static Logger logger=Logger.getLogger(SendMail.class.getName());  
    private MailInfo mailInfo;
    private String to,msg;
    
    public SendMail(MailInfo mailInfo, String to, String msg){
        this.mailInfo=mailInfo;
        this.msg=msg;
        this.to=to;
        
        init();
    }
    
    private void init(){
        Properties prop = new Properties();
        prop.put("mail.smtp.auth", mailInfo.isMailAuth());
        prop.put("mail.smtp.host",mailInfo.getMailHost());
        prop.put("mail.smtp.port",mailInfo.getMailPort());
        
        Session session;
        logger.log(Level.INFO,"Setting up the session");
        session = Session.getDefaultInstance(prop);
        if(mailInfo.isMailAuth()){
                final String user = mailInfo.getMailUser();
                final String pass = DeCrypt.decrypt(mailInfo.getMailPasswd());
                
                session = Session.getInstance(prop, new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication(){
                            return new PasswordAuthentication(user,pass);
                        }
                });
        }
        
        try{
            logger.log(Level.INFO,"Setting up the message");
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mailInfo.getMailUser()));
            message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(to));
            message.setSubject("License Check Findings");
            message.setText(msg);
            
            Transport.send(message);
        }catch(MessagingException e){
            logger.log(Level.SEVERE,e.getMessage());
            throw new RuntimeException(e);
        }
    }
    
}
