package com.atguigu.gmall0624.service;

import com.atguigu.gmall0624.bean.bean.SkuLsInfo;
import com.atguigu.gmall0624.bean.bean.SkuLsParams;
import com.atguigu.gmall0624.bean.bean.SkuLsResult;

public interface ListService {
    public void saveSkuInfo(SkuLsInfo skuLsInfo);

    public SkuLsResult search(SkuLsParams skuLsParams);
}
