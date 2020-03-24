package com.zeed.itbit.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;


@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    private TokenStore jdbcTokenStore;

    @Value("${oauth.resource.id}")
    private String resourceId;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId(resourceId)
                .tokenStore(jdbcTokenStore);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.requestMatcher(new ApiRequestMatcher());
        http.authorizeRequests()
                .antMatchers("/login").permitAll()
                .antMatchers("/oauth/token").permitAll()
                .antMatchers("/login").permitAll()
//                .antMatchers("/userlogout").permitAll()
                .antMatchers("/view/users").permitAll()
                .antMatchers("/view/setting-view").permitAll()
                .antMatchers("/view/view-authorities").permitAll()
                .antMatchers("/view/currency").permitAll()
                .antMatchers("/view/audit-log").permitAll()
                .antMatchers("/user/login").permitAll()
                .antMatchers("/user/forgotPassword").permitAll()
//                .antMatchers("/").permitAll()
                .antMatchers("/regDetails/**").permitAll()
                .antMatchers(
                        new String[]{"/app/**",
                                "/login",
                                "/signup",
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
                                "/image/**",
                                "/assets/**",
                                "/font-awesome-4.7.0 2/**",
                                "/admin.js",
                                "/custom.css",
                                "/dashboard.js",
                                "/jquery.js",
                                "/jquery-ui.js",
                                "/reloadAdmindash.js",
                                "/updateProfile.js",
                                "/bootstrap/**",
                                "/upload.js",
                                "/js/**"
                        }
                ).permitAll()
                .antMatchers("/js/**").permitAll()
                .anyRequest().authenticated()
                .and().csrf().disable();
    }


}
