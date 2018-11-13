package com.springboot.common.base;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Leslie
 */
public class Constant {

    public static final int BYTE_BUFFER = 1024;

    public static Set<String>  METHOD_URL_SET = new HashSet<>();

    /**
     * 用户注册默认角色
     */
    public static final int DEFAULT_REGISTER_ROLE = 5;

    public static final int BUFFER_MULTIPLE = 10;

    //验证码过期时间
    public static final Long PASS_TIME =  50000 * 60 *1000L;
    /**
     * 普通用户
     */
    public static final String ORDINARY_ROLE= "user";

    /**
     * 系统管理员
     */
    public static final String SYS_ASMIN_ROLE= "admin";

    //根菜单节点
    public static final String ROOT_MENU = "0";

    //菜单类型，1：菜单  2：按钮操作
    public static final int TYPE_MENU = 1;

    //菜单类型，1：菜单  2：按钮操作
    public static final int TYPE_BUTTON = 2;

    //启用
    public static final int ENABLE = 1;
    //禁用
    public static final int DISABLE = 0;

    public static class FilePostFix{
        public static final String ZIP_FILE =".zip";
        //图片
        public static final String [] IMAGES ={"jpg", "jpeg", "JPG", "JPEG", "gif", "GIF", "bmp", "BMP", "png"};
        //压缩包
        public static final String [] ZIP ={"ZIP","zip","rar","RAR"};
        //视频
        public static final String [] VIDEO ={"mp4","MP4","mpg","mpe","mpa","m15","m1v", "mp2","rmvb"};
        //安装包
        public static final String [] APK ={"apk","exe"};
        /*//文档
        public static final String [] OFFICE ={"xls","xlsx","docx","doc","ppt","pptx"};*/
        //表格
        public static  final String[] EXCEL={"xls","xlsx"};
        //ppt
        public static final String[] PPT={"ppt","pptx"};
        //文档
        public static final String[] PDF={"docx","doc","PDF"};
    }
    public class FileType{
        public static final int FILE_IMG = 22;
        public static final int FILE_ZIP = 2;
        public static final int FILE_VEDIO= 3;
        public static final int FILE_APK = 4;
        public static final int FILE_EXCEL = 19;
        public static final int FILE_PPT = 20;
        public static final int FILE_PDF = 21;
        public static final String FILE_IMG_DIR= "/img/";
        public static final String FILE_ZIP_DIR= "/zip/";
        public static final String FILE_VEDIO_DIR= "/video/";
        public static final String FILE_APK_DIR= "/apk/";
        public static final String FILE_EXCEL_DIR= "/excel/";
        public static final String FILE_PPT_DIR= "/ppt/";
        public static final String FILE_PDF_DIR= "/pdf/";
    }


}
