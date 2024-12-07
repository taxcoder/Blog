package com.tanx.blog.entity.common;

import lombok.Data;
import lombok.ToString;

/**
 * @description: 域名信息
 * @project_name: blog-server
 * @author: 谭翔
 * @date: 2023/9/26 22:50
 */

@Data
@ToString
public class IpEntity {

    /*** 国家 */
    private String country;
    /*** 地区 */
    private String region;
    /*** 省 */
    private String provinceName;
    /*** 市 */
    private String cityName;
    /*** 运营商 */
    private String isp;
    /*** 获取用户的ip地址 */
    private String requestIp;
}
