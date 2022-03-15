package com.news.nms.shiro;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {
    @Bean(name ="shiroFilterFactoryBean")
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("securityManager")DefaultWebSecurityManager securityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        Map<String,String> filterChain = new HashMap<>();
        filterChain.put("/token", "anon");
        filterChain.put("/*", "authc");
//        shiroFilterFactoryBean.setLoginUrl("/404");
//        shiroFilterFactoryBean.setUnauthorizedUrl("/404");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChain);

        Map<String, Filter> filters = new HashMap<>();
//        filters.put("roles", new RolesFilter());
        filters.put("authc", new LoginFilter());
//        filters.put("perms", new PermissionsFilter());
        shiroFilterFactoryBean.setFilters(filters);
        return shiroFilterFactoryBean;
    }

    @Bean(name="realm")
    public ShiroRealm getRealm(){
        return new ShiroRealm();
    }

    @Bean(name="securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("realm") ShiroRealm shiroRealm){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        DefaultWebSessionManager manager = new DefaultWebSessionManager();
        manager.setSessionIdCookie(new SimpleCookie("NMS_JSESSIONID"));
        securityManager.setSessionManager(manager);
        shiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        securityManager.setRealm(shiroRealm);
        return securityManager;
    }

    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        credentialsMatcher.setHashAlgorithmName("md5");
        credentialsMatcher.setHashIterations(8);
        credentialsMatcher.setStoredCredentialsHexEncoded(true);
        return credentialsMatcher;
    }


//    @Bean
//    public ShiroFilterChainDefinition shiroFilterChainDefinition(){
//        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
//        chainDefinition.addPathDefinition("/token", "anon");
//
//        chainDefinition.addPathDefinition("/*", "authc");
////        chainDefinition.addPathDefinition("/logout", "logout");
//        return chainDefinition;
//    }

}
