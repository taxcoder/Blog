package com.tanx.blog.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author taxcoder
 */
@Slf4j
@Component
public class RedisOperationUtils {

    private static final Long SUCCESS = 1L;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private ValueOperations<String, Object> valueOperations() {
        return redisTemplate.opsForValue();
    }

    private ListOperations<String, Object> listOperations() {
        return redisTemplate.opsForList();
    }

    private HashOperations<String, String, Object> hashOperations() {
        return redisTemplate.opsForHash();
    }

    private SetOperations<String, Object> setOperations() {
        return redisTemplate.opsForSet();
    }

    private ZSetOperations<String, Object> zestOperations() {
        return redisTemplate.opsForZSet();
    }
    /* -------------------key相关操作--------------------- */

    /**
     * 获取锁
     *
     * @param lockKey 锁的key
     * @param value 值
     * @param expireTime：单位-秒
     * @return 返回获取锁的状态
     */
    public boolean getLock(String lockKey, Object value, long expireTime) {
        boolean ret = false;
        try {
            String script = "if redis.call('setNx',KEYS[1],ARGV[1]) then if redis.call('get',KEYS[1])==ARGV[1] then return redis.call('expire',KEYS[1],ARGV[2]) else return 0 end end";

            RedisScript<Long> redisScript = new DefaultRedisScript<>(script, Long.class);
            Long result = redisTemplate.execute(redisScript, Collections.singletonList(lockKey), value,expireTime);
            log.info("result:{}", result);
            if (SUCCESS.equals(result)) {
                return true;
            }

        } catch (Exception e) {
            log.error("getLock error:{}", e.getMessage(), e);
        }
        return ret;
    }

    /**
     * 释放锁
     *
     * @param lockKey　锁的key
     * @param value 值
     * @return 返回释放状态
     */
    public boolean releaseLock(String lockKey, String value) {

        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

        RedisScript<String> redisScript = new DefaultRedisScript<>(script, String.class);

        Object result = redisTemplate.execute(redisScript, Collections.singletonList(lockKey), value);
        return SUCCESS.equals(result);
    }

    /**
     * 删除key
     *
     * @param key key
     */
    public void delete(String key) {
        if (hasKey(key)) {
            redisTemplate.delete(key);
        }
    }

    /**
     * 批量删除key
     *
     * @param keys 存储key的集合
     */
    public void delete(Collection<String> keys) {
        try {
            redisTemplate.delete(keys);
        } catch (RedisConnectionFailureException | QueryTimeoutException exception) {
            log.error("errorMessage:{}",exception.getMessage());
        }
    }

    /**
     * 序列化key
     *
     * @param key key
     * @return 返回序列化后的信息
     */
    public byte[] dump(String key) {
        return redisTemplate.dump(key);
    }

    /**
     * 是否存在key
     *
     * @param key key
     * @return 返回是否存在key的信息
     */
    public Boolean hasKey(String key) {
        Boolean result = false;
        try {
            result = redisTemplate.hasKey(key);
        } catch (RedisConnectionFailureException | QueryTimeoutException exception) {
            log.error("errorMessage:{}",exception.getMessage());
        }
        return result;
    }

    /**
     * 设置过期时间
     *
     * @param key     key
     * @param timeout 超时时间
     * @param unit    时间单位 单位
     * @return 返回是设置的结果
     */
    public Boolean expire(String key, long timeout, TimeUnit unit) {
        return hasKey(key) ? redisTemplate.expire(key, timeout, unit) : Boolean.FALSE;
    }

    /**
     * 设置过期时间
     *
     * @param key  key
     * @param date 时间
     * @return 返回设置的结果
     */
    public Boolean expireAt(String key, Date date) {
        return hasKey(key) ? redisTemplate.expireAt(key, date) : Boolean.FALSE;
    }

    /**
     * 查找匹配的key
     *
     * @param pattern 正则
     * @return 返回查询的结果
     */
    public Set<String> keys(String pattern) {
        try {
            return redisTemplate.keys(pattern);
        } catch (RedisConnectionFailureException | QueryTimeoutException exception) {
            log.error("errorMessage:{}",exception.getMessage());
        }
        return new HashSet<>(2);
    }

