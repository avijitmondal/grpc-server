package com.avijitmondal.grpc.server;

import com.avijitmondal.grpc.server.service.GrpcServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GrpcServerApplication implements CommandLineRunner {

	@Autowired
	private GrpcServer grpcServer;

	public static void main(String[] args) {
		SpringApplication.run(GrpcServerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		grpcServer.start();
	}
}
