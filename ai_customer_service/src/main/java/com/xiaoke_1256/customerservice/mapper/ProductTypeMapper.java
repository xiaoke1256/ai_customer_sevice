package com.xiaoke_1256.customerservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaoke_1256.customerservice.entity.ProductTypeEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 商品分类 Mapper 接口
 * </p>
 *
 * @author xiaoke_1256
 * @since 2025-09-08
 */
@Mapper
public interface ProductTypeMapper extends BaseMapper<ProductTypeEntity> {
    public List<ProductTypeEntity> getTypesByProductCode(String productCode);
}