    /**
     * 将当前数据库的 key 移动到给定的数据库 db 当中
     *
     * @param key     key
     * @param dbIndex redis其他的数据库的索引
     * @return 返回移动的记过
     */
    public Boolean move(String key, int dbIndex) {
        return redisTemplate.move(key, dbIndex);
    }

    /**
     * 移除 key 的过期时间，key 将持久保持
     *
     * @param key key
     * @return 返回移除的结果
     */
    public Boolean persist(String key) {
        return redisTemplate.persist(key);
    }

    /**
     * 返回 key 的剩余的过期时间
     *
     * @param key  key
     * @param unit 时间单位
     * @return 返回剩余的时间
     */
    public Long getExpire(String key, TimeUnit unit) {
        return hasKey(key) ? redisTemplate.getExpire(key, unit) : Long.valueOf(0L);
    }

    /**
     * 返回 key 的剩余的过期时间
     *
     * @param key key
     * @return 返回获取的剩余时间
     */
    public Long getExpire(String key) {
        return hasKey(key) ? redisTemplate.getExpire(key) : Long.valueOf(0L);
    }

    /**
     * 从当前数据库中随机返回一个 key
     *
     * @return 返回获取的key
     */
    public String randomKey() {
        return redisTemplate.randomKey();
    }

    /**
     * 修改 key 的名称
     *
     * @param oldKey 旧的key
     * @param newKey 新的key
     */
    public void rename(String oldKey, String newKey) {
        redisTemplate.rename(oldKey, newKey);
    }

    /**
     * 仅当 newKey 不存在时，将 oldKey 改名为 newKey
     *
     * @param oldKey 旧的key
     * @param newKey 新的key
     * @return 返回设置的结果
     */
    public Boolean renameIfAbsent(String oldKey, String newKey) {
        try {
            return redisTemplate.renameIfAbsent(oldKey, newKey);
        } catch (RedisConnectionFailureException | QueryTimeoutException exception) {
            log.error("errorMessage:{}",exception.getMessage());
            return Boolean.FALSE;
        }
    }

    /**
     * 返回 key 所储存的值的类型
     *
     * @param key key
     * @return 返回key存储数据的类型
     */
    public DataType type(String key) {
        return redisTemplate.type(key);
    }

    /* -------------------string相关操作--------------------- */

    /**
     * 设置指定 key 的值
     *
     * @param key   key
     * @param value value
     */
    public void set(String key, Object value) {
        try {
            valueOperations().set(key, value);
        } catch (RedisConnectionFailureException | QueryTimeoutException exception) {
            log.error("errorMessage:{}",exception.getMessage());
        }
    }

    /**
     * 获取指定 key 的值
     *
     * @param key key
     * @return 返回对应的key存储的数据
     */
    public Object get(String key) {
        return hasKey(key) ? valueOperations().get(key) : null;
    }

    /**
     * 返回 key 中字符串值的子字符
     *
     * @param key   key
     * @param start 开始索引
     * @param end   结束索引
     * @return 返回获取数据
     */
    public String getRange(String key, long start, long end) {
        return valueOperations().get(key, start, end);
    }

    /**
     * 将给定 key 的值设为 value ，并返回 key 的旧值(old value)
     *
     * @param key   key
     * @param value value
     * @return 返回覆盖前存储的value
     */
    public String getAndSet(String key, String value) {
        return (String) valueOperations().getAndSet(key, value);
    }

    /**
     * 对 key 所储存的字符串值，获取指定偏移量上的位(bit)
     *
     * @param key    key
     * @param offset 便宜量
     * @return 返回指定偏移量上的位
     */
    public Boolean getBit(String key, long offset) {
        return valueOperations().getBit(key, offset);
    }

    /**
     * 批量获取
     *
     * @param keys keys
     * @return 获取多个value
     */
    public List<Object> multiGet(Collection<String> keys) {
        return valueOperations().multiGet(keys);
    }

    /**
     * 设置ASCII码, 字符串'a'的ASCII码是97, 转为二进制是'01100001', 此方法是将二进制第offset位值变为value
     *
     * @param key    key
     * @param offset 位置
     * @param value  值,true为1, false为0
     * @return 设置ASCII码, 返回设置的结果
     */
    public boolean setBit(String key, long offset, boolean value) {
        return Boolean.TRUE.equals(valueOperations().setBit(key, offset, value));
    }

