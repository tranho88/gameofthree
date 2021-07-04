package com.takeaway.game.of.three.rest.errorhandlers.exceptions.models;

public class NotAvailableOpponentException extends RuntimeException{
    public NotAvailableOpponentException(){
        super("The opponent is not available.");
    }
}
