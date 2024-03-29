package com.atguigu.gmall0624.manage.mapper;

import com.atguigu.gmall0624.bean.bean.SpuSaleAttr;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;


public interface SpuSaleAttrMapper extends Mapper<SpuSaleAttr>{
    List<SpuSaleAttr> selectSpuSaleAttrList(long spuId);

    List<SpuSaleAttr> selectSpuSaleAttrListCheckBySku(String id, String spuId);
}
