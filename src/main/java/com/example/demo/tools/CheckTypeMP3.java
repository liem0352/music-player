package com.example.demo.tools;

import java.io.*;
/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 19833
 * Date: 2022-10-09
 * Time: 16:31
 */

/**
 * 用来检查文件是否是 mp3 类型
 */
public class CheckTypeMP3 {

    /**
     * 是 MP3 就返回 true, 不是就返回 false
     */
    public boolean checkTypeMP3(File path){

        byte[] bytes = new byte[128];

        try(
                RandomAccessFile randomAccessFile = new RandomAccessFile(path,"r");
                ){

            // 长度不够不可能是 正常的mp3 文件
            if(randomAccessFile.length() <= 128){
                return false;
            }
            // 将指针指向 ID3V1 中 128 个字节的 第一个字节处
            randomAccessFile.seek(randomAccessFile.length()-128);
            // 读取
            randomAccessFile.read(bytes,0,3);

            if("TAG".equalsIgnoreCase(new String(bytes,0,3))){
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
