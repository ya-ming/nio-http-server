package com.yaming.http;

import java.nio.ByteBuffer;
import java.util.Date;


/**
 * 封装响应信息
 * @author Administrator
 *
 */
public class Response {
    //两个常量
    public static final String CRLF="\r\n";
    public static final String BLANK=" ";
    //正文
    private StringBuilder content;

    //存储头信息
    private StringBuilder headInfo;
    //存储正文长度
    private int len = 0;
    public Response(){
        headInfo = new StringBuilder();
        content = new StringBuilder();
        len = 0;
    }

    /**
     * 构建正文
     */
    public Response print(String info){
        content.append(info);
        len+=info.getBytes().length;
        return this;
    }

    /**
     * 构建正文+回车
     */
    public Response println(String info){
        content.append(info).append(CRLF);
        len+=(info+CRLF).getBytes().length;
        return this;
    }

    /**
     * 构建响应头
     */
    private void createHeadInfo(int code){
        //1)  HTTP协议版本、状态代码、描述
        headInfo.append("HTTP/1.1").append(BLANK).append(code).append(BLANK);
        switch(code){
            case 200:
                headInfo.append("OK");
                break;
            case 404:
                headInfo.append("NOT FOUND");
                break;
            case 505:
                headInfo.append("SEVER ERROR");
                break;
        }
        headInfo.append(CRLF);
        //2)  响应头(Response Head)
        headInfo.append("Server:HTTPServer /0.0.1").append(CRLF);
        headInfo.append("Date:").append(new Date()).append(CRLF);
        headInfo.append("Content-type:text/html;charset=UTF-8").append(CRLF);
        //正文长度 ：字节长度
        headInfo.append("Content-Length:").append(len).append(CRLF);
        headInfo.append(CRLF); //分隔符
    }
    //推送到客户端
    public ByteBuffer generateResponse(int code) {

        if(null == headInfo){
            code = 500;
        }
        createHeadInfo(code);

        byte[] head = headInfo.toString().getBytes();
        byte[] cont = content.toString().getBytes();

        ByteBuffer byteBuffer = ByteBuffer.allocate(head.length + cont.length);

        try {
            byteBuffer.put(head);
            byteBuffer.put(cont);
            byteBuffer.flip();
        } catch (RuntimeException ex) {
            System.err.println(ex);
        }

        return byteBuffer;
    }
}