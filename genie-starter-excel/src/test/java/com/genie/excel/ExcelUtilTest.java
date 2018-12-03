package com.genie.excel;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Map;

public class ExcelUtilTest {

    @Test
    public void main() {
        ExcelUtil excelUtil = new ExcelUtil();

        //读取excel数据
        ArrayList<Map<String, String>> result = excelUtil.readExcelToObj("C:\\Users\\handsome\\Desktop\\软文excel\\表\\表\\稿件免费媒体投放链接---上传表.xlsx");
        for (Map<String, String> map : result) {
            System.out.println(map);
        }

    }

}
