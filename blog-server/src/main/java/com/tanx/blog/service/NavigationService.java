package com.tanx.blog.service;

import com.tanx.blog.entity.common.OssEntity;
import com.tanx.blog.entity.po.Navigation;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tanx.blog.entity.vo.AddNavigationVo;
import com.tanx.blog.entity.vo.NavigationVo;
import com.tanx.blog.utils.PageUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

/**
* @author 谭翔
* @description 针对表【b_navigation(导航网站信息)】的数据库操作Service
* @createDate 2024-04-21 11:37:09
*/
public interface NavigationService extends IService<Navigation> {

    PageUtils<NavigationVo> selectByClassificationNavigationId(long id);

    List<NavigationVo> selectAll();

    PageUtils<NavigationVo> selectList(int current,int size);

    String deleteById(long id);

    String updateCnIdById(long id, long cnId);

    String updateTitleById(long id,String title);

    String updateDescriptionById(long id, String description);

    String addNavigation(AddNavigationVo navigationVo);

    String updateFaviconById(long id, MultipartFile file, OssEntity oss);

    PageUtils<NavigationVo> searchById(long searchValue,int current, int size);

    PageUtils<NavigationVo> searchByCnName(String searchValue,int current, int size);

    PageUtils<NavigationVo> searchByTitle(String searchValue,int current, int size);

    PageUtils<NavigationVo> searchByDescription(String searchValue,int current, int size);

    PageUtils<NavigationVo> searchByIsOk(boolean searchValue,int current, int size);

    List<HashMap<String, Object>> selectCnInfo();

    String updateUrlById(long id, String url);
}
