package com.tanx.blog.controller;

import cn.hutool.core.util.NumberUtil;
import com.tanx.blog.api.IpAddressInfoApi;
import com.tanx.blog.entity.dto.ArticleDto;
import com.tanx.blog.entity.vo.AddArticleVo;
import com.tanx.blog.entity.vo.PageVo;
import com.tanx.blog.exception.DataOperationErrorException;
import com.tanx.blog.service.ArticleService;
import com.tanx.blog.utils.CommonUtils;
import com.tanx.blog.utils.IPUtils;
import com.tanx.blog.utils.PageUtils;
import com.tanx.blog.utils.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


@Slf4j
@Validated
@RestController
@Tag(name = "文章管理")
public class ArticleController {

    @Resource
    private ArticleService articleService;
    @Resource
    private IpAddressInfoApi ipAddressInfoApi;

    private List<Integer> changeTagsId2List(String tagsId) {
        if (!tagsId.startsWith("[") || !tagsId.endsWith("]")) {
            throw new DataOperationErrorException("标签ID必须传入");
        }
        List<Integer> integers = new ArrayList<>();
        String[] strings = tagsId.replace("[", "").replace("]", "").split(",");
        Arrays.stream(strings).forEach(s -> {
            if (!NumberUtil.isNumber(s)) throw new DataOperationErrorException("标签ID必须传入");
            integers.add(Integer.parseInt(s));
        });
        return integers;
    }

    @Operation(summary = "根据当前页和每页条数获取指定个数的文章集合")
    @GetMapping("/api/article/list")
    public Response<PageUtils<ArticleDto>> listArticleLimit(
            @Valid
            @Parameter(name = "pageVo", description = "包含当前页和每页条数的Vo", required = true)
            PageVo pageVo) {
        log.info("time:{},method:listArticleLimit,pageVo:{}", CommonUtils.dateCurrent(), pageVo);
        return Response.success(articleService.selectArticleList(pageVo.getCurrent() - 1, pageVo.getSize(), false, true));
    }

    @Operation(summary = "获取所有的文章，以归档的形式返回")
    @GetMapping("/api/article/archived/list")
    public Response<List<ArticleDto>> listArticleArchived() {
        log.info("time:{},method:listArticleArchived", CommonUtils.dateCurrent());
        return Response.success(articleService.selectArticleArchivedList());
    }

    @Operation(summary = "根据标签Id获取对应的文章")
    @GetMapping("/api/article/tag/{id}")
    public Response<PageUtils<ArticleDto>> listArticleTagById(
            @Valid
            @Parameter(name = "pageVo", description = "包含当前页和每页条数的Vo", required = true) PageVo pageVo,
            @NotBlank(message = "请传入正确的参数！")
            @DecimalMin(value = "1", message = "请传入正确的参数！")
            @Parameter(name = "id", description = "分类ID", required = true)
            @PathVariable String id
    ) {
        log.info("time:{},method:listArticleTagById,id:{}", CommonUtils.dateCurrent(), id);
        return Response.success(articleService.selectArticleTagListById(pageVo.getCurrent() - 1, pageVo.getSize(), Integer.parseInt(id)));
    }

    @Operation(summary = "根据Id和当前页、每页条数获取分类集合")
    @GetMapping("/api/article/classification/{id}")
    public Response<PageUtils<ArticleDto>> listArticleClassificationById(
            @Valid
            @Parameter(name = "pageVo", description = "包含当前页和每页条数的Vo", required = true) PageVo pageVo,
            @NotBlank(message = "请传入正确的参数！")
            @DecimalMin(value = "1", message = "请传入正确的参数！")
            @Parameter(name = "id", description = "分类ID", required = true)
            @PathVariable String id
    ) {
        log.info("time:{},method:listArticleClassificationById,id:{}", CommonUtils.dateCurrent(), id);
        return Response.success(articleService.selectArticleClassificationListById(pageVo.getCurrent() - 1, pageVo.getSize(), Integer.parseInt(id)));
    }

    @Operation(summary = "根据Id获取文章信息")
    @GetMapping("/api/article/info/{id}")
    public Response<ArticleDto> articleById(
            @NotBlank(message = "请传入正确的参数！")
            @DecimalMin(value = "1", message = "请传入正确的参数！")
            @Parameter(name = "id", description = "分类ID", required = true)
            @PathVariable String id
    ) {
        log.info("time:{},method:articleById,id:{}", CommonUtils.dateCurrent(), id);
        return Response.success(articleService.selectArticleById(Integer.parseInt(id)));
    }

