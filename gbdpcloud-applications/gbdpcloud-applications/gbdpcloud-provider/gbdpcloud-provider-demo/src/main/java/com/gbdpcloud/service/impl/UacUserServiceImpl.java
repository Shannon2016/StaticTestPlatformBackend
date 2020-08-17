package com.gbdpcloud.service.impl;


import com.gbdpcloud.entity.*;
import com.gbdpcloud.mapper.*;
import com.gbdpcloud.service.UacUserService;
import gbdpcloudcommonbase.gbdpcloudcommonbase.core.BaseService;
import gbdpcloudprovideruserapi.gbdpcloudprovideruserapi.model.UacOffice;
import gbdpcloudprovideruserapi.gbdpcloudprovideruserapi.model.UacRoleUser;
import gbdpcloudprovideruserapi.gbdpcloudprovideruserapi.model.UacUser;
import gbdpcloudprovideruserapi.gbdpcloudprovideruserapi.model.UacUserOffice;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * User service层
 *
 * @date 2020-07-29 18:16:17
 */
@Service
@Transactional(readOnly = true)
public class UacUserServiceImpl extends BaseService<UacUser> implements UacUserService {

    @Resource
    private UacUserMapper uacUserMapper;
    @Resource
    private UacUserOfficeMapper uacUserOfficeMapper;
    @Resource
    private UacOfficeMapper uacOfficeMapper;
    @Resource
    private UacRoleMapper uacRoleMapper;
    @Resource
    private UacRoleUserMapper uacRoleUserMapper;


    @ApiOperation(value = "获得单个用户的全部信息")
    public UacUser getInfoById(String id){
        UacUser t = new UacUser();
        t.setId(id);
        UacUser u =  uacUserMapper.selectByPrimaryKey(t);
        getALlInfo(u);
        return u;
    }
    @ApiOperation(value = "获得用户的role和office")
    public  UacUser getALlInfo(UacUser u){
        return u;
        /*
              UacRoleUser uru = new UacRoleUser();
        uru.setUserId(u.getId());
        UacRoleUser tt =  uacRoleUserMapper.select(uru).get(0);
        String roleName = uacRoleMapper.get( tt.getRoleId() ).getName();
        //u.setRoleName(roleName);

        UacUserOffice t = new UacUserOffice();
        t.setUserId(u.getId());
        UacUserOffice off = uacUserOfficeMapper.selectOne(t);
        if(null != off){
            //u.setOfficeName(uacOfficeMapper.get( off.getOffice_id() ).getName());
        }
        return u;
         */
    }


    @ApiOperation(value = "通过用户名获取用户")
    @Override
    public UacUser getByName(String username) {
        UacUser user=new UacUser();
        user.setLoginName(username);
        return uacUserMapper.selectOne(user);
    }


    @ApiOperation(value = "更新用户信息")
    @Override
    public int updateInfo(UacUser uacUser,String id) {
        /*
        UacUser old = getById(uacUser.getId());
                if(!old.getPassword().equals(uacUser.getPassword())){
            uacUser.setIsChangedPwd(1);
        }
         */

        /*if(null != uacUser.getOffice_name()){
            UacOffice toff = new UacOffice();
            toff.setName(uacUser.getOffice_name());
            String oid = uacOfficeMapper.selectOne(toff).getId();

            UacUserOffice t = new UacUserOffice();
            t.setOffice_id(oid);
            t.setUser_id(uacUser.getId());

            List<UacUserOffice> r = uacUserOfficeMapper.select(t);
            if(0 == r.size()){
                t.setCreateBy(id);
                uacUserOfficeMapper.insert(t);
            }
        }

        uacUser.setUpdate_by(id);
        uacUser.setUpdate_date(new Date());*/
        return saveOrUpdate(uacUser);
    }

    @ApiOperation(value = "添加用户")
    @Override
    public int addU(UacUser uacUser,String office,String id) {
        // uacUser.setCreate_by(id);
        //uacUser.setCreateDate(new Date());
        uacUser.setUacOffice(getOff(office));
        addUserInfo(uacUser);
        return save(uacUser);

    }

    public void addUserInfo(UacUser u){
        u.setPhone("13900000000");
        u.setEmail("1234567890@qq.com");
        if(null == u.getPassword()){
            u.setPassword("1234");
        }
    }

    @ApiOperation(value = "批量添加用户")
    public int addList(List<UacUser> list, String office ,String id){

        for(UacUser u :list){
            addUserInfo(u);
            // u.setCreateby(id);
            u.setUacOffice(getOff(office));
            //u.setCreateDate(new Date());
        }
        return  saveBatch(list);
    }

    public UacOffice getOff(String name){
        return uacOfficeMapper.selectByname(name).get(0);
    }


    @ApiOperation(value = "获得全部用户列表")
    @Override
    public List<UacUser> getAll() {
        List<UacUser> list = uacUserMapper.selectAll();
        for(UacUser u:list){
            getALlInfo(u);
        }
        return list;
    }


}
