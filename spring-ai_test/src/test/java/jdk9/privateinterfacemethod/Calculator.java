package jdk9.privateinterfacemethod;

public interface Calculator {
    default int addAndDouble(int a, int b) {
        return doubleValue(add(a, b));  // 调用私有方法
    }

    default int subtractAndDouble(int a, int b) {
        return doubleValue(subtract(a, b));  // 调用私有方法
    }

    // 私有方法，避免代码重复
    private int doubleValue(int value) {
        return value * 2;
    }

    private int add(int a, int b) {
        return a + b;
    }

    private int subtract(int a, int b) {
        return a - b;
    }
}
