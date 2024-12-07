package com.tanx.blog.controller;

import com.tanx.blog.api.IpAddressInfoApi;
import com.tanx.blog.api.WindyWeatherApi;
import com.tanx.blog.entity.dto.CountQuantityDto;
import com.tanx.blog.entity.dto.WebStationDto;
import com.tanx.blog.entity.vo.UserVo;
import com.tanx.blog.entity.vo.WebStationVo;
import com.tanx.blog.service.*;
import com.tanx.blog.utils.IPUtils;
import com.tanx.blog.utils.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Validated
@RestController
@Tag(name = "网站管理")
public class WebStationController {

    @Resource
    private WebStationService webStationService;

    @Resource
    private PrintTextService printTextService;

    @Resource
    private ArticleService articleService;

    @Resource
    private InformalService informalService;

    @Resource
    private TagService tagService;

    @Resource
    private IpAddressInfoApi ipAddressInfoApi;

    @Resource
    private WindyWeatherApi windyWeatherApi;

    @Operation(summary = "获取网站的基本信息")
    @GetMapping("/api/init")
    public Response<WebStationDto> init() {
        return Response.success(webStationService.findWebStation());
    }

    @Operation(summary = "获取网站打字机输出的文案")
    @GetMapping("/api/text")
    public Response<List<String>> text() {
        return Response.success(printTextService.findAll());
    }

    @Operation(summary = "获取文章、随笔、标签的个数")
    @GetMapping("/api/count")
    public Response<List<CountQuantityDto>> count() {
        List<CountQuantityDto> list = new ArrayList<>();
        list.add(new CountQuantityDto("文章", articleService.allCount()));
        list.add(new CountQuantityDto("说说", informalService.allCount()));
        list.add(new CountQuantityDto("标签", tagService.allCount()));
        return Response.success(list);
    }

    @Operation(summary = "获取管理员信息")
    @GetMapping("/auth/admin/get/info")
    public Response<HashMap<String, String>> getAdminInfo() {
        return Response.success(webStationService.findAdmin());
    }

    @Operation(summary = "更新网站信息")
    @PutMapping("/auth/web/update/info")
    public Response<String> updateWebInfo(
            @Valid
            @RequestBody
            @Parameter(name = "webStationVo", description = "网站信息Vo", required = true)
            WebStationVo webStationVo
    ) {
        return Response.success(webStationService.updateInfo(webStationVo), null);
    }

    @Operation(summary = "更新管理员信息")
    @PutMapping("/auth/admin/update/info")
    public Response<String> updateAdminInfo(@Valid @RequestBody UserVo userVo) {
        return Response.success(webStationService.updateAdmin(userVo), null);
    }

    @Operation(summary = "更新管理员密码")
    @PutMapping("/auth/admin/update/password")
    public Response<String> updateAdminPassword(@NotBlank(message = "密码不能为空！") String password, @NotBlank(message = "确认密码不能为空！") String oldPassword) {
        return Response.success(webStationService.updateAdminPassword(password, oldPassword), null);
    }


    @Operation(summary = "获取未来天气")
    @GetMapping("/auth/future/weather/info")
    public Response<HashMap<String, ?>> futureWeatherInfo(HttpServletRequest request) {
        HashMap<String, String> hashMap = ipAddressInfoApi.belongingPlace(IPUtils.getIpAddress(request));
        HashMap<String, ?> stringHashMap = windyWeatherApi.futureWeather(hashMap);
        log.info("hashMap:{}", hashMap);
        log.info("stringHashMap:{}", stringHashMap);
        return Response.success(stringHashMap);
    }
}

