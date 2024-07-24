package com.example.project1.exception;

public class CustomException extends Exception {

    public CustomException(String msg) {
        super(msg);
    }

    public CustomException() {
        super();
    }
}
