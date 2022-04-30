package com.kovatech.auth.core.security;

import com.kovatech.auth.core.config.WsBasicAuthProperties;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Component
public class WsEncoder {

    private final WsBasicAuthProperties authProperties;
    public WsEncoder(WsBasicAuthProperties authProperties) {
        this.authProperties = authProperties;
    }

    public static byte[] getRandomInitialVector() {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            SecureRandom randomSecureRandom = SecureRandom.getInstance("SHA1PRNG");
            byte[] initVector = new byte[cipher.getBlockSize()];
            randomSecureRandom.nextBytes(initVector);
            return initVector;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static byte[] passwordTo16BitKey(String password) {
        try {
            byte[] srcBytes = password.getBytes("UTF-8");
            byte[] dstBytes = new byte[16];

            if (srcBytes.length == 16) {
                return srcBytes;
            }

            if (srcBytes.length < 16) {
                for (int i = 0; i < dstBytes.length; i++) {
                    dstBytes[i] = (byte) ((srcBytes[i % srcBytes.length]) * (srcBytes[(i + 1) % srcBytes.length]));
                }
            } else if (srcBytes.length > 16) {
                for (int i = 0; i < srcBytes.length; i++) {
                    dstBytes[i % dstBytes.length] += srcBytes[i];
                }
            }

            return dstBytes;
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static String encrypt(String key, String value) {
        return encrypt(passwordTo16BitKey(key), value);
    }

    public static String encrypt(byte[] key, String value) {
        try {
            byte[] initVector = WsEncoder.getRandomInitialVector();
            IvParameterSpec iv = new IvParameterSpec(initVector);
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());
            return Base64.getEncoder().encodeToString(encrypted) + " " + Base64.getEncoder().encodeToString(initVector);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static String decrypt(String key, String encrypted) {
        return decrypt(passwordTo16BitKey(key), encrypted);
    }

    public static String decrypt(byte[] key, String encrypted) {
        try {
            String[] encryptedParts = encrypted.split(" ");
            byte[] initVector = Base64.getDecoder().decode(encryptedParts[1]);
            if (initVector.length != 16) {
                return null;
            }

            IvParameterSpec iv = new IvParameterSpec(initVector);
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(Base64.getDecoder().decode(encryptedParts[0]));

            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public String loginDecrypt(String password) {
        String saltKey = authProperties.getStaticIv();
        return decrypt(saltKey,password);

    }

    public String loginEncrypt(String password) {
        String saltKey = authProperties.getStaticIv();
        return encrypt(saltKey,password);
    }
}
