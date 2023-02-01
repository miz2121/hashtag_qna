package portfolio.project.hashtagqna.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import portfolio.project.hashtagqna.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
    public Member findMemberById(Long id);
}
