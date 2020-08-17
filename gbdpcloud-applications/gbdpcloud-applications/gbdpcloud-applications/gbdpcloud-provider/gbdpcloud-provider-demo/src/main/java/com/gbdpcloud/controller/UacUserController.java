package com.gbdpcloud.controller;

import com.gbdpcloud.service.UacUserService;
import gbdpcloudcommonbase.gbdpcloudcommonbase.core.BaseController;
import gbdpcloudcommonbase.gbdpcloudcommonbase.dto.ResetPasswordDto;
import gbdpcloudcommonbase.gbdpcloudcommonbase.dto.UacUserDto;
import gbdpcloudcommonbase.gbdpcloudcommonbase.httpResult.Result;
import gbdpcloudcommonbase.gbdpcloudcommonbase.httpResult.ResultGenerator;
import gbdpcloudcommonbase.gbdpcloudcommonbase.page.PageRequest;
import gbdpcloudprovideruserapi.gbdpcloudprovideruserapi.model.UacUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jdk.nashorn.internal.objects.annotations.Getter;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import gbdpcloudprovideruserapi.gbdpcloudprovideruserapi.service.UserService;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import gbdpcloudprovideruserapi.gbdpcloudprovideruserapi.model.UacOffice;
@Api(value = "UacUserController")
@RequestMapping("/UacUserController")
@Controller
@Validated
@RestController
public class UacUserController extends BaseController {

    @Resource
    private UacUserService uacUserService;
    @Resource
    private UserService userService;

    private String ADMINROLE = "0";

    @CrossOrigin(origins = {"http://localhost:9527", "null"})
    @GetMapping("/test/{id}")
    public Result T(@PathVariable @Valid String id){
        return ResultGenerator.genSuccessResult("ok"+id);
    }

    @CrossOrigin(origins = {"http://localhost:9527", "null"})
    @GetMapping("get/{id}")
    @ApiOperation(value = "通过id获取用户信息")
    public Result getInfo(@PathVariable String id){
        //UacUser uacUser = uacUserService.getByName(name);
        //UacUser uacUser = uacUserService.getInfoById(id);
        return ResultGenerator.genSuccessResult(userService.get(id));
    }
    @CrossOrigin(origins = {"http://localhost:9527", "null"})
    @GetMapping("/checkId/{id}")
    @ApiOperation(value = "检查id是否存在")
    public Result checkId(@PathVariable String id){
        Result r = null ;
        if(isAdmin()){
            //UacUser uacUser = uacUserService.getById(id);
            if(null == userService.get(id).getData()){
                r = ResultGenerator.genSuccessResult("0");
            }else{
                r =  ResultGenerator.genFailResult("id已经存在");
            }
        }else{
            r = ResultGenerator.genFailResult("没有权限");
        }
        return r;
    }

    @CrossOrigin(origins = {"http://localhost:9527", "null"})
    @GetMapping("/all")
    @ApiOperation(value = "获得全部用户列表")
    public Result getList(@ModelAttribute @Valid PageRequest var1){
        List<UacUser> list = uacUserService.getAll();
        return ResultGenerator.genSuccessResult(list);
        // return userService.list(var1);
    }

    @CrossOrigin(origins = {"http://localhost:9527", "null"})
    @GetMapping("/Testall")
    @ApiOperation(value = "TEST获得全部用户列表")
    public Result TestgetList(){
        //List<UacUser> list = uacUserService.getAll();
        //return ResultGenerator.genSuccessResult(list);
        PageRequest p = new PageRequest();
        //p.setOrderBy();
        p.setPageNum(1);
        p.setPageSize(10);
        p.setOrderBy("create_date");
        p.setOrderRule("desc");
        return userService.list(p);
    }

