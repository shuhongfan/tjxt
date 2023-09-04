package com.tianji.api.client.auth;

import com.tianji.api.dto.auth.RoleDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient("auth-service")
public interface AuthClient {

    @GetMapping("/roles/{id}")
    RoleDTO queryRoleById(@PathVariable("id") Long id);

    @GetMapping("/roles/list")
    List<RoleDTO> listAllRoles();
}
