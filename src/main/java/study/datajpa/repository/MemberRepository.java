package study.datajpa.repository;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom, JpaSpecificationExecutor<Member> {

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    List<Member> findHelloBy();

    List<Member> findTop3HelloBy();

    //    @Query(name = "Member.findByUsername") //없어도 동작
    //먼저 Type의 Member엔티티명을 가지고 findByUsername을 먼저 찾아서 가져옴
    //2번째로 쿼리메서드를 찾음
    //@Param("username") Named 쿼리 안에 :username이 있는경우에 작성
    List<Member> findByUsername(@Param("username") String username);

    //애플리케이션 로딩시 에러 체크됨
    @Query("select m from Member m where m.username = :username  and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") List<String> names);


    List<Member> findListByUsername(String username); //컬렉션

    Member findMemberByUsername(String username); //단건

    Optional<Member> findOptionalMemberByUsername(String username); //단건 Optional


    Page<Member> findByAge(int age, Pageable pageable);

    Slice<Member> findSliceByAge(int age, Pageable pageable);

    @Query(value = "select m from Member m left join m.team t",
            countQuery = "select count(m) from Member m")
    Page<Member> findCountDetachByAge(int age, Pageable pageable);


    //    @Modifying //없으면 안됨 update 쿼리로 인식 안함 //영속성 엔티티 관리 안됨
    @Modifying(clearAutomatically = true) //없으면 안됨 update 쿼리로 인식 안함 //영속성 엔티티 관리 안됨
    //em.flush(), em.clear() 안해줘도 됨(clearAutomatically = true)
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);


    @Query("select m from Member m left join fetch m.team")
    List<Member> findMemberFetchJoin();

    //엔티티 그래프 -> 내부적으로fetchJoin
    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    //JPQL + 엔티티 그래프 (JPQL에 fetch join 추가)
    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

    //사용자 정의 메서드
    @EntityGraph(attributePaths = {"team"})
    List<Member> findEntityGraphByUsername(@Param("username") String username);


    //NamedEntityGraph
    @EntityGraph("Member.all")
    List<Member> findNamedEntityGraphByUsername(@Param("username") String username);

    //하이버네이트 제공
    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByUsername(String username);

    //select for update
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUsername(String username);

    //select for update
    @Lock(LockModeType.OPTIMISTIC)
    List<Member> findOptimisticLockLockByUsername(String username);
}
