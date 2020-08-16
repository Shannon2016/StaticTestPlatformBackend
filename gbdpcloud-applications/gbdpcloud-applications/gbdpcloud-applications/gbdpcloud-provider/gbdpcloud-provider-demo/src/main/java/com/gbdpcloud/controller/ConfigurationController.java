package com.gbdpcloud.controller;

import com.gbdpcloud.entity.Configuration;
import com.gbdpcloud.service.ConfigurationService;
import com.gbdpcloud.service.UacUserService;
import gbdpcloudcommonbase.gbdpcloudcommonbase.core.BaseController;
import gbdpcloudcommonbase.gbdpcloudcommonbase.dto.UacUserDto;
import gbdpcloudcommonbase.gbdpcloudcommonbase.httpResult.Result;
import gbdpcloudcommonbase.gbdpcloudcommonbase.httpResult.ResultGenerator;
import gbdpcloudcommonbase.gbdpcloudcommonbase.security.UacUserUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Api(value = "ConfigurationController")
@RequestMapping("/ConfigurationController")
@Validated
@RestController
public class ConfigurationController extends BaseController {

    public String Base_PATH="F:/configuration/";

    @Resource
    private ConfigurationService configurationService;

    @Resource
    private UacUserService uacUserService;

    private static void makeDir(String filePath) {
        if (filePath.lastIndexOf('/') > 0) {
            String dirPath = filePath.substring(0, filePath.lastIndexOf('/'));
            File dir = new File(dirPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
        }
    }

    @ApiOperation(value = "查看公共方案")
    @GetMapping("/getCommon")
    public Result getCommon(){
        List<Configuration> list=configurationService.getCommon();
        return ResultGenerator.genSuccessResult(list);
    }

    @ApiOperation(value = "查看个人方案")
    @GetMapping("/getPrivate")
    public Result getPrivate(){
        UacUserDto user= UacUserUtils.getUserInfoFromRequest();
        String user_id=user.getId();
        List<Configuration> list=configurationService.getPrivate(user_id);
        return ResultGenerator.genSuccessResult(list);
    }

    @ApiOperation(value = "添加方案")
    @PostMapping("/add")
    public Result save(@RequestParam(value = "name")String name,
                       @RequestParam(value = "tools")String tools,
                       @RequestParam(value = "rule")String rule,
                       @RequestParam(value = "is_common")String is_common,
                       @RequestParam(value = "header") MultipartFile header,
                       @RequestParam(value = "define")MultipartFile [] define)  {
        Configuration configuration=new Configuration();
        configuration.setName(name);
        configuration.setIs_common(is_common);
        configuration.setTools(tools);
        configuration.setRule(rule);
        configuration.setId(UUID.randomUUID().toString());
        configuration.setDelFlag("0");
        configuration.setIs_default("0");
        log.info("ConfigurationController save [{}]", configuration);
        //TODO:上传文件
        String basePath=Base_PATH+configuration.getId()+"/"+"header"+"/";
        if(header==null)
        {
            return ResultGenerator.genFailResult("头文件不能为空");
        }

        if (basePath.endsWith("/")) {
            basePath = basePath.substring(0, basePath.length() - 1);
        }

        String filePath = basePath + "/" + header.getOriginalFilename();
        makeDir(filePath);
        File dest = new File(filePath);
        try {
            header.transferTo(dest);
            configuration.setHeader(filePath);
        }catch (IOException e){

            ResultGenerator.genFailResult("文件上传失败！");
        }

        if (define == null || define.length == 0) {
            return ResultGenerator.genFailResult("上传文件不能为空");
        }

        basePath=Base_PATH+configuration.getId()+"/"+"define"+"/";
        if (basePath.endsWith("/")) {
            basePath = basePath.substring(0, basePath.length() - 1);
        }

        String paths="";

        for (MultipartFile file : define) {
            String Path = basePath + "/" + file.getOriginalFilename();
            makeDir(Path);
            File dest1 = new File(Path);
            try {
                file.transferTo(dest1);
                paths=paths+Path+";";

            }catch (IOException e){

                ResultGenerator.genFailResult("文件上传失败！");
            }
            }

        configuration.setDefine(paths.substring(0,paths.length()-1));

        int i = configurationService.saveOrUpdate(configuration);

        return i > 0 ? ResultGenerator.genSuccessResult() : ResultGenerator.genFailResult("添加失败！");
    }

    @ApiOperation(value = "更新方案")
    @PutMapping("/update")
    public Result update(@RequestBody @Valid Configuration configuration,
                         @RequestParam(required = false,value = "header") MultipartFile header,
                         @RequestParam(required = false,value = "define")MultipartFile [] define) {
        log.info("ConfigurationController update [{}]", configuration);

        String basePath=Base_PATH+configuration.getId()+"/"+"header"+"/";

        if(header!=null)
        {
            if (basePath.endsWith("/")) {
                basePath = basePath.substring(0, basePath.length() - 1);
            }

            String filePath = basePath + "/" + header.getOriginalFilename();
            makeDir(filePath);
            File dest = new File(filePath);
            try {
                header.transferTo(dest);
                String filename=configuration.getHeader();
                File file=new File(filename);
                if(file.exists()&&file.isFile())
                {
                    if(!file.delete()){
                        ResultGenerator.genFailResult("更新文件失败");
                    }
                }
                configuration.setHeader(filePath);
            }catch (IOException e){

                ResultGenerator.genFailResult("文件上传失败！");
            }
        }


        if (define!=null) {

            basePath=Base_PATH+configuration.getId()+"/"+"define"+"/";
            if (basePath.endsWith("/")) {
                basePath = basePath.substring(0, basePath.length() - 1);
            }

            String paths="";

            for (MultipartFile file : define) {
                String Path = basePath + "/" + file.getOriginalFilename();
                makeDir(Path);
                File dest1 = new File(Path);
                try {
                    file.transferTo(dest1);
                    paths=paths+Path+";";

                }catch (IOException e){

                    ResultGenerator.genFailResult("文件上传失败！");
                }
            }
            /*String prePath=configuration.getDefine();
            String [] prePaths=prePath.split(";");
            String [] newPaths=paths.split(";");
            List<String> list=new ArrayList<String>();
            for(int i=0;i<newPaths.length;i++)
            {
                list.add(newPaths[i]);
            }
            for(int i=0;i<prePaths.length;i++)
            {
                if(!list.contains(prePaths[i]))
                {
                    File file=new File(prePaths[i]);
                    if(file.exists()&&file.isFile())
                    {
                        file.delete();
                    }
                }
            }*/
            configuration.setDefine(paths.substring(0,paths.length()-1));
        }


        int i = configurationService.saveOrUpdate(configuration);
        return i > 0 ? ResultGenerator.genSuccessResult() : ResultGenerator.genFailResult("更新失败！");
    }

    @ApiOperation(value = "删除单个方案")
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable @Valid @NotBlank(message = "配置方案id不能为空") String id) {
        log.info("ConfigurationController delete [{}]", id);
       Configuration con= configurationService.getById(id);
       String header=con.getHeader();
       File file=new File(header);
       if(file.exists()&&file.isFile())
       {
           if(!file.delete())
           {
               return ResultGenerator.genFailResult("删除配置文件失败！");
           }
       }
       String define=con.getDefine();
       String [] define1=define.split(";");
       for(int j=0;j<define1.length;j++)
       {
           File f=new File(define1[j]);
           if(f.exists()&&f.isFile())
           {
               if(!f.delete())
               {
                   return ResultGenerator.genFailResult("删除配置文件失败！");
               }
           }
       }
        int i = configurationService.deleteById(id);
        return i > 0 ? ResultGenerator.genSuccessResult() : ResultGenerator.genFailResult("删除失败！");
    }

