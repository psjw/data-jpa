package study.datajpa.repository;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberRepositoryTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TeamRepository teamRepository;

    @Test
    public void testMember() {
        System.out.println("memberRepository.getClass() = " + memberRepository.getClass());
        Member member = new Member("memberA");
        Member savedMember = memberRepository.save(member);

        Member findMember = memberRepository.findById(savedMember.getId()).get();

        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
        Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        Assertions.assertThat(findMember).isEqualTo(member);
    }


    @Test
    public void basicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        //단건 조회 검증
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();
        Assertions.assertThat(findMember1).isEqualTo(member1);
        Assertions.assertThat(findMember2).isEqualTo(member2);

//        findMember1.setUsername("member!!!!!!");

        //리스트 조회 검증
        List<Member> all = memberRepository.findAll();
        Assertions.assertThat(all).hasSize(2);

        //카운트 검증
        long count = memberRepository.count();
        Assertions.assertThat(count).isEqualTo(2);

        //삭제검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);

        long deleteCount = memberRepository.count();
        Assertions.assertThat(deleteCount).isEqualTo(0);
    }

    @Test
    public void findByUsernameAdnAgeGreaterThen() {
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("AAA", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);

        Assertions.assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        Assertions.assertThat(result.get(0).getAge()).isEqualTo(20);
        Assertions.assertThat(result).hasSize(1);
    }


    @Test
    public void findHelloBy() {
        List<Member> result = memberRepository.findHelloBy();
    }


    @Test
    public void findTop3HelloBy() {
        List<Member> result = memberRepository.findTop3HelloBy();
    }


    @Test
    public void testNamedQuery(){
        Member member1 = new Member("AAA",10);
        Member member2 = new Member("BBB",20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> result = memberRepository.findByUsername("AAA");

        Member findMember = result.get(0);
        Assertions.assertThat(findMember).isEqualTo(member1);
    }


    @Test
    public void testQuery(){
        Member member1 = new Member("AAA",10);
        Member member2 = new Member("BBB",20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> result = memberRepository.findUser("AAA", 10);

        Member findMember = result.get(0);
        Assertions.assertThat(findMember).isEqualTo(member1);
    }


    @Test
    public void findUsernameList(){
        Member member1 = new Member("AAA",10);
        Member member2 = new Member("BBB",20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<String> result = memberRepository.findUsernameList();

        for (String s : result) {
            System.out.println("s = " + s);
        }

    }



    @Test
    public void findMemberDto(){
        Team team1 = new Team("teamA");
        teamRepository.save(team1);

        Member member1 = new Member("AAA",10);
        memberRepository.save(member1);
        member1.setTeam(team1);

        List<MemberDto> result = memberRepository.findMemberDto();

        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }

    }



    @Test
    public void findByNames(){
        Member member1 = new Member("AAA",10);
        Member member2 = new Member("BBB",20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> result = memberRepository.findByNames(Arrays.asList("AAA","BBB"));

        for (Member member : result) {
            System.out.println("member = " + member);
        }

    }


    @Test
    public void returnType(){
        Member member1 = new Member("AAA",10);
        Member member2 = new Member("BBB",20);
        memberRepository.save(member1);
        memberRepository.save(member2);

//        List<Member> aaa = memberRepository.findListByUsername("AAA");
//        Member findMember = memberRepository.findMemberByUsername("AAA");
        Optional<Member> findMember = memberRepository.findOptionalMemberByUsername("AAA");
        System.out.println("findMember = " + findMember);

        //빈 컬렉션
        List<Member> result = memberRepository.findListByUsername("sdfdsfds");
        System.out.println("result = " + result.size());

        //결과가 null
        Member findMember2 = memberRepository.findMemberByUsername("sdfdsfds");
        System.out.println("findMember2 = " + findMember2);


        Optional<Member> findMember3 = memberRepository.findOptionalMemberByUsername("sdfdsfds");
        System.out.println("findMember3 = " + findMember3);
    }




    @Test
    public void returnTypeError(){
        Member member1 = new Member("AAA",10);
        Member member2 = new Member("AAA",20);
        memberRepository.save(member1);
        memberRepository.save(member2);
        //Spring추상한 예외에 의존(다른 DB여도 IncorrectResultSizeDataAccessException를 내려주어 클라이언트 코드 변환 필요없음)
        //org.springframework.dao.IncorrectResultSizeDataAccessException: Query did not return a unique result: 2 results were returned
        //Caused by: org.hibernate.NonUniqueResultException: Query did not return a unique result: 2 results were returned
        Optional<Member> findMember3 = memberRepository.findOptionalMemberByUsername("AAA");
        System.out.println("findMember3 = " + findMember3);
    }



    @Test
    public void paging(){
        //given
        memberRepository.save(new Member("member1",10));
        memberRepository.save(new Member("member2",10));
        memberRepository.save(new Member("member3",10));
        memberRepository.save(new Member("member4",10));
        memberRepository.save(new Member("member5",10));

        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Direction.DESC, "username"));

        int age = 10;

        //when
        Page<Member> page = memberRepository.findByAge(age, pageRequest);


        //then
        List<Member> content = page.getContent();
        long totalElements = page.getTotalElements();
        for (Member member : content) {
            System.out.println("member = " + member);
        }

        System.out.println("totalElements = " + totalElements);

        Assertions.assertThat(content.size()).isEqualTo(3);
        Assertions.assertThat(totalElements).isEqualTo(5);
        Assertions.assertThat(page.getNumber()).isEqualTo(0);
        Assertions.assertThat(page.getTotalPages()).isEqualTo(2);
        Assertions.assertThat(page.isFirst()).isTrue();
        Assertions.assertThat(page.hasNext()).isTrue();

    }



    @Test
    public void pagingSlice(){
        //given
        memberRepository.save(new Member("member1",10));
        memberRepository.save(new Member("member2",10));
        memberRepository.save(new Member("member3",10));
        memberRepository.save(new Member("member4",10));
        memberRepository.save(new Member("member5",10));

        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Direction.DESC, "username"));

        int age = 10;

        //when
        //SLICE는 무한 스크롤 더보기 등 페이징이 숫자로 관리되지 않을  경우에만 사용
        Slice<Member> page = memberRepository.findSliceByAge(age, pageRequest);


        //then
        List<Member> content = page.getContent();
        for (Member member : content) {
            System.out.println("member = " + member);
        }


        Assertions.assertThat(content.size()).isEqualTo(3);
        Assertions.assertThat(page.getNumber()).isEqualTo(0);
        Assertions.assertThat(page.isFirst()).isTrue();
        Assertions.assertThat(page.hasNext()).isTrue();

    }



    @Test
    public void pagingSliceCountDetach(){
        //given
        memberRepository.save(new Member("member1",10));
        memberRepository.save(new Member("member2",10));
        memberRepository.save(new Member("member3",10));
        memberRepository.save(new Member("member4",10));
        memberRepository.save(new Member("member5",10));

        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Direction.DESC, "username"));

        int age = 10;

        //when
        //Join이 복잡할 경우 CountQuery를 분리
        Page<Member> page = memberRepository.findCountDetachByAge(age, pageRequest);

        //MemberDto로 변환
        Page<MemberDto> toMap = page.map(
                member -> new MemberDto(member.getId(), member.getUsername(), null));
        //then
        List<Member> content = page.getContent();
        for (Member member : content) {
            System.out.println("member = " + member);
        }


        Assertions.assertThat(content.size()).isEqualTo(3);
        Assertions.assertThat(page.getNumber()).isEqualTo(0);
        Assertions.assertThat(page.isFirst()).isTrue();
        Assertions.assertThat(page.hasNext()).isTrue();

    }



    @Test
    public void bulkUpdate(){
        //given
        memberRepository.save(new Member("member1",10));
        memberRepository.save(new Member("member2",19));
        memberRepository.save(new Member("member3",20));
        memberRepository.save(new Member("member4",21));
        memberRepository.save(new Member("member5",40));

        //when
        int resultCount = memberRepository.bulkAgePlus(20);
        //clearAutomatically = true 이게 있으면 아래 두개 안해줘도됨
//        em.flush();
//        em.clear();

        //영속성 em.flush(), em.clear() 안해주면 1차캐시에서 읽어서 40이다.->bulk 연산은 영속성 컨텍스트에 알려주지 않음
        List<Member> result = memberRepository.findByUsername("member5");
        Member member5 = result.get(0);
        System.out.println("member5 = " + member5);

        //then
        Assertions.assertThat(resultCount).isEqualTo(3);
    }

    @Test
    public void findMemberLazy(){
        //given
        //member1 -> teamA
        //member2 -> teamB

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 10, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();
        
        //when N+1
        //select Member 1
//        List<Member> members = memberRepository.findAll();
//        List<Member> members = memberRepository.findMemberFetchJoin();
        //기본적으로 fetch join은 left outer join으로 나감
//        List<Member> members = memberRepository.findEntityGraphByUsername("member1");
        List<Member> members = memberRepository.findNamedEntityGraphByUsername("member1");

        for (Member member : members) {
            System.out.println("member = " + member.getUsername());
            System.out.println("member.getTeam().getClass() = " + member.getTeam().getClass());
            System.out.println("member.getTeam().getName() = " + member.getTeam().getName());
        }

    }

    @Test
    public void queryHint(){
        //given
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        em.flush();
        em.clear();

        //when
//        Member findMember = memberRepository.findById(member1.getId()).get();
        //업데이트 쿼리 안나감 -> 스냅샷이 없음
        Member findMember = memberRepository.findReadOnlyByUsername("member1");
        findMember.setUsername("member2");

        em.flush(); //변경감지 -> 치명적 단점 (객체를 두개 가지고 있어야함 원본데이터, 변경데이터)

    }

    @Test
    public void lock(){
        //given
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        em.flush();
        em.clear();

        //when
        List<Member> result = memberRepository.findLockByUsername("member1");

        List<Member> member2 = memberRepository.findOptimisticLockLockByUsername("member1");

    }


    @Test
    public void callCustom(){
        List<Member> memberCustom = memberRepository.findMemberCustom();
    }
}