package com.avijitmondal.grpc.server.service;

import com.avijitmondal.grpc.server.proto.SubmitRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.util.logging.Logger;

@Service
public class EmailSenderService implements Runnable {
    private static final Logger logger = Logger.getLogger(EmailSenderService.class.getName());

    @Autowired
    private JavaMailSender javaMailSender;

    @Value( "${email.service.enabled}" )
    private boolean emailEnabled;
    private SubmitRequest request;

    public void setSubmitRequest(SubmitRequest submitRequest) {
        this.request = submitRequest;
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
