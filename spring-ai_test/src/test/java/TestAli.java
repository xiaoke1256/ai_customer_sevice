import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.xiaoke_1256.orders.StartApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = StartApplication.class)
public class TestAli {


    @Test
    public void testQianwen(@Autowired DashScopeChatModel dashScopeChatModel){
        String content = dashScopeChatModel.call("你好,你是谁？");
        System.out.println(content);
        content = dashScopeChatModel.call("可以讲个笑话吗？");
        System.out.println(content);
    }
}