    /**
     * 将值 value 关联到 key ，并将 key 的过期时间设为 timeout
     *
     * @param key     key
     * @param value   value
     * @param timeout 过期时间
     * @param unit    时间单位
     *                时间单位, 天:TimeUnit.DAYS 小时:TimeUnit.HOURS 分钟:TimeUnit.MINUTES
     *                秒:TimeUnit.SECONDS 毫秒:TimeUnit.MILLISECONDS
     */
    public void setEx(String key, String value, long timeout, TimeUnit unit) {
        try {
            valueOperations().set(key, value, timeout, unit);
        } catch (RedisConnectionFailureException | QueryTimeoutException exception) {
            log.error("errorMessage:{}",exception.getMessage());
        }
    }

    /**
     * 只有在 key 不存在时设置 key 的值
     *
     * @param key   key
     * @param value value
     * @return 之前已经存在返回false, 不存在返回true
     */
    public boolean setIfAbsent(String key, String value) {
        return Boolean.TRUE.equals(valueOperations().setIfAbsent(key, value));
    }

    /**
     * 用 value 参数覆写给定 key 所储存的字符串值，从偏移量 offset 开始
     *
     * @param key    key
     * @param value  value
     * @param offset 从指定位置开始覆写
     */
    public void setRange(String key, String value, long offset) {
        valueOperations().set(key, value, offset);
    }

    /**
     * 获取字符串的长度
     *
     * @param key key
     */
    public Long size(String key) {
        return hasKey(key) ? valueOperations().size(key) : null;
    }

    /**
     * 批量添加
     *
     * @param maps maps
     */
    public void multiSet(Map<String, String> maps) {
        valueOperations().multiSet(maps);
    }

    /**
     * 同时设置一个或多个 key-value 对，当且仅当所有给定 key 都不存在
     *
     * @param maps maps
     * @return 之前已经存在返回false, 不存在返回true
     */
    public boolean multiSetIfAbsent(Map<String, String> maps) {
        return Boolean.TRUE.equals(valueOperations().multiSetIfAbsent(maps));
    }

    /**
     * 增加(自增长), 负数则为自减
     *
     * @param key key
     */
    public Long incr(String key) {
        return incrBy(key, 1);
    }


    /**
     * 增加(自增长), 负数则为自减
     *
     * @param key       key
     * @param increment 增加个数
     */
    public Long incrBy(String key, long increment) {
        return valueOperations().increment(key, increment);
    }

    /**
     * @param key       key
     * @param increment 增加个数
     */
    public Double incrByFloat(String key, double increment) {
        return valueOperations().increment(key, increment);
    }

    /**
     * 增加(自增长), 负数则为自减
     *
     * @param key key
     */
    public Long decrBy(String key) {
        return hasKey(key) ? valueOperations().decrement(key) : null;
    }

    /**
     * 增加(自增长), 负数则为自减
     *
     * @param key key
     */
    public Long decrBy(String key, long delta) {
        return hasKey(key) ? valueOperations().decrement(key, delta) : null;
    }


    /**
     * 追加到末尾
     *
     * @param key   key
     * @param value value
     */
    public Integer append(String key, String value) {
        return valueOperations().append(key, value);
    }

    /* -------------------hash相关操作------------------------- */

    /**
     * 获取存储在哈希表中指定字段的值
     *
     * @param key   key
     * @param field
     */
    public Object hGet(String key, Object field) {
        return hasKey(key) ? hashOperations().get(key, String.valueOf(field)) : null;
    }

    /**
     * 获取所有给定字段的值
     *
     * @param key key
     */
    public Map<String, ?> hGetAll(String key) {
        return hasKey(key) ? hashOperations().entries(key) : null;
    }

    /**
     * 获取所有给定字段的值
     *
     * @param key    key
     * @param fields
     */
    public List<Object> hMultiGet(String key, Collection<String> fields) {
        return hashOperations().multiGet(key, fields);
    }

