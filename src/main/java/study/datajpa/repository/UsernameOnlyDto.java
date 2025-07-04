package study.datajpa.repository;

public class UsernameOnlyDto {
    private final String username;

    //생성자 이름으로 파라미터 매칭해서 Projections 실행
    public UsernameOnlyDto(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
