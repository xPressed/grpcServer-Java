package ru.xpressed.grpcserverjava.service;

import grpc.xpressed.UserServiceGrpc;
import grpc.xpressed.UserServiceOuterClass;
import io.grpc.stub.StreamObserver;
import ru.xpressed.grpcserverjava.entity.User;
import ru.xpressed.grpcserverjava.repository.UserRepository;

public class UserServiceImpl extends UserServiceGrpc.UserServiceImplBase {
    private final UserRepository userRepository;
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void getUser(UserServiceOuterClass.UserGetRequest request, StreamObserver<UserServiceOuterClass.UserGetResponse> responseObserver) {
        UserServiceOuterClass.UserGetResponse response = null;
        User user = userRepository.findByUsername(request.getUsername());

        if (user != null) {
            response = UserServiceOuterClass.UserGetResponse.newBuilder()
                    .setUsername(user.getUsername())
                    .setPassword(user.getPassword())
                    .build();
        }

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void addUser(UserServiceOuterClass.UserAddRequest request, StreamObserver<UserServiceOuterClass.UserAddResponse> responseObserver) {
        UserServiceOuterClass.UserAddResponse response;
        if (userRepository.findByUsername(request.getUsername()) == null) {
            userRepository.save(new User(request.getUsername(), request.getPassword()));

            response = UserServiceOuterClass.UserAddResponse.newBuilder()
                    .setStatus(true)
                    .build();
        } else {
            response = UserServiceOuterClass.UserAddResponse.newBuilder()
                    .setStatus(false)
                    .build();
        }

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

//    @Override
//    public void updateUser(UserServiceOuterClass.UserUpdateRequest request, StreamObserver<UserServiceOuterClass.UserUpdateResponse> responseObserver) {
//        UserServiceOuterClass.UserUpdateResponse response;
//        User user = userRepository.findByUsername(request.getOldUsername());
//        if (user.getPassword().equals(request.getOldPassword())) {
//            if (userRepository.findByUsername(request.getNewUsername()) == null) {
//                userRepository.delete(user);
//                user.setUsername(request.getNewUsername());
//                user.setPassword(request.getNewPassword());
//
//                userRepository.save(user);
//
//                response = UserServiceOuterClass.UserUpdateResponse.newBuilder()
//                        .setStatus(1)
//                        .build();
//            } else {
//                response = UserServiceOuterClass.UserUpdateResponse.newBuilder()
//                        .setStatus(2)
//                        .build();
//            }
//        } else {
//            response = UserServiceOuterClass.UserUpdateResponse.newBuilder()
//                    .setStatus(0)
//                    .build();
//        }
//
//        responseObserver.onNext(response);
//        responseObserver.onCompleted();
//    }


    @Override
    public void deleteUser(UserServiceOuterClass.UserDeleteRequest request, StreamObserver<UserServiceOuterClass.UserDeleteResponse> responseObserver) {
        UserServiceOuterClass.UserDeleteResponse response = null;
        User user = userRepository.findByUsername(request.getUsername());

        if (user != null) {
            userRepository.delete(user);
            response = UserServiceOuterClass.UserDeleteResponse.newBuilder()
                    .setStatus(true)
                    .build();
        } else {
            response = UserServiceOuterClass.UserDeleteResponse.newBuilder()
                    .setStatus(false)
                    .build();
        }

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
