package com.fizzgate.fizz.examples.grpc.shopping.cart;

import io.grpc.stub.StreamObserver;

import java.util.stream.Collectors;

/**
 * Our implementation of ShoppingCart service.
 *
 * <p>See shopping_cart.proto for details of the methods.
 */
public class ShoppingCartService extends ShoppingCartServiceGrpc.ShoppingCartServiceImplBase {
    @Override
    public void findByIdsAndName(FindByIdsAndName request, StreamObserver<User> responseObserver) {
        responseObserver.onNext(this.buildUser(request.getIdsList().toString(), String.format("call findByIdsAndName ：%s", request.getName())));
        responseObserver.onCompleted();
    }

    @Override
    public void findByArrayIdsAndName(FindByArrayIdsAndName request, StreamObserver<User> responseObserver) {
        responseObserver.onNext(this.buildUser(request.getIdsList().toString(), String.format("call findByArrayIdsAndName ：%s", request.getName())));
        responseObserver.onCompleted();
    }

    @Override
    public void findByStringArray(FindByStringArray request, StreamObserver<User> responseObserver) {
        responseObserver.onNext(this.buildUser(request.getIdsList().toString(), "call findByStringArray"));
        responseObserver.onCompleted();
    }

    @Override
    public void findByListId(FindByListId request, StreamObserver<User> responseObserver) {
        responseObserver.onNext(this.buildUser(request.getIdsList().toString(), "call findByListId"));
        responseObserver.onCompleted();
    }

    @Override
    public void batchSave(BatchSave request, StreamObserver<User> responseObserver) {
        responseObserver.onNext(this.buildUser(request.getUserListList().stream().map(User::getId).collect(
                Collectors.joining("-")), String.format("call batchSave :%s", request.getUserListList().stream()
                .map(User::getName).collect(Collectors.joining("-")))));
        responseObserver.onCompleted();
    }

    @Override
    public void batchSaveAndNameAndId(BatchSaveAndNameAndId request, StreamObserver<User> responseObserver) {
        responseObserver.onNext(this.buildUser(request.getId(), String.format("call batchSaveAndNameAndId :%s:%s", request.getName(),
            request.getUserListList().stream().map(User::getName).collect(Collectors.joining("-")))));
        responseObserver.onCompleted();
    }

    @Override
    public void saveShoppingCart(ShoppingCart request, StreamObserver<User> responseObserver) {
        responseObserver.onNext(this.buildUser(request.getGoodsIdListsList().toString(),
                String.format("call saveComplexBeanTest :%s", request.getUser().getName())));
        responseObserver.onCompleted();
    }

    @Override
    public void saveShoppingCartWithName(SaveShoppingCartWithName request, StreamObserver<User> responseObserver) {
        responseObserver.onNext(this.buildUser(request.getShoppingCart().getGoodsIdListsList().toString(),
                String.format("call saveComplexBeanTestAndName :%s-%s", request.getShoppingCart().getUser().getName(),
                        request.getName())));
        responseObserver.onCompleted();
    }

    private User buildUser(String id, String name) {
        return User.newBuilder().setId(id).setName(name).build();
    }
}
