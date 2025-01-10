package de.conet.isd.skima.skimabackend.service._common.error;

import br.com.fluentvalidator.context.ValidationResult;

/**
 * Work in Progress
 */
public class ValidationException extends RuntimeException {
    private final ValidationResult validationResult;

    public ValidationException(ValidationResult validationResult) {
        this.validationResult = validationResult;
    }

    public ValidationResult getValidationResult() {
        return validationResult;
    }
}
