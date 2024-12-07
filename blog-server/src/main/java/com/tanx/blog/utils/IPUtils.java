package com.tanx.blog.utils;

import jakarta.servlet.http.HttpServletRequest;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class IPUtils {

    private static final String UNKNOWN = "unknown";
    private static final String HEADER_FORWARDED = "x-forwarded-for";
    private static final String HEADER_PROXY = "Proxy-Client-IP";
    private static final String HEADER_WL_PROXY = "WL-Proxy-Client-IP";
    private static final String HEADER_HTTP = "HTTP_CLIENT_IP";
    private static final String HEADER_HTTP_FORWARDED = "HTTP_X_FORWARDED_FOR";
    private static final String LOCAL_IP = "127.0.0.1";
    private static final String LOCAL_HOST = "localhost";

    /**
     * 获取 IP 地址
     */
    @SuppressWarnings("DuplicatedCode")
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader(HEADER_FORWARDED);

        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader(HEADER_PROXY);
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader(HEADER_WL_PROXY);
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader(HEADER_HTTP);
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader(HEADER_HTTP_FORWARDED);
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 本机访问
        if (LOCAL_IP.equalsIgnoreCase(ip) || LOCAL_HOST.equalsIgnoreCase(ip) || "0:0:0:0:0:0:0:1".equalsIgnoreCase(ip)) {
            // 根据网卡取本机配置的 IP
            try {
                InetAddress localHost = InetAddress.getLocalHost();
                ip = localHost.getHostAddress();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }

        // 对于通过多个代理的情况，第一个 IP 为客户端真实 IP,多个 IP 按照','分割
        if (ip != null && ip.length() > 15) {
            if (ip.indexOf(",") > 15) {
                ip = ip.substring(0, ip.indexOf(","));
            }
        }

        return ip;
    }
}