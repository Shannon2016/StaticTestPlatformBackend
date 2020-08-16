package com.gbdpcloud.service;

import gbdpcloudcommonbase.gbdpcloudcommonbase.core.Service;
import gbdpcloudprovideruserapi.gbdpcloudprovideruserapi.model.UacUserOffice;
import io.swagger.annotations.ApiOperation;

public interface UacUserOfficeService extends Service<UacUserOffice> {


    @ApiOperation(value = "通过用户Id获取")
    UacUserOffice getByUserId(String id);


}
