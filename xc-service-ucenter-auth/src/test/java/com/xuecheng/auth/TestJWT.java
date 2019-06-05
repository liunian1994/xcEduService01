package com.xuecheng.auth;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestJWT {

    //生成一个jwt令牌
    @Test
    public void testCreateJwt(){
        //证书文件
        String key_location = "xc.keystore";
        //密钥库密码
        String keystore_password = "xuechengkeystore";
        //访问证书路径
        ClassPathResource resource = new ClassPathResource(key_location);
        //密钥工厂
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(resource,
                keystore_password.toCharArray());
        //密钥的密码，此密码和别名要匹配
        String keypassword = "xuecheng";
        //密钥别名
        String alias = "xckey";
        //密钥对（密钥和公钥）
        KeyPair keyPair = keyStoreKeyFactory.getKeyPair(alias, keypassword.toCharArray());
        //私钥
        RSAPrivateKey aPrivate = (RSAPrivateKey) keyPair.getPrivate();
        //定义payload信息
        Map<String, Object> tokenMap = new HashMap<>();
        tokenMap.put("id", "123");
        tokenMap.put("name", "mrt");
        tokenMap.put("roles", "r01,r02");
        tokenMap.put("ext", "1");
        //生成jwt令牌
        Jwt jwt = JwtHelper.encode(JSON.toJSONString(tokenMap), new RsaSigner(aPrivate));
        //取出jwt令牌
        String token = jwt.getEncoded();
        System.out.println("token="+token);
    }

    //资源服务使用公钥验证jwt的合法性，并对jwt解码
    @Test
    public void testVerify(){
        //jwt令牌
        String token
                = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJjb21wYW55SWQiOm51bGwsInVzZXJwaWMiOm51bGwsInVzZXJfbmFtZSI6InN1cGVyIiwic2NvcGUiOlsiYXBwIl0sIm5hbWUiOiLotoXnuqfnrqHnkIblkZgiLCJ1dHlwZSI6bnVsbCwiaWQiOiI0NiIsImV4cCI6MTU1OTUwOTc2OSwiYXV0aG9yaXRpZXMiOlsieGNfc3lzbWFuYWdlcl9kb2MiLCJ4Y19zeXNtYW5hZ2VyX3JvbGUiLCJ4Y19zeXNtYW5hZ2VyX3VzZXJfdmlldyIsInhjX3N5c21hbmFnZXJfcm9sZV9lZGl0IiwieGNfc3lzbWFuYWdlcl91c2VyX2FkZCIsInhjX3N5c21hbmFnZXJfbWVudSIsInhjX3N5c21hbmFnZXJfY29tcGFueSIsInhjX3N5c21hbmFnZXJfdXNlcl9kZWxldGUiLCJ4Y19zeXNtYW5hZ2VyX3JvbGVfYWRkIiwieGNfc3lzbWFuYWdlcl9yb2xlX3Blcm1pc3Npb24iLCJ4Y19zeXNtYW5hZ2VyX3VzZXIiLCJ4Y19zeXNtYW5hZ2VyIiwieGNfc3lzbWFuYWdlcl9sb2ciLCJ4Y19zeXNtYW5hZ2VyX3VzZXJfZWRpdCIsInhjX3N5c21hbmFnZXJfbWVudV9lZGl0IiwieGNfc3lzbWFuYWdlcl9tZW51X2FkZCIsInhjX3N5c21hbmFnZXJfbWVudV9kZWxldGUiLCJ4Y19zeXNtYW5hZ2VyX3JvbGVfZGVsZXRlIl0sImp0aSI6Ijg3OThhMDY3LThjMWUtNGMxYi04OGYxLWIyOGFjZjM2NmE0ZSIsImNsaWVudF9pZCI6IlhjV2ViQXBwIn0.HQVeBTTPSkZMnQRuuioCYpNjr23ZbN3a2dz-eTsKFQwqGstDK764KNPYjhVNEa1jrQvC1JCj4jI9JXqcnddW5zF1x2Tv5qfnNda5h7ulEjE95SnrViiSyvIDWLg9uZAs4-aW_TsHwQ6A4R_4Ab6wnhqTIdNMpnaBhW3ztNKL3y7EkEyR8CqBL3BwhN66VO2jDOhPx7hfC6obAFmT3Ks0fe_41OHj8IRN-C2NbctqKSPq0QbFGTvkXpd8_qjszeVAkj8RwndCmKYD-1Ve4QqpsZJQaGRF9hSkJpOMkDWKPq-TQtgGzSYIDCYUB4s7leI2sGHezDWVQdsK0AKYcAKOSQ";
        //公钥
        String publickey = "-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnASXh9oSvLRLxk901HANYM6KcYMzX8vFPnH/To2R+SrUVw1O9rEX6m1+rIaMzrEKPm12qPjVq3HMXDbRdUaJEXsB7NgGrAhepYAdJnYMizdltLdGsbfyjITUCOvzZ/QgM1M4INPMD+Ce859xse06jnOkCUzinZmasxrmgNV3Db1GtpyHIiGVUY0lSO1Frr9m5dpemylaT0BV3UwTQWVW9ljm6yR3dBncOdDENumT5tGbaDVyClV0FEB1XdSKd7VjiDCDbUAUbDTG1fm3K9sx7kO1uMGElbXLgMfboJ963HEJcU01km7BmFntqI5liyKheX+HBUCD4zbYNPw236U+7QIDAQAB-----END PUBLIC KEY-----";
        //校验jwt
        Jwt jwt = JwtHelper.decodeAndVerify(token, new RsaVerifier(publickey));
        //获取jwt原始内容
        String claims = jwt.getClaims();
        System.out.println(claims);
        //jwt令牌
        String encoded = jwt.getEncoded();
        System.out.println(encoded);
    }
    
}
