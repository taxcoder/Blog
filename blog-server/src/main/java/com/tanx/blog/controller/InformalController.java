package com.tanx.blog.controller;

import com.tanx.blog.api.IpAddressInfoApi;
import com.tanx.blog.api.WindyWeatherApi;
import com.tanx.blog.entity.po.Informal;
import com.tanx.blog.entity.vo.AddEssayVo;
import com.tanx.blog.entity.vo.PageVo;
import com.tanx.blog.service.InformalService;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * @description: 日记类
 * @project_name: blog-server
 * @author: 谭翔
 * @date: 2023/9/2 19:24
 */

@Slf4j
@Validated
@RestController
@Tag(name = "随笔管理")
public class InformalController {
    @Resource
    private InformalService informalService;
    @Resource
    private IpAddressInfoApi ipAddressInfoApi;
    @Resource
    private WindyWeatherApi windyWeatherApi;

    @Operation(summary = "根据当前页和每页条数获取随笔集合")
    @GetMapping("/api/essay/limit")
    public Response<PageUtils<Informal>> informalList(
            @Valid
            @Parameter(name = "pageVo", description = "包含当前页和每页条数的Vo", required = true)
            PageVo pageVo) {
        return Response.success(informalService.selectInformalList(pageVo.getCurrent() - 1, pageVo.getSize(), false));
    }

    @Operation(summary = "给随笔点赞")
    @GetMapping("/api/essay/update/like/{id}")
    public Response<Integer> informalAddLike(
            @NotBlank(message = "请传入正确的参数！")
            @DecimalMin(value = "1", message = "请传入正确的参数！")
            @Parameter(name = "id", description = "随笔ID", required = true)
            @PathVariable String id
    ) {
        // 判断点赞是否修改成功
        return Response.success(informalService.addLike(Integer.parseInt(id)));
    }

    @Operation(summary = "根据当前页和每页条数获取随笔集合")
    @GetMapping("/auth/recovery/essay/limit")
    public Response<PageUtils<Informal>> recoveryInformalList(
            @Valid
            @Parameter(name = "pageVo", description = "包含当前页和每页条数的Vo", required = true)
            PageVo pageVo) {
        return Response.success(informalService.selectInformalList(pageVo.getCurrent() - 1, pageVo.getSize(), true));
    }

    @Operation(summary = "根据Id删除随笔内容，进入回收站")
    @DeleteMapping("/auth/essay/delete/{id}")
    public Response<String> essayDelete(
            @NotBlank(message = "请传入正确的参数！")
            @DecimalMin(value = "1", message = "请传入正确的参数！")
            @Parameter(name = "id", description = "随笔ID", required = true)
            @PathVariable String id) {

        return Response.success(informalService.removeEssayById(Integer.parseInt(id)), null);
    }

    @Operation(summary = "根据Id获取随笔内容")
    @GetMapping("/auth/essay/{id}")
    public Response<Informal> informalById(
            @NotBlank(message = "请传入正确的参数！")
            @DecimalMin(value = "1", message = "请传入正确的参数！")
            @Parameter(name = "id", description = "随笔ID", required = true)
            @PathVariable String id) {
        return Response.success(informalService.selectInformalById(Integer.parseInt(id)));
    }

    @Operation(summary = "根据Id更新随笔内容")
    @PutMapping("/auth/essay/update/content/{id}")
    public Response<Boolean> essayUpdateContent(
            @NotBlank(message = "请传入正确的参数！")
            @DecimalMin(value = "1", message = "请传入正确的参数！")
            @Parameter(name = "id", description = "随笔ID", required = true)
            @PathVariable String id,
            @NotBlank(message = "请传入正确的参数！")
            @Parameter(name = "content", description = "随笔内容", required = true)
            String content) {

        return Response.success(informalService.updateContentById(content, Integer.parseInt(id)), null);
    }

    /**
     * 添加随笔
     */
    @Operation(summary = "增加随笔")
    @PostMapping("/auth/essay/add")
    public Response<String> addEssay(
            @Valid
            @Parameter(name = "addEssayVo", description = "包含内容和emojiId的Vo", required = true)
            @RequestBody AddEssayVo addEssayVo,
            HttpServletRequest request) {

        String ipAddress = IPUtils.getIpAddress(request);
        HashMap<String, String> hashMap = ipAddressInfoApi.belongingPlace(ipAddress);
        HashMap<String, Integer> weather = windyWeatherApi.weather(ipAddressInfoApi.belongingPlace(ipAddress));
        String province = "".equals(hashMap.get("city")) ? hashMap.get("province") : hashMap.get("province") + hashMap.get("city");
        return Response.success(informalService.addInformal(addEssayVo, weather,province), null);
    }

