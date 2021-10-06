package com.avijitmondal.together.grpc.server.service;

import com.avijitmondal.grpc.server.proto.GreeterGrpc;
import com.avijitmondal.grpc.server.proto.HelloReply;
import com.avijitmondal.grpc.server.proto.HelloRequest;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class GreeterService extends GreeterGrpc.GreeterImplBase {
    private static final Logger logger = Logger.getLogger(GreeterService.class.getName());

    @Override
    public void sayHello(HelloRequest req, StreamObserver<HelloReply> responseObserver) {
        logger.info("hello " + req.getName());
        HelloReply reply = HelloReply.newBuilder().setMessage("Hello " + req.getName()).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}
