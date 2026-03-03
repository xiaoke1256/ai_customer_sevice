package jdk17;

public class SealedClassTest {
}


sealed class Result<T>
        permits Success, Failure {
}

final class Success<T> extends Result<T> {
    private final T value;
    public Success(T value) { this.value = value; }
    public T getValue() { return value; }
}

final class Failure<T> extends Result<T> {
    private final String errorMessage;
    public Failure(String errorMessage) { this.errorMessage = errorMessage; }
    public String getErrorMessage() { return errorMessage; }
}

//以下编译报错
//final class E<T> extends Result<T>{
//    private final String errorMessage;
//}
