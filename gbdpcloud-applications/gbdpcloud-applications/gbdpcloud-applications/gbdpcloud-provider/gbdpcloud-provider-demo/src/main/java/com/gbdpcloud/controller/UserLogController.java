package com.gbdpcloud.controller;

import com.gbdpcloud.entity.UserLog;
import com.gbdpcloud.service.UserLogService;
import gbdpcloudcommonbase.gbdpcloudcommonbase.core.BaseController;
import gbdpcloudcommonbase.gbdpcloudcommonbase.dto.UacUserDto;
import gbdpcloudcommonbase.gbdpcloudcommonbase.security.UacUserUtils;
import io.swagger.annotations.Api;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(value = "UserLogController")
@Validated
@RestController
public class UserLogController extends BaseController {

    @Resource
    private UserLogService userLogService;

    public void addLog(String action) {
        UserLog userLog=new UserLog();
        UacUserDto user= UacUserUtils.getUserInfoFromRequest();
        userLog.setUser_id(user.getId());
        userLog.setUsername(user.getLoginName());
        userLog.setOperate(action);
        userLogService.save(userLog);
    }




}
