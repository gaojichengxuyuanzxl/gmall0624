package com.atguigu.gmall0624.manage.mapper;

import com.atguigu.gmall0624.bean.bean.SkuSaleAttrValue;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface SkuSaleAttrValueMapper extends Mapper<SkuSaleAttrValue> {
    List<SkuSaleAttrValue> selectSkuSaleAttrValueListBySpu(String spuId);

    List<Map> getSaleAttrValuesBySpu(String spuId);
}
