package com.takeaway.game.of.three.rest.errorhandlers.exceptions.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * Custom Error Response
 */
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    String message;
    String errorCode;
    List<ValidationError> errors;
}
