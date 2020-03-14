package com.zeed.assignment.sms.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class AuthenticationService {

    @Autowired
    private JdbcClientDetailsService jdbcClientDetailsService;

    @Value("${security.oauth2.client.id}")
    private String clientId;

    @Autowired
    private DefaultTokenServices defaultTokenService;

    public OAuth2AccessToken getOauth2AccessToken(Object principal) {

        if (principal == null) {
            return null;
        }

        OAuth2AccessToken accessToken;
        OAuth2Authentication auth2Authentication;
        Collection<GrantedAuthority> grantedAuthorities;

        if (principal instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken loggedInPrincipal = (UsernamePasswordAuthenticationToken) principal;
            grantedAuthorities = loggedInPrincipal.getAuthorities();
            ClientDetails clientDetails = jdbcClientDetailsService.loadClientByClientId(clientId);
            OAuth2Request oAuth2Request = new OAuth2Request(null,clientId, grantedAuthorities,true,
                    clientDetails.getScope(),clientDetails.getResourceIds(),null,clientDetails.getAuthorizedGrantTypes(), null);
            auth2Authentication = new OAuth2Authentication(oAuth2Request, loggedInPrincipal);
        } else {
            auth2Authentication = (OAuth2Authentication) principal;
        }
        if (auth2Authentication == null) {
            return  null;
        }

        accessToken = defaultTokenService.createAccessToken(auth2Authentication);

        return accessToken;

    }

}
