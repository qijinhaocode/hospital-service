package com.qi.hospital.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.concurrent.CopyOnWriteArrayList;


@Log4j2
public class ExcelUtils {
    public static void writeExcel(String fileName , Class head,
                                  HttpServletResponse response, CopyOnWriteArrayList list) {
        try {
            ServletOutputStream outputStream = getOutputStream(fileName, response);
            ExcelWriterBuilder writeBook = EasyExcel.write(outputStream, head);
            ExcelWriterSheetBuilder sheet = writeBook.sheet(fileName);
            sheet.doWrite(list);
        } catch (Exception e) {
            log.error("导出excel表格失败:", e);
        }
    }

    /**
     * 导出文件时为Writer生成OutputStream.
     *
     * @param fileName 文件名
     * @param response response
     * @return ""
     */
    private static ServletOutputStream getOutputStream(String fileName,
                                                       HttpServletResponse response) throws Exception {
        try {
            fileName = URLEncoder.encode(fileName, "UTF-8");
            //设置响应的类型
            response.setContentType("application/vnd.ms-excel");
            //设置响应的编码格式
            response.setCharacterEncoding("utf8");
            //设置响应头
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
            response.setHeader("Pragma", "public");
            response.setHeader("Cache-Control", "no-store");
            response.addHeader("Cache-Control", "max-age=0");
            return response.getOutputStream();
        } catch (IOException e) {
            log.error("导出excel表格失败:", e);
            throw new Exception("导出excel表格失败!", e);
        }
    }
}
