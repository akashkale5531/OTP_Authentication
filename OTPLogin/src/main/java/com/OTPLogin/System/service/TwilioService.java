package com.OTPLogin.System.service;

import com.OTPLogin.System.DTO.OtpStatus;
import com.OTPLogin.System.DTO.PasswordResetDTO;
import com.OTPLogin.System.DTO.PasswordResetResponse;
import com.OTPLogin.System.config.TwilioConfig;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class TwilioService {

    @Autowired
    private TwilioConfig twilioConfig;

    Map<String, String> otpMap=new HashMap<>();
    public Mono<PasswordResetResponse> sendOTPForPasswordRest(PasswordResetDTO passwordResetDTO){

        PasswordResetResponse passwordResetResponse=null;
        try {
            PhoneNumber to = new PhoneNumber(passwordResetDTO.getPhoneNumber());
            PhoneNumber from = new PhoneNumber(twilioConfig.getTrailNumber());

            String otp = generateOTP();

            String otpMessage = "Dear Customer , Your OTP is ##" + otp + "##. Use this Passcode to complete your transaction. Thank You.";
/*

            Message message = Message.creator(to, from,
                            otpMessage)
                    .create();
*/

            Message message = Message.creator(
                            new com.twilio.type.PhoneNumber("+918390205531"),
                            new com.twilio.type.PhoneNumber("+18504241839"),
                            otpMessage)
                    .create();
            otpMap.put(passwordResetDTO.getUserName(),otp);
            passwordResetResponse = new PasswordResetResponse(OtpStatus.DELIVERED, otpMessage);

        }catch(Exception exception){
            passwordResetResponse=new PasswordResetResponse(OtpStatus.FAILED,exception.getMessage());
        }
        return Mono.just(passwordResetResponse);
    }


    public Mono<String> validateOtp(String userInputOtp, String userName){
        if(userInputOtp.equals(otpMap.get(userName))){
            otpMap.remove(userName,userInputOtp);
            return Mono.just("valid OTP please proceed further!");
        }else{
            return Mono.error(new IllegalArgumentException("Invalid OTP please retry!"));
        }
    }


    private String generateOTP(){
        return new DecimalFormat("000000").format(new Random().nextInt(999999));
    }


}
