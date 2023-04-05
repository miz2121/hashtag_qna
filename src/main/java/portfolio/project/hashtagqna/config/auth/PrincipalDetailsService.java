package portfolio.project.hashtagqna.config.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import portfolio.project.hashtagqna.entity.Member;
import portfolio.project.hashtagqna.entity.MemberStatus;
import portfolio.project.hashtagqna.exception.RestApiException;
import portfolio.project.hashtagqna.exception.code.MemberErrorCode;
import portfolio.project.hashtagqna.logger.PrintLog;
import portfolio.project.hashtagqna.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findMemberByEmail(username);
        PrintLog printLog = new PrintLog();
        if (member == null) {
//            printLog.printInfoLog("member is null, so must throw the UsernameNotFoundException");
            throw new UsernameNotFoundException("NOT_MEMBER");
        }
        if (member.getStatus() == MemberStatus.INACTIVE) {
            throw new UsernameNotFoundException("INACTIVE");
        }
        return new PrincipalDetails(member);
    }
}