    @Operation(summary = "根据条件查询数据，返回可选择的信息")
    @GetMapping("/auth/essay/search/id/{searchValue}")
    public Response<PageUtils<Informal>> essaySearchById(
            @Min(value = 1, message = "请输入正确的ID！")
            @Parameter(name = "searchValue", description = "查询数据", required = true)
            @PathVariable int searchValue,
            @Valid
            @Parameter(name = "pageVo", description = "包含当前页和每页条数的Vo", required = true)
            PageVo pageVo
    ) {
        System.out.println(searchValue);
        return Response.success(informalService.selectSearchDataById(searchValue, pageVo.getCurrent() - 1, pageVo.getSize(), false));
    }

    @Operation(summary = "根据条件查询数据，返回可选择的信息")
    @GetMapping("/auth/essay/search/content/{searchValue}")
    public Response<PageUtils<Informal>> essaySearchByName(
            @NotBlank(message = "请传入正确的参数！")
            @Parameter(name = "searchValue", description = "查询数据", required = true)
            @PathVariable String searchValue,
            @Valid
            @Parameter(name = "pageVo", description = "包含当前页和每页条数的Vo", required = true)
            PageVo pageVo
    ) {
        return Response.success(informalService.selectSearchDataLikeContent(searchValue, pageVo.getCurrent() - 1, pageVo.getSize(), false));
    }

    @Operation(summary = "根据条件查询数据，返回可选择的信息")
    @GetMapping("/auth/essay/search/like/gte/{searchValue}")
    public Response<PageUtils<Informal>> essaySearchGte(
            @Min(value = 0, message = "请输入正确的参数！")
            @Parameter(name = "searchValue", description = "查询数据", required = true)
            @PathVariable int searchValue,
            @Valid
            @Parameter(name = "pageVo", description = "包含当前页和每页条数的Vo", required = true)
            PageVo pageVo
    ) {
        return Response.success(informalService.selectSearchDataByLikeCountEqualOrGreaterThan(searchValue, pageVo.getCurrent() - 1, pageVo.getSize(), false));
    }

    @Operation(summary = "根据条件查询数据，返回可选择的信息")
    @GetMapping("/auth/essay/search/like/lte/{searchValue}")
    public Response<PageUtils<Informal>> essaySearchLte(
            @Min(value = 0, message = "请输入正确的参数！")
            @Parameter(name = "searchValue", description = "查询数据", required = true)
            @PathVariable int searchValue,
            @Valid
            @Parameter(name = "pageVo", description = "包含当前页和每页条数的Vo", required = true)
            PageVo pageVo
    ) {
        return Response.success(informalService.selectSearchDataByLikeCountEqualOrLessThan(searchValue, pageVo.getCurrent() - 1, pageVo.getSize(), false));
    }

    @Operation(summary = "根据条件查询数据，返回可选择的信息")
    @GetMapping("/auth/essay/search/like/equ/{searchValue}")
    public Response<PageUtils<Informal>> essaySearchEqu(
            @Min(value = 0, message = "请输入正确的参数！")
            @Parameter(name = "searchValue", description = "查询数据", required = true)
            @PathVariable int searchValue,
            @Valid
            @Parameter(name = "pageVo", description = "包含当前页和每页条数的Vo", required = true)
            PageVo pageVo
    ) {
        return Response.success(informalService.selectSearchDataByLikeCountEqual(searchValue, pageVo.getCurrent() - 1, pageVo.getSize(), false));
    }

    @Operation(summary = "根据条件查询数据，返回可选择的信息")
    @GetMapping("/auth/essay/search/province/{searchValue}")
    public Response<PageUtils<Informal>> essaySearchProvince(
            @NotBlank(message = "请传入正确的参数！")
            @Parameter(name = "searchValue", description = "查询数据", required = true)
            @PathVariable String searchValue,
            @Valid
            @Parameter(name = "pageVo", description = "包含当前页和每页条数的Vo", required = true)
            PageVo pageVo
    ) {

        System.out.println(searchValue);

        return Response.success(informalService.selectSearchDataByProvince(searchValue, pageVo.getCurrent() - 1, pageVo.getSize(), false));
    }

    @Operation(summary = "获取所有的随笔，根据省份进行分组")
    @GetMapping("/auth/essay/province/info")
    public Response<List<HashMap<String, ?>>> essayProvinceCount() {
        return Response.success((List<HashMap<String, ?>>) informalService.selectProvinceCount(false));
    }

