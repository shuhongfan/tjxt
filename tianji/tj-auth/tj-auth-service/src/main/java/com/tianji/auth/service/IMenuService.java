package com.tianji.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tianji.auth.domain.po.Menu;

import java.util.List;

/**
 * <p>
 * 权限表，包括菜单权限和访问路径权限 服务类
 * </p>
 *
 * @author 虎哥
 * @since 2022-06-16
 */
public interface IMenuService extends IService<Menu> {

    List<Menu> listMenuByUser();

    void saveMenu(Menu menu);

    void deleteMenu(Long id);

    void bindRoleMenus(Long roleId, List<Long> menuIds);

    void deleteRoleMenus(Long roleId, List<Long> menuIds);
}