    public void hPut(String key, Object hashKey, Object value) {
        try {
            hashOperations().put(key, String.valueOf(hashKey), value);
        } catch (RedisConnectionFailureException | QueryTimeoutException exception) {
            log.error("errorMessage:{}",exception.getMessage());
        }
    }

    public void hPutAll(String key, Map<String, ?> maps) {
        try {
            hashOperations().putAll(key, maps);
        } catch (RedisConnectionFailureException | QueryTimeoutException exception) {
            log.error("errorMessage:{}",exception.getMessage());
        }
    }

    /**
     * 仅当hashKey不存在时才设置
     *
     * @param key     key
     * @param hashKey
     * @param value   value
     */
    public Boolean hPutIfAbsent(String key, String hashKey, String value) {
        return hashOperations().putIfAbsent(key, hashKey, value);
    }

    /**
     * 删除一个或多个哈希表字段
     *
     * @param key    key
     * @param fields
     */
    public Long hDelete(String key, Object... fields) {
        return hasKey(key) ? hashOperations().delete(key, fields) : null;
    }

    /**
     * 查看哈希表 key 中，指定的字段是否存在
     *
     * @param key   key
     * @param field
     */
    public boolean hExists(String key, String field) {
        return hashOperations().hasKey(key, field);
    }

    /**
     * 为哈希表 key 中的指定字段的整数值加上增量 increment
     *
     * @param key       key
     * @param field
     * @param increment
     */
    public Long hIncrBy(String key, String field, long increment) {
        return hashOperations().increment(key, field, increment);
    }

    /**
     * 为哈希表 key 中的指定字段的整数值加上增量 increment
     *
     * @param key   key
     * @param field
     * @param delta
     */
    public Double hIncrByFloat(String key, String field, double delta) {
        return hashOperations().increment(key, field, delta);
    }

    /**
     * 获取所有哈希表中的字段
     *
     * @param key key
     */
    public Set<String> hKeys(String key) {
        return hashOperations().keys(key);
    }

    /**
     * 获取哈希表中字段的数量
     *
     * @param key key
     */
    public long hSize(String key) {
        return hasKey(key) ? hashOperations().size(key) : 0;
    }

    /**
     * 获取哈希表中所有值
     *
     * @param key key
     */
    public List<?> hValues(String key) {
        return hasKey(key) ? hashOperations().values(key) : new ArrayList<>(1);
    }

    /**
     * 迭代哈希表中的键值对
     *
     * @param key     key
     * @param options
     */
    public Cursor<Map.Entry<String, Object>> hScan(String key, ScanOptions options) {
        return hashOperations().scan(key, options);
    }

    /** ------------------------list相关操作---------------------------- */

    /**
     * 通过索引获取列表中的元素
     *
     * @param key   key
     * @param index
     */
    public String lIndex(String key, long index) {
        return (String) listOperations().index(key, index);
    }

    /**
     * 获取列表指定范围内的元素
     *
     * @param key   key
     * @param start 开始位置, 0是开始位置
     * @param end   结束位置, -1返回所有
     */
    public List<?> lRange(String key, long start, long end) {
        return hasKey(key) ? listOperations().range(key, start, end) : null;
    }

    /**
     * 存储在list头部
     *
     * @param key   key
     * @param value value
     */
    public Long lLeftPush(String key, String value) {
        return listOperations().leftPush(key, value);
    }

    /**
     * @param key   key
     * @param value value
     */
    public Long lLeftPushAll(String key, Object... value) {
        return listOperations().leftPushAll(key, value);
    }

    /**
     * @param key   key
     * @param value value
     */
    public boolean lLeftPushAll(String key, List<?> value) {
        Collection<Object> collection = new ArrayList<>(value);
        Long aLong = listOperations().leftPushAll(key, collection);
        return aLong != null && aLong == value.size();
    }

    /**
     * 当list存在的时候才加入
     *
     * @param key   key
     * @param value value
     */
    public Long lLeftPushIfPresent(String key, String value) {
        return listOperations().leftPushIfPresent(key, value);
    }

    /**
     * 如果pivot存在,再pivot前面添加
     *
     * @param key   key
     * @param pivot
     * @param value value
     */
    public Long lLeftPush(String key, String pivot, String value) {
        return listOperations().leftPush(key, pivot, value);
    }

