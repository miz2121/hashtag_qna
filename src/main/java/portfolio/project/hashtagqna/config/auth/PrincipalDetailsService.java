package portfolio.project.hashtagqna.config.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import portfolio.project.hashtagqna.entity.Member;
import portfolio.project.hashtagqna.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findMemberByEmail(email);
        return new PrincipalDetails(member);
    }
}
