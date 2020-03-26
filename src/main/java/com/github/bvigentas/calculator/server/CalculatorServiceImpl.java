package com.github.bvigentas.calculator.server;

import com.proto.calculator.Calculate;
import com.proto.calculator.CalculateRequest;
import com.proto.calculator.CalculateResponse;
import com.proto.calculator.CalculateServiceGrpc;
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
}