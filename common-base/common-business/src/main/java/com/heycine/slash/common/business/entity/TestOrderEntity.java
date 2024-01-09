package com.heycine.slash.common.business.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("test_order")
public class TestOrderEntity {

    /**
     * 订单ID
     */
    @TableId
    private Long orderId;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 价格
     */
    private BigDecimal price;
}
