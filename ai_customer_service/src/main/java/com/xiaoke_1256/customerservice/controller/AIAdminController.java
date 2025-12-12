package com.xiaoke_1256.customerservice.controller;

import com.xiaoke_1256.customerservice.dto.ProductCondition;
import com.xiaoke_1256.customerservice.entity.ProductEntity;
import com.xiaoke_1256.customerservice.entity.ProductTypeEntity;
import com.xiaoke_1256.customerservice.service.ProductService;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/aiAdmin")
public class AIAdminController {

    @Autowired
    private VectorStore vectorStore;

    @Autowired
    private ProductService productService;

    @PostMapping("/importProductTypes")
    public String getVectorStore(){
        InputStream inputStream = AIAdminController.class.getResourceAsStream("/product_types.md");
        String content = "";
        try {
            content = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Document doc = new Document("productTypes", content, new HashMap<String, Object>());
        vectorStore.add(Arrays.asList(doc));
        return "success!";
    }

    @PostMapping("/importAllProduct")
    public String importAllProduct(){
        ProductCondition condition = new ProductCondition();
        int pageNo = 1;
        List<ProductEntity> productList = new ArrayList<>();
        do {
            condition.setPageNo(pageNo);
            productList = productService.queryByCondition(condition);
            for(ProductEntity p:productList){
                String productFormat = formatDoc(p);
                System.out.println("productFormat:"+productFormat);
                HashMap<String, Object> metaData = new HashMap<String, Object>();
                if(p.getProductCode()!=null) {
                    metaData.put("productCode", p.getProductCode());
                }
                if (p.getProductName() != null) {
                    metaData.put("productName", p.getProductName());
                }
                if (p.getProductTypes() != null) {
                    metaData.put("productTypes", productService.getFullTypeNames(p.getProductTypes()));
                }
                if (p.getProductPrice() != null) {
                    metaData.put("productPrice", (p.getProductPrice()!=null?Double.valueOf(p.getProductPrice().doubleValue()/1000):null));
                }
                if (p.getBrand() != null) {
                    metaData.put("brand", p.getBrand());
                }
                Document doc = new Document("product_" + p.getProductCode(), productFormat, metaData);
                vectorStore.add(Arrays.asList(doc));
            }
            pageNo++;
        } while(productList.size()>0);
        return "success!";
    }

    private String formatDoc(ProductEntity p){

        String template = """
                ### 商品(%s)
                * 类别：%s
                * 商品代码：%s
                * 商品名称：%s
                * 商标：%s
                * 商标价格：%f
                * 商品详情地址：www.orders.com/product/detail/%s
                """;
        return String.format(template,
                p.getProductCode(),
                productService.getFullTypeNames(p.getProductTypes()),
                p.getProductCode(),
                p.getProductName(),
                StringUtils.trimToEmpty(p.getBrand()),
                (p.getProductPrice()!=null?p.getProductPrice().doubleValue()/1000:0.0d),
                p.getProductCode());

    }
}
