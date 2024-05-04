package validators;

public interface BaseValidator<T> {
    void validate(T model);
}
