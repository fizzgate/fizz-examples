package com.fizzgate.fizz.examples.grpc.user;

import io.grpc.stub.StreamObserver;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Our implementation of User service.
 *
 * <p>See user.proto for details of the methods.
 */
public class UserService extends UserServiceGrpc.UserServiceImplBase {
    @Override
    public void findById(FindById request, StreamObserver<User> responseObserver) {
        responseObserver.onNext(this.buildUser(request.getId(), "call findById"));
        responseObserver.onCompleted();
    }

    @Override
    public void findAll(FindAll request, StreamObserver<User> responseObserver) {
        responseObserver.onNext(this.buildUser(String.valueOf(ThreadLocalRandom.current().nextInt()), "call findAll"));
        responseObserver.onCompleted();
    }

    @Override
    public void insert(User request, StreamObserver<User> responseObserver) {
        responseObserver.onNext(this.buildUser(request.getId(), String.format("call insert and vo name is %s", request.getName())));
        responseObserver.onCompleted();
    }

    private User buildUser(String id, String name) {
        return User.newBuilder().setId(id).setName(name).build();
    }
}
