package com.gbdpcloud.controller;

import com.gbdpcloud.entity.*;
import com.gbdpcloud.service.*;
import gbdpcloudcommonbase.gbdpcloudcommonbase.core.BaseController;
import gbdpcloudcommonbase.gbdpcloudcommonbase.dto.UacUserDto;
import gbdpcloudcommonbase.gbdpcloudcommonbase.httpResult.Result;
import gbdpcloudcommonbase.gbdpcloudcommonbase.httpResult.ResultGenerator;
import gbdpcloudcommonbase.gbdpcloudcommonbase.security.UacUserUtils;
import gbdpcloudprovideruserapi.gbdpcloudprovideruserapi.model.*;
import gbdpcloudprovideruserapi.gbdpcloudprovideruserapi.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.*;

@Api(value = "MasterController")
@RequestMapping("")
@Validated
@RestController
public class MasterController extends BaseController {

    @Resource
    private CodeService codeService;

    @Resource
    private ConfigurationService configurationService;

    @Resource
    private ProjectService projectService;

    @Resource
    private ResultService resultService;

    @Resource
    private ResultErrService resultErrService;

    @Resource
    private TestService testService;

    @Resource
    private ToolDeploymentInfoService toolDeploymentInfoService;

    @Resource
    private UserLogService userLogService;

    @Resource
    private UacUserService uacUserService;

    @Resource
    private UacOfficeService uacOfficeService;

    @Resource
    private UacUserOfficeService uacUserOfficeService;

    @Resource
    private ProjectMemberService projectMemberService;

    @Resource
    private CodeVersionService codeVersionService;

    @Resource
    private OfficeService officeService;



    @ApiOperation(value = "我的项目-首页")
    @GetMapping("/project/index")
    @CrossOrigin(origins = {"http://localhost:9527", "null"})
    public Result project_index() {
        UacUserDto user= UacUserUtils.getUserInfoFromRequest();
        /*
        List<Project> list1 = projectService.selectByMember(user.getLogin_name());
        List<Project> list2 = projectService.selectByLeader(user.getLogin_name());
        for(int i=0;i<list2.size();i++)
        {
            Project project=list2.get(i);
            int mark=0;
            for(int j=0;j<list1.size();j++){
                if(project.getID()==list1.get(j).getID())
                {
                    mark=1;
                    break;
                }
            }
            if(mark==0){
                list1.add(project);
            }
        }
                 */
        List<ProjectMember> list=projectMemberService.getByMember(user.getId());
        System.out.println(list.size());

        String ids="";
        for(int i=0;i<list.size();i++){
            String project_ID=list.get(i).getProject_ID();
           ids+=project_ID+",";
        }
        ids=ids.substring(0,ids.length()-1);
        List<Project> list1=projectService.selectByIds(ids);

        System.out.println("项目个数为："+list1.size());

        List<String> office_ids= UacUserUtils.getOfficeId(user);

        List<Project> deleteList = new ArrayList<Project>();
        for(int i=0;i<list1.size();i++)
        {
            Project project=list1.get(i);
            int delete_mark=0;
           String range=project.getRanges();
           String [] strs=range.split(",");
           for(int j=0;j<strs.length;j++)
           {
               if(office_ids.contains(strs[j]) ||strs[j].equals("project")){
                   delete_mark=1;
                   break;
               }
           }
           if(delete_mark==0){
               deleteList.add(project);
           }
        }
        list1.removeAll(deleteList);
        for(int i=0;i<list1.size();i++)
        {
            Project p=list1.get(i);
            List<String> version=new ArrayList<String>();
            List<CodeVersion> codes=codeVersionService.getByProject(p.getId());
            for(int j=0;j<codes.size();j++)
            {
                version.add(codes.get(i).getVersion());
            }
            p.setVersions(version);
            list1.set(i,p);
        }

        return ResultGenerator.genSuccessResult(list1);
    }

