package com.gbdpcloud.controller;

import com.gbdpcloud.entity.Project;
import com.gbdpcloud.entity.ProjectMember;
import com.gbdpcloud.service.ProjectMemberService;
import com.gbdpcloud.service.ProjectService;
import com.gbdpcloud.service.UacUserService;
import gbdpcloudcommonbase.gbdpcloudcommonbase.core.BaseController;
import gbdpcloudcommonbase.gbdpcloudcommonbase.httpResult.Result;
import gbdpcloudcommonbase.gbdpcloudcommonbase.httpResult.ResultGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Api(value = "ProjectController")
@RequestMapping("/ProjectController")
@Validated
@RestController
public class ProjectController extends BaseController {

    @Resource
    private ProjectService projectService;

    @Resource
    private UacUserService uacUserService;

    @Resource
    private ProjectMemberService projectMemberService;

    @Resource
    private MasterController masterController;


    @ApiOperation(value = "添加项目")
    @PostMapping("/add")
    public Result save(@RequestBody @Valid Project project) {
        log.info("projectController save [{}]", project);
        int ii=projectService.save(project);
        if(ii<=0){
            return ResultGenerator.genFailResult("添加失败,项目添加失败");
        }
        Project p =projectService.getOne(project);
        project.setId(p.getId());
        List<String> members=project.getMembers();
        ProjectMember projectMember=new ProjectMember();
        projectMember.setProject_ID(project.getId());
        for(int i=0;i<members.size();i++){
            projectMember.setMember_ID(members.get(i));
            int j=projectMemberService.save(projectMember);
            if(j<=0){
                return ResultGenerator.genFailResult("添加失败,项目成员添加失败");
            }
        }
        projectMember.setMember_ID(uacUserService.getByName(project.getLeader()).getId());
        int j=projectMemberService.save(projectMember);
        if(j<=0){
            return ResultGenerator.genFailResult("添加失败,项目组长添加失败");
        }

        return  ResultGenerator.genSuccessResult();
    }

    @ApiOperation(value = "编辑项目")
    @PutMapping("/update")
    public Result update(@RequestBody @Valid Project project) {
        log.info("projectController update [{}]", project);
        Project pro=projectService.getById(project.getId());
        List<ProjectMember> list=projectMemberService.getByProject(pro.getId());
        List<String> members=project.getMembers();
        for(int i=0;i<list.size();i++)
        {
            ProjectMember p=list.get(i);
            if(!members.contains(p.getMember_ID())) {
                list.remove(p);
                int j = projectMemberService.deleteById(p.getId());
                if (j <= 0) {
                    return ResultGenerator.genFailResult("删除项目成员失败");
                }
            }
        }
        List<String> pre_members=new ArrayList<String>();
        for(int i=0;i<list.size();i++)
        {
            pre_members.add(list.get(i).getMember_ID());
        }
        ProjectMember p=new ProjectMember();
        p.setProject_ID(pro.getId());
        for(int i=0;i<members.size();i++) {
            if(!pre_members.contains(members.get(i)))
            {
                p.setMember_ID(members.get(i));
                int j = projectMemberService.saveOrUpdate(p);
                if (j <= 0) {
                    return ResultGenerator.genFailResult("添加项目成员失败");
                }
            }

        }
        int i = projectService.update(project);
        return i > 0 ? ResultGenerator.genSuccessResult() : ResultGenerator.genFailResult("更新项目信息失败！");
    }

    @ApiOperation(value = "删除项目")
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable @Valid @NotBlank(message = "项目标识不能为空") String id) {
        log.info("projectController delete [{}]", id);
        int i = projectService.deleteById(id);
        return i > 0 ? ResultGenerator.genSuccessResult() : ResultGenerator.genFailResult("删除失败！");
    }
}
