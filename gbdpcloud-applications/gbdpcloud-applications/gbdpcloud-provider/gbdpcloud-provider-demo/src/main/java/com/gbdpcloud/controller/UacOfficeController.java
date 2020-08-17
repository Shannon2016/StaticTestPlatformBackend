package com.gbdpcloud.controller;

import com.gbdpcloud.service.UacOfficeService;
import gbdpcloudcommonbase.gbdpcloudcommonbase.core.BaseController;
import gbdpcloudcommonbase.gbdpcloudcommonbase.dto.UacOfficeDto;
import gbdpcloudcommonbase.gbdpcloudcommonbase.httpResult.Result;
import gbdpcloudcommonbase.gbdpcloudcommonbase.httpResult.ResultGenerator;
import gbdpcloudprovideruserapi.gbdpcloudprovideruserapi.model.UacOffice;
import gbdpcloudprovideruserapi.gbdpcloudprovideruserapi.service.OfficeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.lang.reflect.Field;
import java.util.*;

@Api(value = "UacOfficeController")
@RequestMapping("/UacOfficeController")
@Controller
@Validated
@RestController


public class UacOfficeController extends BaseController {
    @Resource
    private UacOfficeService uacOfficeService;
    @Resource
    private OfficeService officeService;

    private String ADMINROLE = "0";

    @CrossOrigin(origins = {"http://localhost:9527", "null"})
    @PostMapping("/del/{id}")
    @ApiOperation(value = "删除机构")
    public Result delByname(@PathVariable String id){
        Result r = null ;
        if(isAdmin()){
            r = officeService.delete(id);
            //int i= uacOfficeService.delete(id,getCurrentUserId());
            // r =  i>0?ResultGenerator.genSuccessResult():ResultGenerator.genFailResult("deleteById失败");
        }else{
            r = ResultGenerator.genFailResult("更新失败，没有权限");
        }
        return r;
    }

    /*
    {
    "parentId":"1",
     "name":"0816-2338",
     "type":"4"
      }
     */
    @CrossOrigin(origins = {"http://localhost:9527", "null"})
    @PostMapping("/add")
    @ApiOperation(value = "新增机构")
    public Result add(@RequestBody @Valid UacOfficeDto var1){
        Result r = null ;
        if(isAdmin()){
            r = officeService.save(var1);
            //int i= uacOfficeService.add(uacOffice,getCurrentUserId());
            // r =  i>0?ResultGenerator.genSuccessResult():ResultGenerator.genFailResult("新增失败");
        }else{
            r = ResultGenerator.genFailResult("新增失败，没有权限");
        }
        return r;
    }

    @CrossOrigin(origins = {"http://localhost:9527", "null"})
    @PostMapping("/update")
    @ApiOperation(value = "更新机构信息")
    public Result update(@RequestBody @Valid UacOfficeDto var1){
        Result r = null ;
        if(isAdmin()){
            r = officeService.update(var1);
            //int i= uacOfficeService.updateInfo(uacOffice,getCurrentUserId());
            //r =  i>0?ResultGenerator.genSuccessResult():ResultGenerator.genFailResult("新增失败");
        }else{
            r = ResultGenerator.genFailResult("新增失败，没有权限");
        }
        return r;
    }
    @CrossOrigin(origins = {"http://localhost:9527", "null"})
    @GetMapping("/list")
    @ApiOperation(value = "获得机构列表")
    public Result getList(){
        Result r = null ;
        if(isAdmin()){
            r =  ResultGenerator.genSuccessResult(uacOfficeService.listAsT() );
        }else{
            r = ResultGenerator.genFailResult("更新失败，没有权限");
        }
        return r;
    }



    @ApiOperation(value = "获得当前用户角色")
    public String getCurrentUserRole(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String current_user_role = (String) request.getSession().getAttribute("user_role");
        return current_user_role;
    }

    @ApiOperation(value = "判断当前用户是否为管理员")
    public boolean isAdmin(){
        return true;
        // return ADMINROLE.equals(getCurrentUserRole());
    }

    public Result simple(){
        Result r = null ;
        if(isAdmin()){
            r = null;
        }else{
            r = ResultGenerator.genFailResult("更新失败，没有权限");
        }
        return r;
    }

    @ApiOperation(value = "获得当前用户id")
    public String getCurrentUserId(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String current_user_id = (String) request.getSession().getAttribute("user_id");
        return current_user_id;
    }
}
