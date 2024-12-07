package com.tanx.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tanx.blog.annotation.RedisArticleAnnotation;
import com.tanx.blog.constant.Constant;
import com.tanx.blog.entity.dto.WebStationDto;
import com.tanx.blog.entity.po.WebStation;
import com.tanx.blog.entity.vo.UserVo;
import com.tanx.blog.entity.vo.WebStationVo;
import com.tanx.blog.exception.DataOperationErrorException;
import com.tanx.blog.mapper.WebStationMapper;
import com.tanx.blog.service.WebStationService;
import com.tanx.blog.utils.RedisOperationUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

/**
 * @author 谭翔
 * @description 针对表【s_web_station( 网站信息表 )】的数据库操作Service实现
 * @createDate 2023-07-06 15:29:16
 */
@Slf4j
@Service
public class WebStationServiceImpl extends ServiceImpl<WebStationMapper, WebStation> implements WebStationService {

    @Resource
    private WebStationMapper webStationMapper;

    @Resource
    private BCryptPasswordEncoder passwordEncoder;

    @Resource
    private RedisOperationUtils operationUtils;

    @Override
    public WebStationDto findWebStation() {
        WebStation adminInfo = getAdminInfo();
        if (!operationUtils.hasKey(Constant.VISIT)) {
            operationUtils.set(Constant.VISIT, adminInfo.getTotalVisits());
        }
        operationUtils.incrBy(Constant.VISIT, 1);
        return new WebStationDto(adminInfo);
    }

    @Override
    public String updateInfo(WebStationVo webStationVo) {
        WebStation webStation = getAdminInfo();

        webStation.setName(webStationVo.getName());
        webStation.setForTheRecord(webStationVo.getForTheRecord());
        webStation.setBulletinBoard(webStationVo.getBulletinBoard());
        webStation.setPublicSecurityRegistrationNumber(webStationVo.getPublicSecurityRegistrationNumber());
        webStation.setMotto(webStationVo.getMotto());

        if (webStationMapper.updateById(webStation) != 1) {
            throw new DataOperationErrorException("网站信息更新失败！");
        }

        operationUtils.set(Constant.WEB_KEY, webStation);
        return "更新成功！";
    }

    @Override
    @RedisArticleAnnotation
    @Transactional(rollbackFor = {Exception.class, DataOperationErrorException.class})
    public String updateAdmin(UserVo userVo) {
        WebStation webStation = getAdminInfo();

        if (!passwordEncoder.matches(userVo.getOldPassword(), webStation.getPassword())) {
            throw new DataOperationErrorException("密码错误！");
        }

        webStation.setUserName(userVo.getUserName());
        webStation.setEmail(userVo.getEmail());

        if (webStationMapper.updateById(webStation) != 1) {
            throw new DataOperationErrorException("信息更新失败！");
        }

        operationUtils.set(Constant.WEB_KEY, webStation);
        return "更新成功！";
    }

    @Override
    public HashMap<String, String> findAdmin() {
        HashMap<String, String> map = new HashMap<>();
        map.put("email",  getAdminInfo().getEmail());
        return map;
    }

    @Override
    public String updateAdminPassword(String password, String oldPassword) {
        WebStation webStation = getAdminInfo();
        if (password.equals(oldPassword)) {
            throw new DataOperationErrorException("不能输入相同的密码！");
        }

        if (!passwordEncoder.matches(oldPassword, webStation.getPassword())) {
            throw new DataOperationErrorException("密码错误！");
        }

        webStation.setPassword(passwordEncoder.encode(password));
        if (webStationMapper.updateById(webStation) != 1) {
            throw new DataOperationErrorException("修改失败！");
        }

        operationUtils.set(Constant.WEB_KEY, webStation);
        return "修改成功！";
    }

    private WebStation getAdminInfo() {
        if (operationUtils.hasKey(Constant.WEB_KEY)) {
            return (WebStation) operationUtils.get(Constant.WEB_KEY);
        }

        List<WebStation> webStations = webStationMapper.selectList(new QueryWrapper<>());
        if (webStations.size() != 1) {
            throw new DataOperationErrorException("网站权限异常，请检查管理员权限！");
        }

        WebStation webStation = webStations.get(0);

        operationUtils.set(Constant.WEB_KEY, webStation);
        return webStation;
    }
}




