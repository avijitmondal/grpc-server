package com.avijitmondal.together.grpc.server.service;

import com.avijitmondal.together.grpc.server.proto.EmailSubmitGrpc;
import com.avijitmondal.together.grpc.server.proto.SubmitRequest;
import com.avijitmondal.together.grpc.server.proto.SubmitResponse;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class EmailSubmitService extends EmailSubmitGrpc.EmailSubmitImplBase {
    private static final Logger logger = Logger.getLogger(EmailSubmitService.class.getName());

    @Autowired
    private EmailSenderService emailSenderService;

    @Override
    public void submitEmail(SubmitRequest request, StreamObserver<SubmitResponse> responseObserver) {
        logger.info("hello " + request.getSender());
        SubmitResponse.Builder builder = SubmitResponse.newBuilder();

        builder.setSuccess(true);
        builder.setMessage("Successfully submitted mail");
        SubmitResponse response = builder.build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();

        logger.info("Sending Email...");

        emailSenderService.setSubmitRequest(request);
//        Thread thread = new Thread(emailSenderService);
//        thread.start();
//        thread.join();
        emailSenderService.run();
    }
}