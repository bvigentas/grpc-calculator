package com.github.bvigentas.calculator.server;

import com.proto.calculator.*;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

public class CalculatorServiceImpl extends CalculateServiceGrpc.CalculateServiceImplBase {

    @Override
    public void execute(CalculateRequest request, StreamObserver<CalculateResponse> responseObserver) {

        Calculate calculate = request.getCalculate();
        int firstNumber = calculate.getFirstNumber();
        int secondNumber = calculate.getSecondNumber();
        Calculate.Operation operation = calculate.getOperation();

        float result = 0;

        switch (operation) {
            case DIV:
                result = firstNumber / secondNumber;
                break;
            case MUL:
                result = firstNumber * secondNumber;
                break;
            case SUB:
                result = firstNumber - secondNumber;
                break;
            case SUM:
                result = firstNumber + secondNumber;
                break;
        }

        CalculateResponse response = CalculateResponse.newBuilder()
                .setResult(String.valueOf(result))
                .build();

        responseObserver.onNext(response);

        responseObserver.onCompleted();

    }

    @Override
    public void decomposePrimeNumber(DecomposeRequest request, StreamObserver<DecomposeResponse> responseObserver) {
        int number = request.getNumber();

        int k = 2;

        while (number > 1) {
            if (number % k == 0) {
                DecomposeResponse response = DecomposeResponse.newBuilder().setResult(k).build();
                responseObserver.onNext(response);

                number = number / k;
            } else {
                k++;
            }
        }

        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<AverageRequest> average(StreamObserver<AverageResponse> responseObserver) {

        StreamObserver<AverageRequest> requestObserver = new StreamObserver<AverageRequest>() {

            double sum = 0;
            int count = 0;

            @Override
            public void onNext(AverageRequest value) {
                count++;
                sum = sum + value.getNumber();
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {
                responseObserver.onNext(AverageResponse.newBuilder()
                        .setResult(sum/count)
                        .build());

                responseObserver.onCompleted();
            }
        };

        return requestObserver;
    }

    @Override
    public StreamObserver<MaximumRequest> maximum(StreamObserver<MaximumResponse> responseObserver) {
        StreamObserver<MaximumRequest> requestObserver = new StreamObserver<MaximumRequest>() {

            int max = Integer.MIN_VALUE;

            @Override
            public void onNext(MaximumRequest value) {
                if (value.getNumber() > max) {
                    max = value.getNumber();
                    MaximumResponse response = MaximumResponse.newBuilder().setNumber(max).build();
                    responseObserver.onNext(response);
                }
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };

        return requestObserver;
    }

    @Override
    public void squareRoot(SquareRootRequest request, StreamObserver<SquareRootResponse> responseObserver) {
        int number = request.getNumber();

        if (number > 0) {
            double result = Math.sqrt(number);
            responseObserver.onNext(SquareRootResponse.newBuilder()
                    .setResult(result)
                    .build());

            responseObserver.onCompleted();
        } else {
            responseObserver.onError(
                    Status.INVALID_ARGUMENT
                            .withDescription("The number sent is negative")
                            .asRuntimeException()
            );
        }
    }
}
