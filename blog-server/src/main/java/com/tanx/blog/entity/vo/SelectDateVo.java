package com.tanx.blog.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @description:
 * @project_name: blog-server
 * @author: 谭翔
 * @date: 2024/4/20 12:58
 */

@Data
@Schema(title = "日期Vo", description = "保存开始时间和结束时间")
public class SelectDateVo {

    @NotNull(message = "开始时间不能为空")
    @Min(value = 1, message = "请传入正确的参数")
    @Schema(name = "start", description = "开始时间的时间戳")
    private Long start;

    @NotNull(message = "结束时间不能为空")
    @Min(value = 1, message = "请传入正确的参数")
    @Schema(name = "end", description = "结束时间的时间戳")
    private Long end;
}
