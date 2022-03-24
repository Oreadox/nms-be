package com.news.nms.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.news.nms.entity.Admin;
import com.news.nms.mapper.AdminMapper;
import com.news.nms.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {
    @Autowired
    AdminMapper adminMapper;

    @Override
    public Admin getByUsername(String username) {
        Map<String, Object> map = new HashMap<>();
        map.put("username", username);
        List<Admin> adminList = adminMapper.selectByMap(map);
        if(adminList.size()!=0)
            return adminList.get(0);
        return null;
    }

    @Override
    public Admin getByUsernameAndPasswordHash(String username, String passwordHash) {
        Map<String, Object> map = new HashMap<>();
        map.put("username", username);
        map.put("passwordHash", passwordHash);
        List<Admin> adminList = adminMapper.selectByMap(map);
        if(adminList.size()!=0)
            return adminList.get(0);
        return null;
    }

    @Override
    public List<Admin> getByKeyword(String keyword) {
        return adminMapper.selectList(new QueryWrapper<Admin>().
                like("username", keyword).or().like("name", keyword));
    }
}
