package com.example.demo.tools;


import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.SecureUtil;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 19833
 * Date: 2022-09-29
 * Time: 8:55
 */
public class Write {
    public void write(){
        KeyPair pair = SecureUtil.generateKeyPair("RSA");
        PrivateKey aPrivate = pair.getPrivate();
        PublicKey aPublic = pair.getPublic();


        writeDetails(Base64.encode(aPrivate.getEncoded()),Base64.encode(aPublic.getEncoded()));
    }

    public void writeDetails(String pri,String pub){
        System.out.println(pri);
        System.out.println(pub);
        try(
                Writer writerPri = new FileWriter("f:/private.txt");
                Writer writerPub = new FileWriter("f:/public.txt");
                ){

            writerPri.write(pri);
            writerPub.write(pub);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Write write = new Write();
        write.write();
    }
}
