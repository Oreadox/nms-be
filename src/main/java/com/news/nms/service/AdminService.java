package com.news.nms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.news.nms.entity.Admin;

public interface AdminService extends IService<Admin> {
    Admin getByUsername(String username);
    Admin getByUsernameAndPasswordHash(String username, String passwordHash);
}
