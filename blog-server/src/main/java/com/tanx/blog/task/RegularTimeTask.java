package com.tanx.blog.task;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.tanx.blog.annotation.DeleteNavigationAnnotation;
import com.tanx.blog.constant.Constant;
import com.tanx.blog.entity.po.Article;
import com.tanx.blog.entity.po.Informal;
import com.tanx.blog.entity.po.Navigation;
import com.tanx.blog.entity.po.WebStation;
import com.tanx.blog.mapper.ArticleMapper;
import com.tanx.blog.mapper.InformalMapper;
import com.tanx.blog.mapper.NavigationMapper;
import com.tanx.blog.mapper.WebStationMapper;
import com.tanx.blog.service.NavigationService;
import com.tanx.blog.utils.RedisOperationUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @description: 定时任务
 * @project_name: blog-server
 * @author: 谭翔
 * @date: 2023/11/15 21:52
 */

@Slf4j
@Service
public class RegularTimeTask {

    @Resource
    private ArticleMapper articleMapper;
    @Resource
    private WebStationMapper webStationMapper;
    @Resource
    private InformalMapper informalMapper;
    @Resource
    private RedisOperationUtils operationUtils;
    @Resource
    private NavigationMapper navigationMapper;
    @Resource
    private ThreadPoolExecutor poolExecutor;
    @Resource
    private NavigationService navigationService;

    @Async
    @Scheduled(cron = "0 0 0 * * ?")
    public void synchronousVisit() {
        log.info("在执行更新网站访问量操作:{}", "----------------------------visit-start----------------------------");
        long visit = 0;
        if (operationUtils.hasKey(Constant.VISIT)) {
            visit = (int) operationUtils.get(Constant.VISIT);
        } else {
            operationUtils.set(Constant.VISIT, visit);
        }
        log.info("visit:{}", visit);
        WebStation webStation = webStationMapper.selectOne(new QueryWrapper<>());
        // 如果没有变动，则表示网站没有新的访客
        if (webStation == null || webStation.getTotalVisits() == visit) return;
        // 如果存储的访问量大于数据库内的访问量，则表示redis内的访问量是更新的，直接把redis内的存入数据库
        webStation.setTotalVisits(visit);
        webStationMapper.updateById(webStation);
        operationUtils.delete(Constant.WEB_KEY);
        operationUtils.set(Constant.WEB_KEY, webStation);
        log.info("在执行更新网站访问量操作:{}", "----------------------------visit-end----------------------------");
    }

    @Async
    @Scheduled(cron = "0 0 0 * * ?")
    public void synchronousArticleTextCount() {
        log.info("在执行网站文字数据操作:{}", "----------------------------text-start----------------------------");
        long text = 0;
        if (operationUtils.hasKey(Constant.TEXT_COUNT)) {
            text = (int) operationUtils.get(Constant.TEXT_COUNT);
        } else {
            operationUtils.set(Constant.TEXT_COUNT, text);
        }
        log.info("text:{}", text);
        WebStation webStation = webStationMapper.selectOne(new QueryWrapper<>());
        if (webStation == null || webStation.getTotalTextQuantity() == text) return;
        webStation.setTotalTextQuantity(text > webStation.getTotalTextQuantity() ? text : webStation.getTotalTextQuantity() + text);
        webStationMapper.updateById(webStation);
        operationUtils.delete(Constant.WEB_KEY);
        log.info("在执行网站文字数据操作:{}", "----------------------------text-end----------------------------");
    }

    @Async
    @Scheduled(cron = "0 0 0 * * ?")
    public void synchronousEssayLikeCount() {
        log.info("在执行随笔点赞数据操作:{}", "----------------------------like-start----------------------------");
        if (operationUtils.hasKey(Constant.ESSAY_LIKE)) {
            Map<String, ?> stringMap = operationUtils.hGetAll(Constant.ESSAY_LIKE);
            Set<String> keys = operationUtils.hKeys(Constant.ESSAY_LIKE);
            keys.forEach(key -> informalMapper.update(new UpdateWrapper<Informal>().eq("id", key).set("like_count", Long.parseLong(String.valueOf(stringMap.get(key))))));
        }
        log.info("在执行随笔点赞数据操作:{}", "----------------------------like-end----------------------------");
    }

    @Async
    @Scheduled(cron = "0 0 0 * * ?")
    public void synchronousArticleLikeCount() {
        log.info("在执行文章观看数数据操作:{}", "----------------------------like-start----------------------------");
        if (operationUtils.hasKey(Constant.ARTICLE_LOOK_COUNT)) {
            Map<String, ?> stringMap = operationUtils.hGetAll(Constant.ARTICLE_LOOK_COUNT);
            Set<String> keys = operationUtils.hKeys(Constant.ARTICLE_LOOK_COUNT);
            keys.forEach(key -> articleMapper.update(new UpdateWrapper<Article>().eq("id", key).set("look_count", Long.parseLong(String.valueOf(stringMap.get(key))))));
        }
        log.info("在执行文章观看数数据操作:{}", "----------------------------like-end----------------------------");
    }

    @Async
    @Scheduled(cron = "0 0 2 * * ?")
    @DeleteNavigationAnnotation
    public void pingLink() {
        List<Navigation> navigations = navigationMapper.selectList(new QueryWrapper<Navigation>().eq("is_ok", true));
        List<Navigation> list = new ArrayList<>(navigations.size());
        navigations.forEach(navigation ->
                poolExecutor.submit(() -> {
                    if (getResponse(navigation.getUrl()).getStatus() != 200) {
                        navigation.setOk(false);
                        list.add(navigation);
                    }
                })
        );
        if(list.size() > 0) navigationService.saveOrUpdateBatch(list);
    }


    public HttpResponse getResponse(String url) {
        return HttpRequest.get(url)
                .header(Header.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36")
                .timeout(20000)
                .execute();
    }
}
