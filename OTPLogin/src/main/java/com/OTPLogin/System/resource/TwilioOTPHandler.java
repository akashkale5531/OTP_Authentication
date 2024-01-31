package com.OTPLogin.System.resource;

import com.OTPLogin.System.DTO.PasswordResetDTO;
import com.OTPLogin.System.service.TwilioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class TwilioOTPHandler {

    @Autowired
    private TwilioService twilioService;

    public Mono<ServerResponse> sendOTP(ServerRequest serverRequest){
        return serverRequest.bodyToMono(PasswordResetDTO.class)
                .flatMap(dto-> twilioService.sendOTPForPasswordRest(dto))
                .flatMap(dto-> ServerResponse.status(HttpStatus.OK)
                .body(BodyInserters.fromValue(dto)));
    }

    public Mono<ServerResponse> validateOTP(ServerRequest serverRequest){
        return serverRequest.bodyToMono(PasswordResetDTO.class)
                .flatMap(dto -> twilioService.validateOtp(dto.getOtp(),dto.getUserName()))
                .flatMap(dto ->ServerResponse.status(HttpStatus.OK)
                        .bodyValue(dto));
    }


}
