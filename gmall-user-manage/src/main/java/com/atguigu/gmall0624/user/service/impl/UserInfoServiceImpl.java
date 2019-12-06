package com.atguigu.gmall0624.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall0624.bean.UserAddress;
import com.atguigu.gmall0624.bean.UserInfo;
import com.atguigu.gmall0624.service.UserInfoService;
import com.atguigu.gmall0624.user.mapper.UserAddressMapper;
import com.atguigu.gmall0624.user.mapper.UserInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.List;
@Service
public class UserInfoServiceImpl implements UserInfoService{
    //调用mapper
    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private UserAddressMapper userAddressMapper;
    @Override
    public List<UserInfo> findAll() {
        return userInfoMapper.selectAll();
    }

    @Override
    public List<UserInfo> selectByUserInfo(UserInfo userInfo) {
        return null;
    }

    @Override
    public List<UserInfo> selectByExample(String nickName) {
        return null;
    }

    @Override
    public void insertSelective(UserInfo userInfo) {

    }

    @Override
    public void updateByPrimaryKeySelective(UserInfo userInfo) {

    }

    @Override
    public void delUser(UserInfo userInfo) {

    }

    @Override
    public void delUserByFw(UserInfo userInfo) {

    }

    @Override
    public List<UserAddress> findAddressListByUserId(String userId) {
        //查询那张表就用哪张表的mapper
        UserAddress userAddress = new UserAddress();
        userAddress.setUserId(userId);
        return userAddressMapper.select(userAddress);
    }

    @Override
    public List<UserAddress> findAddressListByUserId(UserAddress userAddress) {

        return userAddressMapper.select(userAddress);
    }
}
