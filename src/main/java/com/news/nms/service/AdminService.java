package com.news.nms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.news.nms.entity.Admin;

import java.util.List;

public interface AdminService extends IService<Admin> {
    Admin getByUsername(String username);
    Admin getByUsernameAndPasswordHash(String username, String passwordHash);
    List<Admin> getByKeyword(String keyword);
    List<Admin> getAll();
}
