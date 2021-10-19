package com.avijitmondal.grpc.server.controller;

import com.avijitmondal.grpc.server.model.Attachment;
import com.avijitmondal.grpc.server.model.Mail;
import com.avijitmondal.grpc.server.service.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Base64;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

@RestController
@CrossOrigin
public class MailSubmissionController {

	@Autowired
	private EmailSenderService emailSenderService;

	@PostMapping("/submit")
	public String submit(@RequestBody String mail) {
		System.out.println("submit");
		System.out.println(mail);
		ObjectMapper objectMapper = new ObjectMapper();
		Mail email = null;
		Attachment[] email_attachments = null;
		try {
			JsonNode node = objectMapper.readValue(mail, JsonNode.class);
			JsonNode idNode = node.get("id");
			JsonNode subjectNode = node.get("subject");
			JsonNode bodyNode = node.get("body");
			JsonNode numAttachmentsNode = node.get("num_attachments");
			String id = idNode.asText();
			String subject = subjectNode.asText();
			String body = bodyNode.asText();
			int num_attachments = numAttachmentsNode.asInt();
			email = new Mail(id, subject, body, num_attachments);
//			System.out.println("Email metadata: " + email);
			String attachmentId, name, attachmentType, contentType, content, attachmentFormat;
			int size;
			email_attachments = new Attachment[num_attachments];
			for (int i=0;i<num_attachments;i++) {
//				int num = node.get("attachment").size();
//				System.out.println(num);
				JsonNode attachmentNode = node.get("attachment").get(i);
				JsonNode attachmentIdNode = attachmentNode.get("id");
		        JsonNode nameNode = attachmentNode.get("name");
		        JsonNode attachmentTypeNode = attachmentNode.get("attachment_type");
		        JsonNode contentTypeNode = attachmentNode.get("content_type");
		        JsonNode contentNode = attachmentNode.get("content");
		        JsonNode attachmentFormatNode = attachmentNode.get("attachment_format");
		        JsonNode sizeNode = attachmentNode.get("size");
		        attachmentId = attachmentIdNode.asText();
		        name = nameNode.asText();
		        attachmentType = attachmentTypeNode.asText();
		        contentType = contentTypeNode.asText();
		        content = contentNode.asText();
		        attachmentFormat = attachmentFormatNode.asText();
		        size = sizeNode.asInt();
		        email_attachments[i] = new Attachment(attachmentId, name, attachmentType, contentType, content, attachmentFormat, size);
		        System.out.println(email_attachments[i].toString() + "\n");
			}
			
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		System.out.println("Received a Mail\n");
//		sendEmail(email, email_attachments);
		emailSenderService.setMail(email, email_attachments);
		emailSenderService.run2();
		return "Received mail";
//		HttpHeaders responseHeaders = new HttpHeaders();
//	    responseHeaders.set("Access-Control-Allow-Origin", "*");
//	    return new ResponseEntity<String>("Received mail", responseHeaders, HttpStatus.ACCEPTED);
	}
	
	void sendEmail(Mail email, Attachment[] email_attachments) {
		
		  final String to = "avijitmondal0@yahoo.com";
	      final String from = "avijitmondal0@yahoo.com";

	      String host = "smtp.mail.yahoo.com";
	      Properties properties = System.getProperties();

	      properties.put("mail.smtp.host", host);
	      properties.put("mail.smtp.port", "587");
	      properties.put("mail.smtp.starttls.enable", "true");
	      properties.put("mail.smtp.auth", "true");

	      Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
	          protected PasswordAuthentication getPasswordAuthentication() {
	              return new PasswordAuthentication("avijitmondal0", "bczgtahxlwgnouyl");
	          }
	      });

	      session.setDebug(true);
	      try {
	          MimeMessage message = new MimeMessage(session);

	          message.setFrom(new InternetAddress(from));
	          message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
	          message.setSubject(email.getSubject());
	          
	          // Body of the email
	          BodyPart messageBodyPart = new MimeBodyPart(); 
	          messageBodyPart.setContent(email.getBody(), "text/html");
//	          message.setContent(mail.body, "text/html");
	          
	          Multipart multipart = new MimeMultipart();
	          multipart.addBodyPart(messageBodyPart);

	          // Attachment of the email
	          for(int i=0;i<email.getNumAttachments();i++) {
	        	  File file = new File(email_attachments[i].getName());

		          try ( FileOutputStream fos = new FileOutputStream(file)) {
		            byte[] decoder = Base64.getDecoder().decode(email_attachments[i].getContent());
		            fos.write(decoder);
		          } catch (Exception e) {
		            e.printStackTrace();
		          }
		          MimeBodyPart attachmentPart = new MimeBodyPart();
		          DataSource source = new FileDataSource(file);
		          attachmentPart.setDataHandler(new DataHandler(source));
		          attachmentPart.setFileName(email_attachments[i].getName());

		          multipart.addBodyPart(attachmentPart);
	          }
	          
	          
	          message.setContent(multipart);
	      
	          Transport.send(message);
//	          Attachment attachments[] = mail.attachments;
//	          System.out.println("ATTACHMENTS LENGTH: " + attachments.length);
//	          for (Attachment attachment: attachments) {
//	        	  System.out.println(attachment.name);
//	        	  System.out.println(attachment.attachment);
//	          }
	          System.out.println("Sent message successfully....");
	      } catch (MessagingException mex) {
	          mex.printStackTrace();
	      }
    }
	
}

