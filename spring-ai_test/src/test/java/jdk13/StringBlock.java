package jdk13;

public class StringBlock {
    public static void main(String[] args) {
        String json = """
            {
              "name": "张三",
              "age": 30,
              "skills": ["Java", "Python", "Go"]
            }
        """;

        // SQL查询变得清晰
        String sql = """
            SELECT u.name, u.email, p.title
            FROM users u
            INNER JOIN posts p ON u.id = p.user_id
            WHERE u.status = 'ACTIVE'
            ORDER BY p.created_at DESC
        """;
        System.out.println(json);
        System.out.println(sql);
    }
}
