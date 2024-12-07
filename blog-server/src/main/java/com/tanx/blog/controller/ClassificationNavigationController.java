package com.tanx.blog.controller;

import com.alibaba.fastjson.JSONArray;
import com.tanx.blog.entity.vo.ClassificationNavigationVo;
import com.tanx.blog.entity.vo.LinkDataVo;
import com.tanx.blog.entity.vo.PageVo;
import com.tanx.blog.service.ClassificationNavigationService;
import com.tanx.blog.utils.CommonUtils;
import com.tanx.blog.utils.PageUtils;
import com.tanx.blog.utils.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @description: 导航分类控制类
 * @project_name: blog-server
 * @author: 谭翔
 * @date: 2024/4/21 11:39
 */


@Slf4j
@Validated
@RestController
@Tag(name = "导航分类管理")
public class ClassificationNavigationController {

    @Resource
    private ClassificationNavigationService navigationService;

    @Operation(summary = "增加一个导航分类")
    @PostMapping("/auth/classificationNavigation/add")
    public Response<String> addNav(
            @Valid
            @NotNull(message = "请输入正确的参数!") String name) {
        return Response.success(navigationService.add(name));
    }

    @Operation(summary = "删除一个导航分类")
    @DeleteMapping("/auth/classificationNavigation/delete/{id}")
    public Response<String> delNav(
            @Valid
            @PathVariable
            @NotNull(message = "请输入正确的ID!")
            @Min(value = 1, message = "请输入正确的ID!")
            String id) {
        return Response.success(navigationService.deleteById(Long.parseLong(id)));
    }

    @Operation(summary = "删除一个导航分类并且断开导航网站的链接")
    @DeleteMapping("/auth/classificationNavigation/delete/list/{id}")
    public Response<String> delNavAndCloseLink(
            @Valid
            @PathVariable
            @NotNull(message = "请输入正确的ID!")
            @Min(value = 1, message = "请输入正确的ID!")
            String id,
            @Valid
            String list) {
        List<LinkDataVo> linkDataVos = JSONArray.parseArray(list, LinkDataVo.class);
        return Response.success(navigationService.deleteByIdAndCloseLink(Long.parseLong(id), linkDataVos == null ? new ArrayList<>() : linkDataVos));
    }

    @Operation(summary = "修改一个分类")
    @PutMapping("/auth/classificationNavigation/update/{id}")
    public Response<String> updateNav(
            @Valid
            @PathVariable
            @NotNull(message = "请输入正确的ID!")
            @Min(value = 1, message = "请输入正确的ID!")
            String id,
            @Valid
            @NotNull(message = "请输入正确的参数！")
            String name,
            @Valid
            String list
    ) {
        List<LinkDataVo> linkDataVos = JSONArray.parseArray(list, LinkDataVo.class);
        return Response.success(navigationService.updateInfoById(Long.parseLong(id), name, linkDataVos == null ? new ArrayList<>() : linkDataVos));
    }

    @Operation(summary = "查询导航分类信息")
    @GetMapping("/auth/classificationNavigation/list")
    public Response<PageUtils<ClassificationNavigationVo>> selectData(
            @Valid
            @Parameter(name = "pageVo", description = "包含当前页和每页条数的Vo", required = true)
            PageVo pageVo
    ) {
        return Response.success(navigationService.select(pageVo.getCurrent() - 1, pageVo.getSize()));
    }

    @Operation(summary = "根据ID模糊查询")
    @GetMapping("/auth/classificationNavigation/search/id/{searchValue}")
    public Response<PageUtils<ClassificationNavigationVo>> selectNavById(
            @Valid
            @PathVariable
            @NotNull(message = "请输入正确的参数!")
            @Min(value = 1, message = "请输入正确的参数!")
            String searchValue,
            @Valid
            @Parameter(name = "pageVo", description = "包含当前页和每页条数的Vo", required = true)
            PageVo pageVo
    ) {
        return Response.success(navigationService.selectById(pageVo.getCurrent() - 1, pageVo.getSize(), Long.parseLong(searchValue)));
    }

    @Operation(summary = "根据Name模糊查询")
    @GetMapping("/auth/classificationNavigation/search/name/{searchValue}")
    public Response<PageUtils<ClassificationNavigationVo>> selectNavByName(
            @Valid
            @PathVariable
            @NotNull(message = "请输入正确的参数!")
            String searchValue,
            @Valid
            @Parameter(name = "pageVo", description = "包含当前页和每页条数的Vo", required = true)
            PageVo pageVo
    ) {
        return Response.success(navigationService.selectByName(pageVo.getCurrent() - 1, pageVo.getSize(), searchValue));
    }

    @Operation(summary = "根据条件查询数据，返回可选择的信息")
    @GetMapping("/auth/classificationNavigation/search/link/gte/{searchValue}")
    public Response<PageUtils<ClassificationNavigationVo>> classificationNavigationSearchGte(
            @Min(value = 0, message = "请输入正确的参数！")
            @Parameter(name = "searchValue", description = "查询数据", required = true)
            @PathVariable int searchValue,
            @Valid
            @Parameter(name = "pageVo", description = "包含当前页和每页条数的Vo", required = true)
            PageVo pageVo
    ) {
        log.info("time:{},method:classificationNavigationSearchGte,searchValue:{},pageVo:{}", CommonUtils.dateCurrent(), searchValue, pageVo);
        return Response.success(navigationService.selectSearchDataByLinkCountEqualOrGreaterThan(searchValue, pageVo.getCurrent() - 1, pageVo.getSize()));
    }

    @Operation(summary = "根据条件查询数据，返回可选择的信息")
    @GetMapping("/auth/classificationNavigation/search/link/lte/{searchValue}")
    public Response<PageUtils<ClassificationNavigationVo>> classificationNavigationSearchLte(
            @Min(value = 0, message = "请输入正确的参数！")
            @Parameter(name = "searchValue", description = "查询数据", required = true)
            @PathVariable int searchValue,
            @Valid
            @Parameter(name = "pageVo", description = "包含当前页和每页条数的Vo", required = true)
            PageVo pageVo
    ) {
        log.info("time:{},method:classificationNavigationSearchLte,searchValue:{},pageVo:{}", CommonUtils.dateCurrent(), searchValue, pageVo);
        return Response.success(navigationService.selectSearchDataByLinkCountEqualOrLessThan(searchValue, pageVo.getCurrent() - 1, pageVo.getSize()));
    }

    @Operation(summary = "根据条件查询数据，返回可选择的信息")
    @GetMapping("/auth/classificationNavigation/search/link/equ/{searchValue}")
    public Response<PageUtils<ClassificationNavigationVo>> classificationNavigationSearchEqu(
            @Min(value = 0, message = "请输入正确的参数！")
            @Parameter(name = "searchValue", description = "查询数据", required = true)
            @PathVariable int searchValue,
            @Valid
            @Parameter(name = "pageVo", description = "包含当前页和每页条数的Vo", required = true)
            PageVo pageVo
    ) {
        log.info("time:{},method:classificationNavigationSearchEqu,searchValue:{},pageVo:{}", CommonUtils.dateCurrent(), searchValue, pageVo);
        return Response.success(navigationService.selectSearchDataByLinkCountEqual(searchValue, pageVo.getCurrent() - 1, pageVo.getSize()));
    }
}
