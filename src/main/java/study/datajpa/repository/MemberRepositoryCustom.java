package study.datajpa.repository;

import study.datajpa.entity.Member;

import java.util.List;

//보통 QueryDSL 할떄 많이 사용
public interface MemberRepositoryCustom {
    List<Member> findMemberCustom();

}
