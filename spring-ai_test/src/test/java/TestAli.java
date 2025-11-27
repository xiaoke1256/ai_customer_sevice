import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.alibaba.cloud.ai.dashscope.image.DashScopeImageModel;
import com.alibaba.cloud.ai.dashscope.image.DashScopeImageOptions;
import com.xiaoke_1256.customerservice.StartApplication;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = StartApplication.class)
public class TestAli {


    @Test
    public void testQianwen(@Autowired DashScopeChatModel dashScopeChatModel){
        dashScopeChatModel.setDashScopeChatOptions(DashScopeChatOptions.builder()
                .withModel("qwen-plus")
                .withTemperature(0.2d)
                /*.withTopP(0.8d)*/.build());
        String content = dashScopeChatModel.call("你好,你是谁？");
        System.out.println(content);
        content = dashScopeChatModel.call("可以讲个笑话吗？");
        System.out.println(content);
    }

    /**
     * 文生图
     * @param dashScopeImageModel
     */
    @Test
    public void testTextToImage(@Autowired DashScopeImageModel dashScopeImageModel){
        DashScopeImageOptions option = DashScopeImageOptions.builder()
                .withModel("wanx2.1-t2i-plus")
                /*.withTopP(0.8d)*/.build();
        ImageResponse response = dashScopeImageModel.call(new ImagePrompt("给白蛇传连环画设计个封面", option));

        String url = response.getResult().getOutput().getUrl();
        System.out.println(url);
    }

    @Test
    public void testClient(@Autowired ChatClient.Builder builder){
        ChatClient client = builder
                .defaultSystem("""
                ## 角色定义
                你是一个智能客服。
                """)
                .build();
        ChatClient.CallResponseSpec result = client.prompt().user("你是谁？").call();
        String content = result.content();
        System.out.println(content);
    }
}
