package com.atguigu.gmall0624.manage.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall0624.bean.bean.*;
import com.atguigu.gmall0624.manage.mapper.*;
import com.atguigu.gmall0624.service.SkuManageService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
@Service
public class SkuManageServiceImpl implements SkuManageService{
    @Autowired
    SpuImageMapper spuImageMapper;
    @Autowired
    SkuInfoMapper skuInfoMapper;
    @Autowired
    SkuImageMapper skuImageMapper;
    @Autowired
    SkuAttrValueMapper skuAttrValueMapper;
    @Autowired
    SkuSaleAttrValueMapper skuSaleAttrValueMapper;
    @Autowired
    SpuSaleAttrMapper spuSaleAttrMapper;



    @Override
    public List<SpuImage> getSpuImageList(String spuId) {
        SpuImage spuImage = new SpuImage();
        spuImage.setSpuId(spuId);

        return spuImageMapper.select(spuImage);
    }

    public  <T> boolean checkListIsEmpty(List<T> list){
        if(list!=null && list.size()>0){
            return true;
        }
        return false;
    }

    @Override
    public void saveSkuInfo(SkuInfo skuInfo) {
        //      sku_info
        if(skuInfo.getId()==null || skuInfo.getId().length()==0){
            //设置id为自增
            skuInfo.setId(null);
            skuInfoMapper.insertSelective(skuInfo);
        }else{
            skuInfoMapper.updateByPrimaryKeySelective(skuInfo);
        }
        //      sku_img
        SkuImage skuImage = new SkuImage();
        skuImage.setSkuId(skuInfo.getId());
        skuImageMapper.delete(skuImage);
        //  insert
        List<SkuImage> skuImageList = skuInfo.getSkuImageList();
        if(skuImageList!=null && skuImageList.size()>0){
            for (SkuImage image : skuImageList) {
                if(image.getId()!=null && image.getId().length()==0){
                    image.setId(null);
                }
                //skuId必须赋值
                image.setSkuId(skuInfo.getId());
                skuImageMapper.insertSelective(image);
            }
        }
        //      sku_attr_value
        SkuAttrValue skuAttrValue = new SkuAttrValue();
        skuAttrValue.setId(skuInfo.getId());
        skuAttrValueMapper.delete(skuAttrValue);
        //插入数据
        List<SkuAttrValue> skuAttrValueList = skuInfo.getSkuAttrValueList();
        if(skuAttrValueList!=null && skuAttrValueList.size()>0){
            for (SkuAttrValue attrValue : skuAttrValueList) {
                if(attrValue.getId()!=null && attrValue.getId().length()==0){
                    attrValue.setId(null);
                }
                attrValue.setSkuId(skuInfo.getId());
                skuAttrValueMapper.insertSelective(attrValue);
            }
        }
        //      sku_sale_attr_value
        SkuSaleAttrValue skuSaleAttrValue = new SkuSaleAttrValue();
        skuSaleAttrValue.setId(skuInfo.getId());
        skuSaleAttrValueMapper.delete(skuSaleAttrValue);
        List<SkuSaleAttrValue> skuSaleAttrValueList = skuInfo.getSkuSaleAttrValueList();
        if(checkListIsEmpty(skuSaleAttrValueList)){
            for (SkuSaleAttrValue saleAttrValue : skuSaleAttrValueList) {
                if(saleAttrValue.getId()!=null && saleAttrValue.getId().length()==0){
                    saleAttrValue.setId(null);
                }
                saleAttrValue.setSkuId(skuInfo.getId());
                skuSaleAttrValueMapper.insertSelective(saleAttrValue);
            }
        }
    }

    @Override
    public SkuInfo getSkuInfo(String skuId) {
        SkuInfo skuInfo = skuInfoMapper.selectByPrimaryKey(skuId);
        SkuImage skuImage = new SkuImage();
        skuImage.setSkuId(skuId);
        List<SkuImage> skuImageList = skuImageMapper.select(skuImage);
        skuInfo.setSkuImageList(skuImageList);

        return skuInfo;
    }

    @Override
    public List<SpuSaleAttr> getSpuSaleAttrListCheckBySku(SkuInfo skuInfo) {
        return spuSaleAttrMapper.selectSpuSaleAttrListCheckBySku(skuInfo.getId(),skuInfo.getSpuId());
    }

    @Override
    public List<SkuSaleAttrValue> getSkuSaleAttrValueListBySpu(String spuId) {
        return skuSaleAttrValueMapper.selectSkuSaleAttrValueListBySpu(spuId);
    }
}
