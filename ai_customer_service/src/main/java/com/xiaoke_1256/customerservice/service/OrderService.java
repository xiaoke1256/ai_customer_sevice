package com.xiaoke_1256.customerservice.service;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    @Tool(description="查询订单详情", returnDirect = false)
    public String getOrderDetailByOrderNo(@ToolParam(description="订单号") String orderNo){
        System.out.println("获取订单详情,orderNo:"+orderNo);
        //获取订单详情
        if("03RAPM37355894M".equals(orderNo)) {
            return """
                   { "订单号":"03RAPM37355894M",
                     "运费":7.00,
                     "总金额":15002,
                     "商户号":"2018100021",
                     "订单状态":"送货中",
                     "下单时间":"2025-11-13"
                   }
                   """;
        } else if("03RAPM38172806K".equals(orderNo)){
            return """
                     { "订单号":"03RAPM38172806K",
                     "运费":7.00,
                     "总金额":3896,
                     "商户号":"2018100021",
                     "订单状态":"待发货",
                     "下单时间":"2025-10-12"
                   }
                   """;
        } else {
            throw new RuntimeException("订单不存在");
        }
    }

    /**
     * 取消订单
     * @param orderNo
     * @return
     */
    @Tool(description="取消订单", returnDirect = false)
    public String cancelOrder(@ToolParam(description="订单号") String orderNo){
        System.out.println("取消订单，orderNo："+orderNo);
        return "取消成功";
    }

}
