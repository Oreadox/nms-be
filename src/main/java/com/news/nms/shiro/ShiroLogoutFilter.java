package com.news.nms.shiro;

import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.springframework.http.HttpStatus;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;


public class ShiroLogoutFilter extends LogoutFilter {
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        Subject subject=getSubject(request,response);
        String redirectUrl=getRedirectUrl(request,response,subject);
        ServletContext context= request.getServletContext();
        try {
            subject.logout();
            context.removeAttribute("error");
            ((HttpServletResponse) response).setStatus(HttpStatus.OK.value());
        }catch (SessionException e){
            e.printStackTrace();
        }
//        issueRedirect(request,response,redirectUrl);
        return false;
    }
}
