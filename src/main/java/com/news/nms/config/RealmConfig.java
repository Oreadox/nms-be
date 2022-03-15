package com.news.nms.config;

import com.news.nms.entity.Admin;
import com.news.nms.service.AdminService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;

import javax.annotation.Resource;


public class RealmConfig extends AuthorizingRealm {
    @Resource
    AdminService adminService;

    /*
     * 授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        Subject subject = SecurityUtils.getSubject();
        Admin admin = (Admin) subject.getPrincipal();
        Admin dbAdmin = adminService.getById(admin.getId());
        info.addStringPermissions(PermissionConfig.toSet(dbAdmin.getPermission()));
        return info;
    }


    /*
     * 验证用户名和密码
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken tokenU = (UsernamePasswordToken) token;
        Admin admin = adminService.getByUsername(tokenU.getUsername());
        if (admin != null)
            // 验证密码
            return new SimpleAuthenticationInfo(admin, admin.getPasswordHash(), "");
        return null;
    }


}
