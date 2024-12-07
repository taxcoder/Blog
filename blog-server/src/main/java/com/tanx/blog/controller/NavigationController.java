package com.tanx.blog.controller;

import com.tanx.blog.entity.common.OssEntity;
import com.tanx.blog.entity.vo.AddNavigationVo;
import com.tanx.blog.entity.vo.NavigationVo;
import com.tanx.blog.entity.vo.PageVo;
import com.tanx.blog.service.NavigationService;
import com.tanx.blog.service.UploadService;
import com.tanx.blog.utils.PageUtils;
import com.tanx.blog.utils.RedisOperationUtils;
import com.tanx.blog.utils.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

/**
 * @description: 导航网站控制类
 * @project_name: blog-server
 * @author: 谭翔
 * @date: 2024/4/21 19:10
 */

@Slf4j
@Validated
@RestController
@Tag(name = "导航网站管理")
public class NavigationController {

    @Resource
    private NavigationService navigationService;
    @Resource
    private OssEntity oss;
    @Resource
    private UploadService uploadService;
    @Resource
    private RedisOperationUtils operationUtils;

    @Operation(summary = "根据导航分类ID查询导航网站")
    @GetMapping("/auth/navigation/select/classificationNavigation/{id}")
    public Response<PageUtils<NavigationVo>> selectData(
            @Valid
            @PathVariable
            @NotNull(message = "请输入正确的参数!")
            @Min(value = 1, message = "请输入正确的参数!")
            String id
    ) {
        return Response.success(navigationService.selectByClassificationNavigationId(Long.parseLong(id)));
    }

    @Operation(summary = "获取所有的导航网站")
    @GetMapping("/auth/navigation/list")
    public Response<PageUtils<NavigationVo>> selectList(
            @Valid
            @Parameter(name = "pageVo", description = "包含当前页和每页条数的Vo", required = true)
            PageVo pageVo
    ) {
        return Response.success(navigationService.selectList(pageVo.getCurrent() - 1, pageVo.getSize()));
    }

    @Operation(summary = "获取所有的导航网站")
    @GetMapping("/api/navigation/list")
    public Response<List<NavigationVo>> selectAll() {
        return Response.success(navigationService.selectAll());
    }

    @Operation(summary = "删除一个导航网站")
    @DeleteMapping("/auth/navigation/delete/{id}")
    public Response<String> deleteById(
            @Valid
            @PathVariable
            @NotNull(message = "请输入正确的参数!")
            @Min(value = 1, message = "请输入正确的参数!")
            String id
    ) {
        return Response.success(navigationService.deleteById(Long.parseLong(id)));
    }

    @Operation(summary = "更新导航网站所处的分类")
    @PutMapping("/auth/navigation/update/cnId/{id}")
    public Response<String> updateCnIdById(
            @Valid
            @PathVariable
            @NotNull(message = "请输入正确的参数!")
            @Min(value = 1, message = "请输入正确的参数!")
            String id,
            @Valid
            @NotNull(message = "请输入正确的参数!")
            @Min(value = 1, message = "请输入正确的参数!")
            String cnId
    ) {
        return Response.success(navigationService.updateCnIdById(Long.parseLong(id), Long.parseLong(cnId)));
    }

    @Operation(summary = "更新导航网站的标题")
    @PutMapping("/auth/navigation/update/title/{id}")
    public Response<String> updateTitleById(
            @Valid
            @PathVariable
            @NotNull(message = "请输入正确的参数!")
            @Min(value = 1, message = "请输入正确的参数!")
            String id,
            @Valid
            @NotNull(message = "请输入正确的参数!")
            @Length(max = 20, min = 1, message = "请传入正确的标题")
            String title
    ) {
        return Response.success(navigationService.updateTitleById(Long.parseLong(id), title));
    }

    @Operation(summary = "更新导航网站的描述")
    @PutMapping("/auth/navigation/update/description/{id}")
    public Response<String> updateDescriptionById(
            @Valid
            @PathVariable
            @NotNull(message = "请输入正确的参数!")
            @Min(value = 1, message = "请输入正确的参数!")
            String id,
            @Valid
            @NotNull(message = "请输入正确的参数!")
            @Length(max = 100, min = 1, message = "请传入正确的描述")
            String description
    ) {
        return Response.success(navigationService.updateDescriptionById(Long.parseLong(id), description));
    }

    @Operation(summary = "更新导航网站的图标")
    @PutMapping("/auth/navigation/update/favicon/{id}")
    public Response<String> updateFaviconById(
            @Valid
            @PathVariable
            @NotNull(message = "请输入正确的参数!")
            @Min(value = 1, message = "请输入正确的参数!")
            String id,
            @Valid
            @NotNull(message = "请输入正确的参数!")
            MultipartFile file
    ) {
        return Response.success(navigationService.updateFaviconById(Long.parseLong(id), file, oss));
    }

