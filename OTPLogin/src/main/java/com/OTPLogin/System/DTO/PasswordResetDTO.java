package com.OTPLogin.System.DTO;

import lombok.Data;

@Data
public class PasswordResetDTO {

    private String phoneNumber;

    private String userName;

    private String otp;
}
