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

    @Test
    public void testClient2(@Autowired ChatClient.Builder builder){
        ChatClient client = builder
                .defaultSystem("""
                ## 角色定义
                你是一个智能客服。
                ## 行为指南
                订单回访时仅询问是否收到商品，并引导到商品商品评价页面(www.orders.com/product/{productCode}/remark)
                
                """)
                .build();
        ChatClient.CallResponseSpec result = client.prompt().user("""
               订单信息如下：
               { "订单号":"03RAPM37355894M",
                 "商品代码":"0009100515",
                 "运费":7.00,
                 "总金额":15002,
                 "商户号":"2018100021",
                 "订单状态":"送货中",
                 "下单时间":"2025-11-13"
               }
               请您进行一下回访。
                """).call();
        String content = result.content();
        System.out.println(content);
    }
}
