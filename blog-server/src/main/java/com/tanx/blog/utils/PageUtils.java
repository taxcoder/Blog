package com.tanx.blog.utils;

import lombok.Data;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Data
public class PageUtils<T> implements Serializable {
    /**
     * 查询数据列表
     */
    protected List<T> records = Collections.emptyList();
    /**
     * 总数
     */
    protected long total = 0;
    /**
     * 每页显示条数，默认 10
     */
    protected long size = 10;
    /**
     * 当前页
     */
    protected long current = 1;

    public PageUtils() {
    }

    public PageUtils(long current, long size) {
        this.current = current;
        this.size = size;
    }

    public PageUtils(List<T> records, long total, long size, long current) {
        this.records = records;
        this.total = total;
        this.size = size;
        this.current = current;
    }
}
