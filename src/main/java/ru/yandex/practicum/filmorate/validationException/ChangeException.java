package ru.yandex.practicum.filmorate.validationException;

public class ChangeException extends RuntimeException {
    public ChangeException(String message) {
        super(message);
    }
}