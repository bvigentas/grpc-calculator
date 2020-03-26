package com.github.bvigentas.calculator.client;

import com.proto.calculator.AverageRequest;
import com.proto.calculator.AverageResponse;
import com.proto.calculator.CalculateServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class AverageClient {

    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50052)
                .usePlaintext()// Bypass on SSL
                .build();

        System.out.println("Creating Stub");
        CalculateServiceGrpc.CalculateServiceStub calcularClient = CalculateServiceGrpc.newStub(channel);

        CountDownLatch latch = new CountDownLatch(1);

        StreamObserver<AverageRequest> requestObserver = calcularClient.average(new StreamObserver<AverageResponse>() {
            @Override
            public void onNext(AverageResponse value) {
                System.out.println("Received a response from the server");
                System.out.println("The average is:");
                System.out.println(value.getResult());
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {
                System.out.println("Server has completed sending us something");
                latch.countDown();
            }
        });

        System.out.println("Sending n1");
        requestObserver.onNext(AverageRequest.newBuilder().setNumber(1).build());

        System.out.println("Sending n2");
        requestObserver.onNext(AverageRequest.newBuilder().setNumber(2).build());

        System.out.println("Sending n3");
        requestObserver.onNext(AverageRequest.newBuilder().setNumber(3).build());

        System.out.println("Sending n4");
        requestObserver.onNext(AverageRequest.newBuilder().setNumber(4).build());

        requestObserver.onCompleted();

        try {
            latch.await(3L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
