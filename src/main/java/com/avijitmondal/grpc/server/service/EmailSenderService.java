package com.avijitmondal.grpc.server.service;

import com.avijitmondal.grpc.server.model.Attachment;
import com.avijitmondal.grpc.server.model.Mail;
import com.avijitmondal.grpc.server.proto.SubmitRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Base64;
import java.util.logging.Logger;

@Service
public class EmailSenderService implements Runnable {
    private static final Logger logger = Logger.getLogger(EmailSenderService.class.getName());

    @Autowired
    private JavaMailSender javaMailSender;

    @Value( "${email.service.enabled}" )
    private boolean emailEnabled;
    private SubmitRequest request;

    private Mail email;
    private Attachment[] emailAttachments;

    public void setMail(Mail email, Attachment[] email_attachments) {
        this.email = email;
        this.emailAttachments = email_attachments;
    }

    public void setSubmitRequest(SubmitRequest submitRequest) {
        this.request = submitRequest;
    }

    public void run2() {
        if(!emailEnabled) {
            logger.info("Email service is disabled");
            return;
        }

        try {
            logger.info("Creating MIME ");
            MimeMessage msg = javaMailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(msg, true);
            helper.setTo("avijitmondal0@yahoo.com");
            helper.setFrom("avijitmondal0@yahoo.com");

            helper.setSubject(email.getSubject());

            helper.setText(request.getBody(), true);

//            helper.addAttachment("my_photo.png", new ClassPathResource("android.png"));
//            for(int i=0;i<email.getNumAttachments();i++) {
//                File file = new File(this.emailAttachments[i].getName());
//
//                try ( FileOutputStream fos = new FileOutputStream(file)) {
//                    byte[] decoder = Base64.getDecoder().decode(this.emailAttachments[i].getContent());
//                    fos.write(decoder);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                MimeBodyPart attachmentPart = new MimeBodyPart();
//                DataSource source = new FileDataSource(file);
//                attachmentPart.setDataHandler(new DataHandler(source));
//                attachmentPart.setFileName(this.emailAttachments[i].getName());
//
//                multipart.addBodyPart(attachmentPart);
//            }

            logger.info("Sending ");
            javaMailSender.send(msg);
        } catch (Exception e) {
            logger.finest("Some error occurred while sending email");
            e.printStackTrace();
        }
        logger.info("Done");
    }

    @Override
    public void run() {
        if(!emailEnabled) {
            logger.info("Email service is disabled");
            return;
        }

        try {
            logger.info("Creating MIME ");
            MimeMessage msg = javaMailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(msg, true);
            helper.setTo(request.getReceiver());
            helper.setFrom(request.getSender());

            helper.setSubject(request.getSubject());

            helper.setText(request.getBody(), false);

            helper.addAttachment("my_photo.png", new ClassPathResource("android.png"));

            logger.info("Sending ");
            javaMailSender.send(msg);
        } catch (Exception e) {
            logger.finest("Some error occurred while sending email");
            e.printStackTrace();
        }
        logger.info("Done");
    }
}
