package com.springboot.common.utill;

import com.jcraft.jsch.*;
import com.springboot.common.base.Constant;
import com.springboot.common.exception.FileUploadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * @program: education-parent
 * @description: 用户资源上传
 * @author: Leslie
 * @create: 2018-07-19 15:40
 **/
public class FileUploadUtil {
    private static Logger log=LoggerFactory.getLogger(FileUploadUtil.class);
    private static ResourceBundle bundle = ResourceBundle.getBundle("config/constant");
    private static String fileUploadPath =bundle.getString("file-upload.dir");
    private static String host=bundle.getString("file-upload.url");
    private static String username=bundle.getString("file-upload.user-name");
    private static String password=bundle.getString("file-upload.user-password");
    private static String port=bundle.getString("file-upload.port");
    private static String privateKey=bundle.getString("file-upload.private-key");

    private ChannelSftp sftp;

    private Session session;

    public ChannelSftp getSftp() {
        return sftp;
    }

    public void setSftp(ChannelSftp sftp) {
        this.sftp = sftp;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public void login(){
        try {
            JSch jsch = new JSch();
            if (StringUtils.isNotBlank(privateKey)) {
                // 设置私钥
                jsch.addIdentity(privateKey);
                log.info("sftp connect,path of private key file：{}" , privateKey);
            }
            log.info("sftp connect by host:{} username:{}",host,username);
            Integer defaultPort=22;
            if (StringUtils.isNotBlank(port)){
                defaultPort=Integer.parseInt(port);
            }
            session = jsch.getSession(username, host, defaultPort);
            log.info("Session is build");
            if (StringUtils.isNotBlank(password)) {
                session.setPassword(password);
            }
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();
            log.info("Session is connected");
            Channel channel = session.openChannel("sftp");
            channel.connect();
            log.info("channel is connected");
            sftp = (ChannelSftp) channel;
            log.info(String.format("sftp server host:[%s] port:[%s] is connect successfull", host, port));
        } catch (JSchException e) {
            log.error("Cannot connect to specified sftp server : {}:{} \n Exception message is: {}", host, port, e.getMessage());
            throw new FileUploadException("上传文件发生异常");
        }
    }
    /**
     * 关闭连接 server
     */
    public void logout(){
        if (sftp != null) {
            if (sftp.isConnected()) {
                sftp.disconnect();
                log.info("sftp is closed already");
            }
        }
        if (session != null) {
            if (session.isConnected()) {
                session.disconnect();
                log.info("sshSession is closed already");
            }
        }
    }
    public static StringBuilder upload(Integer fileType,String originaFileName,MultipartFile file){
        //获取当前时间
        Date now=new Date();
        String directory= fileName(now,fileType);
        StringBuilder builder=new StringBuilder(directory);
        //服务器固定的文件夹
        StringBuilder pathName=new StringBuilder(fileUploadPath);
        pathName.append(directory);
        directory=pathName.toString();
        File targetFile=new File(new File(directory).getAbsolutePath()+"/"+originaFileName);
        if (!targetFile.getParentFile().exists()){
            targetFile.getParentFile().mkdirs();
        }
        try {
            file.transferTo(targetFile);
        } catch (IOException e) {
            e.printStackTrace();
            throw new FileUploadException("上传文件失败");
        }
        builder.append("/").append(originaFileName);
        return builder;
    }
    /**
     * 输出流数据进行上传
     * @param fileType
     * @param originaFileName
     * @param input
     * @throws SftpException
     */
    public  StringBuilder upload( Integer fileType, String originaFileName,InputStream input)   {
        login();
        //获取当前时间
        Date now=new Date();
        String directory= fileName(now,fileType);
        StringBuilder builder=new StringBuilder(directory);
        //服务器固定的文件夹
        StringBuilder pathName=new StringBuilder(fileUploadPath);
        StringBuilder name=new StringBuilder(originaFileName);
        pathName.append(directory);
        directory=pathName.toString();
        try {
            sftp.cd(directory);
        } catch (SftpException e) {
            log.warn("directory is not exist");
            try {
                createDir(directory,sftp);
                sftp.cd(directory);
            }catch (SftpException sf){
                logout();
                log.error("创建文件夹失败{}",sf);
                throw new FileUploadException("上传文件发生异常");
            }
        }
        try {
            sftp.put(input, name.toString());
            log.info("file:{} is upload successful" , name.toString());
            input.close();
            logout();
            builder.append("/").append(name);
        }catch (Exception e){
            throw new FileUploadException("上传文件发生异常");
        }
       return builder;
    }


    public static String fileName(Date time, int type) {
        StringBuffer str = new StringBuffer();
        switch (type){
            case Constant.FileType.FILE_IMG:
                str.append(Constant.FileType.FILE_IMG_DIR);
                break;
            case Constant.FileType.FILE_ZIP:
                str.append(Constant.FileType.FILE_ZIP_DIR);
                break;
            case Constant.FileType.FILE_VEDIO:
                str.append(Constant.FileType.FILE_VEDIO_DIR);
                break;
            case Constant.FileType.FILE_APK:
                str.append(Constant.FileType.FILE_APK_DIR);
                break;
            case Constant.FileType.FILE_EXCEL:
                str.append(Constant.FileType.FILE_EXCEL_DIR);
                break;
            case Constant.FileType.FILE_PPT:
                str.append(Constant.FileType.FILE_PPT_DIR);
                break;
            case Constant.FileType.FILE_PDF:
                str.append(Constant.FileType.FILE_PDF_DIR);
                break;
                default:
                  str.append("/others") ;
        }
        str.append(DateTimeUtil.formatDatetoString(time)).append("/").append(time.getTime());
        return str.toString();
    }
    /**
     * 创建目录,支持多级创建
     * @param fullPathName 全路径，如/x/y/z
     */
    public void createDir(String fullPathName,ChannelSftp cs) {

        if(fullPathName.startsWith("/")) {
            fullPathName=fullPathName.substring(1);
        }
        if(fullPathName.endsWith("/")) {
            fullPathName=fullPathName.substring(0,fullPathName.lastIndexOf("/"));
        }
        fullPathName.replace("//", "/");
        if(!isDir(fullPathName,cs)){
            String[] path=fullPathName.split("/");
            String base="/";
            for(int i=0;i<path.length-1;i++){
                base+=path[i]+"/";
                createDir(base,path[i+1],cs);
            }
        }
    }
    /**
     * 是否是目录
     * @param dir
     * @return
     */
    public boolean isDir(String dir,ChannelSftp cs){
        try {
            cs.cd(dir);
            cs.cd("..");
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    /**
     * 创建目录
     * @param parent
     * @param dir
     * @throws SftpException
     */
    public void createDir(String parent,String dir,ChannelSftp cs) {
        try {
            if(!isDir(parent,cs)){
                cs.mkdir(parent);
            }else {
                cs.cd(parent);
                cs.mkdir(dir);
            }
        }catch (SftpException e){
            log.warn("创建文件夹发生异常");
        }
    }
}