    @Operation(summary = "根据Id修改文章的置顶情况")
    @PutMapping("/auth/article/update/top/{id}")
    public Response<Boolean> updateArticleTop(
            @NotBlank(message = "请传入正确的参数！")
            @DecimalMin(value = "1", message = "请传入正确的参数！")
            @Parameter(name = "id", description = "分类ID", required = true)
            @PathVariable String id) {
        log.info("time:{},method:updateArticleTop,id:{}", CommonUtils.dateCurrent(), id);
        return Response.success("更新置顶成功", articleService.updateArticleTop(Integer.parseInt(id)));
    }

    @Operation(summary = "根据Id修改文章")
    @PutMapping("/auth/article/update/info/{id}")
    public Response<String> updateArticleInfo(
            @NotBlank(message = "请传入正确的参数！")
            @DecimalMin(value = "1", message = "请传入正确的参数！")
            @Parameter(name = "id", description = "分类ID", required = true)
            @PathVariable String id,
            @Valid
            @Length(min = 1, max = 30, message = "标题长度必须小于等于30个字符！")
            @NotBlank(message = "标题不能为空！")
            String title,
            @Valid
            @NotBlank(message = "文章内容不能为空！")
            String text,
            @Valid
            @NotNull(message = "分类必须传入！")
            @Min(value = 1, message = "请传入正确的分类ID")
            Integer classificationId,
            @Valid
            @NotNull(message = "标签ID必须传入！")
            String tagsId,
            @Valid
            Boolean top,
            @Valid
            @NotBlank(message = "前言不能为空！")
            @Length(min = 1, max = 200, message = "前言无法超过200个字符！")
            String prefixContent,
            @Valid
            @NotNull(message = "上传文件不能为空！")
            @Parameter(name = "file", description = "上传文件的流", required = true)
            MultipartFile image
    ) {
        AddArticleVo article = new AddArticleVo();
        article.setTagsId(changeTagsId2List(tagsId));
        article.setTop(top);
        article.setText(text);
        article.setImage(image);
        article.setTitle(title);
        article.setClassificationId(classificationId);
        article.setPrefixContent(prefixContent);
        log.info("time:{},method:updateArticleInfo,id:{},AddArticleVo:{},image:{}", CommonUtils.dateCurrent(), id, article, image.getSize());
        return Response.success(articleService.updateArticleInfo(Integer.parseInt(id), article), null);
    }

    @Operation(summary = "增加文章")
    @PostMapping("/auth/article/add/info")
    public Response<Integer> addArticleInfo(
            @Valid
            @Length(min = 1, max = 30, message = "标题长度必须小于等于30个字符！")
            @NotBlank(message = "标题不能为空！")
            String title,
            @Valid
            @NotBlank(message = "文章内容不能为空！")
            String text,
            @Valid
            @NotNull(message = "分类必须传入！")
            @Min(value = 1, message = "请传入正确的分类ID")
            Integer classificationId,
            @Valid
            @NotNull(message = "标签ID必须传入！")
            String tagsId,
            @Valid
            Boolean top,
            @Valid
            @NotBlank(message = "前言不能为空！")
            @Length(min = 1, max = 200, message = "前言无法超过200个字符！")
            String prefixContent,
            @Valid
            @NotNull(message = "上传文件不能为空！")
            @Parameter(name = "file", description = "上传文件的流", required = true)
            MultipartFile image,
            HttpServletRequest request) {
        AddArticleVo article = new AddArticleVo();
        article.setTagsId(changeTagsId2List(tagsId));
        article.setTop(top);
        article.setText(text);
        article.setImage(image);
        article.setTitle(title);
        article.setClassificationId(classificationId);
        article.setPrefixContent(prefixContent);
        HashMap<String, String> hashMap = ipAddressInfoApi.belongingPlace(IPUtils.getIpAddress(request));
        log.info("time:{},method:addArticleInfo,AddArticleVo:{}", CommonUtils.dateCurrent(), article);
        String province = "".equals(hashMap.get("city")) ? hashMap.get("province") : hashMap.get("province") + hashMap.get("city");
        return Response.success("增加成功！", articleService.addArticleInfo(article, province));
    }


