package we.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Example for aggregate function of list
 *
 * @author zhongjie
 * @since 2022-04-24
 */
@RestController
@RequestMapping("/fn-list")
public class FnListController {
    /**
     * All user basic info list
     */
    private static final List<UserBasicInfo> USER_BASIC_INFO_LIST;
    /**
     * Role to user basic info list map
     */
    private static final Map<String, List<UserBasicInfo>> ROLE_TO_USER_BASIC_INFO_LIST_MAP;
    /**
     * All user private info list
     */
    private static final List<UserPrivateInfo> USER_PRIVATE_INFO_LIST;
    /**
     * User id to user private info map
     */
    private static final Map<Long, UserPrivateInfo> USER_ID_TO_USER_PRIVATE_INFO_MAP;

    static {
        USER_BASIC_INFO_LIST = new LinkedList<>();
        USER_BASIC_INFO_LIST.add(new UserBasicInfo(1L, "zhongjie", "admin", "ZHONG.J", "male"));
        USER_BASIC_INFO_LIST.add(new UserBasicInfo(2L, "yujian", "admin", "Big Squirrel", "female"));
        USER_BASIC_INFO_LIST.add(new UserBasicInfo(3L, "qiaowei", "developer", "Lancer", "male"));
        USER_BASIC_INFO_LIST.add(new UserBasicInfo(4L, "xiaofeng", "manager", "Black Hawk", "male"));

        ROLE_TO_USER_BASIC_INFO_LIST_MAP = new HashMap<>(16);
        USER_BASIC_INFO_LIST.forEach(userBasicInfo -> ROLE_TO_USER_BASIC_INFO_LIST_MAP.computeIfAbsent(userBasicInfo.role, key -> new LinkedList<>()).add(userBasicInfo));

        USER_PRIVATE_INFO_LIST = new LinkedList<>();
        USER_PRIVATE_INFO_LIST.add(new UserPrivateInfo(1L, "1@fizzgate.com", "11111111111", "111111111111111111"));
        USER_PRIVATE_INFO_LIST.add(new UserPrivateInfo(2L, "2@fizzgate.com", "22222222222", "222222222222222222"));
        USER_PRIVATE_INFO_LIST.add(new UserPrivateInfo(3L, "3@fizzgate.com", "33333333333", "333333333333333333"));
        USER_PRIVATE_INFO_LIST.add(new UserPrivateInfo(4L, "4@fizzgate.com", "44444444444", "444444444444444444"));

        USER_ID_TO_USER_PRIVATE_INFO_MAP = new HashMap<>(16);
        USER_PRIVATE_INFO_LIST.forEach(userPrivateInfo -> USER_ID_TO_USER_PRIVATE_INFO_MAP.put(userPrivateInfo.userId, userPrivateInfo));
    }

    @GetMapping(value = "/user-basic-info-list-by-role/{role}")
    public Result<List<UserBasicInfo>> userBasicInfoListByRole(@PathVariable String role) {
        return new Result<>(ROLE_TO_USER_BASIC_INFO_LIST_MAP.get(role));
    }

    @GetMapping(value = "/user-basic-info-list-by-roles/{roleList}")
    public Result<List<List<UserBasicInfo>>> userBasicInfoListByRoles(@PathVariable List<String> roleList) {
        return new Result<>(roleList.stream().map(ROLE_TO_USER_BASIC_INFO_LIST_MAP::get).collect(Collectors.toList()));
    }

    @GetMapping(value = "/user-private-list-by-user-ids/{userIdList}")
    public Result<List<UserPrivateInfo>> userPrivateListByUserIds(@PathVariable List<Long> userIdList) {
        return new Result<>(userIdList.stream().map(USER_ID_TO_USER_PRIVATE_INFO_MAP::get).collect(Collectors.toList()));
    }

    static class Result<T> {
        public Result(T data) {
            this.code = 0;
            this.message = "success";
            this.data = data;
        }

        final Integer code;
        final String message;
        final T data;

        public Integer getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        public T getData() {
            return data;
        }
    }

    static class UserBasicInfo {
        public UserBasicInfo(Long id, String name, String role, String nickname, String sex) {
            this.id = id;
            this.name = name;
            this.role = role;
            this.nickname = nickname;
            this.sex = sex;
        }

        final Long id;
        final String name;
        final String role;
        final String nickname;
        final String sex;

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getRole() {
            return role;
        }

        public String getNickname() {
            return nickname;
        }

        public String getSex() {
            return sex;
        }
    }

    static class UserPrivateInfo {
        public UserPrivateInfo(Long userId, String email, String phone, String idNo) {
            this.userId = userId;
            this.email = email;
            this.phone = phone;
            this.idNo = idNo;
        }

        final Long userId;
        final String email;
        final String phone;
        final String idNo;

        public Long getUserId() {
            return userId;
        }

        public String getEmail() {
            return email;
        }

        public String getPhone() {
            return phone;
        }

        public String getIdNo() {
            return idNo;
        }
    }
}
