package com.gbdpcloud.controller;

import com.gbdpcloud.entity.Test;
import com.gbdpcloud.service.TestService;
import gbdpcloudcommonbase.gbdpcloudcommonbase.core.BaseController;
import gbdpcloudcommonbase.gbdpcloudcommonbase.httpResult.Result;
import gbdpcloudcommonbase.gbdpcloudcommonbase.httpResult.ResultGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@Api(value = "TestController")
@RequestMapping("/TestController")
@Validated
@RestController
public class TestController extends BaseController {

    @Resource
    private TestService testService;

    @ApiOperation(value = "添加测试计划")
    @CrossOrigin(origins = {"http://localhost:9527", "null"})
    @PostMapping("/add")
    public Result save(@RequestBody @Valid Test test) {
        log.info("TestController save [{}]", test);
        //TODO:执行计划状态码更新
        test.setStatus("分析中");
        int i = testService.saveOrUpdate(test);
        return i > 0 ? ResultGenerator.genSuccessResult() : ResultGenerator.genFailResult("添加成功！");
    }
}