    @Operation(summary = "获取文章的信息")
    @GetMapping("/auth/article/info/limit")
    public Response<PageUtils<ArticleDto>> articleLimit(
            @Valid
            @Parameter(name = "pageVo", description = "包含当前页和每页条数的Vo", required = true)
            PageVo pageVo
    ) {
        log.info("time:{},method:articleLimit,pageVo:{}", CommonUtils.dateCurrent(), pageVo);
        return Response.success(articleService.selectArticleList(pageVo.getCurrent() - 1, pageVo.getSize(), false, false));
    }

    @Operation(summary = "根据Id获取文章信息")
    @GetMapping("/auth/article/recovery/limit")
    public Response<PageUtils<ArticleDto>> recoveryArticleLimit(
            @Valid
            @Parameter(name = "pageVo", description = "包含当前页和每页条数的Vo", required = true)
            PageVo pageVo
    ) {
        log.info("time:{},method:recoveryArticleLimit,pageVo:{}", CommonUtils.dateCurrent(), pageVo);
        return Response.success(articleService.selectArticleList(pageVo.getCurrent() - 1, pageVo.getSize(), true, false));
    }


    @Operation(summary = "根据Id获取文章删除文章，进入回收站")
    @DeleteMapping("/auth/article/delete/{id}")
    public Response<?> articleDeleteById(
            @NotBlank(message = "请传入正确的参数！")
            @DecimalMin(value = "1", message = "请传入正确的参数！")
            @Parameter(name = "id", description = "分类ID", required = true)
            @PathVariable String id
    ) {
        log.info("time:{},method:articleDeleteById,id:{}", CommonUtils.dateCurrent(), id);
        return Response.success(articleService.articleDeleteById(Integer.parseInt(id)), null);
    }

    @Operation(summary = "根据Id彻底删除文章")
    @DeleteMapping("/auth/recovery/article/delete/{id}")
    public Response<?> articleReadDeleteById(
            @NotBlank(message = "请传入正确的参数！")
            @DecimalMin(value = "1", message = "请传入正确的参数！")
            @Parameter(name = "id", description = "分类ID", required = true)
            @PathVariable String id
    ) {
        log.info("time:{},method:articleReadDeleteById,id:{}", CommonUtils.dateCurrent(), id);
        return Response.success(articleService.articleRealDeleteById(Integer.parseInt(id)), null);
    }

    @Operation(summary = "根据Id将文章还原")
    @PutMapping("/auth/recovery/article/restore/{id}")
    public Response<?> articleRestoreById(
            @NotBlank(message = "请传入正确的参数！")
            @DecimalMin(value = "1", message = "请传入正确的参数！")
            @Parameter(name = "id", description = "分类ID", required = true)
            @PathVariable String id
    ) {
        log.info("time:{},method:articleRestoreById,id:{}", CommonUtils.dateCurrent(), id);
        return Response.success(articleService.restore(Integer.parseInt(id)), null);
    }


    @Operation(summary = "根据Id获取url，从存储空间读取返回")
    @GetMapping("/api/article/content/{id}")
    public Response<String> articleContentById(
            @NotBlank(message = "请传入正确的参数！")
            @DecimalMin(value = "1", message = "请传入正确的参数！")
            @Parameter(name = "id", description = "文章ID", required = true)
            @PathVariable String id) {
        log.info("time:{},method:articleContentById,id:{}", CommonUtils.dateCurrent(), id);
        return Response.success(articleService.selectArticleContentById(Integer.parseInt(id)));
    }

    @Operation(summary = "根据条件查询数据，返回可选择的信息")
    @GetMapping("/api/article/search")
    public Response<PageUtils<ArticleDto>> articleContent(
            @NotBlank(message = "请传入正确的参数！")
            @Parameter(name = "searchValue", description = "查询数据", required = true)
            String searchKey,
            @Valid
            @Parameter(name = "pageVo", description = "包含当前页和每页条数的Vo", required = true)
            PageVo pageVo
    ) {
        log.info("time:{},method:articleContent,searchKey:{}", CommonUtils.dateCurrent(), searchKey);
        return Response.success(articleService.selectSearchByContent(searchKey, pageVo.getCurrent() - 1, pageVo.getSize()));
    }


