package com.news.nms.shiro;

import org.apache.shiro.web.filter.authz.RolesAuthorizationFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RolesFilter extends RolesAuthorizationFilter {
    @Override
    protected boolean onAccessDenied(final ServletRequest request, final ServletResponse response) throws IOException {
        HttpServletResponse httpResponse ;
        try {
            httpResponse = WebUtils.toHttp(response);
        }
        catch (ClassCastException ex) {
            return super.onAccessDenied(request, response) ;
        }
        httpResponse.sendError(401);
        return false;
    }
}
