package com.tanx.blog.handler;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tanx.blog.entity.po.WebStation;
import com.tanx.blog.exception.DataOperationErrorException;
import com.tanx.blog.mapper.WebStationMapper;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description: 自定义用户登录
 * @project_name: blog-server
 * @author: 谭翔
 * @date: 2023/10/25 12:12
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailHandler implements UserDetailsService, UserDetailsPasswordService {

    @Resource
    private WebStationMapper webStationMapper;

    //获取当前用户的方法，使用框架的上下文获取当前请求的用户
    public static Authentication getCurrentUser() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static void setCurrentUser(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    //刷新密码，如果用户更改密码了会重新刷新token或者退出登录，我也没用过，大家可以试一下
    @Override
    public UserDetails updatePassword(UserDetails user, String newPassword) {
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("UserDetailHandler:{}", username);

        List<WebStation> webStations = webStationMapper.selectList(new QueryWrapper<>());
        if (webStations.size() != 1) {
            // 用户信息错误，请检查是否被攻击
            throw new DataOperationErrorException("用户查询异常，请联系管理员！");
        }
        log.info("UserDetailHandler:{}", "数据库没有异常！");
        WebStation webStation = webStations.get(0);

        log.info("判断是否和数据库账号一致,数据库:{},传入账号:{},结果:{}", webStation.getEmail(), username, webStation.getEmail().equals(username));
        if (!webStation.getEmail().equals(username)) {
            throw new UsernameNotFoundException("用户不存在！");
        }
        log.info("UserDetailHandler:{}", "账号存在！");
        List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList("admin");
        return new User(webStation.getEmail(), webStation.getPassword(), authorities);
    }
}
