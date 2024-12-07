package com.tanx.blog.controller;

import com.tanx.blog.entity.common.OssEntity;
import com.tanx.blog.entity.po.Images;
import com.tanx.blog.entity.vo.PageVo;
import com.tanx.blog.entity.vo.SelectDateVo;
import com.tanx.blog.service.AliyunOssService;
import com.tanx.blog.service.ImagesService;
import com.tanx.blog.service.UploadService;
import com.tanx.blog.utils.CommonUtils;
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
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.List;

/**
 * @description: 图集控制类
 * @project_name: blog-server
 * @author: 谭翔
 * @date: 2024/3/30 11:03
 */
@Slf4j
@Validated
@RestController
@Tag(name = "图集管理")
public class ImagesController {

    @Resource
    private ImagesService imagesService;
    @Resource
    private UploadService uploadService;
    @Resource
    private OssEntity oss;
    @Resource
    private AliyunOssService aliyunOssService;

    @Operation(summary = "获取所有图集")
    @GetMapping("/api/images/list")
    public Response<List<Images>> findImages() {
        List<Images> images = imagesService.imageList();
        return Response.success(images.stream().peek(i -> {
            i.setCreateTime(null);
            i.setUpdateTime(null);
        }).toList());
    }

    @Operation(summary = "获取所有图集")
    @GetMapping("/auth/images/list")
    public Response<PageUtils<Images>> findAuthImages(
            @Valid
            @Parameter(name = "pageVo", description = "包含当前页和每页条数的Vo", required = true)
            PageVo pageVo
    ) {
        log.info("pageVo:{}", pageVo);
        return Response.success(imagesService.images(pageVo.getCurrent(), pageVo.getSize()));
    }

    @Operation(summary = "增加多张图片")
    @PostMapping("/auth/images/add/more")
    public Response<String> addMoreImages(
            @Valid
            @NotNull(message = "上传文件不能为空！")
            @Parameter(name = "files", description = "上传文件的流", required = true)
            MultipartFile[] files,
            @Valid
            @NotNull(message = "错误的参数")
            @Min(value = 1, message = "请传入正确的参数")
            Long timestamp) {
        log.info("more:timestamp:{}", timestamp);
        return Response.success(imagesService.addImages(aliyunOssService.updateOss(oss, "tx-blog"), uploadService.uploadAtlasImages(files, oss, timestamp)));
    }

    @Operation(summary = "增加一张图片")
    @PostMapping("/auth/images/add/one")
    public Response<String> addOneImages(
            @Valid
            @NotNull(message = "上传文件不能为空！")
            @Parameter(name = "file", description = "上传文件的流", required = true)
            MultipartFile file,
            @Valid
            @NotNull(message = "错误的参数")
            @Min(value = 1, message = "请传入正确的参数")
            Long timestamp) {
        log.info("one:timestamp:{}", timestamp);
        return Response.success(imagesService.addImage(uploadService.uploadOnceImage(file, oss, "tx-blog", "atlas/" + CommonUtils.currentDate() + "/"), new Timestamp(timestamp)));
    }

    @Operation(summary = "删除一张图片")
    @DeleteMapping("/auth/images/delete/{id}")
    public Response<String> delImages(
            @Parameter(name = "id", description = "目标ID", required = true)
            @PathVariable
            @DecimalMin(value = "1", message = "请传入正确的参数！")
            @NotBlank(message = "请输入正确的参数") String id) {
        return Response.success(imagesService.delImages(Long.parseLong(id)));
    }

    @Operation(summary = "根据ID查询图片")
    @GetMapping("/auth/images/search/id/{id}")
    public Response<PageUtils<Images>> selectImages(
            @Parameter(name = "id", description = "目标ID", required = true)
            @PathVariable
            @DecimalMin(value = "1", message = "请传入正确的参数！")
            @NotBlank(message = "请输入正确的参数") String id,
            @Valid
            @Parameter(name = "pageVo", description = "包含当前页和每页条数的Vo", required = true)
            PageVo pageVo) {
        return Response.success(imagesService.selectImages(Long.parseLong(id), pageVo.getCurrent() - 1, pageVo.getSize()));
    }

    @Operation(summary = "根据指定的日期查询图片")
    @GetMapping("/auth/images/search/time")
    public Response<PageUtils<Images>> selectImagesByTime(
            @Valid
            @Parameter(name = "pageVo", description = "包含当前页和每页条数的Vo", required = true)
            PageVo pageVo,
            @Valid
            @Parameter(name = "timestamps", description = "包含开始时间和结束时间的Vo", required = true)
            SelectDateVo dateVo) {
        return Response.success(imagesService.selectImagesByTime(dateVo.getStart(), dateVo.getEnd(), pageVo.getCurrent() - 1, pageVo.getSize()));
    }
}
