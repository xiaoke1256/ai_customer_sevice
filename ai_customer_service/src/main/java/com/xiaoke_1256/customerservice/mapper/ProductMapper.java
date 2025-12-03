package com.xiaoke_1256.customerservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaoke_1256.customerservice.dto.SimpleProduct;
import com.xiaoke_1256.customerservice.entity.ProductEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 商品表 Mapper 接口
 * </p>
 *
 * @author xiaoke_1256
 * @since 2025-09-07
 */
@Mapper
public interface ProductMapper extends BaseMapper<ProductEntity> {

    SimpleProduct getSimpleProductByCode(String productCode);

}
