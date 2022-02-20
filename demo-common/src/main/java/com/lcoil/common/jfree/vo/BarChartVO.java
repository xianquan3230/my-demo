package com.lcoil.common.jfree.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @Classname BarChartVO
 * @Description TODO
 * @Date 2022/2/18 8:49 PM
 * @Created by l-coil
 */
@Data
@Builder
public class BarChartVO {
    private String series;
    private double value;
    private String keyRow;
}