    @ApiOperation(value = "我的项目-基础信息")
    @GetMapping("/project/projectInfo/{id}")
    @CrossOrigin(origins = {"http://localhost:9527", "null"})
    public Result project_projectInfo(@PathVariable @Valid @NotBlank(message = "项目标识不能为空") String id){
        Project project = projectService.getById(id);
        String [] list= project.getMember().split(",");
        Map<String,String> memberMap=new HashMap<>();
        for(int i=0;i<list.length;i++){
            String member_ID=list[i];
            String member=uacUserService.getById(member_ID).getLoginName();
            memberMap.put(member,member_ID);
        }
        project.setMemberMap(memberMap);

        /*
        Project pro=project.get(0);
        for(int i=0;i<project.size();i++)
        {
            String member=project.get(i).getMember();
            String memberID=uacUserService.getByName(member).getId();
            List<String> members=pro.getMembers();
            Map<String,String> memberMap=pro.getMemberMap();
            members.add(member);
            memberMap.put(member,memberID);
            pro.setMembers(members);
            pro.setMemberMap(memberMap);
        }*/
        UacOffice uac=new UacOffice();
        UacUser user=new UacUser();
        List<UacOffice> officeList=uacOfficeService.listAll();
        List<Object> lists=new ArrayList<>();
        List<UacUser> uacUserList=uacUserService.list(user);
        lists.add(project);
        lists.add(officeList);
        lists.add(uacUserList);
        return ResultGenerator.genSuccessResult(lists);
    }

    @ApiOperation(value = "我的项目-新建项目")
    @GetMapping("/project/add")
    @CrossOrigin(origins = {"http://localhost:9527", "null"})
    public Result project_add(UacUser uacUser, UacOffice uacOffice){

        List<Object> lists=new ArrayList<>();
        List<UacUser> list1=uacUserService.list(uacUser);
        List<UacOffice> list2=uacOfficeService.list(uacOffice);
        lists.add(list1);
        lists.add(list2);

        return ResultGenerator.genSuccessResult(lists);
    }

    @ApiOperation(value = "系统配置")
    @GetMapping("/systemConfig/index")
    @CrossOrigin(origins = {"http://localhost:9527", "null"})
    public Result systemConfig_index(ToolDeploymentInfo tools){
        List<ToolDeploymentInfo> list=toolDeploymentInfoService.list(tools);
        for(int i=0;i<list.size();i++)
        {
            ToolDeploymentInfo tool=list.get(i);
            String hostname=tool.getHost();
            Integer port=tool.getPort();
            try{
            Socket connect = new Socket();
            connect.connect(new InetSocketAddress(hostname, port),100);
            boolean res = connect.isConnected();
            tool.setState("正常");
            }catch (IOException exception){
                tool.setState("连接异常");
            }finally {
                list.set(i,tool);
            }


        }
        return ResultGenerator.genSuccessResult(list);
    }

    @GetMapping("/systemLog/index")
    @ApiOperation(value = "系统日志")
    @CrossOrigin(origins = {"http://localhost:9527", "null"})
    public Result systemLog_index(UserLog userLog){
        List<UserLog> list=userLogService.list(userLog);
        return ResultGenerator.genSuccessResult(list);
    }

    @ApiOperation(value = "配置管理")
    @GetMapping("/configmgmt/index")
    @CrossOrigin(origins = {"http://localhost:9527", "null"})
    public Result configmgmt_index(){
        List<Configuration> list=configurationService.getCommon();
        return ResultGenerator.genSuccessResult(list);
    }

    @ApiOperation(value = "用户管理-部门管理")
    @GetMapping("/usermgmt/orgmgmt")
    @CrossOrigin(origins = {"http://localhost:9527", "null"})
    public Result usermgmt_orgmgmt(UacOffice uacOffice){
        List<UacOffice> list=uacOfficeService.list(uacOffice);
        return ResultGenerator.genSuccessResult(list);
    }

