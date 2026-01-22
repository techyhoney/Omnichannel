package com.kpmg.omnichannel.exception;

public class TransactionNotAllowedException extends RuntimeException {
    public TransactionNotAllowedException(String message) {
        super(message);
    }
}
