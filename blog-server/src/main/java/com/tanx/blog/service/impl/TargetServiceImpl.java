package com.tanx.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tanx.blog.constant.Constant;
import com.tanx.blog.entity.po.Target;
import com.tanx.blog.exception.DataOperationErrorException;
import com.tanx.blog.mapper.TargetMapper;
import com.tanx.blog.service.TargetService;
import com.tanx.blog.utils.RedisOperationUtils;
import jakarta.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 谭翔
 * @description 针对表【b_target(目标表)】的数据库操作Service实现
 * @createDate 2024-03-30 10:58:36
 */
@Service
@Log4j2
public class TargetServiceImpl extends ServiceImpl<TargetMapper, Target>
        implements TargetService {

    @Resource
    private TargetMapper targetMapper;

    @Resource
    private RedisOperationUtils operationUtils;

    public void updateTargetList(int year) {
        log.info("operationUtils.hExists(Constant.TARGET, String.valueOf(year)):{}", operationUtils.hExists(Constant.TARGET, String.valueOf(year)));
        if (operationUtils.hExists(Constant.TARGET, String.valueOf(year)) &&  operationUtils.hDelete(Constant.TARGET, String.valueOf(year)) != 1) {
           throw new DataOperationErrorException("缓存更新失败！");
        }
    }

    @Override
    public List<Target> targets(int year) {
        // 如果存在key，则直接从redis内获取
        if (operationUtils.hExists(Constant.TARGET, String.valueOf(year))) {
            return (List<Target>) operationUtils.hGet(Constant.TARGET, year);
        }
        List<Target> targets = targetMapper.selectTargets(year);
        operationUtils.hPut(Constant.TARGET, year, targets);
        return targets;
    }

    @Override
    public String addTarget(String content,int year) {
        Target target = new Target();
        target.setContent(content);
        target.setYear(year);
        if (targetMapper.insert(target) != 1) {
            throw new DataOperationErrorException("添加失败！");
        }
        updateTargetList(year);
        return "添加成功";
    }

    @Override
    public String delTarget(long id) {
        Target target = targetMapper.selectById(id);
        if (target == null || targetMapper.deleteById(id) != 1) {
            throw new DataOperationErrorException("删除失败！");
        }
        updateTargetList(target.getYear());
        return "删除成功";
    }

    @Override
    public String updateTargetContent(long id, String content) {
        Target target = targetMapper.selectById(id);
        if(target == null || content.equals(target.getContent())) {
            throw new DataOperationErrorException("更新失败！");
        }
        target.setContent(content);
        if (targetMapper.updateById(target) != 1) {
            throw new DataOperationErrorException("更新失败！");
        }
        updateTargetList(target.getYear());
        return "更新成功！";
    }

    @Override
    public String updateTargetStatus(long id, boolean status) {
        Target target = targetMapper.selectById(id);
        if(target == null || target.getIsSuccess() == status) {
            throw new DataOperationErrorException("更新失败！");
        }
        target.setIsSuccess(status);
        if (targetMapper.updateById(target) != 1) {
            throw new DataOperationErrorException("更新失败！");
        }
        updateTargetList(target.getYear());
        return "更新成功！";
    }
}




