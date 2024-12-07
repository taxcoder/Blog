package com.tanx.blog.api;

import cn.hutool.core.net.NetUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.tanx.blog.entity.common.IpEntity;
import com.tanx.blog.exception.DataOperationErrorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * @description: IP数据云API
 * @project_name: blog-server
 * @author: 谭翔
 * @date: 2023/9/29 14:53
 */

@Slf4j
@Component
@PropertySource(value = "classpath:apiKey.properties")
public class IpAddressInfoApi {

    @Value("${ip.datacloud.key}")
    private String dataCloudKey;

    @Value("${ip.dashborad.key}")
    private String dashboradKey;

    public HashMap<String, String> belongingPlace(String ip) {
        if (NetUtil.isInnerIP(ip)) {
            throw new DataOperationErrorException("无法通过内网IP查询数据！");
        }

        log.info("ip:{}", ip);

        HashMap<String, String> result = dataCloud(ip);

        result.put("province", result.get("province").replace("省", ""));
        return result;
    }

    // IP数据云
    private HashMap<String, String> dataCloud(String ip) {
        String message = HttpUtil.get("https://api.ipdatacloud.com/v2/query?ip=" + ip + "&key=" + dataCloudKey);
        HashMap hashMap = getHashMap(message);

        log.info("IP数据云:message:{}", message);

        // 如果返回的状态码不是200或者是当前已经超出key的有效期，就依次调用
        if (!"200".equals(String.valueOf(hashMap.get("code")))) {
            return baidu(ip);
        }

        HashMap location = getHashMap(String.valueOf(getHashMap(String.valueOf(hashMap.get("data"))).get("location")));
        String province = String.valueOf(location.get("province"));
        String city = String.valueOf(location.get("city"));

        if("".equals(city) || "0".equals(city)) {
            return baidu(ip);
        }

        return new HashMap<>() {{
            put("province", province);
            put("city", city);
        }};
    }

    // 百度
    private HashMap<String, String> baidu(String ip) {
        String message = HttpUtil.get("https://qifu-api.baidubce.com/ip/geo/v1/district?ip=" + ip);
        HashMap hashMap = getHashMap(message);

        log.info("百度:message:{}", message);

        if (!"Success".equals(hashMap.get("code").toString())) {
            return dashBoard(ip);
        }
        // 获取的城市
        HashMap data = getHashMap(String.valueOf(hashMap.get("data")));
        String province = String.valueOf(data.get("prov"));
        String city = String.valueOf(data.get("city"));
        return new HashMap<>() {{
            put("province", province);
            put("city", city.equals("0") ? "" : city);
        }};
    }

    // 聚合数据
    private HashMap<String, String> dashBoard(String ip) {
        String message = HttpUtil.get("http://apis.juhe.cn/ip/ipNewV3?ip=" + ip + "&key=" + dashboradKey);
        HashMap hashMap = getHashMap(message);

        log.info("聚合数据:message:{}", message);

        if (!"200".equals(hashMap.get("resultcode").toString())) {
            throw new DataOperationErrorException("查询失败，请重新尝试！");
        }

        // 获取的城市
        HashMap result = getHashMap(hashMap.get("result").toString());
        String city = result.get("City").toString();
        String province = result.get("Province").toString();
        return new HashMap<>() {{
            put("province", province);
            put("city", city.equals("0") ? "" : city);
        }};
    }


    private HashMap getHashMap(String text) {
        return JSON.parseObject(text, HashMap.class);
    }
}
