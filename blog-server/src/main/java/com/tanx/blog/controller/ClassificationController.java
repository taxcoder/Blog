package com.tanx.blog.controller;

import com.alibaba.fastjson.JSONArray;
import com.tanx.blog.entity.dto.ClassificationArticleDto;
import com.tanx.blog.entity.dto.ClassificationDto;
import com.tanx.blog.entity.vo.LinkDataVo;
import com.tanx.blog.entity.vo.PageVo;
import com.tanx.blog.service.ClassificationService;
import com.tanx.blog.utils.PageUtils;
import com.tanx.blog.utils.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @description: 分类
 * @project_name: blog-server
 * @author: 谭翔
 * @date: 2023/9/20 21:32
 */

@Slf4j
@Validated
@RestController
@Tag(name = "分类管理")
public class ClassificationController {

    @Resource
    private ClassificationService classificationService;

    @Operation(summary = "获取所有的分类和对应分类的文章")
    @GetMapping("/auth/classification/limit")
    public Response<PageUtils<ClassificationArticleDto>> classificationLimit(
            @Valid
            @Parameter(name = "pageVo", description = "包含当前页和每页条数的Vo", required = true)
            PageVo pageVo
    ) {
        return Response.success(classificationService.selectClassificationLimit(pageVo.getCurrent() - 1, pageVo.getSize()));
    }

    @Operation(summary = "获取所有的分类")
    @GetMapping("/auth/classification/all")
    public Response<List<ClassificationDto>> classificationAll() {
        return Response.success(classificationService.selectAll());
    }

    @Operation(summary = "根据Id修改分类名称")
    @PutMapping("/auth/classification/update/info/{id}")
    public Response<String> classificationUpdateInfoById(
            @NotBlank(message = "请传入正确的参数！")
            @DecimalMin(value = "1", message = "请传入正确的参数！")
            @Parameter(name = "id", description = "分类ID", required = true)
            @PathVariable String id,
            @NotBlank(message = "分类名不能为空！")
            @Length(min = 2, max = 4, message = "分类名需在2~4个字符之间")
            @Parameter(name = "name", description = "分类名称", required = true)
            String name,
            @Valid
            String list) {
        List<LinkDataVo> classificationVos = JSONArray.parseArray(list, LinkDataVo.class);
        return Response.success(classificationService.updateClassificationInfoById(Integer.parseInt(id), name, classificationVos == null ? new ArrayList<>() : classificationVos), null);
    }
    @Operation(summary = "根据Id删除分类")
    @DeleteMapping("/auth/classification/delete/{id}")
    public Response<String> classificationDeleteById(
            @NotBlank(message = "请传入正确的参数！")
            @DecimalMin(value = "1", message = "请传入正确的参数！")
            @Parameter(name = "id", description = "分类ID", required = true)
            @PathVariable String id,
            @RequestBody
            @Parameter(name = "list", description = "包含文章ID的数组", required = true)
            List<HashMap<String, Integer>> list) {

        return Response.success(classificationService.classificationDeleteById(Integer.parseInt(id), list), null);
    }

    @Operation(summary = "添加一个分类")
    @PostMapping("/auth/classification/add/{name}")
    public Response<ClassificationDto> classificationDeleteById(
            @NotBlank(message = "请传入正确的参数！")
            @Parameter(name = "name", description = "分类名", required = true)
            @PathVariable String name) {

        return Response.success("分类添加成功！", classificationService.classificationAdd(name));
    }

    @Operation(summary = "根据条件查询数据，返回可选择的信息")
    @GetMapping("/auth/classification/search/id/{searchValue}")
    public Response<PageUtils<ClassificationArticleDto>> classificationSearchById(
            @Min(value = 1, message = "请输入正确的ID！")
            @Parameter(name = "searchValue", description = "查询数据", required = true)
            @PathVariable int searchValue,
            @Valid
            @Parameter(name = "pageVo", description = "包含当前页和每页条数的Vo", required = true)
            PageVo pageVo
    ) {
        System.out.println(searchValue);
        return Response.success(classificationService.selectSearchDataById(searchValue, pageVo.getCurrent() - 1, pageVo.getSize()));
    }

    @Operation(summary = "根据条件查询数据，返回可选择的信息")
    @GetMapping("/auth/classification/search/name/{searchValue}")
    public Response<PageUtils<?>> classificationSearchByName(
            @NotBlank(message = "请传入正确的参数！")
            @Parameter(name = "searchValue", description = "查询数据", required = true)
            @PathVariable String searchValue,
            @Valid
            @Parameter(name = "pageVo", description = "包含当前页和每页条数的Vo", required = true)
            PageVo pageVo
    ) {
        return Response.success(classificationService.selectSearchDataLikeName(searchValue, pageVo.getCurrent() - 1, pageVo.getSize()));
    }

    @Operation(summary = "根据条件查询数据，返回可选择的信息")
    @GetMapping("/auth/classification/search/like/gte/{searchValue}")
    public Response<PageUtils<?>> classificationSearchGte(
            @Min(value = 0, message = "请输入正确的参数！")
            @Parameter(name = "searchValue", description = "查询数据", required = true)
            @PathVariable int searchValue,
            @Valid
            @Parameter(name = "pageVo", description = "包含当前页和每页条数的Vo", required = true)
            PageVo pageVo
    ) {
        return Response.success(classificationService.selectSearchDataByArticleCountEqualOrGreaterThan(searchValue, pageVo.getCurrent() - 1, pageVo.getSize()));
    }

    @Operation(summary = "根据条件查询数据，返回可选择的信息")
    @GetMapping("/auth/classification/search/like/lte/{searchValue}")
    public Response<PageUtils<?>> classificationSearchLte(
            @Min(value = 0, message = "请输入正确的参数！")
            @Parameter(name = "searchValue", description = "查询数据", required = true)
            @PathVariable int searchValue,
            @Valid
            @Parameter(name = "pageVo", description = "包含当前页和每页条数的Vo", required = true)
            PageVo pageVo
    ) {
        return Response.success(classificationService.selectSearchDataByArticleCountEqualOrLessThan(searchValue, pageVo.getCurrent() - 1, pageVo.getSize()));
    }

    @Operation(summary = "根据条件查询数据，返回可选择的信息")
    @GetMapping("/auth/classification/search/like/equ/{searchValue}")
    public Response<PageUtils<?>> classificationSearchEqu(
            @Min(value = 0, message = "请输入正确的参数！")
            @Parameter(name = "searchValue", description = "查询数据", required = true)
            @PathVariable int searchValue,
            @Valid
            @Parameter(name = "pageVo", description = "包含当前页和每页条数的Vo", required = true)
            PageVo pageVo
    ) {
        return Response.success(classificationService.selectSearchDataByArticleCountEqual(searchValue, pageVo.getCurrent() - 1, pageVo.getSize()));
    }
}
