package com.tanx.blog.controller;

import com.tanx.blog.entity.vo.PrintTextVo;
import com.tanx.blog.service.PrintTextService;
import com.tanx.blog.utils.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @description: 打印文本
 * @project_name: blog-server
 * @author: 谭翔
 * @date: 2023/10/21 16:09
 */

@Slf4j
@RestController
@Tag(name = "文本打印管理")
public class PrintTextController {

    @Resource
    private PrintTextService printTextService;

    @Operation(summary = "更新文本打印")
    @PutMapping("/auth/print/update/info")
    public Response<String> update(@Valid @RequestBody List<PrintTextVo> voList) {
        return Response.success(printTextService.updatePrintText(voList), null);
    }
}
