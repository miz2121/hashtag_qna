package portfolio.project.hashtagqna.repository;

import portfolio.project.hashtagqna.dto.MemberInfoDto;
import portfolio.project.hashtagqna.entity.Member;

import java.util.Optional;

public interface MemberRepositoryCustom {

    public long editMember(Long oldMemberId, Member edMember);

    public MemberInfoDto viewMemberInfo(Long id);

    public Long makeInactiveMember(Long id);

    public Long findByEmailNickname(String email, String nickname);

    public Long findMemberByEmailPwd(String email, String pwd);
}
