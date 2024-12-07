package com.tanx.blog.entity.vo;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @description:
 * @project_name: blog-server
 * @author: 谭翔
 * @date: 2024/4/21 21:50
 */

@Data
public class AddNavigationVo {

    @NotNull(message = "导航分类ID不能为空")
    @Min(value = 1,message = "导航分类ID输入错误")
    private Integer cnId;

    @NotNull(message = "网址不能为空")
    @Pattern(regexp = "^(http://www\\.|https://www\\.|http://|https://)?[a-z0-9]+([\\-.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(/.*)?$",message = "网址格式错误")
    private String url;

    @NotNull(message = "标题不能为空")
    @Length(max = 20, min = 1, message = "请传入正确的标题")
    private String title;

    @NotNull(message = "描述不能为空")
    @Length(max = 100, min = 1, message = "请传入正确的描述")
    private String description;

    private String favicon;
}
