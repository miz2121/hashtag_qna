package portfolio.project.hashtagqna.repository;

import portfolio.project.hashtagqna.dto.MemberInfoDto;
import portfolio.project.hashtagqna.dto.MemberStatusDto;
import portfolio.project.hashtagqna.entity.Member;

public interface MemberRepositoryCustom {

    public long editNickname(Long oldMemberId, String nickname);

    public MemberInfoDto viewMemberInfo(Long id);

    public Long makeInactiveMember(Long id);

    public Long findByEmailNickname(String email, String nickname);

    public Long findByNickname(String nickname);

//    /**
//     * @param email
//     * @param pwd
//     * @return id, memberStatus
//     */
//    public MemberStatusDto findMemberStatusDtoByEmailPwd(String email, String pwd);

    public Member findMemberByEmail(String email);

    public Member findMemberByEmailPwd(String email, String pwd);
}
