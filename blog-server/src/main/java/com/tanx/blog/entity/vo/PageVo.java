package com.tanx.blog.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @description: 分页
 * @project_name: blog-server
 * @author: 谭翔
 * @date: 2023/9/27 11:00
 */

@Data
@Schema(title = "分页Vo", description = "查询需要的基础条件")
public class PageVo {

    @NotNull(message = "当前页不能为空")
    @Min(value = 1, message = "当前页最小应为第1页")
    @Schema(name = "current", description = "分页需要的当前页")
    private Integer current;

    @NotNull(message = "每页条数不能为空")
    @Min(value = 5, message = "每页条数不能小于5条")
    @Max(value = 20, message = "每页条数不能超过20条")
    @Schema(name = "size", description = "每页展示的条数")
    private Integer size;
}
