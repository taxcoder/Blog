package com.tanx.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tanx.blog.entity.dto.WebStationDto;
import com.tanx.blog.entity.po.WebStation;
import com.tanx.blog.entity.vo.UserVo;
import com.tanx.blog.entity.vo.WebStationVo;

import java.util.HashMap;

/**
 * @author 谭翔
 * @description 针对表【s_web_station( 网站信息表 )】的数据库操作Service
 * @createDate 2023-07-06 15:29:16
 */
public interface WebStationService extends IService<WebStation> {

    WebStationDto findWebStation();

    String updateInfo(WebStationVo webStationVo);

    String updateAdmin(UserVo userVo);

    HashMap<String, String> findAdmin();

    String updateAdminPassword(String password, String oldPassword);

}
