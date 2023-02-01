package portfolio.project.hashtagqna.repository;

import portfolio.project.hashtagqna.dto.MemberInfoDto;
import portfolio.project.hashtagqna.entity.Member;

public interface MemberRepositoryCustom {
    public Member findMember(String loginEmail, String loginPwd);

    public long editMember(Member oldMember, Member edMember);

    public MemberInfoDto viewMemberInfo(Long id);

    public long makeInactiveMember(Long memberId);
}
