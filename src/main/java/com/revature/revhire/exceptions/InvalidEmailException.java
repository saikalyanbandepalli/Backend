package com.revature.revhire.exceptions;

import lombok.Data;

@Data
public class InvalidEmailException extends RuntimeException{
    private String message;
    public InvalidEmailException(String message){
        this.message=message;
    }
}