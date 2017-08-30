package controllers;

import play.data.validation.ValidationError;

import java.util.List;

@SuppressWarnings("WeakerAccess")
class FieldValidationErrorDTO {
    public String field;
    public List<String> messages;

    public FieldValidationErrorDTO(ValidationError srcError) {
        this.field = srcError.key();
        this.messages = srcError.messages();
    }
}
