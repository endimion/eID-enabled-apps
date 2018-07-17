/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package teem.loginapp.service;

import teem.loginapp.pojo.SwellrtEvent;
import java.util.List;


/**
 *
 * @author nikos
 */
public interface MailService {
    
     
    public String prepareAndSend(String recipient, String message, String userName,String displayName,String password);
    
    public String sendEventMail(String recipient, SwellrtEvent evt);
    
    public String sendEmailsForEvent(SwellrtEvent evt, List<String> participants);
}
