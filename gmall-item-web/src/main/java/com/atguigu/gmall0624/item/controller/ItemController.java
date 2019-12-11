package com.atguigu.gmall0624.item.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall0624.bean.bean.SkuInfo;
import com.atguigu.gmall0624.bean.bean.SkuSaleAttrValue;
import com.atguigu.gmall0624.bean.bean.SpuSaleAttr;
import com.atguigu.gmall0624.bean.bean.SpuSaleAttrValue;
import com.atguigu.gmall0624.service.ManageService;
import com.atguigu.gmall0624.service.SkuManageService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;


@Controller
public class ItemController {
    @Reference
    private SkuManageService skuManageService;

    @RequestMapping("{skuId}.html")
    public String getItem(@PathVariable(value = "skuId") String skuId,HttpServletRequest request){
        SkuInfo skuInfo = skuManageService.getSkuInfo(skuId);
        List<SpuSaleAttr> spuSaleAttrList=skuManageService.getSpuSaleAttrListCheckBySku(skuInfo);
        List<SkuSaleAttrValue> skuSaleAttrValueList=skuManageService.getSkuSaleAttrValueListBySpu(skuInfo.getSpuId());
        String key ="";
        HashMap<String, String> map = new HashMap<>();
        if(skuSaleAttrValueList!=null && skuSaleAttrValueList.size()>0){
            for (int i = 0; i < skuSaleAttrValueList.size(); i++) {
                SkuSaleAttrValue skuSaleAttrValue = skuSaleAttrValueList.get(i);
                if(key.length()>0){
                    key+="|";
                }
                key+=skuSaleAttrValue.getSaleAttrValueId();
                if((i+1)==skuSaleAttrValueList.size() || !skuSaleAttrValue.getSkuId().equals(skuSaleAttrValueList.get(i+1).getSkuId())){
                    map.put(key,skuSaleAttrValue.getSkuId());
                    key="";
                }
            }
        }
        String valuesSkuJson = JSON.toJSONString(map);
        request.setAttribute("valuesSkuJson",valuesSkuJson);
        request.setAttribute("spuSaleAttrList", spuSaleAttrList);
        request.setAttribute("skuInfo",skuInfo);
        return "item";
    }
}
