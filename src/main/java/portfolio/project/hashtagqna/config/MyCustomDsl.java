package portfolio.project.hashtagqna.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import portfolio.project.hashtagqna.config.jwt.JwtAuthenticationFilter;
import portfolio.project.hashtagqna.config.jwt.JwtAuthorizationFilter;
import portfolio.project.hashtagqna.repository.MemberRepository;


@RequiredArgsConstructor
public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {
    private final CorsConfig corsConfig;
    private final MemberRepository memberRepository;
    private final AuthenticationFailureHandler failureHandler;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager);
        jwtAuthenticationFilter.setAuthenticationFailureHandler(this.failureHandler);
        http
                .addFilter(corsConfig.corsFilter())
                .addFilter(jwtAuthenticationFilter)
                .addFilter(new JwtAuthorizationFilter(authenticationManager, memberRepository));

        System.out.println("MyCustomDsl is applied");
    }
}
