package com.tianji.auth.controller;


import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tianji.auth.domain.dto.PrivilegeDTO;
import com.tianji.auth.domain.po.Privilege;
import com.tianji.auth.domain.vo.PrivilegeOptionVO;
import com.tianji.auth.service.IPrivilegeService;
import com.tianji.common.domain.dto.PageDTO;
import com.tianji.common.domain.query.PageQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 权限表，包括菜单权限和访问路径权限 前端控制器
 * </p>
 *
 * @author 虎哥
 * @since 2022-06-15
 */
@RestController
@RequestMapping("/privileges")
@Api(tags = "权限管理接口")
@RequiredArgsConstructor
public class PrivilegeController {

    private final IPrivilegeService privilegesService;

    /**
     * 分页查询所有权限
     *
     * @param pageQuery 分页查询条件
     * @return 分页结果
     */
    @ApiOperation("分页查询所有权限")
    @GetMapping
    public PageDTO<PrivilegeDTO> listAllPrivileges(PageQuery pageQuery) {
        // 1.分页查询
        Page<Privilege> page = privilegesService.listPrivilegesByPage(pageQuery);
        // 2.非空判断
        List<Privilege> list = page.getRecords();
        if (CollectionUtil.isEmpty(list)) {
            // 结果为空，返回空结果 添加总页码数
            return new PageDTO<>(page.getTotal(), page.getPages(), Collections.emptyList());
        }
        // 3.数据转换
        List<PrivilegeDTO> dtoList = list.stream().map(Privilege::toDTO).collect(Collectors.toList());
        // 4.封装返回
        return new PageDTO<>(page.getTotal(), page.getPages(), dtoList);
    }

    /**
     * 查询所有权限，作为下拉选框菜单
     *
     * @return 分页结果
     */
    @ApiOperation("查询菜单下的所有权限，作为下拉选框菜单")
    @GetMapping("options/{menuId}")
    public List<PrivilegeOptionVO> listAllPrivilegesOptionsByMenuId(
            @ApiParam(value = "菜单id", example = "1") @PathVariable("menuId") Long menuId
    ) {
        // 1.查询菜单下的权限
        List<Privilege> list = privilegesService.lambdaQuery()
                .eq(Privilege::getMenuId, menuId)
                .eq(Privilege::getInternal, false)
                .list();
        // 2.非空判断
        if (CollectionUtil.isEmpty(list)) {
            // 结果为空，返回空结果
            return Collections.emptyList();
        }
        // 3.数据转换
        return list.stream()
                .map(PrivilegeOptionVO::new).collect(Collectors.toList());
    }

    /**
     * 查询某个角色的权限
     *
     * @return 某个角色的权限列表
     */
    @ApiOperation("查询菜单下的权限列表，某个角色的权限")
    @GetMapping("/roles/{roleId}/{menuId}")
    public List<PrivilegeOptionVO> listPrivilegeByRoleId(
            @ApiParam(value = "角色id", required = true, example = "1") @PathVariable("roleId") Long roleId,
            @ApiParam(value = "菜单id", required = true, example = "1") @PathVariable("menuId") Long menuId
    ) {
        // 1.查询角色对应的权限id
        Set<Long> privilegeIds = privilegesService.listPrivilegeByRoleId(roleId);
        if (CollectionUtil.isEmpty(privilegeIds)) {
            return Collections.emptyList();
        }
        // 2.查询菜单下所有权限
        List<PrivilegeOptionVO> vos = listAllPrivilegesOptionsByMenuId(menuId);
        // 3.标记
        for (PrivilegeOptionVO vo : vos) {
            vo.setChecked(privilegeIds.contains(vo.getId()));
        }
        return vos;
    }

    /**
     * 新增权限
     *
     * @param privilegeDTO 权限数据
     * @return 新增成功的权限数据
     */
    @ApiOperation("新增权限")
    @PostMapping
    public PrivilegeDTO savePrivilege(@Validated @RequestBody PrivilegeDTO privilegeDTO) {
        // 域对象转换
        Privilege privilege = new Privilege(privilegeDTO);
        // 新增
        privilegesService.savePrivilege(privilege);
        // 返回
        return privilege.toDTO();
    }

    /**
     * 修改权限
     *
     * @param privilegeDTO 权限数据
     * @param id           要修改的权限的id
     * @return 修改后的权限结果
     */
    @ApiOperation("修改权限")
    @PutMapping("{id}")
    public PrivilegeDTO updatePrivilege(
            @RequestBody PrivilegeDTO privilegeDTO,
            @ApiParam(value = "要修改的权限id", required = true, example = "1") @PathVariable("id") Long id) {
        // 域对象转换
        Privilege privilege = new Privilege(privilegeDTO);
        privilege.setId(id);
        // 修改
        privilegesService.updateById(privilege);
        // 返回
        return privilege.toDTO();
    }

    /**
     * 删除权限
     *
     * @param id 权限id
     */
    @ApiOperation("删除权限")
    @DeleteMapping("{id}")
    public void removePrivilegeById(
            @ApiParam(value = "要删除的权限id", required = true, example = "1") @PathVariable("id") Long id) {
        privilegesService.removePrivilegeById(id);
    }

    /**
     * 绑定角色与API权限
     *
     * @param roleId       角色id
     * @param privilegeIds 权限id集合
     */
    @PostMapping("/role/{roleId}")
    @ApiOperation("绑定角色与API权限")
    public void bindRolePrivileges(
            @ApiParam(value = "角色id", example = "1") @PathVariable("roleId") Long roleId,
            @ApiParam(value = "API权限的id集合") List<Long> privilegeIds) {
        privilegesService.bindRolePrivileges(roleId, privilegeIds);
    }

    /**
     * 解除角色的API权限
     *
     * @param roleId       角色id
     * @param privilegeIds 权限id集合
     */
    @DeleteMapping("/role/{roleId}")
    @ApiOperation("解除角色的API权限")
    public void deleteRolePrivileges(
            @ApiParam(value = "角色id", example = "1") @PathVariable("roleId") Long roleId,
            @ApiParam(value = "API权限的id集合") List<Long> privilegeIds) {
        privilegesService.deleteRolePrivileges(roleId, privilegeIds);
    }


}
