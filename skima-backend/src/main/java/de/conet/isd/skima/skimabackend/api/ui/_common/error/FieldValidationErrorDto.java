package de.conet.isd.skima.skimabackend.api.ui._common.error;

import br.com.fluentvalidator.context.Error;

public class FieldValidationErrorDto {
    private String field;
    private String message;

    public FieldValidationErrorDto() {
        // jackson
    }

    public FieldValidationErrorDto(Error error) {
        this.field = error.getField();
        this.message = error.getMessage();
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
