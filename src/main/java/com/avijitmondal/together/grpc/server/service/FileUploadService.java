package com.avijitmondal.together.grpc.server.service;

import com.avijitmondal.grpc.server.proto.FileServiceGrpc;
import com.avijitmondal.grpc.server.proto.FileUploadRequest;
import com.avijitmondal.grpc.server.proto.FileUploadResponse;
import com.avijitmondal.grpc.server.proto.Status;
import com.google.protobuf.ByteString;
import io.grpc.stub.StreamObserver;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Service
public class FileUploadService extends FileServiceGrpc.FileServiceImplBase {
    private final Log logger = LogFactory.getLog(this.getClass());
    private static final Path SERVER_BASE_PATH = Paths.get("/tmp");

    @Override
    public StreamObserver<FileUploadRequest> upload(StreamObserver<FileUploadResponse> responseObserver) {
        logger.debug("upload");

        return new StreamObserver<FileUploadRequest>() {
            // upload context variables
            OutputStream writer;
            Status status = Status.IN_PROGRESS;

            @Override
            public void onNext(FileUploadRequest fileUploadRequest) {
                logger.debug("onNext");
                try {
                    if (fileUploadRequest.hasMetadata()) {
                        writer = getFilePath(fileUploadRequest);
                        System.out.println(writer);
                    } else {
                        writeFile(writer, fileUploadRequest.getFile().getContent());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    this.onError(e);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                logger.debug("onError");
                status = Status.FAILED;
                this.onCompleted();
            }

            @Override
            public void onCompleted() {
                logger.debug("onCompleted");
                closeFile(writer);
                status = Status.IN_PROGRESS.equals(status) ? Status.SUCCESS : status;
                FileUploadResponse response = FileUploadResponse.newBuilder()
                        .setStatus(status)
                        .build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
            }
        };
    }

    private OutputStream getFilePath(FileUploadRequest request) throws IOException {
        logger.debug("getFilePath");
        String fileName = request.getMetadata().getName() + "." + request.getMetadata().getType();
        return Files.newOutputStream(SERVER_BASE_PATH.resolve(fileName), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    private void writeFile(OutputStream writer, ByteString content) throws IOException {
        logger.debug("writeFile");
        writer.write(content.toByteArray());
        writer.flush();
    }

    private void closeFile(OutputStream writer) {
        logger.debug("closeFile");
        try {
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
