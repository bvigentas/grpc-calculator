package com.github.bvigentas.calculator.client;

import com.proto.calculator.Calculate;
import com.proto.calculator.CalculateRequest;
import com.proto.calculator.CalculateResponse;
import com.proto.calculator.CalculateServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class CalculatorClient {

    public static void main(String[] args) {

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50052)
                .usePlaintext()// Bypass on SSL
                .build();

        System.out.println("Creating Stub");
        CalculateServiceGrpc.CalculateServiceBlockingStub calculateCliente = CalculateServiceGrpc.newBlockingStub(channel);

        Calculate calculate = Calculate.newBuilder()
                .setFirstNumber(1)
                .setSecondNumber(2)
                .setOperation(Calculate.Operation.MUL)
                .build();

        CalculateRequest request = CalculateRequest.newBuilder()
                .setCalculate(calculate)
                .build();

        CalculateResponse response = calculateCliente.execute(request);

        System.out.println(response.getResult());

        channel.shutdown();

    }

}
