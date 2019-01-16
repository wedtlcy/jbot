package com.wllfengshu.core.before;

import com.wllfengshu.common.constant.Collective;
import com.wllfengshu.common.utils.FileUtil;
import com.wllfengshu.common.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 负责core开始前的准备工作
 */
public class BeforeHandle {

    private static Logger logger = LoggerFactory.getLogger(BeforeHandle.class);

    public static void start(String projectName, String packageName){
        //1、创建项目名
        createProjectName(projectName);
        //2、创建包
        createPackageName(projectName,packageName);
        //3、复制dockerfile、pom、startup、readme等文件
        copyConfFile(projectName);
        //4、复制resources文件夹
        copyResource(projectName);
        //5、删除resources/mapper目录里的xml文件
        deleteFile(projectName);
    }

    private static void createProjectName(String projectName){
        FileUtil.createDir(Collective.TARGET_PROJECT_HOME+"/"+projectName);
    }

    private static void createPackageName(String projectName,String packageName){
        List<String> packs = StringUtil.stringToList(packageName,"\\.");
        packs.add(projectName);
        String pack="";
        for (String p:packs) {
            FileUtil.createDir(Collective.TARGET_PROJECT_HOME+"/"+projectName+"/src/main/java/"+(pack+=(p+"/")));
        }
    }

    private static void copyConfFile(String projectName){
        String[] fileName={"Dockerfile","pom.xml","README.md","startup.sh"};
        for (String f:fileName) {
            FileUtil.copyFile(Collective.MODEL_PROJECT_HOME+"/"+f,Collective.TARGET_PROJECT_HOME+"/"+projectName+"/"+f);
        }
    }

    private static void copyResource(String projectName){
        FileUtil.copyDir(Collective.MODEL_PROJECT_HOME+"/src/main/resources",Collective.TARGET_PROJECT_HOME+"/"+projectName+"/src/main/resources");
    }

    private static void deleteFile(String projectName){
        FileUtil.deleteFile(Collective.TARGET_PROJECT_HOME+"/"+projectName+"/src/main/resources/mapper/user.xml");
    }
}
