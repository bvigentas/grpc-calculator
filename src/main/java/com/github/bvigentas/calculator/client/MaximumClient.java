package com.github.bvigentas.calculator.client;

import com.proto.calculator.CalculateServiceGrpc;
import com.proto.calculator.MaximumRequest;
import com.proto.calculator.MaximumResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class MaximumClient {

    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50052)
                .usePlaintext()// Bypass on SSL
                .build();

        System.out.println("Creating Stub");
        CalculateServiceGrpc.CalculateServiceStub calcularClient = CalculateServiceGrpc.newStub(channel);

        CountDownLatch latch = new CountDownLatch(1);

        StreamObserver<MaximumRequest> requestObserver = calcularClient.maximum(new StreamObserver<MaximumResponse>() {
            @Override
            public void onNext(MaximumResponse value) {
                System.out.println(value.getNumber() + " is now the maximum");
            }

            @Override
            public void onError(Throwable t) {
                latch.countDown();
            }

            @Override
            public void onCompleted() {
                System.out.println("Server is done sending data");
                latch.countDown();
            }
        });

        Arrays.asList(1,5,3,6,2,20).forEach(number -> {
            requestObserver.onNext(MaximumRequest.newBuilder()
                    .setNumber(number)
                    .build());
        });

        requestObserver.onCompleted();

        try {
            latch.await(3L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