    @Operation(summary = "根据条件查询数据，返回可选择的信息")
    @GetMapping("/auth/article/search/id/{searchValue}")
    public Response<PageUtils<ArticleDto>> articleSearchById(
            @Min(value = 1, message = "请输入正确的ID！")
            @Parameter(name = "searchValue", description = "查询数据", required = true)
            @PathVariable int searchValue,
            @Valid
            @Parameter(name = "pageVo", description = "包含当前页和每页条数的Vo", required = true)
            PageVo pageVo
    ) {
        log.info("time:{},method:articleSearchById,searchValue:{},pageVo:{}", CommonUtils.dateCurrent(), searchValue, pageVo);
        return Response.success(articleService.selectSearchDataById(searchValue, pageVo.getCurrent() - 1, pageVo.getSize(), false));
    }

    @Operation(summary = "根据条件查询数据，返回可选择的信息")
    @GetMapping("/auth/article/search/title/{searchValue}")
    public Response<PageUtils<ArticleDto>> articleSearchByTitle(
            @NotBlank(message = "请传入正确的参数！")
            @Parameter(name = "searchValue", description = "查询数据", required = true)
            @PathVariable String searchValue,
            @Valid
            @Parameter(name = "pageVo", description = "包含当前页和每页条数的Vo", required = true)
            PageVo pageVo
    ) {
        log.info("time:{},method:articleSearchByTitle,searchValue:{},pageVo:{}", CommonUtils.dateCurrent(), searchValue, pageVo);
        return Response.success(articleService.selectSearchDataLikeTitle(searchValue, pageVo.getCurrent() - 1, pageVo.getSize(), false));
    }

    @Operation(summary = "根据条件查询数据，返回可选择的信息")
    @GetMapping("/auth/article/search/classification/{searchValue}")
    public Response<PageUtils<ArticleDto>> articleSearchByClassification(
            @NotBlank(message = "请传入正确的参数！")
            @Parameter(name = "searchValue", description = "查询数据", required = true)
            @PathVariable String searchValue,
            @Valid
            @Parameter(name = "pageVo", description = "包含当前页和每页条数的Vo", required = true)
            PageVo pageVo
    ) {
        log.info("time:{},method:articleSearchByClassification,searchValue:{},pageVo:{}", CommonUtils.dateCurrent(), searchValue, pageVo);
        return Response.success(articleService.selectSearchDataLikeClassification(searchValue, pageVo.getCurrent() - 1, pageVo.getSize(), false));
    }

    @Operation(summary = "根据条件查询数据，返回可选择的信息")
    @GetMapping("/auth/article/search/tag/{searchValue}")
    public Response<PageUtils<ArticleDto>> articleSearchByTag(
            @NotBlank(message = "请传入正确的参数！")
            @Parameter(name = "searchValue", description = "查询数据", required = true)
            @PathVariable String searchValue,
            @Valid
            @Parameter(name = "pageVo", description = "包含当前页和每页条数的Vo", required = true)
            PageVo pageVo
    ) {
        log.info("time:{},method:articleSearchByTag,searchValue:{},pageVo:{}", CommonUtils.dateCurrent(), searchValue, pageVo);
        return Response.success(articleService.selectSearchDataTag(searchValue, pageVo.getCurrent() - 1, pageVo.getSize(), false));
    }

    @Operation(summary = "根据条件查询数据，返回可选择的信息")
    @GetMapping("/auth/article/search/position/{searchValue}")
    public Response<PageUtils<ArticleDto>> articleSearchByPosition(
            @NotBlank(message = "请传入正确的参数！")
            @Parameter(name = "searchValue", description = "查询数据", required = true)
            @PathVariable String searchValue,
            @Valid
            @Parameter(name = "pageVo", description = "包含当前页和每页条数的Vo", required = true)
            PageVo pageVo
    ) {
        log.info("time:{},method:articleSearchByPosition,searchValue:{},pageVo:{}", CommonUtils.dateCurrent(), searchValue, pageVo);
        return Response.success(articleService.selectSearchDataPosition(searchValue, pageVo.getCurrent() - 1, pageVo.getSize(), false));
    }

    @Operation(summary = "根据条件查询数据，返回可选择的信息")
    @GetMapping("/auth/article/search/like/gte/{searchValue}")
    public Response<PageUtils<ArticleDto>> articleSearchGte(
            @Min(value = 0, message = "请输入正确的参数！")
            @Parameter(name = "searchValue", description = "查询数据", required = true)
            @PathVariable int searchValue,
            @Valid
            @Parameter(name = "pageVo", description = "包含当前页和每页条数的Vo", required = true)
            PageVo pageVo
    ) {
        log.info("time:{},method:articleSearchGte,searchValue:{},pageVo:{}", CommonUtils.dateCurrent(), searchValue, pageVo);
        return Response.success(articleService.selectSearchDataByLikeCountEqualOrGreaterThan(searchValue, pageVo.getCurrent() - 1, pageVo.getSize(), false));
    }

