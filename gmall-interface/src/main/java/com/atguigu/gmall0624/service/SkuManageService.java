package com.atguigu.gmall0624.service;

import com.atguigu.gmall0624.bean.bean.*;

import java.util.List;

public interface SkuManageService {


    /**
     * 根据商品id获得商品图片
     * @param spuId
     * @return
     */
    List<SpuImage> getSpuImageList(String spuId);
    void saveSkuInfo(SkuInfo skuInfo);

    SkuInfo getSkuInfo(String skuId);

    List<SpuSaleAttr> getSpuSaleAttrListCheckBySku(SkuInfo skuInfo);

    List<SkuSaleAttrValue> getSkuSaleAttrValueListBySpu(String spuId);
}
