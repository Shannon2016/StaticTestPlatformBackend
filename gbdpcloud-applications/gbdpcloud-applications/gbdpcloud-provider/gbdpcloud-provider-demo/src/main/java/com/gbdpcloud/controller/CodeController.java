package com.gbdpcloud.controller;

import com.gbdpcloud.entity.Code;
import com.gbdpcloud.entity.CodeVersion;
import com.gbdpcloud.service.CodeService;
import com.gbdpcloud.service.CodeVersionService;
import gbdpcloudcommonbase.gbdpcloudcommonbase.core.BaseController;
import gbdpcloudcommonbase.gbdpcloudcommonbase.httpResult.Result;
import gbdpcloudcommonbase.gbdpcloudcommonbase.httpResult.ResultGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Api(value = "CodeController")
@RequestMapping("/CodeController")
@Validated
@RestController
public class CodeController extends BaseController {

    public String BASE_PATH="F:/Project/";
    @Resource
    private CodeService codeService;

    @Resource
    private CodeVersionService codeVersionService;

    private static void makeDir(String filePath) {
        if (filePath.lastIndexOf('/') > 0) {
            String dirPath = filePath.substring(0, filePath.lastIndexOf('/'));
            File dir = new File(dirPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
        }
    }


    @ApiOperation(value = "上传代码")
    @PostMapping("/add")
    public Result addCode(@RequestParam(value = "project_id") String project_id,
                          @RequestParam(value = "version") String version,
                          @RequestParam(value = "code") MultipartFile[] files){

        List<CodeVersion> list=codeVersionService.getByProject(project_id);
        int is_exist=0;
        for(int i=0;i<list.size();i++)
        {
            if(list.get(i).getVersion().equals(version))
            {
                is_exist=1;
                break;
            }
        }
        if(is_exist==0)
        {
            CodeVersion codeVersion=new CodeVersion();
            codeVersion.setProject_ID(project_id);
            codeVersion.setVersion(version);
            codeVersionService.save(codeVersion);
        }

        String basePath=BASE_PATH+project_id+"/"+version+"/";

        if (files == null || files.length == 0) {
            return ResultGenerator.genFailResult("上传文件不能为空");
        }

        CodeVersion model=new CodeVersion();
        model.setVersion(version);
        model.setProject_ID(project_id);
        CodeVersion codeVersion=codeVersionService.getOne(model);
        if (basePath.endsWith("/")) {
            basePath = basePath.substring(0, basePath.length() - 1);
        }

        for (MultipartFile file : files) {
            String filePath = basePath + "/" + file.getOriginalFilename();
            makeDir(filePath);
            File dest = new File(filePath);
            try {
                file.transferTo(dest);
                if(dest.isFile())
                {
                    Code code=new Code();
                    code.setProject_ID(project_id);
                    code.setCode_version_ID(codeVersion.getId());
                    code.setVersion(version);
                    code.setPath(filePath);
                    code.setName(filePath.substring(filePath.lastIndexOf("/")+1));
                    int i=codeService.save(code);
                    if(i<=0){
                        return ResultGenerator.genFailResult("文件写入数据库失败");
                    }

                }
            } catch (IllegalStateException | IOException e) {
                e.printStackTrace();
                return ResultGenerator.genFailResult("文件上传失败");
            }
        }
        return ResultGenerator.genSuccessResult();
    }


}