    @Operation(summary = "根据条件查询数据，返回可选择的信息")
    @GetMapping("/auth/recovery/essay/search/id/{searchValue}")
    public Response<PageUtils<Informal>> essayRecoverySearchById(
            @Min(value = 1, message = "请输入正确的ID！")
            @Parameter(name = "searchValue", description = "查询数据", required = true)
            @PathVariable int searchValue,
            @Valid
            @Parameter(name = "pageVo", description = "包含当前页和每页条数的Vo", required = true)
            PageVo pageVo
    ) {
        System.out.println(searchValue);
        return Response.success(informalService.selectSearchDataById(searchValue, pageVo.getCurrent() - 1, pageVo.getSize(), true));
    }

    @Operation(summary = "根据条件查询数据，返回可选择的信息")
    @GetMapping("/auth/recovery/essay/search/content/{searchValue}")
    public Response<PageUtils<Informal>> essayRecoverySearchByName(
            @NotBlank(message = "请传入正确的参数！")
            @Parameter(name = "searchValue", description = "查询数据", required = true)
            @PathVariable String searchValue,
            @Valid
            @Parameter(name = "pageVo", description = "包含当前页和每页条数的Vo", required = true)
            PageVo pageVo
    ) {
        return Response.success(informalService.selectSearchDataLikeContent(searchValue, pageVo.getCurrent() - 1, pageVo.getSize(), true));
    }

    @Operation(summary = "根据条件查询数据，返回可选择的信息")
    @GetMapping("/auth/recovery/essay/search/like/gte/{searchValue}")
    public Response<PageUtils<Informal>> essayRecoverySearchGte(
            @Min(value = 0, message = "请输入正确的参数！")
            @Parameter(name = "searchValue", description = "查询数据", required = true)
            @PathVariable int searchValue,
            @Valid
            @Parameter(name = "pageVo", description = "包含当前页和每页条数的Vo", required = true)
            PageVo pageVo
    ) {
        return Response.success(informalService.selectSearchDataByLikeCountEqualOrGreaterThan(searchValue, pageVo.getCurrent() - 1, pageVo.getSize(), true));
    }

    @Operation(summary = "根据条件查询数据，返回可选择的信息")
    @GetMapping("/auth/recovery/essay/search/like/lte/{searchValue}")
    public Response<PageUtils<Informal>> essayRecoverySearchLte(
            @Min(value = 0, message = "请输入正确的参数！")
            @Parameter(name = "searchValue", description = "查询数据", required = true)
            @PathVariable int searchValue,
            @Valid
            @Parameter(name = "pageVo", description = "包含当前页和每页条数的Vo", required = true)
            PageVo pageVo
    ) {
        return Response.success(informalService.selectSearchDataByLikeCountEqualOrLessThan(searchValue, pageVo.getCurrent() - 1, pageVo.getSize(), true));
    }

    @Operation(summary = "根据条件查询数据，返回可选择的信息")
    @GetMapping("/auth/recovery/essay/search/like/equ/{searchValue}")
    public Response<PageUtils<Informal>> essayRecoverySearchEqu(
            @Min(value = 0, message = "请输入正确的参数！")
            @Parameter(name = "searchValue", description = "查询数据", required = true)
            @PathVariable int searchValue,
            @Valid
            @Parameter(name = "pageVo", description = "包含当前页和每页条数的Vo", required = true)
            PageVo pageVo
    ) {
        return Response.success(informalService.selectSearchDataByLikeCountEqual(searchValue, pageVo.getCurrent() - 1, pageVo.getSize(), true));
    }

    @Operation(summary = "将随笔还原")
    @GetMapping("/auth/recovery/essay/restore/{id}")
    public Response<String> informalOnRestore(
            @NotBlank(message = "请传入正确的参数！")
            @DecimalMin(value = "1", message = "请传入正确的参数！")
            @Parameter(name = "id", description = "随笔ID", required = true)
            @PathVariable String id
    ) {
        // 判断点赞是否修改成功
        return Response.success(informalService.restore(Integer.parseInt(id)));
    }

    @Operation(summary = "将随笔彻底删除")
    @GetMapping("/auth/recovery/essay/delete/{id}")
    public Response<String> informalOnDelete(
            @NotBlank(message = "请传入正确的参数！")
            @DecimalMin(value = "1", message = "请传入正确的参数！")
            @Parameter(name = "id", description = "随笔ID", required = true)
            @PathVariable String id
    ) {
        // 判断点赞是否修改成功
        return Response.success(informalService.realRemoveEssayById(Integer.parseInt(id)));
    }
}
