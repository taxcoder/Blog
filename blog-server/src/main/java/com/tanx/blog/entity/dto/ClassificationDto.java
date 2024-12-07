package com.tanx.blog.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class ClassificationDto implements Serializable {

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 分类ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 分类名称
     */
    private String name;

    private Integer count;

    public ClassificationDto() {
    }

    public ClassificationDto(com.tanx.blog.entity.po.Classification classification) {
        this.id = classification.getId();
        this.name = classification.getName();
    }

    public ClassificationDto(ClassificationArticleDto dto) {
        this.id = dto.getId();
        this.name = dto.getName();
        this.count = dto.getArticles().size();
    }

    public ClassificationDto(Integer id, String name, Integer count) {
        this.id = id;
        this.name = name;
        this.count = count;
    }
}
