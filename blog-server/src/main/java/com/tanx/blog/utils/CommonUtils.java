package com.tanx.blog.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.tanx.blog.api.OssOperationApi;
import com.tanx.blog.entity.common.OssEntity;
import com.tanx.blog.entity.po.Article;
import com.tanx.blog.exception.DataOperationErrorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description: 通用工具类
 * @project_name: blog-server
 * @author: 谭翔
 * @date: 2023/9/2 0:20
 */

@Slf4j
public class CommonUtils {

    public static boolean isPureDigital(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

    public static String uploadImage(MultipartFile image, Article article, OssEntity oss) {
        if (image.isEmpty()) {
            // 如果传递的是空的，则从API获取一张图片填充
            List<String> list = List.of("dongman");
            HashMap map = JSON.parseObject(HttpUtil.get("https://tuapi.eees.cc/api.php?category=" + list.get(0) + "&type=json"), HashMap.class);
            if (!map.get("result").equals("200")) throw new DataOperationErrorException("图片API无法获取图片！");

            try (HttpResponse response = HttpRequest.get(map.get("img").toString()).header("Accept", "image/" + map.get("format")).execute()) {
                String type = response.header("Content-Type");
                String suffix = type.substring(type.lastIndexOf("/") + 1);
                String fileName = "article-cover/data-" + System.currentTimeMillis() + "." + "webp";
                log.info("fileName:{},response.contentLength():{}", fileName, response.contentLength());
                return OssOperationApi.getInstance().uploadImage(fileName, CommonUtils.saveImage(response.bodyStream(), suffix), oss);
            } catch (Exception e) {
                throw new DataOperationErrorException("封面上传失败！");
            }
        } else {
            if (matcher("jpg|png|jpeg|webp", image.getContentType()) != null) {
                // 如果传递的不是空的，并且和数据库内的图片url不一致，则表示换了一张图片
                String fileName = "article-cover/data-" + System.currentTimeMillis() + "." + "webp";
                try {
                    return OssOperationApi.getInstance().uploadImage(fileName, CommonUtils.saveImage(image.getInputStream(), matcher("jpg|png|jpeg|webp", image.getContentType())), oss);
                } catch (IOException e) {
                    throw new DataOperationErrorException("封面上传失败！");
                }
            } else {
                throw new DataOperationErrorException("请传入正确的封面！");
            }
        }
    }

    /**
     * 判断字符串是否为base64编码
     *
     * @param str 需要判断的字符串
     * @return Ascii码说明：共95个可读字符
     * 0～31及127(共33个)是控制字符或通信专用字符（其余为可显示字符）
     * 32～126(共95个)是字符(32是空格），其中48～57为0到9十个阿拉伯数字。
     * 65～90为26个大写英文字母，97～122号为26个小写英文字母，其余为一些标点符号、运算符号等。
     */
    public static boolean checkBase64(String str) {
        //使用正则来判断是否符合base64编码的特征（但是无法排除类似于root这种特殊情况）
        String base64Pattern = "^(data:image/)(jpg|png|jpeg)(;base64,)+([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{2,4}={0,2})$";
        return str.matches(base64Pattern);
    }

    /**
     * 判断传入的字符串是否是base64
     *
     * @param base64 需要判断的数据
     * @return 判断成功返回转换后的字节数组
     */
    public static byte[] base642byte(String base64) {
        if (checkBase64(base64)) {
            return Base64.getMimeDecoder().decode(base64.split(",")[1]);
        }
        throw new DataOperationErrorException("请传入正确的base64参数！");
    }

    /**
     * 获取匹配到的数据
     *
     * @param regex 正则表达书
     * @param str   需要匹配的数据
     * @return 返回匹配出的数据
     */
    public static String matcher(String regex, String str) {
        Matcher matcher = Pattern.compile(regex).matcher(str);
        return matcher.find() ? matcher.group() : null;
    }

    public static String dateCurrent() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        return format.format(new Date());
    }

    /**
     * 保存图片为 webp
     *
     * @param multipartFile 上传数据流
     * @return 返回优化后的字节数据
     * @throws IOException IO异常
     */
    public static byte[] saveImage(MultipartFile multipartFile) throws IOException {
        return saveImage(multipartFile.getInputStream(), multipartFile.getContentType() == null ? multipartFile.getContentType().split("/")[1] : null);
    }

    /**
     * 保存图片为 webp
     *
     * @param stream 字节流
     * @return 返回优化后的字节数据
     * @throws IOException IO异常
     */
    public static byte[] saveImage(InputStream stream) throws IOException {
        return saveImage(stream, null);
    }

    /**
     * 保存图片为 webp
     *
     * @param bytes 字节数据
     * @return 返回优化后的字节数据
     * @throws IOException IO异常
     */
    public static byte[] saveImage(byte[] bytes) throws IOException {
        File tempFile = File.createTempFile("data-", "." + "webp");
        OutputStream outputStream = new FileOutputStream(tempFile);
        outputStream.write(bytes);
        outputStream.flush();
        outputStream.close();
        return saveImage(new FileInputStream(tempFile));
    }

    /**
     * 保存图片为 webp
     *
     * @param bytes  字节数据
     * @param suffix 图片格式后缀名
     * @return 返回优化后的字节数据
     * @throws IOException IO异常
     */
    public static byte[] saveImage(byte[] bytes, String suffix) throws IOException {
        File tempFile = File.createTempFile("data-", "." + suffix);
        OutputStream outputStream = new FileOutputStream(tempFile);
        outputStream.write(bytes);
        outputStream.flush();
        outputStream.close();
        return saveImage(new FileInputStream(tempFile), suffix);
    }

    /**
     * 保存图片为 webp
     *
     * @param stream 字节流
     * @param suffix 后缀
     * @return 返回优化后的字节数据
     * @throws IOException IO异常
     */
    public static byte[] saveImage(InputStream stream, String suffix) throws IOException {
        if (suffix != null && suffix.equals("webp")) {
            return stream.readAllBytes();
        }
        File tempFile;
        if (isLinux()) {
            tempFile = File.createTempFile("data-", "." + (suffix == null ? "webp" : suffix), new File("/tmp"));
        } else {
            tempFile = File.createTempFile("data-", "." + (suffix == null ? "webp" : suffix), new File("I:\\tmp"));
        }
        BufferedImage bufferedImage = ImageIO.read(stream);
        ImageIO.write(bufferedImage, "webp", tempFile);
        byte[] bytes = Files.readAllBytes(tempFile.toPath());
        boolean delete = tempFile.delete();
        log.info("文件：{} 删除状态:{}", tempFile.getName(), delete ? "删除成功" : "删除失败");
        return bytes;
    }

    public static boolean isLinux() {
        return System.getProperty("os.name").toLowerCase().contains("linux");
    }

    public static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("windows");
    }

    public static String currentDate() {
        Calendar calendar = Calendar.getInstance();
        // 上传当天日期
        return calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.DATE) + "/" + calendar.get(Calendar.HOUR);
    }
}
