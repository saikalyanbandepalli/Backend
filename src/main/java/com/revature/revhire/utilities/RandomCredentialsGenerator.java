package com.revature.revhire.utilities;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Random;

@Component
public class RandomCredentialsGenerator {
    public String generateOtp(){
        Random random=new Random();
        int otp=new Random().nextInt(900000)+100000;
        return String.valueOf(otp);
    }
}
