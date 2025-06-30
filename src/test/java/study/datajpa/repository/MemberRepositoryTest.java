package study.datajpa.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberRepositoryTest {

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





}