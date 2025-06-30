package study.datajpa.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    List<Member> findHelloBy();
    List<Member> findTop3HelloBy();

//    @Query(name = "Member.findByUsername") //없어도 동작
    //먼저 Type의 Member엔티티명을 가지고 findByUsername을 먼저 찾아서 가져옴
    //2번째로 쿼리메서드를 찾음
    //@Param("username") Named 쿼리 안에 :username이 있는경우에 작성
    List<Member> findByUsername(@Param("username") String username);
}
