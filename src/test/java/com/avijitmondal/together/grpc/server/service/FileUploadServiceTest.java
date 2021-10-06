package com.avijitmondal.together.grpc.server.service;

import com.avijitmondal.grpc.server.proto.*;
import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FileUploadServiceTest {
    private final static Log logger = LogFactory.getLog(FileUploadServiceTest.class);
    private ManagedChannel channel;
    private FileServiceGrpc.FileServiceStub fileServiceStub;

    @BeforeAll
    public void setup() {
        this.channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();
        this.fileServiceStub = FileServiceGrpc.newStub(channel);
    }

    @Test
    void unaryServiceTest() throws IOException, InterruptedException {
        StreamObserver<FileUploadRequest> streamObserver = this.fileServiceStub.upload(new FileUploadObserver());

        // input file for testing
        Path path = Paths.get("src/test/resources/input/java_output.pdf");

        // build metadata
        FileUploadRequest metadata = FileUploadRequest.newBuilder()
                .setMetadata(MetaData.newBuilder()
                        .setName("java_output")
                        .setType("pdf").build())
                .build();
        long start = System.currentTimeMillis();
        System.out.println(start);
        streamObserver.onNext(metadata);
        // upload bytes
        InputStream inputStream = Files.newInputStream(path);
        byte[] bytes = new byte[4096];
        int size;
        while ((size = inputStream.read(bytes)) > 0) {
            FileUploadRequest uploadRequest = FileUploadRequest.newBuilder()
                    .setFile(File.newBuilder().setContent(ByteString.copyFrom(bytes, 0, size)).build())
                    .build();
            streamObserver.onNext(uploadRequest);
        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);
        Thread.sleep(5000);

        // close the stream
        inputStream.close();
        streamObserver.onCompleted();
    }

    @AfterAll
    public void teardown() {
        this.channel.shutdown();
    }

    private static class FileUploadObserver implements StreamObserver<FileUploadResponse> {
        @Override
        public void onNext(FileUploadResponse fileUploadResponse) {
            logger.info("File upload status :: " + fileUploadResponse.getStatus());
        }

        @Override
        public void onError(Throwable throwable) {
            throwable.printStackTrace();
            logger.error(throwable.getMessage());
        }

        @Override
        public void onCompleted() {
            logger.debug("OnCompleted");
        }
    }
}
