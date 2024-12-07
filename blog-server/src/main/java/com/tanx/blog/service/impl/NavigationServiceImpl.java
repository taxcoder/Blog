package com.tanx.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tanx.blog.annotation.DeleteNavigationAnnotation;
import com.tanx.blog.constant.Constant;
import com.tanx.blog.entity.common.OssEntity;
import com.tanx.blog.entity.po.ClassificationNavigation;
import com.tanx.blog.entity.po.Navigation;
import com.tanx.blog.entity.vo.AddNavigationVo;
import com.tanx.blog.entity.vo.ClassificationNavigationVo;
import com.tanx.blog.entity.vo.NavigationVo;
import com.tanx.blog.exception.DataOperationErrorException;
import com.tanx.blog.mapper.ClassificationNavigationMapper;
import com.tanx.blog.mapper.NavigationMapper;
import com.tanx.blog.service.NavigationService;
import com.tanx.blog.utils.FunctionUtils;
import com.tanx.blog.utils.PageUtils;
import com.tanx.blog.utils.RedisOperationUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

/**
 * @author 谭翔
 * @description 针对表【b_navigation(导航网站信息)】的数据库操作Service实现
 * @createDate 2024-04-21 11:37:09
 */
@Slf4j
@Service
public class NavigationServiceImpl extends ServiceImpl<NavigationMapper, Navigation>
        implements NavigationService {

    @Resource
    private NavigationMapper navigationMapper;
    @Resource
    private RedisOperationUtils operationUtils;
    @Resource
    private UploadServiceImpl uploadService;
    @Resource
    private ClassificationNavigationMapper classificationNavigationMapper;

    @Override
    public PageUtils<NavigationVo> selectByClassificationNavigationId(long id) {
        PageUtils<NavigationVo> utils = new PageUtils<>(0, 15);
        ClassificationNavigation classificationNavigation = classificationNavigationMapper.selectById(id);
        if (classificationNavigation == null) {
            throw new DataOperationErrorException("获取失败，请输入正确的参数，重新尝试！");
        }

        List<Navigation> navigation = navigationMapper.selectList(new QueryWrapper<Navigation>().eq("classification_navigation_id", id));
        List<NavigationVo> list = navigation.stream().map(n -> new NavigationVo(n.getId(), classificationNavigation.getName(),n.getUrl(), n.getTitle(), n.getDescription(), n.getFavicon(), n.isOk(), n.getCreateTime(), n.getUpdateTime())).toList();
        utils.setTotal(list.size());
        utils.setRecords(list);
        return utils;
    }

    @Override
    public List<NavigationVo> selectAll() {
        List<NavigationVo> voList;
        if (operationUtils.hasKey(Constant.NAVIGATION)) {
            voList = (List<NavigationVo>) operationUtils.lRange(Constant.NAVIGATION, 0, -1);
        } else {
            voList = navigationMapper.selectDataList();
            operationUtils.lRightPushAll(Constant.NAVIGATION, voList);
        }
        return voList;
    }

    @Override
    public PageUtils<NavigationVo> selectList(int current, int size) {
        PageUtils<NavigationVo> utils = new PageUtils<>(current, size);
        if (operationUtils.hasKey(Constant.NAVIGATION)) {
            List<NavigationVo> voList = (List<NavigationVo>) operationUtils.lRange(Constant.NAVIGATION, (long) current * size, (long) current * size + size - 1);
            utils.setTotal(operationUtils.lLen(Constant.NAVIGATION));
            utils.setRecords(voList);
        } else {
            List<NavigationVo> voList = navigationMapper.selectDataList();
            utils.setTotal(voList.size());
            utils.setRecords(voList.stream().skip((long) current * size).limit(size).toList());
            operationUtils.lRightPushAll(Constant.NAVIGATION, voList);
        }
        return utils;
    }

    @Override
    @DeleteNavigationAnnotation
    public String deleteById(long id) {
        if (navigationMapper.deleteById(id) != 1) {
            throw new DataOperationErrorException("删除失败！");
        }
        return "删除成功！";
    }

    @Override
    @DeleteNavigationAnnotation
    public String updateCnIdById(long id, long cnId) {
        Navigation navigation = navigationMapper.selectById(id);
        if (navigation == null) {
            throw new DataOperationErrorException("请传入正确的参数！");
        }

        if (navigation.getClassificationNavigationId() == cnId) {
            throw new DataOperationErrorException("数据未改变！");
        }

        if (navigationMapper.updateById(new Navigation((int) id, (int) cnId)) != 1) {
            throw new DataOperationErrorException("更新失败！");
        }
        return "更新成功！";
    }

    @Override
    @DeleteNavigationAnnotation
    public String updateTitleById(long id, String title) {
        Navigation navigation = navigationMapper.selectById(id);
        if (navigation == null) {
            throw new DataOperationErrorException("请传入正确的参数！");
        }

        if (navigation.getTitle().equals(title)) {
            throw new DataOperationErrorException("数据未改变！");
        }

        Navigation data = new Navigation();
        data.setId((int) id);
        data.setTitle(title);
        if (navigationMapper.updateById(data) != 1) {
            throw new DataOperationErrorException("更新失败！");
        }
        return "更新成功！";
    }

    @Override
    @DeleteNavigationAnnotation
    public String updateDescriptionById(long id, String description) {
        Navigation navigation = navigationMapper.selectById(id);
        if (navigation == null) {
            throw new DataOperationErrorException("请传入正确的参数！");
        }

        if (navigation.getDescription().equals(description)) {
            throw new DataOperationErrorException("数据未改变！");
        }

        Navigation data = new Navigation();
        data.setId((int) id);
        data.setDescription(description);
        if (navigationMapper.updateById(data) != 1) {
            throw new DataOperationErrorException("更新失败！");
        }
        return "更新成功！";
    }

    @Override
    @DeleteNavigationAnnotation
    public String addNavigation(AddNavigationVo navigationVo) {
        Navigation navigation = new Navigation();
        navigation.setClassificationNavigationId(navigationVo.getCnId());
        navigation.setDescription(navigationVo.getDescription());
        navigation.setTitle(navigationVo.getTitle());
        navigation.setUrl(navigationVo.getUrl());

        QueryWrapper<Navigation> wrapper = new QueryWrapper<>();
        wrapper.eq("url", navigation.getUrl());
        if(navigationMapper.selectList(wrapper).size() >= 1) {
            throw new DataOperationErrorException("添加失败，存在相同的网站，请勿重复添加！");
        }

        if(navigationVo.getFavicon() != null && !"null".equals(navigationVo.getFavicon())) {
            navigation.setFavicon(navigationVo.getFavicon());
        }
        if(navigationMapper.insert(navigation) != 1) {
            throw new DataOperationErrorException("添加失败!");
        }
        return "添加成功！";
    }

    @Override
    @DeleteNavigationAnnotation
    public String updateFaviconById(long id, MultipartFile file, OssEntity oss) {
        Navigation navigation = navigationMapper.selectById(id);
        if (navigation == null) {
            throw new DataOperationErrorException("请传入正确的参数！");
        }

        String url = uploadService.uploadOnceImage(file, oss, "tx-blog", "web/" + id);
        if (url == null) {
            throw new DataOperationErrorException("更新失败");
        }

        Navigation data = new Navigation();
        data.setId((int) id);
        data.setFavicon(url);
        if (navigationMapper.updateById(data) != 1) {
            throw new DataOperationErrorException("更新失败！");
        }
        return url;
    }

    @Override
    @DeleteNavigationAnnotation
    public String updateUrlById(long id, String url) {
        Navigation navigation = navigationMapper.selectById(id);
        if(navigation == null) {
            throw new DataOperationErrorException("请传入正确的参数！");
        }

        if(navigation.getUrl().equals(url)) {
            throw new DataOperationErrorException("数据未改变！");
        }
        navigation.setUrl(url);
        if(navigationMapper.updateById(navigation) != 1) {
            throw new DataOperationErrorException("更新失败！");
        }
        return "更新成功！";
    }

    @Override
    public PageUtils<NavigationVo> searchById(long searchValue, int current, int size) {
        return search(current, size, String.valueOf(searchValue), "id");
    }

    @Override
    public PageUtils<NavigationVo> searchByCnName(String searchValue, int current, int size) {
        return search(current, size, searchValue, "cnName");
    }

    @Override
    public PageUtils<NavigationVo> searchByTitle(String searchValue, int current, int size) {
        return search(current, size, searchValue, "title");
    }

    @Override
    public PageUtils<NavigationVo> searchByDescription(String searchValue, int current, int size) {
        return search(current, size, searchValue, "description");
    }

    @Override
    public PageUtils<NavigationVo> searchByIsOk(boolean searchValue, int current, int size) {
        PageUtils<NavigationVo> utils = new PageUtils<>(current, size);
        List<NavigationVo> navigation;
        if(operationUtils.hasKey(Constant.NAVIGATION)) {
            navigation = (List<NavigationVo>) operationUtils.lRange(Constant.NAVIGATION, 0, -1);
        } else {
            navigation = change(navigationMapper.selectList(new QueryWrapper<>()));
            operationUtils.lRightPushAll(Constant.NAVIGATION, navigation);
        }
        List<NavigationVo> list = navigation.stream().filter(n -> n.isOk() == searchValue).toList();
        utils.setRecords(list.stream().skip((long) current * size).limit(size).toList());
        utils.setTotal(list.size());
        return utils;
    }

    @Override
    public List<HashMap<String, Object>> selectCnInfo() {
        return classificationNavigationMapper.selectList(new QueryWrapper<>()).stream().map(n -> {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("value", n.getId());
            hashMap.put("label", n.getName());
            return hashMap;
        }).toList();
    }

    public PageUtils<NavigationVo> search(int current, int size, String searchValue, String condition) {
        PageUtils<NavigationVo> utils = new PageUtils<>(current, size);
        return FunctionUtils.isTrueOrFalse(operationUtils.hasKey(Constant.NAVIGATION), utils).trueOrFalseHandle(
                trueHandle -> {
                    List<NavigationVo> navigation = (List<NavigationVo>) operationUtils.lRange(Constant.NAVIGATION, 0, -1);
                    List<NavigationVo> list = navigation.stream().filter(n -> switch (condition) {
                        case "id" -> n.getId().toString().matches(".*" + searchValue + ".*");
                        case "cnName" -> n.getClassificationNavigationName().matches(".*" + searchValue + ".*");
                        case "title" -> n.getTitle().matches(".*" + searchValue + ".*");
                        case "description" -> n.getDescription().matches(".*" + searchValue + ".*");
                        default -> false;
                    }).toList();
                    utils.setRecords(list.stream().skip((long) current * size).limit(size).toList());
                    utils.setTotal(list.size());
                    return utils;
                },
                falseHandle -> {
                    QueryWrapper<Navigation> wrapper = new QueryWrapper<>();
                    wrapper.last(" LOCATE('" + searchValue + "', " + condition + ") > 0");
                    List<NavigationVo> navigation = change(navigationMapper.selectList(wrapper));
                    utils.setRecords(navigation.stream().skip((long) current * size).limit(size).toList());
                    utils.setTotal(navigation.size());
                    return utils;
                });
    }

    public List<NavigationVo> change(List<Navigation> navigation) {
        return navigation.stream().map(nav -> {
            ClassificationNavigation classificationNavigation = classificationNavigationMapper.selectById(nav.getClassificationNavigationId());
            NavigationVo vo = new NavigationVo();
            vo.setId(nav.getId());
            vo.setClassificationNavigationName(classificationNavigation.getName());
            vo.setTitle(nav.getTitle());
            vo.setFavicon(nav.getFavicon());
            vo.setOk(nav.isOk());
            vo.setDescription(nav.getDescription());
            vo.setCreateTime(nav.getCreateTime());
            vo.setUpdateTime(nav.getUpdateTime());
            return vo;
        }).toList();
    }

}




