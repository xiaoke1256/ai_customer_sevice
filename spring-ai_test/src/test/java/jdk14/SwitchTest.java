package jdk14;

import org.junit.jupiter.api.Test;

public class SwitchTest {

    // 新式switch表达式：简洁优雅
    String getSeason(int month) {
        return switch (month) {
            case 12, 1, 2 -> "冬季";
            case 3, 4, 5 -> "春季";
            case 6, 7, 8 -> "夏季";
            case 9, 10, 11 -> "秋季";
            default -> "未知";
        };
    }

    // 复杂逻辑用yield
    int calculatePrice(String type, int quantity) {
        return switch (type) {
            case "BOOK" -> {
                var basePrice = quantity * 20;
                yield quantity > 10 ? (int)(basePrice * 0.9) : basePrice;//大于10本，优惠9折
            }
            case "ELECTRONICS" -> quantity * 100;
            default -> throw new IllegalArgumentException("未知商品类型");
        };
    }

    @Test
    public void testGetSeason() {
        System.out.println(getSeason(1));
        System.out.println(getSeason(4));
        System.out.println(getSeason(8));
        System.out.println(getSeason(13));
    }

    @Test
    public void testCalculatePrice() {
        System.out.println(calculatePrice("BOOK", 5));
        System.out.println(calculatePrice("BOOK", 15));
        System.out.println(calculatePrice("ELECTRONICS", 5));
        System.out.println(calculatePrice("ELECTRONICS", 15));
        try {
            System.out.println(calculatePrice("OTHER", 5));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
