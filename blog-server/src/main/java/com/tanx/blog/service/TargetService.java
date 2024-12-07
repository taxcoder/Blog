package com.tanx.blog.service;

import com.tanx.blog.entity.po.Target;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tanx.blog.utils.Response;

import java.util.List;

/**
* @author 谭翔
* @description 针对表【b_target(目标表)】的数据库操作Service
* @createDate 2024-03-30 10:58:36
*/
public interface TargetService extends IService<Target> {

    List<Target> targets(int year);

    String addTarget(String content, int year);

    public String delTarget(long id);

    String updateTargetContent(long id,String content);

    String updateTargetStatus(long id,boolean status);
}
