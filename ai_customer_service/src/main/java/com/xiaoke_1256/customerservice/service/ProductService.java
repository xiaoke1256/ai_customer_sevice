package com.xiaoke_1256.customerservice.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoke_1256.customerservice.dto.ProductCondition;
import com.xiaoke_1256.customerservice.dto.SimpleProduct;
import com.xiaoke_1256.customerservice.entity.ProductEntity;
import com.xiaoke_1256.customerservice.entity.ProductTypeEntity;
import com.xiaoke_1256.customerservice.mapper.ProductMapper;
import com.xiaoke_1256.customerservice.mapper.ProductTypeMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);


    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductTypeMapper productTypeMapper;

    public ProductEntity getSimpleProduct(String productCode){
        LambdaQueryWrapper<ProductEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductEntity::getProductCode, productCode);
        ProductEntity productEntity = productMapper.selectOne(wrapper);
        List<ProductTypeEntity> productTypes = productTypeMapper.getTypesByProductCode(productCode);
        productEntity.setProductTypes(productTypes);
        return productEntity;
    }

    public List<ProductEntity> queryByCondition(ProductCondition condition) {
        LambdaQueryWrapper<ProductEntity> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(condition.getProductCode())){
            wrapper.like(ProductEntity::getProductCode,condition.getProductCode()+"%");
        }
        if (StringUtils.isNotBlank(condition.getProductName())){
            wrapper.like(ProductEntity::getProductName,"%"+condition.getProductName()+"%");
        }
        if(StringUtils.isNotBlank(condition.getStoreNo())){
            wrapper.eq(ProductEntity::getStoreNo,condition.getStoreNo());
        }
        if(condition.getStoreNos()!=null && condition.getStoreNos().length>0){
            wrapper.in(ProductEntity::getStoreNo, Arrays.asList(condition.getStoreNos()));
        }
        Long total = productMapper.selectCount(wrapper);
        condition.setTotal(total.hashCode());
        Page<ProductEntity> page = new Page<>(condition.getPageNo(), condition.getPageSize());
        Page<ProductEntity> productPage = productMapper.selectPage(page, wrapper);
        productPage.getRecords().forEach((p)->{
            loadCascade(p);
            logger.info("p:"+ JSON.toJSONString(p));
        });
        return productPage.getRecords();
    }

    private void loadCascade(ProductEntity p){
        if(p==null){
            return;
        }
        p.setProductTypes(productTypeMapper.getTypesByProductCode(p.getProductCode()));
    }

    public String getFullTypeName(ProductTypeEntity type) {
        StringBuilder typeSb = new StringBuilder();
        if(type.getParentTypeId()!=null) {
            ProductTypeEntity parentType = productTypeMapper.selectById(type.getParentTypeId());
            typeSb.append( getFullTypeName(parentType));
            if(!typeSb.isEmpty()) {
                typeSb.append(" > ");
            }
        }
        typeSb.append(type.getTypeName());
        return typeSb.toString();
    }

    public String getFullTypeNames(List<ProductTypeEntity> types) {
        StringBuilder typesSb = new StringBuilder();
        for(ProductTypeEntity type:types){
            if(!typesSb.isEmpty()){
                typesSb.append(" ; ");
            }
            typesSb.append(getFullTypeName(type));
        }
        return typesSb.toString();
    }


}
