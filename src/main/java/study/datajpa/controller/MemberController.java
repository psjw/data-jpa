package study.datajpa.controller;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id) {
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    @GetMapping("/members2/{id}")
    public String findMember2(@PathVariable("id") Member member) {
        return member.getUsername();
    }


    //글로벌 설정은 application.yml
    //개별 설정은 @PageableDefault
    @GetMapping("/members")
    public Page<MemberDto> list(@PageableDefault(size = 5, sort = "username") Pageable pageable) {
        //페이지를 1부터 시작하기 위한 방법
        //1. 별도 Paging 객체를 새로 만듬 (궁극적 방법)
        //2. one-indexed-parameters: true (한계가 존재) -> 내부 파라미터가 안맞음
        return memberRepository.findAll(pageable).map(MemberDto::new);
    }

    @PostConstruct
    public void init() {
        memberRepository.save(new Member("user1"));
        memberRepository.save(new Member("user2"));
        memberRepository.save(new Member("user3"));
        memberRepository.save(new Member("user4"));
        memberRepository.save(new Member("user5"));
        memberRepository.save(new Member("user6"));
        memberRepository.save(new Member("user7"));
        memberRepository.save(new Member("user8"));
        memberRepository.save(new Member("user9"));
        memberRepository.save(new Member("user10"));
        memberRepository.save(new Member("user11"));
        memberRepository.save(new Member("user12"));
        memberRepository.save(new Member("user13"));
        memberRepository.save(new Member("user14"));
        memberRepository.save(new Member("user15"));
        memberRepository.save(new Member("user16"));
        memberRepository.save(new Member("user17"));
        memberRepository.save(new Member("user18"));
        memberRepository.save(new Member("user19"));
        memberRepository.save(new Member("user20"));
        memberRepository.save(new Member("user21"));
        memberRepository.save(new Member("user22"));
        memberRepository.save(new Member("user23"));
        memberRepository.save(new Member("user24"));
    }

}