    @ApiOperation(value = "用户管理-首页")
    @GetMapping("/usermgmt/index")
    @CrossOrigin(origins = {"http://localhost:9527", "null"})
    public Result usermgmt_index(UacUser uacUser){
        List<UacUser> list=uacUserService.list(uacUser);
        for(int i=0;i<list.size();i++){
            UacUser user=list.get(i);
            String user_id=user.getId();
            UacUserOffice uacUserOffice=uacUserOfficeService.getByUserId(user_id);
            String office_id=uacUserOffice.getOfficeId();
            UacOffice uacOffice=uacOfficeService.getById(office_id);
            String office_name=uacOffice.getName();
            user.setUacOffice(uacOffice);
            list.set(i,uacUser);
        }
        return ResultGenerator.genSuccessResult(list);
    }

    @ApiOperation(value = "我的项目-分析结果")
    @GetMapping("/project/analysisResult/{id}")
    @CrossOrigin(origins = {"http://localhost:9527", "null"})
    public Result project_analysisResult(@PathVariable @Valid @NotBlank(message = "项目标识不能为空") String id){
        //TODO 状态码更新
        List<Test> list=testService.getByProject(id);
        List<Configuration> commonList=configurationService.getCommon();
        String user_id= UacUserUtils.getUserInfoFromRequest().getId();
        List<Configuration> privateList=configurationService.getPrivate(user_id);
        List<Object> lists=new ArrayList<Object>();
        List<CodeVersion> codeVersionList=codeVersionService.getByProject(id);
        lists.add(list);
        lists.add(commonList);
        lists.add(privateList);
        lists.add(codeVersionList);
        return ResultGenerator.genSuccessResult(lists);
    }

    @ApiOperation(value = "我的项目-查看分析")
    @GetMapping("/project/resultErr/{id}")
    @CrossOrigin(origins = {"http://localhost:9527", "null"})
    public Result project_resultErr(@PathVariable @Valid @NotBlank(message = "测试计划标识不能为空") String id){
        Test t=testService.getById(id);
        String code_version=t.getCode_version();
        String project_id=t.getProject_ID();
        List<Code> codes=codeService.getByProjectAndVersion(project_id,code_version);
        List<ResultErr> results=resultErrService.getByTest(id);
        List<Object> lists=new ArrayList<Object>();
        lists.add(results);
        lists.add(codes);
        return ResultGenerator.genSuccessResult(lists);
    }

    @ApiOperation(value = "我的项目-代码管理")
    @GetMapping("/project/codemgmt/{id}")
    @CrossOrigin(origins = {"http://localhost:9527", "null"})
    public Result project_codemgmt(@PathVariable @Valid @NotBlank(message = "项目标识不能为空") String id){

        List<CodeVersion> list=codeVersionService.getByProject(id);
        /*
        List<Code> list=codeService.getByProject(id);
        List<List<Code>> lists=new ArrayList<List<Code>>();
        for(int i=0;i<list.size();i++)
        {
            Code code=list.get(i);
            int type_mark=0;
            for(int j=0;j<lists.size();j++)
            {
                if(code.getVersion().equals(lists.get(j).get(0).getVersion())){
                    lists.get(j).add(code);
                    type_mark=1;
                    break;
                }
            }
            if(type_mark==0)
            {
                List<Code> temp=new ArrayList<Code>();
                temp.add(code);
                lists.add(temp);
            }
        }
        Map<String,Date> result=new HashMap<String, Date>();
        for(int i=0;i<lists.size();i++)
        {
            Date time=lists.get(i).get(0).getCreateDate();
            String version=lists.get(i).get(0).getVersion();
            List<Code> temp=lists.get(i);
            for(int j=0;j<temp.size();j++){
                if(temp.get(j).getCreateDate().before(time)){
                    time=temp.get(j).getCreateDate();
                }
            }

            result.put(version,time);
        }*/
        return ResultGenerator.genSuccessResult(list);


    }

    @ApiOperation("我的项目-代码浏览")
    @GetMapping("/project/codeView/{id}")
    @CrossOrigin(origins = {"http://localhost:9527", "null"})
    public Result project_codeView(@PathVariable @Valid @NotBlank(message = "代码工程id不能为空") String id){
        //TODO:代码目录展示
        List<Code> list=codeService.getByCodeVersion(id);
        return ResultGenerator.genSuccessResult(list);
    }

