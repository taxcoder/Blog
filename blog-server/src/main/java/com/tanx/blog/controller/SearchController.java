package com.tanx.blog.controller;

import cn.hutool.core.util.NumberUtil;
import com.tanx.blog.entity.dto.ArticleDto;
import com.tanx.blog.entity.dto.TagDto;
import com.tanx.blog.exception.DataOperationErrorException;
import com.tanx.blog.service.ArticleService;
import com.tanx.blog.service.TagService;
import com.tanx.blog.utils.PageUtils;
import com.tanx.blog.utils.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: 查询控制类
 * @project_name: blog-server
 * @author: 谭翔
 * @date: 2024/1/10 16:56
 */

@Slf4j
@Validated
@RestController
@Tag(name = "前台查询管理")
@RequestMapping("/api/search")
public class SearchController {

    @Resource
    private ArticleService articleService;
    @Resource
    private TagService tagService;

    @Operation(summary = "根据当前页和每页条数获取指定个数的文章集合")
    @GetMapping("/article/list/{value}")
    public Response<PageUtils<ArticleDto>> searchArticle(
            @Valid
            @NotBlank(message = "请输入正确的查询条件！")
            @Parameter(name = "value", description = "查询的条件", required = true)
            @PathVariable String value,
            /*
            sort:
            -1: 表示综合排序，默认从低到高
             0: 表示最新排序
             1: 表示最热排序
            */
            @Valid
            @NotNull(message = "参数不能为空！")
            @Max(value = 1, message = "排序条件错误!")
            @Min(value = -1, message = "排序条件错误!")
            String s,
            /*
            time:
           -1: 表示时间不限
            1: 表示当前一天
            7: 表示当前一周
           90: 表示当前三个月
            */
            @Valid
            @NotNull(message = "参数不能为空！")
            @Min(value = -1, message = "排序条件错误!")
            String t,
            @Valid
            @NotNull(message = "参数不能为空！")
            @Min(value = 1, message = "请输入正确的参数!")
            String c) {

        log.info("method:searchArticle,params: value:{},s:{},t:{},c:{}", value, s, t, c);
        if (!NumberUtil.isNumber(t)) {
            throw new DataOperationErrorException("排序条件错误!");
        }
        int time = Integer.parseInt(t);
        if (time != -1 && time != 1 && time != 7 && time != 90) {
            throw new DataOperationErrorException("排序条件错误!");
        }

        return Response.success("请求成功!", articleService.selectArticleSearchData(value, Integer.parseInt(s), time, Long.parseLong(c) - 1, 20));
    }

    @Operation(summary = "根据当前页和每页条数获取指定个数的标签集合")
    @GetMapping("/tag/list/{value}")
    public Response<PageUtils<TagDto>> searchTag(
            @Valid
            @NotBlank(message = "请输入正确的查询条件！")
            @Parameter(name = "value", description = "查询的条件", required = true)
            @PathVariable String value,
            @Valid
            @NotNull(message = "参数不能为空！")
            @Min(value = 1, message = "请输入正确的参数!")
            String c) {
        return Response.success("请求成功!",tagService.selectTagSearchData(value, Long.parseLong(c) - 1, 20));
    }
}
