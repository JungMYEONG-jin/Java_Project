package study.datajpa.repository;

public class UsernameOnlyDto {

    private final String username;

    // constructor is important
    // 생성자의 인자 이름에 맞는 값으로 반환해줌
    public UsernameOnlyDto(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
