package study.datajpa.repository;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

public class MemberSpec {
    public static Specification<Member> teamName(final String teamName) {
        return (root, query, criteriaBuilder) -> {
            if(StringUtils.isEmpty(teamName)) {
                return null;
            }

            Join<Member, Team> t = root.join("team", JoinType.INNER);//회원과 조인
            return criteriaBuilder.equal(t.get("name"), teamName);
        };
    }

    public static Specification<Member> userName(final String userName) {
        return (root, query, builder) -> builder.equal(root.get("username"), userName);
    }
}
