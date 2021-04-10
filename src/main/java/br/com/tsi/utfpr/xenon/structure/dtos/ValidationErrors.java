package br.com.tsi.utfpr.xenon.structure.dtos;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;
import org.springframework.validation.FieldError;

@Data
public class ValidationErrors implements Serializable {

    private List<Field> fieldErrors;

    public ValidationErrors(List<FieldError> fieldErrors) {
        this.fieldErrors = fieldErrors.stream().map(Field::new).collect(Collectors.toList());
    }

    @Data
    public static class Field {
        private String message;
        private String field;
        private Object rejectedValue;
        private String code;

        public Field(FieldError objectError) {
            this.code = objectError.getCode();
            this.field = objectError.getField();
            this.message = objectError.getDefaultMessage();
            this.rejectedValue = objectError.getRejectedValue();
        }
    }
}
