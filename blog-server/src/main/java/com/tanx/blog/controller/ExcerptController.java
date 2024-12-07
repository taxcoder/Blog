package com.tanx.blog.controller;

import com.tanx.blog.entity.po.Excerpt;
import com.tanx.blog.entity.vo.ExcerptVo;
import com.tanx.blog.entity.vo.PageVo;
import com.tanx.blog.service.ExcerptService;
import com.tanx.blog.utils.PageUtils;
import com.tanx.blog.utils.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @description: 摘抄控制类
 * @project_name: blog-server
 * @author: 谭翔
 * @date: 2024/4/27 15:26
 */

@Slf4j
@Validated
@RestController
@Tag(name = "摘抄控制类")
public class ExcerptController {

    @Resource
    private ExcerptService excerptService;

    @PostMapping("/auth/excerpt/add/content")
    public Response<String> addExcerptContent(
            @Valid
            @NotNull(message = "摘抄内容不能为空！")
            String content) {
        return Response.success(excerptService.addExcerpt(content));
    }

    @PostMapping("/auth/excerpt/add/file")
    public Response<String> addExcerptFile(
            @Valid
            @NotNull(message = "摘抄内容不能为空！")
            MultipartFile file) {
        return Response.success(excerptService.addExcerpt(file));
    }

    @DeleteMapping("/auth/excerpt/delete/{id}")
    public Response<String> deleteExcerpt(
            @NotBlank(message = "请传入正确的参数！")
            @DecimalMin(value = "1", message = "请传入正确的参数！")
            @Parameter(name = "id", description = "随笔ID", required = true)
            @PathVariable String id
    ) {
        return Response.success(excerptService.deleteExcerpt(Integer.parseInt(id)));
    }

    @PutMapping("/auth/excerpt/{id}/update/content")
    public Response<String> updateExcerptContent(
            @NotBlank(message = "请传入正确的参数！")
            @DecimalMin(value = "1", message = "请传入正确的参数！")
            @Parameter(name = "id", description = "随笔ID", required = true)
            @PathVariable String id,
            @Valid
            @NotNull(message = "摘抄内容不能为空！")
            String content) {
        return Response.success("更新成功！",excerptService.updateExcerpt(Integer.parseInt(id), content));
    }

    @PutMapping("/auth/excerpt/{id}/update/file")
    public Response<String> updateExcerptFile(
            @NotBlank(message = "请传入正确的参数！")
            @DecimalMin(value = "1", message = "请传入正确的参数！")
            @Parameter(name = "id", description = "随笔ID", required = true)
            @PathVariable String id,
            @Valid
            @NotNull(message = "摘抄内容不能为空！")
            MultipartFile file) {
        return Response.success("更新成功！",excerptService.updateExcerpt(Integer.parseInt(id), file));
    }

    @GetMapping("/api/excerpt/list")
    public Response<PageUtils<ExcerptVo>> selectList(
            @Valid
            @Parameter(name = "pageVo", description = "包含当前页和每页条数的Vo", required = true)
            PageVo pageVo) {

        PageUtils<ExcerptVo> utils = excerptService.selectList(pageVo.getCurrent() - 1, pageVo.getSize());
        utils.setRecords(utils.getRecords().stream().peek(excerpt -> excerpt.setUpdateTime(null)).toList());
        return Response.success(utils);
    }

    @GetMapping("/auth/excerpt/list")
    public Response<PageUtils<ExcerptVo>> authSelectList(
            @Valid
            @Parameter(name = "pageVo", description = "包含当前页和每页条数的Vo", required = true)
            PageVo pageVo) {
        return Response.success(excerptService.selectList(pageVo.getCurrent() - 1, pageVo.getSize()));
    }

    @Operation(summary = "根据ID查询摘抄")
    @GetMapping("/auth/excerpt/search/id/{searchValue}")
    public Response<PageUtils<ExcerptVo>> selectImages(
            @Parameter(name = "id", description = "目标ID", required = true)
            @PathVariable
            @DecimalMin(value = "1", message = "请传入正确的参数！")
            @NotBlank(message = "请输入正确的参数") String searchValue,
            @Valid
            @Parameter(name = "pageVo", description = "包含当前页和每页条数的Vo", required = true)
            PageVo pageVo) {
        return Response.success(excerptService.selectListById(Integer.parseInt(searchValue), pageVo.getCurrent() - 1, pageVo.getSize()));
    }
}
