package com.atguigu.gmall0624.service;

import com.atguigu.gmall0624.bean.UserAddress;
import com.atguigu.gmall0624.bean.UserInfo;

import java.util.List;

public interface UserInfoService {
    /**
     * 返回所有数据
     * @return
     */
    List<UserInfo> findAll();
    //想根据用户id,或者用户的昵称或者根据用户名称等多个不同字段进行查询，如果不输入则查询所有
    List<UserInfo> selectByUserInfo(UserInfo userInfo);
    //可以做等值条件查询，模糊查询
    List<UserInfo> selectByExample(String nickName);
    //添加数据
    void insertSelective(UserInfo userInfo);
    //修改数据
    void updateByPrimaryKeySelective(UserInfo userInfo);
    //删除数据
    void delUser(UserInfo userInfo);
    //范围删除数据
    void delUserByFw(UserInfo userInfo);

    /**
     * 根据用户Id查找用户的收货地址列表
     * @param userId
     * @return
     */
    List<UserAddress> findAddressListByUserId(String userId);
    /**
     * 根据用户Id查找用户的收货地址列表
     * @param userId
     * @return
     */
    List<UserAddress> findAddressListByUserId(UserAddress userAddress);
}
