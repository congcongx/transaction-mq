package com.xie.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author xiecong
 * @date 2019/9/27
 */
@Data
public class SendMsg {

    @TableId(type = IdType.INPUT)
    private String messageId;
}
