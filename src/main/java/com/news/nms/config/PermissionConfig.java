package com.news.nms.config;


import java.util.*;

public class PermissionConfig {
    public static final String NEWS_NEW_AND_EDIT = "news:newEdit";
    public static final String NEWS_CHECK = "news:check";
    public static final String USER_ALL = "user:*";
    public static final String SUPERUSER = "*:*";

    private static final List<String> ALL =
            Arrays.asList(NEWS_NEW_AND_EDIT, NEWS_CHECK, USER_ALL, SUPERUSER);

    public static Set<String> getDefaultPermission(String permission) {
        Set<String> set = new HashSet<>();
        return addPermission(set, NEWS_NEW_AND_EDIT);
    }

    public static Set<String> toSet(String permission) {
        if (Objects.equals(permission, "") || permission == null)
            return new HashSet<>();
        return new HashSet<>(Arrays.asList(permission.split(",")));
    }

    public static String toString(Set<String> set) {
        if (set == null) {
            return null;
        }
        return String.join(",", set);
    }

    public static Set<String> addPermission(Set<String> perm, String permission) {
        perm.add(permission);
        return perm;
    }

    public static Set<String> addPermission(String perm, String permission) {
        return addPermission(toSet(perm), permission);
    }

    public static Set<String> removePermission(Set<String> perm, String permission) {
        perm.remove(permission);
        return perm;
    }

    public static Set<String> removePermission(String perm, String permission) {
        return removePermission(toSet(perm), permission);
    }

    public static Boolean verifyPermission(Set<String> permission) {
        for (String perm : permission) {
            if (!ALL.contains(perm))
                return false;
        }
        return true;
    }

    public static Boolean verifyPermission(String permission) {
        return verifyPermission(toSet(permission));
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
