package com.qbk.easypoidemo.test;

import cn.afterturn.easypoi.word.WordExportUtil;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

public class Test {
    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "qbk");
        map.put("date", "2020-05-18");
        try(
                XWPFDocument doc = WordExportUtil.exportWord07("C:/Users/qbk/Desktop/import.docx", map);
                FileOutputStream fos = new FileOutputStream("C:/Users/qbk/Desktop/export.docx");
        ){
            doc.write(fos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
