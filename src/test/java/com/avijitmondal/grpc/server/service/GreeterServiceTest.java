package com.avijitmondal.grpc.server.service;

import com.avijitmondal.grpc.server.proto.GreeterGrpc;
import com.avijitmondal.grpc.server.proto.HelloReply;
import com.avijitmondal.grpc.server.proto.HelloRequest;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GreeterServiceTest {
    private static final Logger logger = Logger.getLogger(GreeterServiceTest.class.getName());
    private ManagedChannel channel;
    private GreeterGrpc.GreeterBlockingStub blockingStub;

    @BeforeAll
    public void setup() {

        this.channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
                // needing certificates.
                .usePlaintext()
                .build();
        blockingStub = GreeterGrpc.newBlockingStub(channel);
    }

    @Test
    void testGreet() {
        HelloRequest request = HelloRequest.newBuilder().setName("Avijit").build();
        HelloReply response;
        try {
            response = blockingStub.sayHello(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            return;
        }

        logger.info("Greeting: " + response.getMessage());
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getMessage());
    }

    @AfterAll
    public void teardown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }
}
