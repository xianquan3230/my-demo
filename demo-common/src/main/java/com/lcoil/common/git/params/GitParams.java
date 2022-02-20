package com.lcoil.common.git.params;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.io.File;

/**
 * git参数类
 *
 * @author lcoil
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class GitParams {

    /**
     * 任务id
     */
    private Long taskId;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 密码
     */
    private String passWord;
    /**
     * git地址
     */
    private String gitUrl;
    /**
     * 分支名称
     */
    private String branch = "master";

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 本地保存路径
     */
    public static String LOCAL_PATH = "C:\\project\\code-help\\";

    public static String getLocalPath() {
        String osName = System.getProperty("os.name");
        if (!osName.contains("Windows")) {
            LOCAL_PATH = "code-path" + File.separator;
        }
        return LOCAL_PATH;
    }


    public String getProjectName() {
        if (StringUtils.isNotEmpty(this.gitUrl)) {
            this.projectName = this.gitUrl.substring(this.gitUrl.lastIndexOf("/") + 1, this.gitUrl.indexOf(".git"));
        }
        return projectName;
    }

}