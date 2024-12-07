package com.tanx.blog.api;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.tanx.blog.exception.DataOperationErrorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

/**
 * @description: 和风天气API
 * @project_name: blog-server
 * @author: 谭翔
 * @date: 2023/9/29 17:21
 */

@Slf4j
@Component
@PropertySource(value = "classpath:apiKey.properties")
public class WindyWeatherApi {

    @Value("${windy.weather.key}")
    private String weatherKey;

    @Value("${windy.weather.vip.key}")
    private String weatherVipKey;

    public HashMap<String, Integer> weather(HashMap<String, String> map) {
        String weather = HttpUtil.get("https://devapi.qweather.com/v7/weather/now?key=" + weatherKey + "&location=" + location(weatherKey, map));
        if (!"200".equals(String.valueOf(getHashMap(weather).get("code")))) {
            throw new DataOperationErrorException("未来天气获取失败！");
        }
        HashMap<?, ?> now = getHashMap(String.valueOf(getHashMap(weather).get("now")));
        return new HashMap<>() {{
            put("temperature", Integer.parseInt(now.get("temp").toString()));
            put("icon", Integer.parseInt(now.get("icon").toString()));
        }};
    }

    public HashMap<String, ?> futureWeather(HashMap<String, String> map) {
        String future = HttpUtil.get("https://api.qweather.com/v7/weather/30d?location=" + location(weatherVipKey, map) + "&key=" + weatherVipKey);
        if (!"200".equals(String.valueOf(getHashMap(future).get("code")))) {
            throw new DataOperationErrorException("未来天气获取失败！");
        }
        String updateTime = String.valueOf(getHashMap(future).get("updateTime"));
        List<HashMap> daily = JSONArray.parseArray(String.valueOf(getHashMap(future).get("daily")), HashMap.class);
            return new HashMap<>() {{
            put("updateTime", updateTime);
            put("daily", daily);
        }};
    }

    private String location(String key, HashMap<String, String> map) {
        var province = map.get("province");
        var city = "".equals(map.get("city")) ? province : map.get("city");
        String location = HttpUtil.get(StrUtil.format("https://geoapi.qweather.com/v2/city/lookup?location={}&key={}&adm={}",city, key, province));
        HashMap<?, ?> hashMap = getHashMap(location);
        List<HashMap> hashMaps = JSONArray.parseArray(String.valueOf(hashMap.get("location")), HashMap.class);
        // 城市的location
        return String.valueOf(hashMaps.get(0).get("id"));
    }

    private HashMap<?, ?> getHashMap(String text) {
        return JSON.parseObject(text, HashMap.class);
    }
}