    /**
     * @param key   key
     * @param value value
     */
    public Long lRightPush(String key, String value) {
        return listOperations().rightPush(key, value);
    }

    /**
     * @param key   key
     * @param value value
     */
    public Long lRightPushAll(String key, Object... value) {
        return listOperations().rightPushAll(key, value);
    }

    /**
     * @param key   key
     * @param value value
     */
    public boolean lRightPushAll(String key, List<?> value) {
        Collection<Object> collection = new ArrayList<>(value);
        Long aLong = null;
        if (value.size() > 0) {
            try {
                aLong = listOperations().rightPushAll(key, collection);
            } catch (RedisConnectionFailureException | QueryTimeoutException exception) {
                log.error("errorMessage:{}",exception.getMessage());
            }
        }
        return aLong != null && aLong == value.size();
    }

    /**
     * 为已存在的列表添加值
     *
     * @param key   key
     * @param value value
     */
    public Long lRightPushIfPresent(String key, String value) {
        return listOperations().rightPushIfPresent(key, value);
    }

    /**
     * 在pivot元素的右边添加值
     *
     * @param key   key
     * @param pivot
     * @param value value
     */
    public Long lRightPush(String key, String pivot, String value) {
        return listOperations().rightPush(key, pivot, value);
    }

    /**
     * 通过索引设置列表元素的值
     *
     * @param key   key
     * @param index 位置
     * @param value value
     */
    public void lSet(String key, long index, String value) {
        listOperations().set(key, index, value);
    }

    /**
     * 移出并获取列表的第一个元素
     *
     * @param key key
     * @return 删除的元素
     */
    public String lLeftPop(String key) {
        return (String) listOperations().leftPop(key);
    }

    /**
     * 移出并获取列表的第一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止
     *
     * @param key     key
     * @param timeout 等待时间
     * @param unit    时间单位
     *                时间单位
     */
    public String lBLeftPop(String key, long timeout, TimeUnit unit) {
        return (String) listOperations().leftPop(key, timeout, unit);
    }

    /**
     * 移除并获取列表最后一个元素
     *
     * @param key key
     * @return 删除的元素
     */
    public String lRightPop(String key) {
        return (String) listOperations().rightPop(key);
    }

    /**
     * 移出并获取列表的最后一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止
     *
     * @param key     key
     * @param timeout 等待时间
     * @param unit    时间单位
     *                时间单位
     */
    public String lBRightPop(String key, long timeout, TimeUnit unit) {
        return (String) listOperations().rightPop(key, timeout, unit);
    }

    /**
     * 移除列表的最后一个元素，并将该元素添加到另一个列表并返回
     *
     * @param sourceKey      原数组
     * @param destinationKey 新数组
     */
    public String lRightPopAndLeftPush(String sourceKey, String destinationKey) {
        return (String) listOperations().rightPopAndLeftPush(sourceKey, destinationKey);
    }

    /**
     * 从列表中弹出一个值，将弹出的元素插入到另外一个列表中并返回它； 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止
     *
     * @param sourceKey      原数组
     * @param destinationKey 新数组
     * @param timeout        超时时间
     * @param unit           时间单位
     */
    public String lBRightPopAndLeftPush(String sourceKey, String destinationKey, long timeout, TimeUnit unit) {
        return (String) listOperations().rightPopAndLeftPush(sourceKey, destinationKey, timeout, unit);
    }

    /**
     * 删除集合中值等于value得元素
     *
     * @param key   key
     * @param index index=0, 删除所有值等于value的元素; index>0, 从头部开始删除第一个值等于value的元素;
     *              index<0, 从尾部开始删除第一个值等于value的元素;
     * @param value value
     */
    public Long lRemove(String key, long index, String value) {
        return listOperations().remove(key, index, value);
    }

    /**
     * 裁剪list
     *
     * @param key   key
     * @param start 开始索引
     * @param end   结束索引
     */
    public void lTrim(String key, long start, long end) {
        listOperations().trim(key, start, end);
    }

    /**
     * 获取列表长度
     *
     * @param key key
     */
    public long lLen(String key) {
        if (hasKey(key)) {
            Long size = listOperations().size(key);
            return size == null ? 0 : (long) size;
        }
        return 0;
    }

