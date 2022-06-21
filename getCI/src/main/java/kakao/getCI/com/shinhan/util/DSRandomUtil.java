package kakao.getCI.com.shinhan.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

public class DSRandomUtil {

    private SecureRandom mSecureRandom = null;
    private Random mRandom = null;

    public DSRandomUtil() {
        try {
            if (getJavaVersion() > 1.7f) {
                mSecureRandom = SecureRandom.getInstance("NativePRNGNonBlocking");
            } else {
                //java 설정에서 urandom 설정 권장!!!
                mSecureRandom = SecureRandom.getInstance("SHA1PRNG");
            }
        } catch (NoSuchAlgorithmException e) {
            mRandom = new Random();
        }
    }
    public void nextBytes(byte[] arr) {
        if (mSecureRandom != null) {
            mSecureRandom.nextBytes(arr);
        } else if (mRandom != null) {
            mRandom.nextBytes(arr);
        }
    }

    public byte[] generateSeed(int numBytes) {

        if (mSecureRandom != null) {

            if (getJavaVersion() > 1.7f) {

                return mSecureRandom.generateSeed(numBytes);

            } else {

                byte[] arr = new byte[numBytes];
                mSecureRandom.nextBytes(arr);
                return arr;

            }

        } else if (mRandom != null) {

            byte[] arr = new byte[numBytes];
            mRandom.nextBytes(arr);
            return arr;

        } else {

            Random random = new Random();
            byte[] arr = new byte[numBytes];
            random.nextBytes(arr);
            return arr;

        }
    }

    public SecureRandom getmSecureRandom() {
        return mSecureRandom;
    }

    public Random getmRandom() {
        return mRandom;
    }

    private float getJavaVersion() {

        String version = System.getProperty("java.specification.version");
        return Float.parseFloat(version);

    }

}
