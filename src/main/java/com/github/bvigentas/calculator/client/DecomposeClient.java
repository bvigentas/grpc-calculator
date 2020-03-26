package com.github.bvigentas.calculator.client;

import com.proto.calculator.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class DecomposeClient {

    public static void main(String[] args) {

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50052)
                .usePlaintext()// Bypass on SSL
                .build();

        System.out.println("Creating Stub");
        CalculateServiceGrpc.CalculateServiceBlockingStub calculateCliente = CalculateServiceGrpc.newBlockingStub(channel);


        DecomposeRequest request = DecomposeRequest.newBuilder().setNumber(120).build();

        calculateCliente.decomposePrimeNumber(request).forEachRemaining(decomposeResponse -> {
            System.out.println(decomposeResponse.getResult());
        });

        channel.shutdown();

    }
}
