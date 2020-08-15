package com.gbdpcloud.controller;

import com.gbdpcloud.service.UserLogService;
import gbdpcloudcommonbase.gbdpcloudcommonbase.core.BaseController;
import io.swagger.annotations.Api;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(value = "UserLogController")
@RequestMapping("/UserLogController")
@Validated
@RestController
public class UserLogController extends BaseController {

    @Resource
    private UserLogService userLogService;




}
