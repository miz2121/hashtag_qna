package portfolio.project.hashtagqna.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.transaction.annotation.Transactional;
import portfolio.project.hashtagqna.dto.MemberInfoDto;
import portfolio.project.hashtagqna.dto.MemberStatusDto;
import portfolio.project.hashtagqna.dto.QMemberInfoDto;
import portfolio.project.hashtagqna.dto.QMemberStatusDto;
import portfolio.project.hashtagqna.entity.Member;
import portfolio.project.hashtagqna.entity.MemberStatus;
import portfolio.project.hashtagqna.entity.QuestionStatus;

import java.util.Optional;

import static org.springframework.util.StringUtils.hasText;
import static portfolio.project.hashtagqna.entity.QAnComment.anComment;
import static portfolio.project.hashtagqna.entity.QAnswer.answer;
import static portfolio.project.hashtagqna.entity.QQuComment.quComment;
import static portfolio.project.hashtagqna.entity.QQuestion.question;
import static portfolio.project.hashtagqna.entity.QMember.member;


public class MemberRepositoryImpl implements MemberRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    public MemberRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
        this.em = em;
    }

    @Transactional
    @Override
    public long editNickname(Long oldMemberId, String nickname) {
        long execute = queryFactory
                .update(member)
                .set(member.nickname, nickname)
                .where(member.id.eq(oldMemberId))
                .execute();
        em.flush();
        em.clear();
        return execute;
    }

    @Override
    public MemberInfoDto viewMemberInfo(Long id) {
        return queryFactory
                .select(new QMemberInfoDto(
                        member.nickname,
                        member.email,
                        member.questionCount,
                        member.answerCount,
                        member.commentCount,
                        member.hashtagCount
                ))
                .from(member)
                .where(member.id.eq(id))
                .fetchFirst();
    }

    @Transactional
    @Override
    public Long makeInactiveMember(Long id) {
        String message = "탈퇴한 회원의 정보입니다.";
        queryFactory
                .update(member)
                .set(member.status, MemberStatus.INACTIVE)
                .where(member.id.eq(id))
                .execute();
        em.flush();
        em.clear();
        queryFactory
                .update(question)
                .set(question.title, message)
                .set(question.content, message)
                .set(question.writer, message)
                .set(question.questionStatus, QuestionStatus.CLOSED)
                .where(question.member.id.eq(id))
                .execute();
        em.flush();
        em.clear();
        queryFactory
                .update(answer)
                .set(answer.content, message)
                .set(answer.writer, message)
                .where(answer.member.id.eq(id))
                .execute();
        em.flush();
        em.clear();
        queryFactory
                .update(anComment)
                .set(anComment.content, message)
                .set(anComment.writer, message)
                .where(anComment.member.id.eq(id))
                .execute();
        em.flush();
        em.clear();
        queryFactory
                .update(quComment)
                .set(quComment.content, message)
                .set(quComment.writer, message)
                .where(quComment.member.id.eq(id))
                .execute();
        em.flush();
        em.clear();
        // 위 5개 update 문을 하나로 합치고 싶다...
        return id;
    }

    @Override
    public Long findByNickname(String nickname) {
        return queryFactory
                .select(member.id)
                .from(member)
                .where(memberNicknameEq(nickname))
                .fetchFirst();
    }

    @Override
    public Long findByEmailNickname(String email, String nickname) {
        return queryFactory
                .select(member.id)
                .from(member)
                .where(memberEmailOrNicknameEq(email, nickname))
                .fetchFirst();
    }

//    @Override
//    public MemberStatusDto findMemberStatusDtoByEmailPwd(String email, String pwd) {
//        return queryFactory
//                .select(new QMemberStatusDto(member.id, member.status))
//                .from(member)
//                .where(memberEmailEq(email).and(memberPwdEq(pwd)))
//                .fetchFirst();
//    }

    @Override
    public Member findMemberByEmail(String email) {
        return queryFactory
                .selectFrom(member)
                .where(memberEmailEq(email))
                .fetchFirst();
    }

    @Override
    public Member findMemberByEmailPwd(String email, String pwd){
        return queryFactory
                .selectFrom(member)
                .where(memberEmailEq(email).and(memberPwdEq(pwd)))
                .fetchFirst();
    }

    private BooleanExpression memberNicknameEq(String nicknameCond) {
        return nicknameCond != null ? member.nickname.eq(nicknameCond) : null;
    }

    private BooleanExpression memberEmailEq(String emailCond) {
        return emailCond != null ? member.email.eq(emailCond) : null;
    }

    private BooleanExpression memberPwdEq(String pwdCond) {
        return pwdCond != null ? member.pwd.eq(pwdCond) : null;
    }

    private BooleanBuilder memberEmailOrNicknameEq(String emailCond, String nicknameCond) {
//        return emailCond != null ? member.email.eq(emailCond) : null;
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (hasText(emailCond)) {
            booleanBuilder.or(member.email.eq(emailCond));
        }
        if (hasText(nicknameCond)) {
            booleanBuilder.or(member.nickname.eq(nicknameCond));
        }
        return booleanBuilder;
    }
}
