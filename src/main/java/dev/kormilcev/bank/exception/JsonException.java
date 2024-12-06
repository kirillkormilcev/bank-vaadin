package dev.kormilcev.bank.exception;

import java.io.IOException;

public class JsonException extends IOException {
    public JsonException(String message) {
        super(message);
    }
}