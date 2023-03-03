package portfolio.project.hashtagqna.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import portfolio.project.hashtagqna.config.jwt.JwtAuthenticationFilter;
import portfolio.project.hashtagqna.config.jwt.JwtAuthorizationFilter;
import portfolio.project.hashtagqna.repository.MemberRepository;


@RequiredArgsConstructor
public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {
    private final CorsConfig corsConfig;
    private final MemberRepository memberRepository;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
        http
                .addFilter(corsConfig.corsFilter())
                .addFilter(new JwtAuthenticationFilter(authenticationManager))
                .addFilter(new JwtAuthorizationFilter(authenticationManager, memberRepository));
        System.out.println("MyCustomDsl is applied");
    }
}
