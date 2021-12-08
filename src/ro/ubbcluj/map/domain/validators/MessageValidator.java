package ro.ubbcluj.map.domain.validators;

import ro.ubbcluj.map.domain.Message;

import java.util.Objects;

public class MessageValidator implements Validator<Message>{
    @Override
    public void validate(Message entity) throws ValidationException {
        if(entity.getMessage() == null || Objects.equals(entity.getMessage(), ""))
            throw new ValidationException("Mesajul nu poate fi null!");

    }
}
