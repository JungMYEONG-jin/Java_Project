package kakao.getCI.com.shinhan.security.callback;

public interface PasswordListener {

    public boolean CheckPasswordValidation(String password, String authType);
}

