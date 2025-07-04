package study.datajpa.repository;

import org.springframework.beans.factory.annotation.Value;

public interface UsernameOnly {

    //Open Projection 엔티티를 다 가져와서 조회
    //Close Projection은 필요 값만 가져와서 조회
    @Value("#{target.username} + ' '  + target.age}")
    String getUsername();
}
