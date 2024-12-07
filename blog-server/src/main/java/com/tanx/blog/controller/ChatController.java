package com.tanx.blog.controller;

import com.tanx.blog.entity.po.Chat;
import com.tanx.blog.service.ChatService;
import com.tanx.blog.utils.Response;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 聊天控制类
 * @project_name: blog-server
 * @author: 谭翔
 * @date: 2024/4/27 16:22
 */

@Slf4j
@Validated
@RestController
@Tag(name = "聊天管理")
public class ChatController {

    @Resource
    private ChatService chatService;

    @PostMapping("/api/chat/add")
    public Response<Chat> addChat(
            @Valid
            @NotBlank(message = "请输入正确的内容！")
            String content,
            @Valid
            @NotBlank(message = "请输入昵称！")
            String author,
            @Valid
            @Pattern(regexp = "^[0-9A-Za-z_]+([-+.][0-9A-Za-z_]+)*@[0-9A-Za-z_]+([-.][0-9A-Za-z_]+)*\\.[0-9A-Za-z_]+([-.][0-9A-Za-z_]+)*$", message = "请输入正确的邮箱！")
            String email,
            String webUrl,
            @Valid
            @NotNull(message = "传入错误！")
            Long eId
            ) {
        return Response.success("评论成功！",chatService.addChat(eId, content, author, email, webUrl, false));
    }

    @PostMapping("/admin/chat/add")
    public Response<Chat> adminAddChat(
            @Valid
            @NotBlank(message = "请输入正确的内容！")
            String content,
            @Valid
            @NotBlank(message = "请输入昵称！")
            String author,
            @Valid
            @Pattern(regexp = "^[0-9A-Za-z_]+([-+.][0-9A-Za-z_]+)*@[0-9A-Za-z_]+([-.][0-9A-Za-z_]+)*\\.[0-9A-Za-z_]+([-.][0-9A-Za-z_]+)*$", message = "请输入正确的邮箱！")
            String email,
            String webUrl,
            @Valid
            @NotNull(message = "传入错误！")
            Long eId
    ) {
        return Response.success("评论成功！",chatService.addChat(eId, content, author, email, webUrl, true));
    }

    @DeleteMapping("/auth/chat/delete/{id}")
    public Response<String> deleteChat(
            @NotBlank(message = "请传入正确的参数！")
            @DecimalMin(value = "1", message = "请传入正确的参数！")
            @Parameter(name = "id", description = "聊天ID", required = true)
            @PathVariable String id
    ) {
        return Response.success(chatService.deleteChat(Long.parseLong(id)));
    }

    @DeleteMapping("/auth/chat/delete/list")
    public Response<String> deleteChat(
            @Valid
            @RequestBody
            @NotNull(message = "数据不能为空！")
            ArrayList<Long> ids
    ) {
        return Response.success(chatService.deleteChats(ids));
    }


    @GetMapping("/api/chat/list/{id}")
    public Response<List<Chat>> selectAll(
            @NotBlank(message = "请传入正确的参数！")
            @DecimalMin(value = "1", message = "请传入正确的参数！")
            @Parameter(name = "id", description = "摘抄ID", required = true)
            @PathVariable String id
    ) {
        return Response.success(chatService.selectChat(Long.parseLong(id)));
    }
}
