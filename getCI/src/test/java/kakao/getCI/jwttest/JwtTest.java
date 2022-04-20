package kakao.getCI.jwttest;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jose.util.Base64URL;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.ReadOnlyJWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.apache.tomcat.util.codec.binary.Base64;
import org.assertj.core.api.Assertions;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import sun.security.ec.ECPrivateKeyImpl;

import java.io.FileReader;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.interfaces.ECPrivateKey;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;

@SpringBootTest
public class JwtTest {

    public static final String keyPath = "static/apple/AuthKey_FXNMXR3KNH.p8";
    
    @Test
    public void TokenTest() throws ParseException {
        JWSHeader jwsHeader = new JWSHeader.Builder(JWSAlgorithm.ES256).type(JOSEObjectType.JWT).build();


        System.out.println("jwsHeader = " + jwsHeader.toBase64URL().toString());
        String encHeader = jwsHeader.toBase64URL().toString();

        byte[] bytes = Base64.decodeBase64URLSafe(encHeader);

        String str = new String(bytes);
        System.out.println("str = " + str);



        JWTClaimsSet claimsSet = new JWTClaimsSet();
        claimsSet.setSubject("1234567890");
        claimsSet.setClaim("name", "John Doe");
        claimsSet.setClaim("admin", true);
        claimsSet.setIssuer("com.shinhan.com");
        claimsSet.setIssueTime(new Date(1516239122));

        SignedJWT jwt = new SignedJWT(jwsHeader, claimsSet);

        ReadOnlyJWTClaimsSet jwtClaimsSet = jwt.getJWTClaimsSet();
        Map<String, Object> allClaims = jwtClaimsSet.getAllClaims();
        for (String s : allClaims.keySet()) {
            System.out.println(s +" "+ allClaims.get(s));
        }



        try{
            ECPrivateKey ecPrivateKey = new ECPrivateKeyImpl(readPrivateKey(keyPath));
            JWSSigner jwsSigner = new ECDSASigner(ecPrivateKey.getS());
            jwt.sign(jwsSigner);
            
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (JOSEException e) {
            e.printStackTrace();
        }

        System.out.println("jwt.serialize() = " + jwt.serialize());


    }


    private byte[] readPrivateKey(String keyPath)
    {
        Resource resource = new ClassPathResource(keyPath);
        byte[] content = null;

        try(FileReader keyReader = new FileReader(resource.getFile());
            PemReader pemReader = new PemReader(keyReader))
        {
            PemObject pemObject = pemReader.readPemObject();
            content = pemObject.getContent();

        }catch(IOException e)
        {

        }
        return content;
    }
    
}
