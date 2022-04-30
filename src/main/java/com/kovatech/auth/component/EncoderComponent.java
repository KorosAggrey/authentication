package com.kovatech.auth.component;

import com.kovatech.auth.core.config.WsBasicAuthProperties;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Component
public class EncoderComponent {
    private final WsBasicAuthProperties authProperties;
    private final static int ITERATION_COUNT = 5;

    private EncoderComponent(WsBasicAuthProperties authProperties) {
        this.authProperties = authProperties;
    }

    public String encode(String password, String saltKey) {
        String encodedPassword = null;
        byte[] salt = new byte[0];
        try {
            salt = base64ToByte(saltKey);
        } catch (IOException e) {
            e.printStackTrace();
        }

        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        digest.reset();
        digest.update(salt);

        byte[] btPass = new byte[0];
        try {
            btPass = digest.digest(password.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < ITERATION_COUNT; i++) {
            digest.reset();
            btPass = digest.digest(btPass);
        }

        encodedPassword = byteToBase64(btPass);
        return encodedPassword;
    }

    private byte[] base64ToByte(String str) throws IOException {
        //BASE64Decoder decoder = new BASE64Decoder();
        //byte[] returnbyteArray = decoder.decodeBuffer(str);
        //return returnbyteArray;
        byte[] base64DecodedData = Base64.getDecoder().decode(str);
        System.out.println(base64DecodedData +" base64DecodedData");
        return  base64DecodedData;
    }

    private String byteToBase64(byte[] bt) {
        /*BASE64Encoder endecoder = new BASE64Encoder();
        String returnString = endecoder.encode(bt);
        return returnString;*/
        String encryptedData= Base64.getEncoder().encodeToString(bt);
        System.out.println(encryptedData +" encryptedData");
        return encryptedData;
    }

    public String loginDecrypt(String password) {
        String saltKey = authProperties.getStaticIv();
        return encode(password,saltKey);
    }
}
