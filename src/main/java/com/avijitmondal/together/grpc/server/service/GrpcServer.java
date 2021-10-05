package com.avijitmondal.together.grpc.server.service;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class GrpcServer {
    private static final Logger logger = Logger.getLogger(GrpcServer.class.getName());

    @Autowired
    private GreeterImpl greeter;

    @Autowired
    private EmailSubmitService submitService;

    @Autowired
    private SubmitEmailService submitEmailService;

//    @Value("#{new Integer('${grpc.server.port}')}")
    private Integer grpcServerPort=50051;
    private Server server;

    public void start() throws IOException {
        server = ServerBuilder.forPort(grpcServerPort)
                .addService(greeter)
                .addService(submitService)
                .addService(submitEmailService)
                .build()
                .start();
        logger.log(Level.INFO, "Server started, listening on {0}", grpcServerPort);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // Use stderr here since the logger may have been reset by its JVM shutdown hook.
            logger.finest("*** shutting down gRPC server since JVM is shutting down");
            try {
                GrpcServer.this.stop();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            logger.finest("*** server shut down");
        }));
    }

    private void stop() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        }
    }
}
