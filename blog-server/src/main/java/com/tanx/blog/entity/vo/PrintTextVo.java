package com.tanx.blog.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @description: 打印文本Vo
 * @project_name: blog-server
 * @author: 谭翔
 * @date: 2023/10/21 15:53
 */

@Data
@Schema(title = "文章Vo", description = "上传文章数据需要的条件")
public class PrintTextVo {

    /**
     * 文本打印ID
     */
    @NotNull(message = "id不能为空！")
    @Min(value = 0, message = "传入参数异常！")
    @Schema(title = "id", description = "上传文章数据需要的条件")
    private Integer id;

    /**
     * 需要打印的名称
     */
    @NotBlank(message = "文本内容不能为空！")
    @Schema(title = "content", description = "打印文本内容！")
    private String content;

    public PrintTextVo() {
    }

    public PrintTextVo(String content) {
        this.content = content;
    }
}
