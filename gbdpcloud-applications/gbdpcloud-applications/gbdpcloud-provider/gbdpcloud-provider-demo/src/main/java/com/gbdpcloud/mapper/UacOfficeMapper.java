package com.gbdpcloud.mapper;

import gbdpcloudcommonbase.gbdpcloudcommonbase.core.IMapper;
import gbdpcloudprovideruserapi.gbdpcloudprovideruserapi.model.UacOffice;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UacOfficeMapper extends IMapper<UacOffice> {
    //int deleteByPrimaryKey(String id);

    //int insert(UacOffice record);

    //int insertSelective(UacOffice record);

   // UacOffice selectByPrimaryKey(String id);

    //int updateByPrimaryKeySelective(UacOffice record);

    //int updateByPrimaryKey(UacOffice record);

    List<UacOffice> list();
}