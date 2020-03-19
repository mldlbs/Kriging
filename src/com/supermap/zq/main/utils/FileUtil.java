package com.supermap.zq.main.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 操作文件类
 */
public class FileUtil {

    /**
     * 解析micaps4格点数据
     * @return
     * @throws Exception
     */
    public static Map<String,Object> Micaps4(String filePath) {
        List<Double> list = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        String[] variables = new String[]{"refTime","dx","dy","lo1","lo2","la1","la2","nx","ny"};
        try {
            InputStream in = FileUtil.class.getClassLoader().getResourceAsStream(filePath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));// 换成你的文件名
            String line;
            AtomicInteger index= new AtomicInteger(1);
            while ((line = reader.readLine()) != null) {
                if(index.get() == 2){
                    String item[] = line.split("\\s+");	// CSV格式文件为逗号分隔符文件，这里根据逗号切分
                    map.put("refTime", item[0]+"-"+item[1]+"-"+item[2]);
                    for (int i = 6; i < item.length; i++) {
                        if(!"".equals(item[i])){
                            map.put(variables[i-5],Double.parseDouble(item[i]));
                        }
                    }
                }else if (index.get() > 2) {
                    System.out.println(line);
                    String item[] = line.split("\\s+");	// CSV格式文件为逗号分隔符文件，这里根据逗号切分
                    for (int i = 0; i < item.length; i++) {
                        if(!"".equals(item[i])){
                            list.add(Double.parseDouble(item[i]));
                        }
                    }
                }
                index.getAndIncrement();
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        map.put("Data",list);
        return map;
    }
}
