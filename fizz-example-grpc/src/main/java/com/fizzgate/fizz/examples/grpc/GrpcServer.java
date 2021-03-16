package com.fizzgate.fizz.examples.grpc;

import com.fizzgate.fizz.examples.grpc.shopping.cart.ShoppingCartService;
import com.fizzgate.fizz.examples.grpc.user.UserService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.protobuf.services.ProtoReflectionService;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * gRPC server that serve the User service & ShoppingCart service
 */
public class GrpcServer {
    private static final Logger logger = Logger.getLogger(GrpcServer.class.getName());

    private final int port;
    private final Server server;

    private GrpcServer(int port) {
        this.port = port;
        server = ServerBuilder.forPort(port)
                .addService(new UserService())
                .addService(new ShoppingCartService())
                // gRPC reflection enabled
                .addService(ProtoReflectionService.newInstance())
                .build();
    }

    /** Start serving requests. */
    private void start() throws IOException {
        server.start();
        logger.info("Server started, listening on " + port);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // Use stderr here since the logger may have been reset by its JVM shutdown hook.
            System.err.println("*** shutting down gRPC server since JVM is shutting down");
            try {
                GrpcServer.this.stop();
            } catch (InterruptedException e) {
                e.printStackTrace(System.err);
            }
            System.err.println("*** server shut down");
        }));
    }

    /** Stop serving requests and shutdown resources. */
    private void stop() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        }
    }

    /**
     * Await termination on the main thread since the grpc library uses daemon threads.
     */
    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    /**
     * Main method.  This comment makes the linter happy.
     */
    public static void main(String[] args) throws Exception {
        GrpcServer server = new GrpcServer(8980);
        server.start();
        server.blockUntilShutdown();
    }
}