    /* --------------------set相关操作-------------------------- */

    /**
     * set添加元素
     *
     * @param key    key
     * @param values values
     */
    public Long sAdd(String key, Object... values) {
        return setOperations().add(key, values);
    }

    /**
     * set移除元素
     *
     * @param key    key
     * @param values values
     */
    public Long sRemove(String key, Object... values) {
        return setOperations().remove(key, values);
    }

    /**
     * 移除并返回集合的一个随机元素
     *
     * @param key key
     */
    public String sPop(String key) {
        return (String) setOperations().pop(key);
    }

    /**
     * 将元素value从一个集合移到另一个集合
     *
     * @param key     key
     * @param value   value
     * @param destKey 新的key
     */
    public Boolean sMove(String key, String value, String destKey) {
        return setOperations().move(key, value, destKey);
    }

    /**
     * 获取集合的大小
     *
     * @param key key
     */
    public Long sSize(String key) {
        return setOperations().size(key);
    }

    /**
     * 判断集合是否包含value
     *
     * @param key   key
     * @param value value
     */
    public Boolean sIsMember(String key, Object value) {
        return setOperations().isMember(key, value);
    }

    /**
     * 获取两个集合的交集
     *
     * @param key      key
     * @param otherKey 其他的key
     */
    public Set<Object> sIntersect(String key, String otherKey) {
        return setOperations().intersect(key, otherKey);
    }

    /**
     * 获取key集合与多个集合的交集
     *
     * @param key       key
     * @param otherKeys 其他的keys
     */
    public Set<Object> sIntersect(String key, Collection<String> otherKeys) {
        return setOperations().intersect(key, otherKeys);
    }

    /**
     * key集合与otherKey集合的交集存储到destKey集合中
     *
     * @param key      key
     * @param otherKey 其他的key
     * @param destKey  新的key
     */
    public Long sIntersectAndStore(String key, String otherKey, String destKey) {
        return setOperations().intersectAndStore(key, otherKey, destKey);
    }

    /**
     * key集合与多个集合的交集存储到destKey集合中
     *
     * @param key       key
     * @param otherKeys 其他的keys
     * @param destKey   新的key
     */
    public Long sIntersectAndStore(String key, Collection<String> otherKeys, String destKey) {
        return setOperations().intersectAndStore(key, otherKeys, destKey);
    }

    /**
     * 获取两个集合的并集
     *
     * @param key       key
     * @param otherKeys 其他的keys
     */
    public Set<Object> sUnion(String key, String otherKeys) {
        return setOperations().union(key, otherKeys);
    }

    /**
     * 获取key集合与多个集合的并集
     *
     * @param key       key
     * @param otherKeys 其他的keys
     */
    public Set<Object> sUnion(String key, Collection<String> otherKeys) {
        return setOperations().union(key, otherKeys);
    }

    /**
     * key集合与otherKey集合的并集存储到destKey中
     *
     * @param key      key
     * @param otherKey 其他的key
     * @param destKey  新的key
     */
    public Long sUnionAndStore(String key, String otherKey, String destKey) {
        return setOperations().unionAndStore(key, otherKey, destKey);
    }

    /**
     * key集合与多个集合的并集存储到destKey中
     *
     * @param key       key
     * @param otherKeys 其他的keys
     * @param destKey   新的key
     */
    public Long sUnionAndStore(String key, Collection<String> otherKeys, String destKey) {
        return setOperations().unionAndStore(key, otherKeys, destKey);
    }

    /**
     * 获取两个集合的差集
     *
     * @param key      key
     * @param otherKey 其他的key
     */
    public Set<Object> sDifference(String key, String otherKey) {
        return setOperations().difference(key, otherKey);
    }

    /**
     * 获取key集合与多个集合的差集
     *
     * @param key       key
     * @param otherKeys 其他的keys
     */
    public Set<Object> sDifference(String key, Collection<String> otherKeys) {
        return setOperations().difference(key, otherKeys);
    }

    /**
     * key集合与otherKey集合的差集存储到destKey中
     *
     * @param key      key
     * @param otherKey 其他的key
     * @param destKey  新的key
     */
    public Long sDifference(String key, String otherKey, String destKey) {
        return setOperations().differenceAndStore(key, otherKey,
                destKey);
    }

