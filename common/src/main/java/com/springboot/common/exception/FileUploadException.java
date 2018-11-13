package com.springboot.common.exception;

/**
 * @program: education-parent
 * @description: 上传文件异常类
 * @author: Leslie
 * @create: 2018-07-19 13:49
 **/
public class FileUploadException extends RuntimeException{


    private static final long serialVersionUID = -277606186479990878L;
    private String message;
    public FileUploadException(){

    }
    public FileUploadException(String message){
        super(message);
        this.message=message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
