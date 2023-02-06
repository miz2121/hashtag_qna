package portfolio.project.hashtagqna.repository;

import portfolio.project.hashtagqna.dto.MemberInfoDto;
import portfolio.project.hashtagqna.entity.Member;

import java.util.Optional;

public interface MemberRepositoryCustom {

    public long editMember(Member oldMember, Member edMember);

    public MemberInfoDto viewMemberInfo(Member viewMember);

    public Long makeInactiveMember(Member deleteMember);

    public Optional<Long> findByEmailNickname(String email, String nickname);
}
