package jdk16;

public class InstanceofTest {
    public String describeObject(Object obj) {
        if (obj instanceof String str && str.length() > 10) {
            return "长字符串: " + str.toUpperCase();
        } else if (obj instanceof Integer num && num > 100) {
            return "大整数: " + num;
        }
        return "其他类型";
    }

    // Switch中的模式匹配
    public String handleResult(Result<String> result) {
        return switch (result) {
            case Success<String> success -> "成功: " + success.getValue();
            case Failure<String> failure -> "失败: " + failure.getErrorMessage();
            default -> throw new IllegalStateException("Unexpected value: " + result);
        };
    }

    public static void main(String[] args) {
        InstanceofTest test = new InstanceofTest();
        System.out.println(test.describeObject("hello world"));
        System.out.println(test.describeObject(123));

        System.out.println(test.handleResult(new Success<>("成功")));
        System.out.println(test.handleResult(new Failure<>("失败")));
    }
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
