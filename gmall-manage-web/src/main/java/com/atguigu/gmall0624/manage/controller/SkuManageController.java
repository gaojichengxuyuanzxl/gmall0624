package com.atguigu.gmall0624.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall0624.bean.bean.SkuInfo;
import com.atguigu.gmall0624.bean.bean.SpuImage;
import com.atguigu.gmall0624.bean.bean.SpuInfo;
import com.atguigu.gmall0624.bean.bean.SpuSaleAttr;
import com.atguigu.gmall0624.service.ManageService;
import com.atguigu.gmall0624.service.SkuManageService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class SkuManageController {
    @Reference
    SkuManageService skuManageService;







    //http://localhost:8082/spuImageList?spuId=58
    @RequestMapping("spuImageList")
    @ResponseBody
    public List<SpuImage> getSpuImageList(String spuId){

        return skuManageService.getSpuImageList(spuId);
    }
    //http://localhost:8082/saveSkuInfo
    @RequestMapping("saveSkuInfo")
    public String saveSkuInfo(@RequestBody SkuInfo skuInfo){
        skuManageService.saveSkuInfo(skuInfo);
        return "ok";
    }

}
