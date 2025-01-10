package de.conet.isd.skima.skimabackend.api.ui._common.error;

import de.conet.isd.skima.skimabackend.service._common.error.ValidationException;

import java.util.List;

public class ValidationErrorDto extends ErrorDto {

    private List<FieldValidationErrorDto> validationErrors;

    public ValidationErrorDto() {
        // Constructor for jackson
    }

    public ValidationErrorDto(ValidationException validationErrors) {
        this.validationErrors = validationErrors.getValidationResult().getErrors().stream()
                .map(FieldValidationErrorDto::new).toList();
    }

    public List<FieldValidationErrorDto> getValidationErrors() {
        return validationErrors;
    }

    public void setValidationErrors(List<FieldValidationErrorDto> validationErrors) {
        this.validationErrors = validationErrors;
    }
}
