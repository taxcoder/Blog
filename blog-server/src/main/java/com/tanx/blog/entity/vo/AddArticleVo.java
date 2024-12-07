package com.tanx.blog.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @description: 增加文章类
 * @project_name: blog-server
 * @author: 谭翔
 * @date: 2023/10/4 11:11
 */

@Data
@Schema(title = "文章Vo", description = "上传文章数据需要的条件")
public class AddArticleVo {

    private String text;

    private String title;

    private Integer classificationId;

    private List<Integer> tagsId;

    private Boolean top;

    private MultipartFile image;

    private String prefixContent;
}