    @ApiOperation(value = "设为默认方案")
    @PutMapping("setDefault/{id}")
    public Result setDefault(@PathVariable @Valid @NotBlank(message = "配置方案id不能为空") String id){
        Configuration con=configurationService.getById(id);
        con.setIs_default("1");
        int i=configurationService.saveOrUpdate(con);
        return i > 0 ? ResultGenerator.genSuccessResult() : ResultGenerator.genFailResult("设为默认方案失败！");
    }

    @ApiOperation(value = "取消默认方案")
    @PutMapping("removeDefault/{id}")
    public Result removeDefault(@PathVariable @Valid @NotBlank(message = "配置方案id不能为空") String id){
        Configuration con=configurationService.getById(id);
        con.setIs_default("0");
        int i=configurationService.saveOrUpdate(con);
        return i > 0 ? ResultGenerator.genSuccessResult() : ResultGenerator.genFailResult("取消默认方案失败！");
    }

    @ApiOperation(value = "复制方案")
    @PostMapping("copy/{id}")
    public Result copy(@PathVariable @Valid @NotBlank(message = "配置方案id不能为空") String id){

        Configuration con=configurationService.getById(id);
        con.setIs_common("0");
        con.setIs_default("0");
        con.setId(null);
        int i=configurationService.save(con);
        return i > 0 ? ResultGenerator.genSuccessResult() : ResultGenerator.genFailResult("复制方案失败！");
    }
}