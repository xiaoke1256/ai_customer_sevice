package jdk11;

public class StringUtilsTest {
    public static void main(String[] args) {
        String text = "  Hello Java World  ";

        System.out.println("   ".isBlank());        // true，比isEmpty()更智能
        System.out.println(text.strip());           // "Hello Java World"
        System.out.println("Java".repeat(3));       // "JavaJavaJava"

        // 按行分割，配合Stream处理
        "第一行\n第二行\n第三行".lines()
                .filter(line -> !line.isBlank())
                .forEach(System.out::println);
    }
}
