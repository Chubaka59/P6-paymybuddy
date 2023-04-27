package com.openclassrooms.paymybuddy.exception;

public class UsernameAlreadyExistException extends RuntimeException {
    public UsernameAlreadyExistException(String email){
        super("this email = " + email + " is already used");
    }
}
