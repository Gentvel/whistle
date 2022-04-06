package com.whistle.code.main.test;

import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.BCUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.SM2;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.crypto.engines.SM2Engine;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;

/**
 * @author Lin
 * @version 1.0.0
 */
@Slf4j
public class HutoolTest {
    @Test
    public void testStr(){
        String s ="02223444111";
        log.info(StrUtil.subPre(s,1));
    }

    @Test
    public void testSm(){
        KeyPair pair = SecureUtil.generateKeyPair("SM2");
        SM2 sm2 = new SM2(pair.getPrivate(),pair.getPublic());
        String publicKey = StrUtil.utf8Str(sm2.getPublicKey());
        String privateKey = StrUtil.utf8Str(sm2.getPrivateKey());
        log.info("public key :{}",publicKey);
        log.info("private key :{}",privateKey);
        String hello_world = sm2.encryptBcd("hello world", KeyType.PublicKey);
        log.info("encrypt: {}",hello_world);
        byte[] bytes = sm2.decryptFromBcd(hello_world, KeyType.PrivateKey, StandardCharsets.UTF_8);
        log.info("decrypt:{}",StrUtil.utf8Str(bytes));



    }
    
    @Test
    public void testSm2(){
        SM2 sm = SmUtil.sm2();

        // sm2的加解密时有两种方式即 C1C2C3、 C1C3C2，
        sm.setMode(SM2Engine.Mode.C1C2C3);
        sm.usePlainEncoding();

        // 生成私钥
        String privateKey = HexUtil.encodeHexStr(sm.getPrivateKey().getEncoded());
        //log.info("私钥: {}", privateKey);
        // 生成公钥
        String publicKey = HexUtil.encodeHexStr(sm.getPublicKey().getEncoded());
        //log.info("公钥: {}", publicKey);

        // 生成私钥D
        String privateKeyD = HexUtil.encodeHexStr(BCUtil.encodeECPrivateKey(sm.getPrivateKey())); // ((BCECPrivateKey) privateKey).getD().toByteArray();
        log.info("私钥D: {}", privateKeyD);

        // 生成公钥Q，以q值做为js端的加密公钥
        String publicKeyQ = HexUtil.encodeHexStr(((BCECPublicKey) sm.getPublicKey()).getQ().getEncoded(false));
        log.info("公钥Q: {}", publicKeyQ);

    }


    /**
     * 指定私钥签名测试
     * <i scr="https://i.goto327.top/CryptTools/SM2.aspx?tdsourcetag=s_pctim_aiomsg">秘钥验证</i>
     */
    @Test
    public void signTest() {
        //指定的私钥
        String privateKeyHex = "7daef7bafff1311eab392d9ec9fc510a200c286e1c9ca51236cb879d229bf4d7";
        //需要加密的明文,得到明文对应的字节数组
        byte[] dataBytes = "我是一段测试aaaa".getBytes();
        ECPrivateKeyParameters privateKeyParameters = BCUtil.toSm2Params(privateKeyHex);
        //创建sm2 对象
        SM2 sm2 = new SM2(privateKeyParameters, null);
        //这里需要手动设置，sm2 对象的默认值与我们期望的不一致 , 使用明文编码
        sm2.usePlainEncoding();
        sm2.setMode(SM2Engine.Mode.C1C2C3);
        byte[] sign = sm2.sign(dataBytes, null);
        String test = "04BA9D9E16F8739D71641D077910500BD854940B5A9B753D93405809509FEBE2C00037261745488D000F662A69FB5FD007AA7AEAD00B5C5CA803A634492741581D6CC6D8BA01CEE83FD16DCD5E58B9412F856EB0F501B12B846378F2766B147691C57FEED7711BA81CDE1D969CE197C4DF165CE03C45";
        String s = StrUtil.utf8Str(sm2.decryptFromBcd(test, KeyType.PrivateKey));
        log.info(s);
        System.out.println("数据: " + HexUtil.encodeHexStr(dataBytes));
        System.out.println("签名: " + HexUtil.encodeHexStr(sign));
    }

    /**
     * 指定私钥签名测试
     * <i scr="https://i.goto327.top/CryptTools/SM2.aspx?tdsourcetag=s_pctim_aiomsg">秘钥验证</i>
     */
    @Test
    public void verifyTest() {
        //指定的公钥
        String publicKeyHex = "04f61cdc197fca6049d40a026079000dd3b781656b7f1428fb592c18fe473aa94b6876bb0f250a1dda3d2257fde19a88f527b89766c957a1e40be6b9822262c0ed";
        //需要加密的明文,得到明文对应的字节数组
        byte[] dataBytes = "我是一段测试aaaa".getBytes();
        //签名值
        String signHex = "af3b994d8026e54218d82741f01b6ad5d9cab63b07bdc0fb520e16d1995a44b7e0c6ae6134542ff6f599f29c0ed8e299c900424c1e58fcc2de3b4c07a3b4a11b";
        //这里需要根据公钥的长度进行加工
        if (publicKeyHex.length() == 130) {
            //这里需要去掉开始第一个字节 第一个字节表示标记
            publicKeyHex = publicKeyHex.substring(2);
        }
        String xhex = publicKeyHex.substring(0, 64);
        String yhex = publicKeyHex.substring(64, 128);
        ECPublicKeyParameters ecPublicKeyParameters = BCUtil.toSm2Params(xhex, yhex);
        //创建sm2 对象
        SM2 sm2 = new SM2(null, ecPublicKeyParameters);
        //这里需要手动设置，sm2 对象的默认值与我们期望的不一致 , 使用明文编码
        sm2.usePlainEncoding();
        sm2.setMode(SM2Engine.Mode.C1C2C3);
        boolean verify = sm2.verify(dataBytes, HexUtil.decodeHex(signHex));
        String test = sm2.encryptBcd("测试加解密嘻43523fwfsdf4354sdf嘻7978978978978", KeyType.PublicKey);
        log.info(test);
        System.out.println("数据: " + HexUtil.encodeHexStr(dataBytes));
        System.out.println("验签结果: " + verify);
    }
    @Test
    public void testEncryptLength(){
        String code ="04D237D6E45B348F49BBB42BB3DF3E28C29B6A075D82775534CFFEDE2C1B88427BE8A7C570FB32BCD82E3BF5135B9DAB079A819CE63F5A8E1A780F92E23AF9D6B8EBA5F830E9EF0B6097E4E049E07D31EA7949829ED176828E3027BA8B7C5DDBE964F6F13CC22E9F6FF9522783633CB297EBDC9A1D621DAC2FA3486CED9DF36B919D0A6467776FF42D4843CEF752E5EF8E8DDE66CB";
        System.out.println(code.length());
    }
}