    @Operation(summary = "更新导航网站的网址")
    @PutMapping("/auth/navigation/update/url/{id}")
    public Response<String> updateUrlById(
            @Valid
            @PathVariable
            @NotNull(message = "请输入正确的参数！")
            @Min(value = 1, message = "请输入正确的参数!")
            String id,
            @Valid
            @NotNull(message = "请输入正确的网址！")
            @Pattern(regexp = "^(http://www\\.|https://www\\.|http://|https://)?[a-z0-9]+([\\-.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(/.*)?$",message = "格式错误！")
            String url
    ) {
        return Response.success(navigationService.updateUrlById(Long.parseLong(id), url));
    }

    @Operation(summary = "增加一个导航网站")
    @PostMapping("/auth/navigation/add")
    public Response<String> updateDescriptionById(
            @Valid
            @NotNull(message = "请输入正确的参数!")
            AddNavigationVo navigationVo
    ) {
        return Response.success(navigationService.addNavigation(navigationVo));
    }

    @Operation(summary = "根据ID模糊查询")
    @GetMapping("/auth/navigation/search/id/{searchValue}")
    public Response<PageUtils<NavigationVo>> searchById(
            @Valid
            @PathVariable
            @NotNull(message = "请输入正确的参数!")
            @Min(value = 1, message = "请输入正确的参数!")
            String searchValue,
            @Valid
            @Parameter(name = "pageVo", description = "包含当前页和每页条数的Vo", required = true)
            PageVo pageVo
    ) {
        return Response.success(navigationService.searchById(Long.parseLong(searchValue), pageVo.getCurrent() -1, pageVo.getSize()));
    }

    @Operation(summary = "根据导航分类ID模糊查询")
    @GetMapping("/auth/navigation/search/cnId/{searchValue}")
    public Response<PageUtils<NavigationVo>> searchByCnId(
            @Valid
            @PathVariable
            @NotNull(message = "请输入正确的参数!")
            @Min(value = 1, message = "请输入正确的参数!")
            String searchValue,
            @Valid
            @Parameter(name = "pageVo", description = "包含当前页和每页条数的Vo", required = true)
            PageVo pageVo
    ) {
        return Response.success(navigationService.searchByCnName(searchValue, pageVo.getCurrent() -1, pageVo.getSize()));
    }

    @Operation(summary = "根据导航分类ID模糊查询")
    @GetMapping("/auth/navigation/search/title/{searchValue}")
    public Response<PageUtils<NavigationVo>> searchByTitle(
            @Valid
            @PathVariable
            @NotNull(message = "请输入正确的参数!")
            @Length(max = 20, min = 1, message = "不在范围内")
            String searchValue,
            @Valid
            @Parameter(name = "pageVo", description = "包含当前页和每页条数的Vo", required = true)
            PageVo pageVo
    ) {
        return Response.success(navigationService.searchByTitle(searchValue, pageVo.getCurrent() -1, pageVo.getSize()));
    }

    @Operation(summary = "根据导航分类ID模糊查询")
    @GetMapping("/auth/navigation/search/description/{searchValue}")
    public Response<PageUtils<NavigationVo>> searchByDescription(
            @Valid
            @PathVariable
            @NotNull(message = "请输入正确的参数!")
            @Length(max = 100, min = 1, message = "不在范围内")
            String searchValue,
            @Valid
            @Parameter(name = "pageVo", description = "包含当前页和每页条数的Vo", required = true)
            PageVo pageVo
    ) {
        return Response.success(navigationService.searchByDescription(searchValue, pageVo.getCurrent() -1, pageVo.getSize()));
    }

    @Operation(summary = "根据导航分类ID模糊查询")
    @GetMapping("/auth/navigation/search/ok/{searchValue}")
    public Response<PageUtils<NavigationVo>> searchByIsOk(
            @Valid
            @PathVariable
            Boolean searchValue,
            @Valid
            @Parameter(name = "pageVo", description = "包含当前页和每页条数的Vo", required = true)
            PageVo pageVo
    ) {
        return Response.success(navigationService.searchByIsOk(searchValue, pageVo.getCurrent() -1, pageVo.getSize()));
    }

    @Operation(summary = "获取所有的导航分类")
    @GetMapping("/auth/navigation/select/cnInfo")
    public Response<List<HashMap<String, Object>>> selectCnInfo() {
        return Response.success(navigationService.selectCnInfo());
    }
}
