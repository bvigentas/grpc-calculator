package com.github.bvigentas.calculator.client;

import com.proto.calculator.CalculateServiceGrpc;
import com.proto.calculator.SquareRootRequest;
import com.proto.calculator.SquareRootResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

public class SquareRootClient {

    public static void main(String[] args) {

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50052)
                .usePlaintext()// Bypass on SSL
                .build();

        System.out.println("Creating Stub");
        CalculateServiceGrpc.CalculateServiceBlockingStub calculateCliente = CalculateServiceGrpc.newBlockingStub(channel);

        SquareRootRequest request = SquareRootRequest.newBuilder().setNumber(-1).build();

        try {
            SquareRootResponse response = calculateCliente.squareRoot(request);
        } catch (StatusRuntimeException e) {
            System.out.println("Erro");
            e.printStackTrace();
        } finally {
            channel.shutdown();
        }


    }
}
