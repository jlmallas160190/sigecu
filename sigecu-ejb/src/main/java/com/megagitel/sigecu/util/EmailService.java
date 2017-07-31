/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.megagitel.sigecu.util;

import com.megagitel.sigecu.database.SetupService;
import com.megagitel.sigecu.dto.MailDto;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author jorgemalla
 */
public class EmailService implements Serializable {

    public static boolean enviar(final MailDto mailDto) {
        Properties properties = EmailService.getProporties();
        Properties props = new Properties();
        props.setProperty("mail.smtp.host", properties.getProperty("emailHost"));
        // TLS si está disponible  
        props.setProperty("mail.smtp.starttls.enable", "true");
        // Puerto de gmail para envio de correos  
        props.setProperty("mail.smtp.port", properties.getProperty("puertoEmail"));
        // Nombre del usuario  
        props.setProperty("mail.smtp.user", properties.getProperty("email"));
        // Si requiere o no usuario y password para conectarse.  
        props.setProperty("mail.smtp.auth", "true");
        // Se inicia una nueva sesion  
        Session session = Session.getDefaultInstance(props);
        MimeMessage mimeMessage = new MimeMessage(session);

        try {
            MimeMultipart correo = new MimeMultipart();
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(mailDto.getDestino()));
            mimeMessage.setSubject(properties.getProperty("subjectEmail"));
            mimeMessage.setContent(mailDto.getMensaje(), "text/html");
            agregarArchivos(!mailDto.getArchivos().isEmpty() ? mailDto.getArchivos() : new ArrayList<String>(), correo);
            Transport t = session.getTransport("smtp");
            t.connect(properties.getProperty("email"), properties.getProperty("passwordEmail"));
            t.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
            t.close();
        } catch (MessagingException ex) {
            try {
                throw ex;
            } catch (MessagingException ex1) {
                Logger.getLogger(EmailService.class.getName()).log(Level.SEVERE, null, ex1);
            }
            return false;
        }
        return true;
    }

    private static void agregarArchivos(List<String> archivos, MimeMultipart correo) {
        try {
            for (String archivo : archivos) {
                MimeBodyPart messageBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(archivo);
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(source.getName());
                correo.addBodyPart(messageBodyPart);
            }
        } catch (MessagingException e) {
        }

    }

    private static Properties getProporties() {
        Properties properties = new Properties();
        InputStream input = null;
        try {
            String filename = "config.properties";
            input = SetupService.class.getClassLoader().getResourceAsStream(filename);
            properties.load(input);
        } catch (IOException e) {
            try {
                throw e;
            } catch (IOException ex) {
                Logger.getLogger(EmailService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return properties;
    }
}
