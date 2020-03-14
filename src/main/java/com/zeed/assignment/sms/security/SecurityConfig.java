package com.zeed.assignment.sms.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSessionListener;
import java.util.List;
import java.util.regex.Pattern;

@Configuration
@EnableWebSecurity
//@Order(Ordered.HIGHEST_PRECEDENCE)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private TokenStore jdbcTokenStore;

    @Autowired
    private List<org.springframework.security.authentication.AuthenticationProvider> authenticationProviders;

    @Bean
    public List<org.springframework.security.authentication.AuthenticationProvider> authenticationProviders() {
        return authenticationProviders;
    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        for (AuthenticationProvider a : authenticationProviders) {
            auth.authenticationProvider(a);
        }
    }

    @Override
    @Bean(name = "userAuthenticationManager")
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/user/createUser");
        web.ignoring().antMatchers("/user/forgotPassword");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(
                        new String[]{"/app/**",
//                                "/login",
                                "/signup",
                                "/css/**",
                                "/js/**",
                                "/app/**",
                                "/login",
                                "/home",
                                "/bootstrap/**",
                                "/user/forgotPassword",
                                "/settings",
                                "/regDetails/**",
                                "/home",
                                "/css/**",
                                "/js/**",
                                "/img/**",
                                "/fonts/**",
                                "/font-awesome/**",
                                "/template-js/**",
                                "/template-pages/**",
                                "/template-css/**",
                                "/authority/**",
                                "/favicon.ico",
                                "/font-awesome-4.7.0 2/**",
                                "/admin.js",
                                "/custom.css",
                                "/dashboard.js",
                                "/jquery.js",
                                "/jquery-ui.js",
                                "/reloadAdmindash.js",
                                "/updateProfile.js",
                                "/upload.js",
                                "/js/**"
                        }
                ).permitAll()
//                .antMatchers("/login").permitAll()
                .antMatchers("/signup").permitAll()
//                .antMatchers("/userlogout").permitAll()
                .antMatchers("/js/**").permitAll()
                .antMatchers("/oauth/token").permitAll()
                .anyRequest().fullyAuthenticated()
                .and()
                .csrf().ignoringAntMatchers("/user/**","/oauth/**","/**")
                .requireCsrfProtectionMatcher(new RequestMatcher() {
                    private Pattern allowedMethods = Pattern.compile("^(GET|HEAD|TRACE|OPTIONS)$");
                    @Override
                    public boolean matches(HttpServletRequest httpServletRequest) {
                        if (allowedMethods.matcher(httpServletRequest.getMethod()).matches())
                            return false;
                        return true;
                    }
                })
                .and().formLogin().loginPage("/home")
                .permitAll()
                .and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .sessionFixation().migrateSession()
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
                .sessionRegistry(new SessionRegistryImpl()).and()
                .sessionAuthenticationStrategy(userConcurrentSessions())
                .and().formLogin()
                .loginPage("/home")
                .and()
                .exceptionHandling().accessDeniedPage("/")
        .and().anonymous().disable();
    }


    @Bean
    public SessionRegistry sessionRegistry() {
        SessionRegistry sessionRegistry = new SessionRegistryImpl();
        return sessionRegistry;
    }


    @Bean
    public HttpSessionListener httpSessionListener() {
        return new SessionListener();
    }

    @Bean
    public UserConcurrentSessions userConcurrentSessions(){
        return new UserConcurrentSessions(sessionRegistry(), jdbcTokenStore);

    }

    @Bean
    @Primary
    public DefaultTokenServices defaultTokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(jdbcTokenStore);
        return defaultTokenServices;
    }
}
