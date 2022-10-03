package ru.xpressed.grpcserverjava;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.xpressed.grpcserverjava.repository.UserRepository;
import ru.xpressed.grpcserverjava.service.UserServiceImpl;

@SpringBootApplication
public class GrpcServerJavaApplication implements CommandLineRunner {
    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private UserRepository userRepository;

    public static void main(String[] args) {
        SpringApplication.run(GrpcServerJavaApplication.class, args);
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Server server = ServerBuilder
                .forPort(8080)
                .addService(new UserServiceImpl(userRepository))
                .build();

        server.start();
        logger.info("Server started!");
        server.awaitTermination();
    }
}
