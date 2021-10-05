package com.avijitmondal.together.grpc.server.service;

import com.avijitmondal.together.grpc.GreeterGrpc;
import com.avijitmondal.together.grpc.HelloReply;
import com.avijitmondal.together.grpc.HelloRequest;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.logging.Logger;

@Service
public class GreeterImpl extends GreeterGrpc.GreeterImplBase {
    private static final Logger logger = Logger.getLogger(GreeterImpl.class.getName());

    @Autowired
    private EmailSenderService emailSenderService;

    @Override
    public void sayHello(HelloRequest req, StreamObserver<HelloReply> responseObserver) {
        logger.info("hello " + req.getName());
        HelloReply reply = HelloReply.newBuilder().setMessage("Hello " + req.getName()).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();

//        logger.info("Sending Email...");
//
//        try {
//            emailSenderService.sendEmailWithAttachment(req.getName());
//
//        } catch (MessagingException e) {
//            logger.finest("Some error occurred while sending email");
//            e.printStackTrace();
//        }
//
//        logger.info("Done");
    }
}
