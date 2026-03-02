package jdk16;

public class RecordTest {

    public static record Person(String name, int age, String email) {
        // 可以添加验证逻辑
        public Person {
            if (age < 0) throw new IllegalArgumentException("年龄不能为负数");
        }

        // 可以添加自定义方法
        public boolean isAdult() {
            return age >= 18;
        }
    }

    public static void main(String[] args) {
        var person = new Person("张三", 18, "zhangsan@example.com");
        System.out.println(person);
        System.out.println(person.isAdult());
    }
}
