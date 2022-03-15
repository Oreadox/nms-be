package com.news.nms.test.mapper;

import com.news.nms.entity.Admin;
import com.news.nms.mapper.AdminMapper;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class AdminMapperTest {
    @Resource
    private AdminMapper adminMapper;

    @Test
    public void testAll(){
        testDelete();
        testInsert();
        testSelectAll();
        testDelete();
    }

    @Test
    public void testInsert(){
        System.out.println(("----- testing AdminMapper Insert ------"));
        Admin admin = new Admin();
        admin.setUsername("test");

        admin.setPasswordHash(new Md5Hash("test", "", 8).toHex());
        adminMapper.insert(admin);
    }

    @Test
    public void testSelectAll(){
        System.out.println(("----- testing AdminMapper SelectAll ------"));
        List<Admin> adminList = adminMapper.selectList(null);
        if(adminList.size()!=0)
            adminList.forEach(System.out::println);
    }

    @Test
    public void testDelete(){
        System.out.println(("----- testing AdminMapper Delete ------"));
        Map<String, Object> map = new HashMap<>();
        map.put("username", "test");
        adminMapper.deleteByMap(map);
    }
}
