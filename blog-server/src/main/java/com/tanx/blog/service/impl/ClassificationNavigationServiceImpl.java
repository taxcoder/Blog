package com.tanx.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tanx.blog.annotation.DeleteClassificationNavAnnotation;
import com.tanx.blog.constant.Constant;
import com.tanx.blog.entity.po.ClassificationNavigation;
import com.tanx.blog.entity.po.Navigation;
import com.tanx.blog.entity.vo.ClassificationNavigationVo;
import com.tanx.blog.entity.vo.LinkDataVo;
import com.tanx.blog.exception.DataOperationErrorException;
import com.tanx.blog.mapper.ClassificationNavigationMapper;
import com.tanx.blog.mapper.NavigationMapper;
import com.tanx.blog.service.ClassificationNavigationService;
import com.tanx.blog.utils.FunctionUtils;
import com.tanx.blog.utils.PageUtils;
import com.tanx.blog.utils.RedisOperationUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 谭翔
 * @description 针对表【d_classification_navigation(存储导航分类信息)】的数据库操作Service实现
 * @createDate 2024-04-21 11:37:30
 */
@Service
public class ClassificationNavigationServiceImpl extends ServiceImpl<ClassificationNavigationMapper, ClassificationNavigation>
        implements ClassificationNavigationService {

    @Resource
    private ClassificationNavigationMapper classificationNavigationMapper;

    @Resource
    private NavigationMapper navigationMapper;

    @Resource
    private RedisOperationUtils operationUtils;

    @Override
    @DeleteClassificationNavAnnotation
    public String add(String name) {
        ClassificationNavigation classificationNavigation = new ClassificationNavigation();
        classificationNavigation.setName(name);
        if (classificationNavigationMapper.insert(classificationNavigation) != 1) {
            throw new DataOperationErrorException("增加失败！");
        }
        return "增加成功！";
    }

    @Override
    @DeleteClassificationNavAnnotation
    public String deleteById(long id) {
        QueryWrapper<Navigation> wrapper = new QueryWrapper<>();
        wrapper.eq("classification_navigation_id", id);
        if (navigationMapper.selectCount(wrapper) > 0) {
            throw new DataOperationErrorException("删除失败，仍有与其关联的导航网站！");
        }

        if (classificationNavigationMapper.deleteById(id) != 1) {
            throw new DataOperationErrorException("删除失败！");
        }
        return "删除成功！";
    }

    @Override
    @DeleteClassificationNavAnnotation
    @Transactional(rollbackFor = {DataOperationErrorException.class, Exception.class})
    public String deleteByIdAndCloseLink(long id, List<LinkDataVo> voList) {
        if (classificationNavigationMapper.deleteById(id) != 1) {
            throw new DataOperationErrorException("删除失败！");
        }

        voList.forEach(linkDataVo -> {
            UpdateWrapper<Navigation> wrapper = new UpdateWrapper<>();
            wrapper.in("id", List.of(linkDataVo.getDatas())).set("classification_navigation_id", linkDataVo.getSelect());
            if(navigationMapper.update(wrapper) != linkDataVo.getDatas().length) {
                throw new DataOperationErrorException("关系更新失败！");
            }
        });
        return "删除成功！";
    }

    @Override
    @DeleteClassificationNavAnnotation
    @Transactional(rollbackFor = {DataOperationErrorException.class, Exception.class})
    public String updateInfoById(long id, String name, List<LinkDataVo> list) {
        ClassificationNavigation navigation = classificationNavigationMapper.selectById(id);

        if (navigation == null) {
            throw new DataOperationErrorException("请传入正确的参数！");
        }

        long size = classificationNavigationMapper.selectCount(new QueryWrapper<ClassificationNavigation>().eq("name", name));
        if (size > 0 && list.size() == 0) {
            throw new DataOperationErrorException("没有需要更新的数据！");
        }

        navigation.setName(name);
        if(size == 0 && classificationNavigationMapper.updateById(navigation) != 1) {
            throw new DataOperationErrorException("分类名更新失败！");
        }

        list.forEach(l -> {
            UpdateWrapper<Navigation> navigationUpdateWrapper = new UpdateWrapper<>();
            navigationUpdateWrapper.in("id", List.of(l.getDatas())).set("classification_navigation_id", l.getSelect());
            if(navigationMapper.update(navigationUpdateWrapper) != l.getDatas().length) {
                throw new DataOperationErrorException("修改失败！");
            }
        });

        return "修改成功！";
    }

    @Override
    public PageUtils<ClassificationNavigationVo> select(int current, int size) {
        PageUtils<ClassificationNavigationVo> utils = new PageUtils<>(current, size);
        return FunctionUtils.isTrueOrFalse(operationUtils.hasKey(Constant.CLASS_NAV), utils).trueOrFalseHandle(
                trueHandle -> {
                    utils.setRecords((List<ClassificationNavigationVo>) operationUtils.lRange(Constant.CLASS_NAV, 0, -1));
                    utils.setTotal(operationUtils.lLen(Constant.CLASS_NAV));
                    utils.setSize(utils.getTotal());
                    return utils;
                },
                falseHandle -> {
                    List<ClassificationNavigation> navigation = classificationNavigationMapper.selectList(new QueryWrapper<>());
                    List<ClassificationNavigationVo> vos = change(navigation);
                    operationUtils.lRightPushAll(Constant.CLASS_NAV, vos);
                    utils.setRecords(vos);
                    utils.setTotal(vos.size());
                    utils.setSize(vos.size());
                    return utils;
                });
    }

    @Override
    public PageUtils<ClassificationNavigationVo> selectById(int current, int size, long id) {
        return search(current, size, String.valueOf(id), "id");
    }

    @Override
    public PageUtils<ClassificationNavigationVo> selectByName(int current, int size, String name) {
        return search(current, size, name, "name");
    }

    @Override
    public PageUtils<ClassificationNavigationVo> selectSearchDataByLinkCountEqualOrGreaterThan(int searchValue, int current, int size) {
        return searchByLinkCount(current, size, searchValue, 1);
    }

    @Override
    public PageUtils<ClassificationNavigationVo> selectSearchDataByLinkCountEqualOrLessThan(int searchValue, int current, int size) {
        return searchByLinkCount(current, size, searchValue, -1);
    }

    @Override
    public PageUtils<ClassificationNavigationVo> selectSearchDataByLinkCountEqual(int searchValue, int current, int size) {
        return searchByLinkCount(current, size, searchValue, 0);
    }

    public List<ClassificationNavigationVo> change(List<ClassificationNavigation> navigation) {
        return navigation.stream().map(nav -> {
            QueryWrapper<Navigation> wrapper = new QueryWrapper<>();
            wrapper.eq("classification_navigation_id", nav.getId());
            ClassificationNavigationVo vo = new ClassificationNavigationVo();
            vo.setId(nav.getId());
            vo.setName(nav.getName());
            vo.setNavigationCount(navigationMapper.selectCount(wrapper));
            vo.setCreateTime(nav.getCreateTime());
            vo.setUpdateTime(nav.getUpdateTime());
            return vo;
        }).toList();
    }


    public PageUtils<ClassificationNavigationVo> search(int current, int size, String searchValue, String condition) {
        PageUtils<ClassificationNavigationVo> utils = new PageUtils<>(current, size);
        return FunctionUtils.isTrueOrFalse(operationUtils.hasKey(Constant.CLASS_NAV), utils).trueOrFalseHandle(
                trueHandle -> {
                    List<ClassificationNavigationVo> navigation = (List<ClassificationNavigationVo>) operationUtils.lRange(Constant.CLASS_NAV, 0, -1);
                    List<ClassificationNavigationVo> list = navigation.stream().filter(n -> {
                        if ("id".equals(condition)) {
                            return n.getId().toString().matches(".*" + searchValue + ".*");
                        } else if ("name".equals(condition)) {
                            return n.getName().matches(".*" + searchValue + ".*");
                        }
                        return false;
                    }).toList();
                    utils.setRecords(list.stream().skip((long) current * size).limit(size).toList());
                    utils.setTotal(list.size());
                    return utils;
                },
                falseHandle -> {
                    QueryWrapper<ClassificationNavigation> wrapper = new QueryWrapper<>();
                    wrapper.last(" LOCATE('" + searchValue + "', " + condition + ") > 0");
                    List<ClassificationNavigationVo> navigation = change(classificationNavigationMapper.selectList(wrapper));
                    utils.setRecords(navigation.stream().skip((long) current * size).limit(size).toList());
                    utils.setTotal(navigation.size());
                    return utils;
                });
    }

    public PageUtils<ClassificationNavigationVo> searchByLinkCount(int current, int size, int searchValue, int status) {
        PageUtils<ClassificationNavigationVo> utils = new PageUtils<>(current, size);
        return FunctionUtils.isTrueOrFalse(operationUtils.hasKey(Constant.CLASS_NAV), utils).trueOrFalseHandle(
                trueHandle -> {
                    List<ClassificationNavigationVo> navigation = (List<ClassificationNavigationVo>) operationUtils.lRange(Constant.CLASS_NAV, 0, -1);
                    List<ClassificationNavigationVo> list = navigation.stream().filter(n -> {
                        if (status == -1) {
                            return n.getNavigationCount() <= searchValue;
                        } else if (status == 1) {
                            return n.getNavigationCount() >= searchValue;
                        } else {
                            return n.getNavigationCount() == searchValue;
                        }
                    }).toList();
                    utils.setRecords(list.stream().skip((long) current * size).limit(size).toList());
                    utils.setTotal(list.size());
                    return utils;
                },
                falseHandle -> {
                    List<ClassificationNavigation> navigation = classificationNavigationMapper.selectList(new QueryWrapper<>());
                    List<ClassificationNavigationVo> vos = change(navigation).stream().filter(n -> {
                        if (status == -1) {
                            return n.getNavigationCount() <= searchValue;
                        } else if (status == 1) {
                            return n.getNavigationCount() >= searchValue;
                        } else {
                            return n.getNavigationCount() == searchValue;
                        }
                    }).toList();
                    utils.setRecords(vos.stream().skip((long) current * size).limit(size).toList());
                    utils.setSize(vos.size());
                    return utils;
                });
    }
}




