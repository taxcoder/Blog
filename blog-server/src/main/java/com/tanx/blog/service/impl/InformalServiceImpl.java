package com.tanx.blog.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tanx.blog.annotation.AddTextCountAnnotation;
import com.tanx.blog.constant.Constant;
import com.tanx.blog.entity.po.Emoji;
import com.tanx.blog.entity.po.Informal;
import com.tanx.blog.entity.vo.AddEssayVo;
import com.tanx.blog.exception.DataOperationErrorException;
import com.tanx.blog.mapper.EmojiMapper;
import com.tanx.blog.mapper.InformalMapper;
import com.tanx.blog.service.InformalService;
import com.tanx.blog.utils.FunctionUtils;
import com.tanx.blog.utils.PageUtils;
import com.tanx.blog.utils.RedisOperationUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 谭翔
 * @description 针对表【b_informal( 随笔表 )】的数据库操作Service实现
 * @createDate 2023-09-02 19:23:38
 */
@Slf4j
@Service
public class InformalServiceImpl extends ServiceImpl<InformalMapper, Informal>
        implements InformalService {

    @Resource
    private InformalMapper informalMapper;

    @Resource
    private EmojiMapper emojiMapper;
    @Resource
    private RedisOperationUtils operationUtils;

    private List<Informal> sort(List<Informal> list) {
        try {
            list.sort(Comparator.comparing(Informal::getCreateTime).reversed());
            return list;
        } catch (Exception e) {
            String format = StrUtil.format(Constant.ESSAY_LIST, Boolean.TRUE);
            String formatFalse = StrUtil.format(Constant.ESSAY_LIST, Boolean.FALSE);
            operationUtils.delete(format);
            operationUtils.delete(formatFalse);
            throw new DataOperationErrorException("获取失败，请尝试重新刷新！");
        }
    }

    @Override
    public PageUtils<Informal> selectInformalList(int current, int size, boolean recovery) {
        // TODO: 修改成map的格式,，解决增加点赞的问题
        PageUtils<Informal> pageUtils = new PageUtils<>(current, size);
        String key = StrUtil.format(Constant.ESSAY_LIST, recovery);
        return FunctionUtils.isTrueOrFalse(operationUtils.hasKey(key), pageUtils).trueOrFalseHandle((utils) -> {
            List<Informal> informal = sort((List<Informal>) operationUtils.hValues(key));
            utils.setRecords(informal.stream().skip((long) current * size).limit(size).toList());
            utils.setTotal(informal.size());
            return utils;
        }, (utils) -> {
            List<Informal> informalList = sort(informalMapper.selectList(new QueryWrapper<Informal>().eq("recovery", recovery)));
            utils.setRecords(informalList.stream().skip((long) current * size).limit(size).toList());
            utils.setTotal(informalList.size());
            operationUtils.hPutAll(key, informalList.stream().collect(Collectors.toMap(Informal -> String.valueOf(Informal.getId()), Informal -> Informal)));
            return utils;
        });
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, DataOperationErrorException.class})
    public int addLike(int id) {
        Informal informal = null;
        if (!operationUtils.hasKey(Constant.ESSAY_LIKE) || operationUtils.hGet(Constant.ESSAY_LIKE, String.valueOf(id)) == null) {
            informal = informalMapper.selectById(id);
            operationUtils.hPut(Constant.ESSAY_LIKE, String.valueOf(id), informal.getLikeCount());
        }
        operationUtils.hIncrBy(Constant.ESSAY_LIKE, String.valueOf(id), 1);
        Informal ret = (Informal) operationUtils.hGet(StrUtil.format(Constant.ESSAY_LIST, Boolean.FALSE), String.valueOf(id));
        // 如果redis内没有点赞信息，则直接从数据库查询，并存入redis内，redis内存储的日记列表也进行更新
        ret.setLikeCount(informal != null ? informal.getLikeCount() + 1 : (int) operationUtils.hGet(Constant.ESSAY_LIKE, String.valueOf(id)));
        operationUtils.hPut(StrUtil.format(Constant.ESSAY_LIST, Boolean.FALSE), String.valueOf(id), ret);
        return ret.getLikeCount();
    }

    @Override
    public String restore(int id) {
        Informal informal = new Informal();
        informal.setId(id);
        informal.setRecovery(false);
        // 判断是否更新成功
        FunctionUtils.isError(informalMapper.updateById(informal) != 1).throwFunction("还原失败！");
        String format = StrUtil.format(Constant.ESSAY_LIST, Boolean.TRUE);
        if (operationUtils.hExists(format, String.valueOf(id)) && operationUtils.hDelete(format, String.valueOf(id)) != 1) {
            throw new DataOperationErrorException("缓存更新失败！");
        }
        String formatFalse = StrUtil.format(Constant.ESSAY_LIST, Boolean.FALSE);
        if (operationUtils.hExists(formatFalse, String.valueOf(id)) && operationUtils.hDelete(formatFalse, String.valueOf(id)) != 1) {
            throw new DataOperationErrorException("缓存更新失败！");
        }

        return "还原成功！";
    }

    @Override
    public String removeEssayById(int id) {
        Informal informal = new Informal();
        informal.setId(id);
        informal.setRecovery(true);
        informal.setRemoveTime(new Timestamp(System.currentTimeMillis()));
        // 判断是否删除成功
        FunctionUtils.isError(informalMapper.updateById(informal) == 0).throwFunction("删除失败！");
        String format = StrUtil.format(Constant.ESSAY_LIST, Boolean.FALSE);
        if (operationUtils.hExists(format, String.valueOf(id)) && operationUtils.hDelete(format, String.valueOf(id)) != 1) {
            throw new DataOperationErrorException("缓存更新失败！");
        }

        operationUtils.hPut(StrUtil.format(Constant.ESSAY_LIST, Boolean.TRUE), String.valueOf(id), informal);
        return "删除成功！";
    }

    @Override
    public String realRemoveEssayById(int id) {
        QueryWrapper<Informal> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id).eq("recovery", true);
        // 判断是否删除成功
        FunctionUtils.isError(informalMapper.delete(wrapper) != 1).throwFunction("删除失败！");
        String format = StrUtil.format(Constant.ESSAY_LIST, Boolean.TRUE);
        if(operationUtils.hExists(format, String.valueOf(id)) && operationUtils.hDelete(format, String.valueOf(id)) != 1) {
            throw new DataOperationErrorException("缓存更新失败！");
        }
        return "删除成功！";
    }

    @Override
    public Informal selectInformalById(int id) {
        String key = StrUtil.format(Constant.ESSAY_LIST, Boolean.FALSE);
        if (operationUtils.hasKey(key)) {
            Informal informalList = (Informal) operationUtils.hGet(key, String.valueOf(id));
            if (informalList == null) operationUtils.delete(key);
            else return informalList;
        }
        Informal informal = informalMapper.selectById(id);
        // 判断查询到的日记是否为null
        FunctionUtils.isNull(informal).throwFunction("传入参数异常！");
        return informal;
    }

    @Override
    public String updateContentById(String content, int id) {
        Informal informal = informalMapper.selectById(id);
        FunctionUtils.isNull(informal).throwFunction("错误的参数！");
        // 判断内容是否发生了改变
        FunctionUtils.isError(content.equals(informal.getContent())).throwFunction("当前传入的内容无需更改！");
        informal.setContent(content);
        // 判断是否更新成功了
        FunctionUtils.isError(informalMapper.updateById(informal) == 0).throwFunction("内容更新失败！");
        operationUtils.hPut(StrUtil.format(Constant.ESSAY_LIST, Boolean.FALSE), String.valueOf(id), informal);
        return "更新成功！";
    }

    @Override
    @AddTextCountAnnotation
    @Transactional(rollbackFor = {Exception.class, DataOperationErrorException.class})
    public String addInformal(AddEssayVo addEssayVo, HashMap<String, Integer> map, String province) {

        Emoji emoji = emojiMapper.selectById(addEssayVo.getEmojiId());
        FunctionUtils.isNull(emoji).throwFunction("传入了错误的参数！");

        Informal informal = new Informal();
        informal.setTemperature(map.get("temperature"));
        informal.setContent(addEssayVo.getContent());
        informal.setMood(emoji.getIcon());
        informal.setProvince(province);
        informal.setWeather(map.get("icon"));

        FunctionUtils.isError(informalMapper.insert(informal) == 0).throwFunction("上传失败！");

        operationUtils.hPut(StrUtil.format(Constant.ESSAY_LIST, Boolean.FALSE), String.valueOf(informal.getId()), informal);
        if (!operationUtils.hasKey(Constant.TEXT_COUNT)) operationUtils.set(Constant.TEXT_COUNT, 0);
        operationUtils.incrBy(Constant.TEXT_COUNT, addEssayVo.getContent().length());
        return "上传成功！";
    }

    @Override
    public PageUtils<Informal> selectSearchDataById(int searchValue, int current, int size, boolean recovery) {
        PageUtils<Informal> utils = new PageUtils<>(current, size);
        QueryWrapper<Informal> wrapper = new QueryWrapper<>();
        wrapper.eq("recovery", recovery).last("and LOCATE('" + searchValue + "', id) > 0");
        List<Informal> informalList = informalMapper.selectList(wrapper);
        utils.setRecords(informalList.stream().skip((long) current * size).limit(size).toList());
        utils.setTotal(informalList.size());
        return utils;
    }

    @Override
    public PageUtils<Informal> selectSearchDataLikeContent(String searchValue, int current, int size, boolean recovery) {
        PageUtils<Informal> utils = new PageUtils<>(current, size);
        QueryWrapper<Informal> wrapper = new QueryWrapper<>();
        wrapper.eq("recovery", recovery);
        wrapper.last("and LOCATE('" + searchValue + "', content) > 0");
        List<Informal> informalList = informalMapper.selectList(wrapper);
        utils.setRecords(informalList.stream().skip((long) current * size).limit(size).toList());
        utils.setTotal(informalList.size());
        return utils;
    }

    @Override
    public PageUtils<Informal> selectSearchDataByLikeCountEqualOrGreaterThan(int searchValue, int current, int size, boolean recovery) {
        PageUtils<Informal> utils = new PageUtils<>(current, size);
        QueryWrapper<Informal> wrapper = new QueryWrapper<>();
        wrapper.eq("recovery", recovery);
        wrapper.ge("like_count", searchValue);
        List<Informal> informalList = informalMapper.selectList(wrapper);
        utils.setRecords(informalList.stream().skip((long) current * size).limit(size).toList());
        utils.setTotal(informalList.size());
        return utils;
    }

    @Override
    public PageUtils<Informal> selectSearchDataByLikeCountEqualOrLessThan(int searchValue, int current, int size, boolean recovery) {
        PageUtils<Informal> utils = new PageUtils<>(current, size);
        QueryWrapper<Informal> wrapper = new QueryWrapper<>();
        wrapper.eq("recovery", recovery);
        wrapper.le("like_count", searchValue);
        List<Informal> informalList = informalMapper.selectList(wrapper);
        utils.setRecords(informalList.stream().skip((long) current * size).limit(size).toList());
        utils.setTotal(informalList.size());
        return utils;
    }

    @Override
    public PageUtils<Informal> selectSearchDataByLikeCountEqual(int searchValue, int current, int size, boolean recovery) {
        PageUtils<Informal> utils = new PageUtils<>(current, size);
        QueryWrapper<Informal> wrapper = new QueryWrapper<>();
        wrapper.eq("recovery", recovery);
        wrapper.eq("like_count", searchValue);
        List<Informal> informalList = informalMapper.selectList(wrapper);
        utils.setRecords(informalList.stream().skip((long) current * size).limit(size).toList());
        utils.setTotal(informalList.size());
        return utils;
    }

    @Override
    public PageUtils<Informal> selectSearchDataByProvince(String searchValue, int current, Integer size, boolean recovery) {
        PageUtils<Informal> utils = new PageUtils<>(current, size);
        QueryWrapper<Informal> wrapper = new QueryWrapper<>();
        wrapper.eq("recovery", recovery);
        wrapper.eq("province", searchValue);
        List<Informal> list = informalMapper.selectList(wrapper);
        utils.setRecords(list.stream().skip((long) current * size).limit(size).toList());
        utils.setTotal(list.size());
        return utils;
    }

    @Override
    public List<? extends HashMap<String, ?>> selectProvinceCount(boolean recovery) {
        String key = StrUtil.format(Constant.ESSAY_LIST, recovery);

        if (operationUtils.hasKey(key)) {
            List<Informal> list = (List<Informal>) operationUtils.hValues(key);
            List<HashMap<String, Object>> mapList = new ArrayList<>(list.size());
            HashMap<String, Integer> map = new HashMap<>((int) (list.size() + list.size() * 0.75));
            list.forEach(l -> map.put(l.getProvince(), map.get(l.getProvince()) == null ? 0 : map.get(l.getProvince()) + 1));

            map.keySet().forEach(k -> {
                HashMap<String, Object> hashMap = new HashMap<>(4);
                hashMap.put("province", k);
                hashMap.put("count", map.get(k));
                mapList.add(hashMap);
            });
            return mapList;
        }

        return informalMapper.selectByProvinceList(recovery);
    }

    @Override
    public long allCount() {
        String key = StrUtil.format(Constant.ESSAY_LIST, Boolean.FALSE);
        return FunctionUtils.isTrueOrFalse(operationUtils.hasKey(key), 0).trueOrFalseHandle(
                (count) -> Math.toIntExact(operationUtils.hSize(key)),
                (count) -> Math.toIntExact(count(new QueryWrapper<Informal>().eq("recovery", false)))
        );
    }
}



