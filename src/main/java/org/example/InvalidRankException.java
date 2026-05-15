package org.example;

// Excepción personalizada para errores en el juego
public class InvalidRankException extends Exception {
    public InvalidRankException(String message) {
        super(message);
    }
}