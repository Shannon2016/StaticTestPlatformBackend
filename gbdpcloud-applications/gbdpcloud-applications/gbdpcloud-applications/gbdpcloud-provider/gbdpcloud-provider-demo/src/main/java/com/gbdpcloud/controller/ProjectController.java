package com.gbdpcloud.controller;

import com.gbdpcloud.entity.Project;
import com.gbdpcloud.entity.ProjectMember;
import com.gbdpcloud.service.ProjectMemberService;
import com.gbdpcloud.service.ProjectService;
import com.gbdpcloud.service.UacUserService;
import gbdpcloudcommonbase.gbdpcloudcommonbase.core.BaseController;
import gbdpcloudcommonbase.gbdpcloudcommonbase.httpResult.Result;
import gbdpcloudcommonbase.gbdpcloudcommonbase.httpResult.ResultGenerator;
import gbdpcloudcommonbase.gbdpcloudcommonbase.security.UacUserUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

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
        project.setLeader(UacUserUtils.getUserInfoFromRequest().getLoginName());
        project.setId(UUID.randomUUID().toString());
        project.setDelFlag("0");
        int ii=projectService.save(project);
        if(ii<=0){
            return ResultGenerator.genFailResult("添加失败,项目添加失败");
        }
        String member=project.getMember();
        String []members=member.split(",");
        List<ProjectMember> projectMemberList=new ArrayList<ProjectMember>();
        for(int i=0;i<members.length;i++){
            ProjectMember projectMember=new ProjectMember();
            projectMember.setProject_ID(project.getId());
            projectMember.setMember_ID(members[i]);
            projectMemberList.add(projectMember);
        }
        int j=projectMemberService.saveBatch(projectMemberList);
        if(j<=0){
            return ResultGenerator.genFailResult("添加失败,项目成员添加失败");
        }
        ProjectMember projectMember=new ProjectMember();
        projectMember.setProject_ID(project.getId());
        projectMember.setMember_ID(UacUserUtils.getUserInfoFromRequest().getId());
        int i=projectMemberService.save(projectMember);
        if(i<=0){
            return ResultGenerator.genFailResult("添加失败,项目组长添加失败");
        }
        return  ResultGenerator.genSuccessResult();
    }

    @ApiOperation(value = "编辑项目")
    @PutMapping("/update")
    public Result update(@RequestBody @Valid Project project,@RequestParam(value = "user_id") String user_id) {
        log.info("projectController update [{}]", project);
        String preMember=projectService.selectById(project.getId()).getMember();
        String newMember=project.getMember();
        if(preMember.equals("")&&!newMember.equals(""))
        {
            String [] newMemberList=newMember.split(",");
            List<ProjectMember> list1=new ArrayList<>();
            for(int i=0;i<newMemberList.length;i++)
            {
                ProjectMember member=new ProjectMember();
                member.setMember_ID(newMemberList[i]);
                member.setProject_ID(project.getId());
                list1.add(member);
            }

            if(list1.size()!=0) {
                int j1 = projectMemberService.saveBatch(list1);
                if (j1 <= 0) {
                    return ResultGenerator.genFailResult("添加项目成员失败");
                }
            }

        }else if(newMember.equals("")&&!preMember.equals(""))
        {
            List<ProjectMember> list=projectMemberService.getByProject(project.getId());
            List<String> deleteIDs=new ArrayList<String>();
            for(int i=0;i<list.size();i++)
            {
                deleteIDs.add(list.get(i).getId());
            }
            if(deleteIDs.size()!=0) {
                int j = projectMemberService.deleteIds(deleteIDs);
                if (j <= 0) {
                    return ResultGenerator.genFailResult("删除项目成员失败");
                }
            }
        }else if(!newMember.equals(preMember))
        {
            List<ProjectMember> list=projectMemberService.getByProject(project.getId());
            String [] newMembers=newMember.split(",");
            String [] preMembers=preMember.split(",");
            List<String> newMemberList=new ArrayList<>();
            List<String> preMemberList=new ArrayList<>();
            newMemberList.addAll(Arrays.asList(newMembers));
            preMemberList.addAll(Arrays.asList(preMembers));
            List<String> deleteIDs=new ArrayList<String>();
            List<ProjectMember> projectMemberList=new ArrayList<>();

            for(int i=0;i<list.size();i++)
            {
                ProjectMember p=list.get(i);
                if(!newMember.contains(p.getMember_ID())&&!p.getMember_ID().equals(user_id)) {
                    deleteIDs.add(p.getId());
                }
            }
            if(deleteIDs.size()!=0) {
                int j = projectMemberService.deleteIds(deleteIDs);
                if (j <= 0) {
                    return ResultGenerator.genFailResult("删除项目成员失败");
                }
            }

            for(int i=0;i<newMemberList.size();i++)
            {
                if(!preMemberList.contains(newMemberList.get(i)))
                {
                    ProjectMember p=new ProjectMember();
                    p.setProject_ID(project.getId());
                    p.setMember_ID(newMemberList.get(i));
                    projectMemberList.add(p);
                }
            }
            if(projectMemberList.size()!=0) {
            int j1 = projectMemberService.saveBatch(projectMemberList);
            if (j1 <= 0) {
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
