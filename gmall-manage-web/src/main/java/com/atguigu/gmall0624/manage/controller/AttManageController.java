package com.atguigu.gmall0624.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall0624.bean.bean.SkuInfo;
import com.atguigu.gmall0624.bean.bean.SkuLsInfo;
import com.atguigu.gmall0624.service.ListService;
import com.atguigu.gmall0624.service.SkuManageService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class AttManageController {
    @Reference
    SkuManageService skuManageService;

    @Reference
    ListService listService;
    @RequestMapping(value="onSale",method = RequestMethod.GET)
    @ResponseBody
    public void onSale(String skuId){
        SkuInfo skuInfo = skuManageService.getSkuInfo(skuId);
        SkuLsInfo skuLsInfo = new SkuLsInfo();
        try {
            BeanUtils.copyProperties(skuInfo,skuLsInfo);
        } catch (BeansException e) {
            e.printStackTrace();
        }
        listService.saveSkuInfo(skuLsInfo);
    }
}
