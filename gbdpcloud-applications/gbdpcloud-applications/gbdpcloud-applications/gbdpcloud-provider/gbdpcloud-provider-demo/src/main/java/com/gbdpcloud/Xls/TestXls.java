package com.gbdpcloud.Xls;

import com.alibaba.excel.annotation.ExcelProperty;
import com.gbdpcloud.entity.ResultErr;
import lombok.Data;

/*
<dependency>
     <groupId>com.alibaba</groupId>
     <artifactId>easyexcel</artifactId>
     <version>1.1.2-beta5</version>
</dependency>
 */

@Data
public class TestXls {
    @ExcelProperty("编号")
    private String id;

    @ExcelProperty("标记")
    private String sta;

    @ExcelProperty("问题类型")
    private String type;

    @ExcelProperty("对比结果")
    private String compR;

    @ExcelProperty("源文件")
    private String sor;

    @ExcelProperty("函数")
    private String fun;

    @ExcelProperty("代码行号")
    private String line;

    @ExcelProperty("批注")
    private String com;

    public TestXls(){}

    public TestXls(ResultErr err){
        id = err.getId();
        sta = err.getStatus();
        type = err.getType();
        compR = err.getCompResult();
        sor = err.getSource();
        fun = err.getFunction();
        line = err.getLine();
    }

}