    @CrossOrigin(origins = {"http://localhost:9527", "null"})
    @GetMapping("/del/{id}")
    @ApiOperation(value = "删除用户")
    public Result delByname(@PathVariable String id){
        Result r = null ;
        if(isAdmin()){
            r = userService.delete(id);
            //UacUser uacUser = uacUserService.getById(id);
            //int i=uacUserService.deleteById(id);
            //r =  i>0?ResultGenerator.genSuccessResult():ResultGenerator.genFailResult("deleteById失败");
        }else{
            r = ResultGenerator.genFailResult("删除失败，没有权限");
        }
        return r;
    }
    @CrossOrigin(origins = {"http://localhost:9527", "null"})
    @PostMapping("/add")
    @ApiOperation(value = "添加用户")
    public Result addU(@RequestParam("user")  @Valid UacUser uacUser,@RequestParam("office") @Valid String office ){
        Result r = null ;
        if(isAdmin()){
            //int i=uacUserService.saveOrUpdate(uacUser)
            int i = uacUserService.addU(uacUser,office,getCurrentUserId());
            r = i>0?ResultGenerator.genSuccessResult():ResultGenerator.genFailResult("update失败");
        }else{
            r = ResultGenerator.genFailResult("添加失败，没有权限");
        }
        return r;
    }
    @CrossOrigin(origins = {"http://localhost:9527", "null"})
    @PostMapping("/addTEST")
    @ApiOperation(value = "添加用户")
    public Result TestAdd(){
        UacUser u = new UacUser();
        u.setLoginName("hhh");
        u.setPassword("1234");
        u.setId("777");
        //u.setUacOffice();
        u.setStatus("ENABLE");
        return ResultGenerator.genSuccessResult(uacUserService.addU(u,"市场部","0"));
    }

    @CrossOrigin(origins = {"http://localhost:9527", "null"})
    @PostMapping("/addList")
    @ApiOperation(value = "批量添加用户")
    public Result addList(@RequestParam("list")  @Valid List<UacUser> list,@RequestParam("office") String office ){
        Result r = null ;
        if(isAdmin()){
            //int i=uacUserService.saveOrUpdate(uacUser);
            int i = uacUserService.addList(list,office,getCurrentUserId());
            r = i>0?ResultGenerator.genSuccessResult(i):ResultGenerator.genFailResult("update失败");
        }else{
            r = ResultGenerator.genFailResult("批量添加失败，没有权限");
        }
        return r;
    }
    @CrossOrigin(origins = {"http://localhost:9527", "null"})
    @PostMapping("/addTESTList")
    @ApiOperation(value = "添加用户")
    public Result TestAddLIST(){
        List<UacUser> list = new ArrayList<UacUser>();
        for(int i = 0;i<5;i++){
            UacUser u = new UacUser();
            u.setStatus("ENABLE");
            u.setName(""+i);
            u.setLoginName(u.getName());
            list.add(u);
            //UacOffice o = new UacOffice();
            //u.setUacOffice();
        }
        String o = "市场部";
        return ResultGenerator.genSuccessResult(uacUserService.addList(list,o,getCurrentUserId()));
    }

    @CrossOrigin(origins = {"http://localhost:9527", "null"})
    @PostMapping("/updateUerInfo")
    @ApiOperation(value = "更新用户信息")
    public Result updateInfo(@RequestBody @Valid UacUser uacUser){
        Result r = null ;
        if(isAdmin()){
            //int i=uacUserService.saveOrUpdate(uacUser);
            if(null == uacUser.getId()){
                r = ResultGenerator.genFailResult("只能更新已存在的用户，缺少用户id");
            }else{
                int i = uacUserService.updateInfo(uacUser,getCurrentUserId());
                r = i>0?ResultGenerator.genSuccessResult():ResultGenerator.genFailResult("update失败");
            }
        }else{
            r = ResultGenerator.genFailResult("更新失败，没有权限");
        }
        return r;
    }
    @CrossOrigin(origins = {"http://localhost:9527", "null"})
    @PostMapping("/updateTEST")
    @ApiOperation(value = "添加用户")
    public Result Testupdate(){
        UacUser u = new UacUser();
        u.setLoginName("xxx");
        u.setPassword("0000");
        u.setId("777");
        //u.setUacOffice();
        u.setStatus("ENABLE");
        return ResultGenerator.genSuccessResult(uacUserService.updateInfo(u,getCurrentUserId()));
    }

    @CrossOrigin(origins = {"http://localhost:9527", "null"})
    @PostMapping("/changePW")
    @ApiOperation(value = "修改密码")
    public Result changePW(@RequestBody @Validated ResetPasswordDto var1){
        return userService.changPassword(var1);
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
