package kakao.getCI;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jwt.JWTClaimsSet;
import kakao.getCI.apple.AppleController;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.FileReader;
import java.io.IOException;
import java.util.Date;

public class Test {

    public static final String issuer_Id = "66UW797TRG";
    public static final String keyId = "FXNMXR3KNH";
    public static final String keyPath = "static/apple/AuthKey_FXNMXR3KNH.p8";

    public static void main(String[] args) {




    }
}