    /**
     * key集合与多个集合的差集存储到destKey中
     *
     * @param key       key
     * @param otherKeys 其他的keys
     * @param destKey   新的key
     */
    public Long sDifference(String key, Collection<String> otherKeys, String destKey) {
        return setOperations().differenceAndStore(key, otherKeys, destKey);
    }

    /**
     * 获取集合所有元素
     *
     * @param key key
     */
    public Set<Object> setMembers(String key) {
        return setOperations().members(key);
    }

    /**
     * 随机获取集合中的一个元素
     *
     * @param key key
     */
    public String sRandomMember(String key) {
        return (String) setOperations().randomMember(key);
    }

    /**
     * 随机获取集合中count个元素
     *
     * @param key   key
     * @param count count
     */
    public List<Object> sRandomMembers(String key, long count) {
        return setOperations().randomMembers(key, count);
    }

    /**
     * 随机获取集合中count个元素并且去除重复的
     *
     * @param key   key
     * @param count count
     */
    public Set<Object> sDistinctRandomMembers(String key, long count) {
        return setOperations().distinctRandomMembers(key, count);
    }

    /**
     * @param key     key
     * @param options options
     */
    public Cursor<Object> sScan(String key, ScanOptions options) {
        return setOperations().scan(key, options);
    }

    /*------------------zSet相关操作--------------------------------*/

    /**
     * 添加元素,有序集合是按照元素的score值由小到大排列
     *
     * @param key   key
     * @param value value
     * @param score score
     */
    public Boolean zAdd(String key, String value, double score) {
        return zestOperations().add(key, value, score);
    }

    /**
     * @param key    key
     * @param values values
     */
    public Long zAdd(String key, Set<ZSetOperations.TypedTuple<Object>> values) {
        return zestOperations().add(key, values);
    }

    /**
     * @param key    key
     * @param values values values
     */
    public Long zRemove(String key, Object... values) {
        return zestOperations().remove(key, values);
    }

    /**
     * 增加元素的score值，并返回增加后的值
     *
     * @param key   key
     * @param value value
     * @param delta delta
     */
    public Double zIncrementScore(String key, String value, double delta) {
        return zestOperations().incrementScore(key, value, delta);
    }

    /**
     * 返回元素在集合的排名,有序集合是按照元素的score值由小到大排列
     *
     * @param key   key
     * @param value value
     * @return 0表示第一位
     */
    public Long zRank(String key, Object value) {
        return zestOperations().rank(key, value);
    }

    /**
     * 返回元素在集合的排名,按元素的score值由大到小排列
     *
     * @param key   key
     * @param value value
     */
    public Long zReverseRank(String key, Object value) {
        return zestOperations().reverseRank(key, value);
    }

    /**
     * 获取集合的元素, 从小到大排序
     *
     * @param key   key
     * @param start 开始位置
     * @param end   结束位置, -1查询所有
     */
    public Set<Object> zRange(String key, long start, long end) {
        return zestOperations().range(key, start, end);
    }

    /**
     * 获取集合元素, 并且把score值也获取
     *
     * @param key   key
     * @param start 开始索引
     * @param end   结束索引
     */
    public Set<ZSetOperations.TypedTuple<Object>> zRangeWithScores(String key, long start, long end) {
        return zestOperations().rangeWithScores(key, start, end);
    }

    /**
     * 根据Score值查询集合元素
     *
     * @param key key
     * @param min 最小值
     * @param max 最大值
     */
    public Set<Object> zRangeByScore(String key, double min, double max) {
        return zestOperations().rangeByScore(key, min, max);
    }

    /**
     * 根据Score值查询集合元素, 从小到大排序
     *
     * @param key key
     * @param min 最小值
     * @param max 最大值
     */
    public Set<ZSetOperations.TypedTuple<Object>> zRangeByScoreWithScores(String key, double min, double max) {
        return zestOperations().rangeByScoreWithScores(key, min, max);
    }

    /**
     * @param key   key
     * @param min   最小值
     * @param max   最大值
     * @param start 开始索引
     * @param end   结束索引
     */
    public Set<ZSetOperations.TypedTuple<Object>> zRangeByScoreWithScores(String key, double min, double max,
                                                                          long start, long end) {
        return zestOperations().rangeByScoreWithScores(key, min, max, start, end);
    }