  /*
  0813 add by ld
 */
    /*
    第一个会从前端拿两个test_id，根据test_id可以获取两个测试的所有resulterr,Resulterr具体的属性可以看实体类
    第二个根据报错所在的代码id拿取Code，通过path取得文件，读取文件内容返回，根据报错所在行号返回代码行号。
    第三个写一下对ResultErr的各种排序
    主要用到testservice,codeService,resulterrService,resulterrController
我是提醒你别用那个类，直接从test_id拿到List<ResultErr>
     */
    /*@ApiOperation("我的项目-分析结果-对比分析")
    @GetMapping("/project/com/{oid}/{nid}")
    public Result com(@PathVariable @Valid String oid, @PathVariable @Valid  String nid){
        List<ResultErr> old = resultErrService.getByTest(oid);
        List<ResultErr> n = resultErrService.getByTest(nid);
        comp(old,n);
        return ResultGenerator.genSuccessResult(n);
    }
    @ApiOperation("分析结果的对比")
    public void comp(List<ResultErr> old,List<ResultErr> n){
        for (ResultErr r :n){
            boolean flag = false;
            for (ResultErr o:old){
                if(r.getSource().equals(o.getSource()) && r.getFunction().equals(o.getFunction())
                        && r.getRule().equals(o.getRule())  && r.getType().equals(r.getType())){
                    r.setCompVsersion(o.getTest_ID());
                    r.setCompLine(o.getLine());
                    r.setCompResult(getCompR(r,o));
                    flag = true;
                }
            }
            if(!flag){
                r.setCompResult("新增");
                r.setCompVsersion("--");
                r.setCompLine("--");
            }
        }
    }

    @ApiOperation("获得对比结果的Xls")
    @GetMapping("/project/comXls/{oid}/{nid}")
    public void getCompXls(@PathVariable @Valid String oid, @PathVariable @Valid  String nid,HttpServletResponse response) throws IOException {
        List<ResultErr> old = resultErrService.getByTest(oid);
        List<ResultErr> n = resultErrService.getByTest(nid);
        comp(old,n);

        List<TestCompXls> xlslist = new ArrayList<TestCompXls>();
        for (int i =0;i<n.size();i++){
            xlslist.add(new TestCompXls(n.get(i)));
        }
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("测试", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), TestCompXls.class).sheet("模板").doWrite(xlslist);
    }

    @ApiOperation("获得两个错误的对比结果")
    private String getCompR(ResultErr r, ResultErr o) {
        return "消除/不同";
    }


    @PostMapping("/project/getCode")
    @ApiOperation("我的项目-分析结果-对比分析Code")
    public Result getCode(@RequestParam @Valid String pid, @RequestParam @Valid String version1,
                          @RequestParam @Valid String version2, @RequestParam @Valid String name){
        Code code1 = codeService.getByProjectVserionAndName(pid,version1,name);
        Code code2 = codeService.getByProjectVserionAndName(pid,version2,name);
        try {
            code1.setContent(readFile(code1.getPath()));
            code2.setContent(readFile(code2.getPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Code> list = new ArrayList<Code>();
        list.add(code1);
        list.add(code2);
        return ResultGenerator.genSuccessResult(list);
    }


    @ApiOperation("文件转String")
    public String readFile(String path) throws IOException {
        File file = new File(path);
        FileReader fileReader = new FileReader(file);
        BufferedReader br = new BufferedReader(fileReader);
        StringBuilder sb = new StringBuilder();
        String temp = "";
        while ((temp = br.readLine()) != null) {
            sb.append(temp + "\n");
        }
        br.close();
        return sb.toString();
    }
    @GetMapping("/project/testFile")
    public Result testFile(){
         Code code = new Code();
         code.setPath("D://1.txt");
        try {
            code.setContent(readFile(code.getPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Code> list = new ArrayList<Code>();
        list.add(code);
        return ResultGenerator.genSuccessResult(list);
    }*/

}