    @Operation(summary = "根据条件查询数据，返回可选择的信息")
    @GetMapping("/auth/article/search/like/lte/{searchValue}")
    public Response<PageUtils<ArticleDto>> articleSearchLte(
            @Min(value = 0, message = "请输入正确的参数！")
            @Parameter(name = "searchValue", description = "查询数据", required = true)
            @PathVariable int searchValue,
            @Valid
            @Parameter(name = "pageVo", description = "包含当前页和每页条数的Vo", required = true)
            PageVo pageVo
    ) {
        log.info("time:{},method:articleSearchLte,searchValue:{},pageVo:{}", CommonUtils.dateCurrent(), searchValue, pageVo);
        return Response.success(articleService.selectSearchDataByLikeCountEqualOrLessThan(searchValue, pageVo.getCurrent() - 1, pageVo.getSize(), false));
    }

    @Operation(summary = "根据条件查询数据，返回可选择的信息")
    @GetMapping("/auth/article/search/like/equ/{searchValue}")
    public Response<PageUtils<ArticleDto>> articleSearchEqu(
            @Min(value = 0, message = "请输入正确的参数！")
            @Parameter(name = "searchValue", description = "查询数据", required = true)
            @PathVariable int searchValue,
            @Valid
            @Parameter(name = "pageVo", description = "包含当前页和每页条数的Vo", required = true)
            PageVo pageVo
    ) {
        log.info("time:{},method:articleSearchEqu,searchValue:{},pageVo:{}", CommonUtils.dateCurrent(), searchValue, pageVo);
        return Response.success(articleService.selectSearchDataByLikeCountEqual(searchValue, pageVo.getCurrent() - 1, pageVo.getSize(), false));
    }

    @Operation(summary = "根据条件查询数据，返回可选择的信息")
    @GetMapping("/auth/recovery/article/search/id/{searchValue}")
    public Response<PageUtils<ArticleDto>> articleRecoverySearchById(
            @Min(value = 1, message = "请输入正确的ID！")
            @Parameter(name = "searchValue", description = "查询数据", required = true)
            @PathVariable int searchValue,
            @Valid
            @Parameter(name = "pageVo", description = "包含当前页和每页条数的Vo", required = true)
            PageVo pageVo
    ) {
        log.info("time:{},method:articleRecoverySearchById,searchValue:{},pageVo:{}", CommonUtils.dateCurrent(), searchValue, pageVo);
        return Response.success(articleService.selectSearchDataById(searchValue, pageVo.getCurrent() - 1, pageVo.getSize(), true));
    }

    @Operation(summary = "根据条件查询数据，返回可选择的信息")
    @GetMapping("/auth/recovery/article/search/title/{searchValue}")
    public Response<PageUtils<ArticleDto>> articleRecoverySearchByTitle(
            @NotBlank(message = "请传入正确的参数！")
            @Parameter(name = "searchValue", description = "查询数据", required = true)
            @PathVariable String searchValue,
            @Valid
            @Parameter(name = "pageVo", description = "包含当前页和每页条数的Vo", required = true)
            PageVo pageVo
    ) {
        log.info("time:{},method:articleRecoverySearchByTitle,searchValue:{},pageVo:{}", CommonUtils.dateCurrent(), searchValue, pageVo);
        return Response.success(articleService.selectSearchDataLikeTitle(searchValue, pageVo.getCurrent() - 1, pageVo.getSize(), true));
    }

    @Operation(summary = "根据条件查询数据，返回可选择的信息")
    @GetMapping("/auth/recovery/article/search/classification/{searchValue}")
    public Response<PageUtils<ArticleDto>> articleRecoverySearchByClassification(
            @NotBlank(message = "请传入正确的参数！")
            @Parameter(name = "searchValue", description = "查询数据", required = true)
            @PathVariable String searchValue,
            @Valid
            @Parameter(name = "pageVo", description = "包含当前页和每页条数的Vo", required = true)
            PageVo pageVo
    ) {
        log.info("time:{},method:articleRecoverySearchByClassification,searchValue:{},pageVo:{}", CommonUtils.dateCurrent(), searchValue, pageVo);
        return Response.success(articleService.selectSearchDataLikeClassification(searchValue, pageVo.getCurrent() - 1, pageVo.getSize(), true));
    }

