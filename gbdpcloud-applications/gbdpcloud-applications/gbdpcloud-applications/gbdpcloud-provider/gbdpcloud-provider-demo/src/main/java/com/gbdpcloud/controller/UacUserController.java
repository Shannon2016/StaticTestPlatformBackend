package com.gbdpcloud.controller;

import com.gbdpcloud.service.UacUserService;
import gbdpcloudcommonbase.gbdpcloudcommonbase.core.BaseController;
import gbdpcloudcommonbase.gbdpcloudcommonbase.dto.UacUserDto;
import gbdpcloudcommonbase.gbdpcloudcommonbase.httpResult.Result;
import gbdpcloudcommonbase.gbdpcloudcommonbase.httpResult.ResultGenerator;
import gbdpcloudprovideruserapi.gbdpcloudprovideruserapi.model.UacUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jdk.nashorn.internal.objects.annotations.Getter;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Api(value = "UacUserController")
@RequestMapping("/UacUserController")
@Controller
@Validated
@RestController
public class UacUserController extends BaseController {

    @Resource
    private UacUserService uacUserService;

    private String ADMINROLE = "0";


    @GetMapping("/test/{id}")
    public Result T(@PathVariable @Valid String id){
        return ResultGenerator.genSuccessResult("ok"+id);
    }

    @GetMapping("get/{id}")
    @ApiOperation(value = "通过id获取用户信息")
    public Result getInfo(@PathVariable String id){
        //UacUser uacUser = uacUserService.getByName(name);
        UacUser uacUser = uacUserService.getInfoById(id);
        return ResultGenerator.genSuccessResult((uacUser));
    }

    @GetMapping("/checkId/{id}")
    @ApiOperation(value = "检查id是否存在")
    public Result checkId(@PathVariable String id){
        Result r = null ;
        if(isAdmin()){
            UacUser uacUser = uacUserService.getById(id);
            if(null == uacUser){
                r = ResultGenerator.genSuccessResult("0");
            }else{
                r =  ResultGenerator.genFailResult("id已经存在");
            }
        }else{
            r = ResultGenerator.genFailResult("没有权限");
        }
        return r;
    }


    @GetMapping("/all")
    @ApiOperation(value = "获得全部用户列表")
    public Result getList(){
        List<UacUser> list = uacUserService.getAll();
         // List<UacUser> list = uacUserService.list(new UacUser());
        //List<UacUserDto> list_dto = new ArrayList<UacUserDto>();
        return ResultGenerator.genSuccessResult(list);
    }


    @GetMapping("/del/{id}")
    @ApiOperation(value = "删除用户")
    public Result delByname(@PathVariable String id){
        Result r = null ;
        if(isAdmin()){
            //UacUser uacUser = uacUserService.getById(id);
            int i=uacUserService.deleteById(id);
            r =  i>0?ResultGenerator.genSuccessResult():ResultGenerator.genFailResult("deleteById失败");
        }else{
            r = ResultGenerator.genFailResult("更新失败，没有权限");
        }
        return r;
    }
    @PostMapping("/add")
    @ApiOperation(value = "添加用户")
    public Result addU(@RequestBody @Valid UacUser uacUser){
        Result r = null ;
        if(isAdmin()){
            //int i=uacUserService.saveOrUpdate(uacUser);
            int i = uacUserService.addU(uacUser,getCurrentUserId());
            r = i>0?ResultGenerator.genSuccessResult():ResultGenerator.genFailResult("update失败");
        }else{
            r = ResultGenerator.genFailResult("更新失败，没有权限");
        }
        return r;
    }

    @PostMapping("/addList")
    @ApiOperation(value = "批量添加用户")
    public Result addList(@RequestBody @Valid List<UacUser> list){
        Result r = null ;
        if(isAdmin()){
            //int i=uacUserService.saveOrUpdate(uacUser);
            int i = uacUserService.addList(list,getCurrentUserId());
            r = i>0?ResultGenerator.genSuccessResult(i):ResultGenerator.genFailResult("update失败");
        }else{
            r = ResultGenerator.genFailResult("更新失败，没有权限");
        }
        return r;
    }


    @PostMapping("/updateUerInfo")
    @ApiOperation(value = "更新用户信息")
    public Result updateInfo(@RequestBody @Valid UacUser uacUser){
        Result r = null ;
        if(isAdmin()){
            //int i=uacUserService.saveOrUpdate(uacUser);
            int i = uacUserService.updateInfo(uacUser,getCurrentUserId());
            r = i>0?ResultGenerator.genSuccessResult():ResultGenerator.genFailResult("update失败");
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
        //return ADMINROLE.equals(getCurrentUserRole());
    }

    @ApiOperation(value = "获得当前用户id")
    public String getCurrentUserId(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String current_user_id = (String) request.getSession().getAttribute("user_id");

        // !!!!!!!!!
        if(null ==current_user_id ){
            current_user_id = "0";
        }
        return current_user_id;
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



}
