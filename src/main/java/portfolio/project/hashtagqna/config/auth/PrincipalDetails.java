package portfolio.project.hashtagqna.config.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import portfolio.project.hashtagqna.entity.Member;
import portfolio.project.hashtagqna.exception.RestApiException;
import portfolio.project.hashtagqna.exception.code.MemberErrorCode;

import java.util.Collection;

@RequiredArgsConstructor
public class PrincipalDetails implements UserDetails {
    private final Member member;

    public Member getMember() {
        return member;
    }

    @Override
    public String getPassword() {
        return member.getPwd();
    }

    @Override
    public String getUsername() {
        return member.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }
}