    /**
     * 获取集合的元素, 从大到小排序
     *
     * @param key   key
     * @param start 开始索引
     * @param end   结束索引
     */
    public Set<Object> zReverseRange(String key, long start, long end) {
        return zestOperations().reverseRange(key, start, end);
    }

    /**
     * 获取集合的元素, 从大到小排序, 并返回score值
     *
     * @param key   key
     * @param start 开始索引
     * @param end   结束索引
     */
    public Set<ZSetOperations.TypedTuple<Object>> zReverseRangeWithScores(String key, long start, long end) {
        return zestOperations().reverseRangeWithScores(key, start, end);
    }

    /**
     * 根据Score值查询集合元素, 从大到小排序
     *
     * @param key key
     * @param min 最小值
     * @param max 最大值
     */
    public Set<Object> zReverseRangeByScore(String key, double min, double max) {
        return zestOperations().reverseRangeByScore(key, min, max);
    }

    /**
     * 根据Score值查询集合元素, 从大到小排序
     *
     * @param key key
     * @param min 最小值
     * @param max 最大值
     */
    public Set<ZSetOperations.TypedTuple<Object>> zReverseRangeByScoreWithScores(String key, double min, double max) {
        return zestOperations().reverseRangeByScoreWithScores(key, min, max);
    }

    /**
     * @param key   key
     * @param min   最小值
     * @param max   最大值
     * @param start 开始索引
     * @param end   结束索引
     */
    public Set<Object> zReverseRangeByScore(String key, double min, double max, long start, long end) {
        return zestOperations().reverseRangeByScore(key, min, max, start, end);
    }

    /**
     * 根据score值获取集合元素数量
     *
     * @param key key
     * @param min 最小值
     * @param max 最大值
     */
    public Long zCount(String key, double min, double max) {
        return zestOperations().count(key, min, max);
    }

    /**
     * 获取集合大小
     *
     * @param key key
     */
    public Long zSize(String key) {
        return zestOperations().size(key);
    }

    /**
     * 获取集合大小
     *
     * @param key key
     */
    public Long zZCard(String key) {
        return zestOperations().zCard(key);
    }

    /**
     * 获取集合中value元素的score值
     *
     * @param key   key
     * @param value value
     */
    public Double zScore(String key, Object value) {
        return zestOperations().score(key, value);
    }

    /**
     * 移除指定索引位置的成员
     *
     * @param key   key
     * @param start 开始索引
     * @param end   结束索引
     */
    public Long zRemoveRange(String key, long start, long end) {
        return zestOperations().removeRange(key, start, end);
    }

    /**
     * 根据指定的score值的范围来移除成员
     *
     * @param key key
     * @param min 最小值
     * @param max 最大值
     */
    public Long zRemoveRangeByScore(String key, double min, double max) {
        return zestOperations().removeRangeByScore(key, min, max);
    }

    /**
     * 获取key和otherKey的并集并存储在destKey中
     *
     * @param key      key
     * @param otherKey 其他的key
     * @param destKey  新的key
     */
    public Long zUnionAndStore(String key, String otherKey, String destKey) {
        return zestOperations().unionAndStore(key, otherKey, destKey);
    }

    /**
     * @param key       key
     * @param otherKeys 其他的keys
     * @param destKey   新的key
     */
    public Long zUnionAndStore(String key, Collection<String> otherKeys, String destKey) {
        return redisTemplate.opsForZSet().unionAndStore(key, otherKeys, destKey);
    }

    /**
     * 交集
     *
     * @param key      key
     * @param otherKey 其他的key
     * @param destKey  新的key
     */
    public Long zIntersectAndStore(String key, String otherKey, String destKey) {
        return zestOperations().intersectAndStore(key, otherKey, destKey);
    }

    /**
     * 交集
     *
     * @param key       key
     * @param otherKeys 其他的keys
     * @param destKey   新的key
     */
    public Long zIntersectAndStore(String key, Collection<String> otherKeys, String destKey) {
        return zestOperations().intersectAndStore(key, otherKeys,
                destKey);
    }
}
