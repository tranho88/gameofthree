package com.takeaway.game.of.three.rest.errorhandlers.exceptions.models;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * Custom validation error
 */
@Builder
@Getter
@ToString
public class ValidationError {
    String field;
    String invalidValue;
    String message;
}
