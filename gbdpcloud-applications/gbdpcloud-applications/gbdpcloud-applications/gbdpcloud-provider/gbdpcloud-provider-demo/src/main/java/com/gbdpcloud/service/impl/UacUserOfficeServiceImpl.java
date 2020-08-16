package com.gbdpcloud.service.impl;

import com.gbdpcloud.mapper.UacUserOfficeMapper;
import com.gbdpcloud.service.UacUserOfficeService;
import gbdpcloudcommonbase.gbdpcloudcommonbase.core.BaseService;
import gbdpcloudprovideruserapi.gbdpcloudprovideruserapi.model.UacUserOffice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional(readOnly = true)
public class UacUserOfficeServiceImpl extends BaseService<UacUserOffice> implements UacUserOfficeService {

    @Resource
    private UacUserOfficeMapper uacUserOfficeMapper;

    public UacUserOffice getByUserId(String id){
        UacUserOffice uacUserOffice=new UacUserOffice();
        uacUserOffice.setUserId(id);
        UacUserOffice uacUserOffice1 = uacUserOfficeMapper.selectOne(uacUserOffice);
        return uacUserOffice1;
    }
}

