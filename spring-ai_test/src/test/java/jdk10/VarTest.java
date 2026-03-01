package jdk10;

public class VarTest {
    public static void main(String[] args) {
        var a = 1;
        System.out.println(a);

        var b = "hello";
        System.out.println(b);
        System.out.println(b.getClass());

        var c = true;
        System.out.println(c);

    }
}