    @Operation(summary = "根据条件查询数据，返回可选择的信息")
    @GetMapping("/auth/recovery/article/search/tag/{searchValue}")
    public Response<PageUtils<ArticleDto>> articleRecoverySearchByTag(
            @NotBlank(message = "请传入正确的参数！")
            @Parameter(name = "searchValue", description = "查询数据", required = true)
            @PathVariable String searchValue,
            @Valid
            @Parameter(name = "pageVo", description = "包含当前页和每页条数的Vo", required = true)
            PageVo pageVo
    ) {
        log.info("time:{},method:articleRecoverySearchByTag,searchValue:{},pageVo:{}", CommonUtils.dateCurrent(), searchValue, pageVo);
        return Response.success(articleService.selectSearchDataTag(searchValue, pageVo.getCurrent() - 1, pageVo.getSize(), true));
    }

    @Operation(summary = "根据条件查询数据，返回可选择的信息")
    @GetMapping("/auth/recovery/article/search/position/{searchValue}")
    public Response<PageUtils<ArticleDto>> articleRecoverySearchByPosition(
            @NotBlank(message = "请传入正确的参数！")
            @Parameter(name = "searchValue", description = "查询数据", required = true)
            @PathVariable String searchValue,
            @Valid
            @Parameter(name = "pageVo", description = "包含当前页和每页条数的Vo", required = true)
            PageVo pageVo
    ) {
        log.info("time:{},method:articleRecoverySearchByPosition,searchValue:{},pageVo:{}", CommonUtils.dateCurrent(), searchValue, pageVo);
        return Response.success(articleService.selectSearchDataPosition(searchValue, pageVo.getCurrent() - 1, pageVo.getSize(), true));
    }

    @Operation(summary = "根据条件查询数据，返回可选择的信息")
    @GetMapping("/auth/recovery/article/search/like/gte/{searchValue}")
    public Response<PageUtils<ArticleDto>> articleRecoverySearchGte(
            @Min(value = 0, message = "请输入正确的参数！")
            @Parameter(name = "searchValue", description = "查询数据", required = true)
            @PathVariable int searchValue,
            @Valid
            @Parameter(name = "pageVo", description = "包含当前页和每页条数的Vo", required = true)
            PageVo pageVo
    ) {
        log.info("time:{},method:articleRecoverySearchGte,searchValue:{},pageVo:{}", CommonUtils.dateCurrent(), searchValue, pageVo);
        return Response.success(articleService.selectSearchDataByLikeCountEqualOrGreaterThan(searchValue, pageVo.getCurrent() - 1, pageVo.getSize(), true));
    }

    @Operation(summary = "根据条件查询数据，返回可选择的信息")
    @GetMapping("/auth/recovery/article/search/like/lte/{searchValue}")
    public Response<PageUtils<ArticleDto>> articleRecoverySearchLte(
            @Min(value = 0, message = "请输入正确的参数！")
            @Parameter(name = "searchValue", description = "查询数据", required = true)
            @PathVariable int searchValue,
            @Valid
            @Parameter(name = "pageVo", description = "包含当前页和每页条数的Vo", required = true)
            PageVo pageVo
    ) {
        log.info("time:{},method:articleRecoverySearchLte,searchValue:{},pageVo:{}", CommonUtils.dateCurrent(), searchValue, pageVo);
        return Response.success(articleService.selectSearchDataByLikeCountEqualOrLessThan(searchValue, pageVo.getCurrent() - 1, pageVo.getSize(), true));
    }

    @Operation(summary = "根据条件查询数据，返回可选择的信息")
    @GetMapping("/auth/recovery/article/search/like/equ/{searchValue}")
    public Response<PageUtils<ArticleDto>> articleRecoverySearchEqu(
            @Min(value = 0, message = "请输入正确的参数！")
            @Parameter(name = "searchValue", description = "查询数据", required = true)
            @PathVariable int searchValue,
            @Valid
            @Parameter(name = "pageVo", description = "包含当前页和每页条数的Vo", required = true)
            PageVo pageVo
    ) {
        log.info("time:{},method:articleRecoverySearchEqu,searchValue:{},pageVo:{}", CommonUtils.dateCurrent(), searchValue, pageVo);
        return Response.success(articleService.selectSearchDataByLikeCountEqual(searchValue, pageVo.getCurrent() - 1, pageVo.getSize(), true));
    }
}
