package com.github.bvigentas.calculator.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.protobuf.services.ProtoReflectionService;

import java.io.IOException;

public class CalculatorServer {

    public static void main(String[] args) throws IOException, InterruptedException {

        Server server = ServerBuilder.forPort(50052)
                .addService(new CalculatorServiceImpl())
                .addService(ProtoReflectionService.newInstance())
                .build();

        server.start();
        System.out.println("Server started");

        Runtime.getRuntime().addShutdownHook(new Thread( () -> {
            server.shutdown();
        }));

        server.awaitTermination();

    }

}
