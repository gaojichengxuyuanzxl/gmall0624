package com.atguigu.gmall0624.manage.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall0624.bean.bean.*;
import com.atguigu.gmall0624.config.RedisUtil;
import com.atguigu.gmall0624.manage.constant.ManageConst;
import com.atguigu.gmall0624.manage.mapper.*;
import com.atguigu.gmall0624.service.SkuManageService;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import java.util.*;

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
    @Autowired
    RedisUtil redisUtil;


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
        SkuInfo skuInfo = new SkuInfo();
        Jedis jedis = null;
       /* //测试工具类
        Jedis jedis = redisUtil.getJedis();
        jedis.set("result","success");
        jedis.close();*/
       //缓存测试
        try{
            //获取jedis
            jedis = redisUtil.getJedis();
            //定义key
            String skuKey = ManageConst.SKUKEY_PREFIX+skuId+ManageConst.SKUKEY_SUFFIX;
            //获取缓存中的数据
            String skuJson = jedis.get(skuKey);
            //什么时候上锁
            if(skuJson==null){
                //说明缓存中没有数据
                //查询数据库 上锁！ set k2 v2 px1000 nx |k2是锁！
                //定义一个锁的key =sku:skuId:lock
                String skuLockKey=ManageConst.SKUKEY_PREFIX+skuId+ManageConst.SKULOCK_SUFFIX;
                //锁的值
                String token = UUID.randomUUID().toString().replace("_", "");
                //执行锁
                String lockKey=jedis.set(skuLockKey,token,"NX","PX",ManageConst.SKULOCK_EXPIRE_PX);
                //上锁成功
                if("OK".equals(lockKey)){
                    //查询数据库并放入缓存
                    skuInfo=getSkuInfoDB(skuId);
                    //表示商品永远存在了缓存！
                    //jedis.set(skuKey,JSON.toJSONString(skuInfo))
                    //设置过期时间
                    jedis.setex(skuKey,ManageConst.SKUKEY_TIMEOUT, JSON.toJSONString(skuInfo));
                    //解锁 jedis.del(lockKey); 误删锁！ lua 脚本。。。
                    String script ="if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
                    //执行删除锁
                    jedis.eval(script, Collections.singletonList(skuLockKey), Collections.singletonList(token));
                    return skuInfo;
                }else{
                    //等待
                    Thread.sleep(1000);
                    return  getSkuInfoDB(skuId);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //如何解决空指针问题
            if(jedis!=null){
                //关闭
                jedis.close();
            }
        }

        return getSkuInfoDB(skuId);
    }

    private SkuInfo getSkuInfoDB(String skuId) {
        SkuInfo skuInfo = skuInfoMapper.selectByPrimaryKey(skuId);
        SkuImage skuImage = new SkuImage();
        skuImage.setSkuId(skuId);
        List<SkuImage> skuImageList = skuImageMapper.select(skuImage);
        skuInfo.setSkuImageList(skuImageList);
        //SkuAttrValue信息
        SkuAttrValue skuAttrValue = new SkuAttrValue();
        skuAttrValue.setSkuId(skuId);
        List<SkuAttrValue> skuAttrValues = skuAttrValueMapper.select(skuAttrValue);
        skuInfo.setSkuAttrValueList(skuAttrValues);
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

    @Override
    public Map getSkuValueIdsMap(String spuId) {
        HashMap<Object, Object> returnMap = new HashMap<>();

        // 给returnMap 赋值 skuValueIdsMap.put("122|126","37") skuValueIdsMap.put("123|126","38")
        /*
            调用mapper 执行当前的sql 语句
            SELECT group_concat(sale_attr_value_id ORDER BY sale_attr_id SEPARATOR '|') value_id,sku_id
            FROM sku_sale_attr_value ssav INNER  JOIN sku_info si ON  ssav.sku_id = si.id
            WHERE si.spu_id = 60
            GROUP BY sku_id;
         */
        List<Map> mapList = skuSaleAttrValueMapper.getSaleAttrValuesBySpu(spuId);
        if (mapList != null && mapList.size() > 0) {
            for (Map map : mapList) {
//                String value_id = (String) map.get("value_ids");//122|126
//                String sku_id = (String) map.get("sku_id"); // 37
                returnMap.put(map.get("value_ids"), map.get("sku_id"));
            }
        }
        return returnMap;
    }
}
