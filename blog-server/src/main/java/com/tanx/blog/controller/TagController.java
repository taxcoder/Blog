package com.tanx.blog.controller;

import com.tanx.blog.entity.dto.TagDto;
import com.tanx.blog.entity.vo.PageVo;
import com.tanx.blog.entity.vo.TagVo;
import com.tanx.blog.service.TagService;
import com.tanx.blog.utils.PageUtils;
import com.tanx.blog.utils.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Validated
@RestController
@io.swagger.v3.oas.annotations.tags.Tag(name = "标签管理")
public class TagController {

    @Resource
    private TagService tagService;

    @Operation(summary = "获取所有的标签")
    @GetMapping("/api/tag/all")
    public Response<List<TagDto>> tagsAll() {
        return Response.success(tagService.tagList());
    }

    @Operation(summary = "根据条件查询数据，返回可选择的信息")
    @GetMapping("/api/tag/search/name")
    public Response<PageUtils<TagDto>> SearchTagByName(
            @NotBlank(message = "请传入正确的参数！")
            @Parameter(name = "searchValue", description = "查询数据", required = true)
            String searchValue,
            @Valid
            @Parameter(name = "pageVo", description = "包含当前页和每页条数的Vo", required = true)
            PageVo pageVo
    ) {
        return Response.success(tagService.selectTagByName(searchValue, pageVo.getCurrent() - 1, pageVo.getSize()));
    }

    @Operation(summary = "根据当前页和每页条数获取标签集合")
    @GetMapping("/auth/tag/limit")
    public Response<PageUtils<TagDto>> tagLimit(
            @Valid
            @Parameter(name = "pageVo", description = "包含当前页和每页条数的Vo", required = true)
            PageVo pageVo
    ) {
        return Response.success(tagService.tagListLimit(pageVo.getCurrent() - 1, pageVo.getSize()));
    }

    @Operation(summary = "根据Id获取所有对应的文章信息")
    @GetMapping("/auth/tag/article/{id}")
    public Response<List<Map<String, String>>> tagArticleList(
            @NotBlank(message = "请传入正确的参数！")
            @DecimalMin(value = "1", message = "请传入正确的参数！")
            @Parameter(name = "id", description = "标签ID", required = true)
            @PathVariable String id
    ) {
        return Response.success(tagService.selectTagArticleById(Integer.parseInt(id)));
    }

    @Operation(summary = "根据Id修改标签名称")
    @PutMapping("/auth/tag/update/{id}")
    public Response<String> updateTag(
            @NotBlank(message = "请传入正确的参数！")
            @DecimalMin(value = "1", message = "请传入正确的参数！")
            @Parameter(name = "id", description = "标签ID", required = true)
            @PathVariable
            String id,
            @Valid
            @NotBlank(message = "传入参数异常！")
            @Length(max = 20, min = 1, message = "请输入正确的参数！")
            String name
    ) {
        return Response.success(tagService.updateTagById(Integer.parseInt(id), name), null);
    }

    @Operation(summary = "根据Id删除标签")
    @DeleteMapping("/auth/tag/delete/{id}")
    public Response<String> updateTag(
            @NotBlank(message = "请传入正确的参数！")
            @DecimalMin(value = "1", message = "请传入正确的参数！")
            @Parameter(name = "id", description = "标签ID", required = true)
            @PathVariable String id
    ) {
        return Response.success(tagService.deleteTagById(Integer.parseInt(id)), null);
    }

    @Operation(summary = "增加标签")
    @PostMapping("/auth/tag/add")
    public Response<List<TagDto>> addTags(
            @Valid
            @RequestBody
            @Parameter(name = "tagName", description = "标签数组", required = true)
            TagVo tagVo
    ) {
        return Response.success("添加成功！", tagService.addTags(tagVo));
    }

    @Operation(summary = "根据条件查询数据，返回可选择的信息")
    @GetMapping("/auth/tag/search/id/{searchValue}")
    public Response<PageUtils<TagDto>> tagSearchById(
            @Min(value = 1, message = "请输入正确的ID！")
            @Parameter(name = "searchValue", description = "查询数据", required = true)
            @PathVariable int searchValue,
            @Valid
            @Parameter(name = "pageVo", description = "包含当前页和每页条数的Vo", required = true)
            PageVo pageVo
    ) {
        return Response.success(tagService.selectSearchDataById(searchValue, pageVo.getCurrent() - 1, pageVo.getSize()));
    }

    @Operation(summary = "根据条件查询数据，返回可选择的信息")
    @GetMapping("/auth/tag/search/name/{searchValue}")
    public Response<PageUtils<TagDto>> tagSearchByName(
            @NotBlank(message = "请传入正确的参数！")
            @Parameter(name = "searchValue", description = "查询数据", required = true)
            @PathVariable String searchValue,
            @Valid
            @Parameter(name = "pageVo", description = "包含当前页和每页条数的Vo", required = true)
            PageVo pageVo
    ) {
        return Response.success(tagService.selectSearchDataLikeName(searchValue, pageVo.getCurrent() - 1, pageVo.getSize()));
    }

    @Operation(summary = "根据条件查询数据，返回可选择的信息")
    @GetMapping("/auth/tag/search/like/gte/{searchValue}")
    public Response<PageUtils<TagDto>> tagSearchGte(
            @Min(value = 0, message = "请输入正确的参数！")
            @Parameter(name = "searchValue", description = "查询数据", required = true)
            @PathVariable int searchValue,
            @Valid
            @Parameter(name = "pageVo", description = "包含当前页和每页条数的Vo", required = true)
            PageVo pageVo
    ) {
        return Response.success(tagService.selectSearchDataByArticleCountEqualOrGreaterThan(searchValue, pageVo.getCurrent() - 1, pageVo.getSize()));
    }

    @Operation(summary = "根据条件查询数据，返回可选择的信息")
    @GetMapping("/auth/tag/search/like/lte/{searchValue}")
    public Response<PageUtils<TagDto>> tagSearchLte(
            @Min(value = 0, message = "请输入正确的参数！")
            @Parameter(name = "searchValue", description = "查询数据", required = true)
            @PathVariable int searchValue,
            @Valid
            @Parameter(name = "pageVo", description = "包含当前页和每页条数的Vo", required = true)
            PageVo pageVo
    ) {
        return Response.success(tagService.selectSearchDataByArticleCountEqualOrLessThan(searchValue, pageVo.getCurrent() - 1, pageVo.getSize()));
    }

    @Operation(summary = "根据条件查询数据，返回可选择的信息")
    @GetMapping("/auth/tag/search/like/equ/{searchValue}")
    public Response<PageUtils<TagDto>> tagSearchEqu(
            @Min(value = 0, message = "请输入正确的参数！")
            @Parameter(name = "searchValue", description = "查询数据", required = true)
            @PathVariable int searchValue,
            @Valid
            @Parameter(name = "pageVo", description = "包含当前页和每页条数的Vo", required = true)
            PageVo pageVo
    ) {

        return Response.success(tagService.selectSearchDataByArticleCountEqual(searchValue, pageVo.getCurrent() - 1, pageVo.getSize()));
    }
}
