package com.tanx.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tanx.blog.constant.Constant;
import com.tanx.blog.entity.po.PrintText;
import com.tanx.blog.entity.vo.PrintTextVo;
import com.tanx.blog.exception.DataOperationErrorException;
import com.tanx.blog.mapper.PrintTextMapper;
import com.tanx.blog.service.PrintTextService;
import com.tanx.blog.utils.RedisOperationUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author 谭翔
 * @description 针对表【d_print_text( 文本打印表 )】的数据库操作Service实现
 * @createDate 2023-07-06 15:29:16
 */
@Slf4j
@Service
public class PrintTextServiceImpl extends ServiceImpl<PrintTextMapper, PrintText>
        implements PrintTextService {

    @Resource
    private PrintTextMapper printTextMapper;

    @Resource
    private RedisOperationUtils operationUtils;

    /**
     * 获取文本打印的全部内容
     *
     * @return 返回只含有文本的集合，如果数据库异常，返回空集合
     */
    @Override
    public List<String> findAll() {
        // 如果存在key，则直接从redis内获取
        if (operationUtils.hasKey(Constant.PRINT_TEXT)) {
            List<PrintText> list = (List<PrintText>) operationUtils.lRange(Constant.PRINT_TEXT, 0, -1);
            return list.stream().map(PrintText::getContent).toList();
        }
        // 不存在去数据库查询，然后再存入redis
        LambdaQueryWrapper<PrintText> wrapper = new LambdaQueryWrapper<>();
        List<PrintText> list = printTextMapper.selectList(wrapper);
        if (list.size() == 0) {
            throw new DataOperationErrorException("打字机文本获取失败！");
        }

        operationUtils.lRightPushAll(Constant.PRINT_TEXT, list);
        return list.stream().map(PrintText::getContent).toList();
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, DataOperationErrorException.class})
    public String updatePrintText(List<PrintTextVo> text) {
        if (text.size() == 0 || text.size() > 5) {
            throw new DataOperationErrorException("传入数据异常！");
        }

        QueryWrapper<PrintText> wrapper = new QueryWrapper<>();
        List<PrintText> printTexts = printTextMapper.selectList(wrapper);
        List<PrintText> list = new ArrayList<>(5);
        // 将数据循环变便利，判断哪些是还没有的
        text.forEach(txt -> {
            // 获取数据库内没有的数据，需要存入数据库内的数据
            if(printTexts.stream().filter(t -> t.getContent().equals(txt.getContent())).toList().size() == 0) {
                PrintText printText = new PrintText();
                printText.setContent(txt.getContent());
                list.add(printText);
            }
        });
        // 将数据库内查询出的对象转成String
        Set<String> difference = printTexts.stream().map(PrintText::getContent).collect(Collectors.toSet());
        // 获取需要从数据库删除的数据
        difference.removeAll(text.stream().map(PrintTextVo::getContent).collect(Collectors.toSet()));

        wrapper.in("content", difference);
        //  删除数据库内没有的数据
        if(difference.size() > 0 && printTextMapper.delete(wrapper) != difference.size()) {
            throw new DataOperationErrorException("更新失败！");
        }
        // 添加还没有的数据
        if (!saveBatch(list)) {
            throw new DataOperationErrorException("更新失败！");
        }
        // 将一样的数据和新的数据存在一起，存入redis，减少一次mysql查询

        operationUtils.delete(Constant.PRINT_TEXT);
        operationUtils.lRightPushAll(Constant.PRINT_TEXT, printTextMapper.selectList(new QueryWrapper<>()));
        return "更新成功！";
    }
}




