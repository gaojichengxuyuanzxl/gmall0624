package com.atguigu.gmall0624.service;

import com.atguigu.gmall0624.bean.bean.*;

import java.util.List;

public interface ManageService {
     List<BaseCatalog1> getCatalog1();
     List<BaseCatalog2> getCatalog2(String catalog1Id);
     List<BaseCatalog3> getCatalog3(String catalog2Id);
     List<BaseAttrInfo> getAttrInfoList(String catalog3Id);

    /**
     * 保存平台属性和平台属性值
     * @param baseAttrInfo
     */
    void saveAttrInfo(BaseAttrInfo baseAttrInfo);

    /**
     * 通过attrId查询平台属性值集合
     * @param attrId
     * @return
     */
    List<BaseAttrValue> getAttrValueList(String attrId);

    BaseAttrInfo getAttrInfo(String attrId);

    List<SpuInfo> getSpuInfoList(SpuInfo spuInfo);

    //查询基本销售属性表
    List<BaseSaleAttr> getBaseSaleAttrList();

    public void saveSpuInfo(SpuInfo spuInfo);

    /**
     * 根据spuId 获取销售属性列表
     * @param spuId
     * @return
     */
    List<SpuSaleAttr> getSpuSaleAttrList(long  spuId);


}
