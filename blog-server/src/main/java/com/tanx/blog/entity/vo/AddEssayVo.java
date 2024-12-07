package com.tanx.blog.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @description: 添加随笔
 * @project_name: blog-server
 * @author: 谭翔
 * @date: 2023/9/26 23:15
 */

@Data
@Schema(title = "随笔Vo", description = "上传随笔数据需要的条件")
public class AddEssayVo {

    @NotBlank(message = "随笔内容不能为空！")
    @Schema(name = "content", description = "存储随笔内容的属性")
    private String content;

    @NotNull(message = "Id不能为空！")
    @Min(value = 1, message = "传入了错误的参数！")
    @Schema(name = "emojiId", description = "emojiId属性")
    private Integer emojiId;

    public AddEssayVo(String content, @NotNull Integer emojiId) {
        this.content = content;
        this.emojiId = emojiId;
    }
}
