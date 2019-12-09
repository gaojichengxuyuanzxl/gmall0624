package com.atguigu.gmall0624.service;

import com.atguigu.gmall0624.bean.bean.SkuInfo;
import com.atguigu.gmall0624.bean.bean.SpuImage;
import com.atguigu.gmall0624.bean.bean.SpuInfo;
import com.atguigu.gmall0624.bean.bean.SpuSaleAttr;

import java.util.List;

public interface SkuManageService {


    /**
     * 根据商品id获得商品图片
     * @param spuId
     * @return
     */
    List<SpuImage> getSpuImageList(String spuId);
    void saveSkuInfo(SkuInfo skuInfo);
}
