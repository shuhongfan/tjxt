package com.tianji.api.client.user;


import com.tianji.api.client.user.fallback.UserClientFallback;
import com.tianji.api.dto.user.LoginFormDTO;
import com.tianji.api.dto.user.UserDTO;
import com.tianji.common.domain.dto.LoginUserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "user-service", fallbackFactory = UserClientFallback.class)
public interface UserClient {

    /**
     * 根据手机号查询用户id
     * @param phone 手机号
     * @return 用户id
     */
    @GetMapping("/users/ids")
    Long exchangeUserIdWithPhone(@RequestParam("phone") String phone);

    /**
     * 登录接口
     * @param loginDTO 登录信息
     * @param isStaff 是否是员工
     * @return 用户详情
     */
    @PostMapping("/users/detail/{isStaff}")
    LoginUserDTO queryUserDetail(@RequestBody LoginFormDTO loginDTO, @PathVariable("isStaff") boolean isStaff);

    /**
     * 查询用户类型
     * @param id 用户id
     * @return 用户类型，0-普通学员，1-老师，2-其他员工
     */
    @GetMapping("/users/{id}/type")
    Integer queryUserType(@PathVariable("id") Long id);

    /**
     * <h1>根据id批量查询用户信息</h1>
     * @param ids 用户id集合
     * @return 用户集合
     */
    @GetMapping("/users/list")
    List<UserDTO> queryUserByIds(@RequestParam("ids") Iterable<Long> ids);


    /**
     * 根据id查询单个学生信息
     * @param id 用户id
     * @return 学生
     */
    @GetMapping("/users/{id}")
    UserDTO queryUserById(@PathVariable("id") Long id);
}
