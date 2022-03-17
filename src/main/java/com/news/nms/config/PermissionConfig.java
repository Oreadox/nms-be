package com.news.nms.config;


import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class PermissionConfig {
    public static final String NEWS_NEW_AND_EDIT = "news:newEdit";
    public static final String NEWS_CHECK = "news:check";
    public static final String USER_ALL = "user:*";
    public static final String SUPERUSER = "*:*";

    public static Set<String> getDefaultPermission(String permission){
        Set<String> set =  new HashSet<>();
        addPermission(set, NEWS_NEW_AND_EDIT);
        return set;
    }

    public static Set<String> toSet(String permission){
        return new HashSet<>(Arrays.asList(permission.split(",")));
    }

    public static String toString(Set<String> set){
        return String.join(",", set);
    }

    public static Set<String> addPermission(Set<String> set, String permission) {
        set.add(permission);
        return set;
    }

    public static Set<String> removePermission(Set<String> set, String permission) {
        set.remove(permission);
        return set;
    }



}

/*

@AllArgsConstructor
public enum PermissionConfig implements IEnum<Integer> {
    READONLY(1, "仅阅读"),
    EDIT(10, "可创建/修改"),
    CHECK(50, "可进行审核"),
    ADDUSER(100, "可创建/编辑用户"),
    SUPERUSER(255, "管理员，可维护系统");

    private final Integer value;
    private final String desc;

    @Override
    public Integer getValue() {
        return value;
    }
}

 */
