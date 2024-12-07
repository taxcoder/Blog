package com.tanx.blog.controller;

import com.tanx.blog.entity.common.OssEntity;
import com.tanx.blog.service.UploadService;
import com.tanx.blog.utils.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.parameters.P;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/**
 * @description: 专门负责上传数据的控制类
 * @project_name: blog-server
 * @author: 谭翔
 * @date: 2023/8/9 17:37
 */

@Slf4j
@Validated
@RestController
@Tag(name = "文件上传")
@RequestMapping("/auth/upload")
public class UploadController {

    @Resource
    private OssEntity oss;

    @Resource
    private UploadService uploadService;

    /**
     * 上传头像，接受blob元素
     *
     * @param file MultipartFile对象包含需要上传的图片信息
     * @return 返回是否上传成功的消息
     */
    @Operation(summary = "上传头像到存储空间")
    @PostMapping("/head")
    public Response<?> uploadHeaderImage(
            @Valid
            @NotNull(message = "上传文件不能为空！")
            @Parameter(name = "file", description = "上传文件的流", required = true)
            MultipartFile file) {
        return Response.success("上传成功！", uploadService.uploadHead(file, oss));
    }

    @Operation(summary = "上传背景图到存储空间")
    @PostMapping("/background")
    public Response<?> uploadBackgroundImage(
            @Valid
            @NotNull(message = "上传文件不能为空！")
            @Parameter(name = "file", description = "上传文件的流", required = true)
            MultipartFile file) {
        return Response.success("上传成功！", uploadService.uploadBackground(file, oss));
    }

    @Operation(summary = "上传文章内图片")
    @PostMapping("/image/article")
    public Response<?> uploadImageArticle(
            @Valid
            @NotNull(message = "上传文件不能为空！")
            @Parameter(name = "file", description = "上传文件的流", required = true)
            MultipartFile file) {
        return Response.success("上传成功！", uploadService.uploadOnceImage(file, oss, "tanxiang-typora-images", "images/article-"));
    }

    @Operation(summary = "上传随笔内图片")
    @PostMapping("/image/essay")
    public Response<?> uploadImageEssay(
            @Valid
            @NotNull(message = "上传文件不能为空！")
            @Parameter(name = "file", description = "上传文件的流", required = true)
            MultipartFile file) {
        return Response.success(uploadService.uploadOnceImage(file, oss, "tx-blog", "essay/essay-"));
    }

    @Operation(summary = "上传表情")
    @PostMapping("/image/emoji")
    public Response<?> uploadImageEmoji(
            @Valid
            @NotNull(message = "上传文件不能为空！")
            @Parameter(name = "files", description = "上传文件的流", required = true)
            MultipartFile[] files,
            @Valid
            @NotNull(message = "文件名称不能为空！")
            @Parameter(name = "names", description = "文件名称", required = true)
            String[] names,
            @Valid
            @NotNull(message = "分组名！")
            @Parameter(name = "groupName", description = "分组名称", required = true)
            String groupName) throws IOException {
        Map<String, Object> stringObjectMap = uploadService.uploadEmoji(files, oss, groupName, names);
        int code = (int) stringObjectMap.get("code");
        // 如果上传成功，就将上传失败的图片的名字返回前端，告知哪些上传失败！
        // 如果上传失败，就将上传成功的图片的地址返回前端，告知哪些图片上传成功！
        return code == 200 ? Response.success("上传成功！", stringObjectMap.get("data")) : Response.error(code, "上传失败！", stringObjectMap.get("data"));
    }
}
