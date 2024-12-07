package com.tanx.blog.controller;

import com.tanx.blog.service.EmojiService;
import com.tanx.blog.utils.PageUtils;
import com.tanx.blog.utils.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @description: emoji类操作
 * @project_name: blog-server
 * @author: 谭翔
 * @date: 2023/9/23 20:07
 */
@Slf4j
@Validated
@RestController
@Tag(name = "emoji管理")
@RequestMapping("/auth/emoji")
public class EmojiController {

    @Resource
    private EmojiService emojiService;

    @Operation(summary = "获取所有的emoji")
    @GetMapping("/list")
    public Response<PageUtils<Map<String, ?>>> emojiAll() {
        return Response.success(emojiService.selectEmojiAll());
    }

    @Operation(summary = "删除emoji")
    @DeleteMapping("/delete/{id}")
    public Response<String> deleteEmoji(
            @PathVariable("id")
            @NotBlank(message = "请传入正确的参数！")
            @DecimalMin(value = "1", message = "请传入正确的参数！")
            @Parameter(name = "id", description = "表情ID", required = true) String id
    ) {
        return Response.success(emojiService.deleteEmoji(Long.parseLong(id)));
    }

    @Operation(summary = "更新emoji的分组名")
    @PutMapping("/update/group/{id}")
    public Response<String> updateEmoji(
            @NotBlank(message = "请传入正确的参数！")
            @DecimalMin(value = "1", message = "请传入正确的参数！")
            @Parameter(name = "id", description = "表情ID", required = true)
            @PathVariable
            String id,
            @NotBlank(message = "分组名不能为空！")
            @Length(min = 1, max = 8, message = "分组名需在1~8个字符之间")
            @Parameter(name = "name", description = "emoji分组名称", required = true)
            String name) {
        return Response.success(emojiService.updateEmojiGroupNameById(Long.parseLong(id), name));
    }

    @Operation(summary = "更新emoji名称")
    @PutMapping("/update/name/{id}")
    public Response<String> updateEmojiName(
            @NotBlank(message = "请传入正确的参数！")
            @DecimalMin(value = "1", message = "请传入正确的参数！")
            @Parameter(name = "id", description = "表情ID", required = true)
            @PathVariable
            String id,
            @NotBlank(message = "emoji名称不能为空！")
            @Length(min = 1, max = 20, message = "emoji名称需在1~20个字符之间")
            @Parameter(name = "name", description = "emoji名称", required = true)
            String name) {
        return  Response.success(emojiService.updateEmojiNameById(Long.parseLong(id), name));
    }
}
