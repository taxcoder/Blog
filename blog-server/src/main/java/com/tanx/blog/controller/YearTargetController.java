package com.tanx.blog.controller;

import com.tanx.blog.entity.po.Target;
import com.tanx.blog.service.TargetService;
import com.tanx.blog.utils.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description: 目标控制类
 * @project_name: blog-server
 * @author: 谭翔
 * @date: 2024/3/30 11:03
 */
@Slf4j
@Validated
@RestController
@Tag(name = "目标管理")
public class YearTargetController {

    @Resource
    private TargetService targetService;

    @Operation(summary = "获取每年目标")
    @GetMapping("/api/list/target/{year}")
    public Response<List<Target>> findTarget(
            @DecimalMin(value = "2000", message = "请传入正确的参数！")
            @PathVariable String year
    ) {
        return Response.success(targetService.targets(Integer.parseInt(year)));
    }

    @Operation(summary = "增加一个目标")
    @PostMapping("/auth/target/add")
    public Response<String> addTarget(
            @NotBlank(message = "请输入正确的参数") String content,
            @DecimalMin(value = "2000", message = "请传入正确的参数！")
            String year) {
        return Response.success(targetService.addTarget(content, Integer.parseInt(year)));
    }

    @Operation(summary = "删除一个目标")
    @DeleteMapping("/auth/target/delete/{id}")
    public Response<String> delTarget(
            @Parameter(name = "id", description = "目标ID", required = true)
            @PathVariable
            @DecimalMin(value = "1", message = "请传入正确的参数！")
            @NotBlank(message = "请输入正确的参数") String id) {
        return Response.success(targetService.delTarget(Long.parseLong(id)));
    }

    @Operation(summary = "更新一个目标")
    @PutMapping("/auth/target/update/content/{id}")
    public Response<String> updateTargetContent(
            @Parameter(name = "id", description = "目标ID", required = true)
            @PathVariable
            @DecimalMin(value = "1", message = "请传入正确的参数！")
            @NotBlank(message = "请输入正确的参数") String id,
            @NotBlank(message = "请输入正确的目标内容") String content) {
        return Response.success(targetService.updateTargetContent(Long.parseLong(id), content));
    }

    @Operation(summary = "更新一个目标")
    @PutMapping("/auth/target/update/status/{id}")
    public Response<String> updateTargetStatus(
            @Parameter(name = "id", description = "目标ID", required = true)
            @PathVariable
            @DecimalMin(value = "1", message = "请传入正确的参数！")
            @NotBlank(message = "请输入正确的参数") String id,
            boolean status) {
        return Response.success(targetService.updateTargetStatus(Long.parseLong(id), status));
    }
}
