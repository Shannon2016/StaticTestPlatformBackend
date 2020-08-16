package com.gbdpcloud.service;

import com.gbdpcloud.entity.Test;
import gbdpcloudcommonbase.gbdpcloudcommonbase.core.Service;
import io.swagger.annotations.ApiOperation;

import java.util.List;

public interface TestService extends Service<Test> {

    @ApiOperation(value = "通过项目id获取测试计划")
    List<Test> getByProject(String id);
}
