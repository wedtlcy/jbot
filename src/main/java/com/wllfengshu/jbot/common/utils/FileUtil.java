package com.wllfengshu.jbot.common.utils;

import com.wllfengshu.jbot.common.exception.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件工具类
 * @author wllfengshu
 */
public class FileUtil {

    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);

    /**
     * 把文件读到List中
     *       注意：1、一行只允许一个字符串；
     *             2、以#开头的字符串将被忽略；
     * @param fileName
     * @return
     */
    public static List<String> readFile2Set(String fileName)throws CustomException {
        logger.info("开始把文件读到List中，fileName:{}",fileName);
        List<String> items = new ArrayList<>();
        Resource resource = new ClassPathResource(fileName);
        if (!resource.exists()){
            logger.error("把文件读到List中时，资源文件不存在");
            return items;
        }
        InputStream input = null;
        Reader reader = null;
        BufferedReader br = null;
        try {
            input = resource.getInputStream();
            reader = new InputStreamReader(input, "UTF-8");
            br = new BufferedReader(reader);
            String temp = null;
            while (null != (temp = br.readLine())) {
                if (null!=temp && !"".equals(temp) && !"\n".equals(temp) && !"\r\n".equals(temp) && !temp.startsWith("#")) {
                    items.add(temp);
                }
            }
        }catch (Exception e){
            logger.error("把文件读到List中时异常",e);
        }finally{
            try {
                if (br != null){
                    br.close();
                }
                if (reader != null){
                    reader.close();
                }
                if (input != null){
                    input.close();
                }
            }catch (Exception e){
                logger.error("把文件读到List中时异常 finally",e);
            }
        }
        logger.info("文件读到List中完毕");
        return items;
    }

    /**
     * 创建文件
     * @param fileName
     * @return
     */
    public static void createFile(String fileName,String content) throws CustomException {
        File file = new File(fileName);
        FileWriter fw = null;
        BufferedWriter bw = null;
        try{
            if (file.getParentFile()!=null && !file.getParentFile().exists()){
                file.getParentFile().mkdirs();
            }
            if (!file.exists()){
                file.createNewFile();
            }
            fw = new FileWriter(file.getAbsoluteFile());
            bw = new BufferedWriter(fw);
            bw.write(content);
        } catch (Exception e) {
            logger.error("创建文件异常",e);
        }finally {
            try{
                if (bw != null){
                    bw.close();
                }
                if (fw != null){
                    fw.close();
                }
            }catch (Exception e){
                logger.error("创建文件异常",e);
            }
        }
    }

    /**
     * 创建目录
     * @param destDirName
     * @return
     */
    public static void createDir(String destDirName) throws CustomException {
        File dir = new File(destDirName);
        if (dir.exists() && dir.isDirectory()) {
            return;
        }
        dir.mkdirs();
    }

    /**
     * 复制文件
     * @param oldPath
     * @param newPath
     */
    public static void copyFile(String oldPath, String newPath) throws CustomException {
        logger.info("开始复制文件，oldPath:{},newPath:{}",oldPath,newPath);
        Resource resource = new ClassPathResource(oldPath);
        if (!resource.exists()){
            logger.error("复制文件时，资源文件不存在");
            return;
        }
        InputStream input = null;
        FileOutputStream out = null;
        try {
            File file = new File(newPath);
            if (file.getParentFile()!=null && !file.getParentFile().exists()){
                file.getParentFile().mkdirs();
            }
            if (!file.exists()){
                file.createNewFile();
            }
            input = resource.getInputStream();
            out = new FileOutputStream(file);
            byte[] buffer = new byte[2048];
            int readByte = 0;
            while ((readByte = input.read(buffer)) != -1) {
                out.write(buffer, 0, readByte);
            }
        }catch (Exception e){
            logger.error("复制文件异常",e);
        }finally {
            try {
                if (out != null){
                    out.close();
                }
                if (input != null){
                    input.close();
                }
            }catch (Exception e){
                logger.error("复制文件异常",e);
            }
        }
    }

    /**
     * 删除一个文件。
     * @param filename
     */
    public static void deleteFile(String filename)throws CustomException {
        File file = new File(filename);
        if (file.isDirectory()) {
            return;
        }
        if (!file.exists()) {
            return;
        }
        file.delete();
    }

    /**
     * 删除文件夹及其下面的子文件夹
     * @param dir
     */
    public static void deleteDir(File dir)throws CustomException {
        if (dir.isFile()){
            return;
        }
        File[] files = dir.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                if (file.isFile()) {
                    file.delete();
                } else {
                    deleteDir(file);
                }
            }
        }
        dir.delete();
    }
}