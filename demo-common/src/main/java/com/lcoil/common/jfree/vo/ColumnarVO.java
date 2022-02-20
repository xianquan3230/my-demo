package com.lcoil.common.jfree.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.awt.*;

/**
 * @Classname ColumnarVO
 * @Description TODO
 * @Date 2022/2/18 8:48 PM
 * @Created by l-coil
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ColumnarVO {

    /**
     * y轴坐标
     */
    private Number value;
    /**
     * y轴名称
     */
    private String key;
    /**
     * x轴坐标
     */
    private String keyName;
    /**
     * 柱子颜色
     */
    private Color color;
}
