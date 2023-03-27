package portfolio.project.hashtagqna.config.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import portfolio.project.hashtagqna.entity.Member;
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
//        PrintLog printLog = new PrintLog();
        if (member == null) {
//            printLog.printInfoLog("member is null, so must throw the UsernameNotFoundException");
            throw new UsernameNotFoundException("해당 이메일 정보가 없습니다.");
        }
        return new PrincipalDetails(member);
    }

}
