package com.lcoil.common.jfree.utils;
import com.lcoil.common.jfree.vo.BarChartVO;
import com.lcoil.common.jfree.vo.ColumnarVO;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
/**
 * @Classname BarSpChart
 * @Description TODO
 * @Date 2022/2/18 8:48 PM
 * @Created by l-coil
 */
public class BarSpChart {

    /**
     * @param columnarVos 数据
     * @param imgWidth    图片宽度
     * @param imageHeight 图片高度
     * @param path        生成图片位置
     * @return
     */
    public static String createColumnar(List<ColumnarVO> columnarVos, int imgWidth, int imageHeight, String path) {//柱形间的间距
        int marginLeft = 10;
        //最上边刻度距离图片的最顶端的距离
        int top = 20;
        BufferedImage image = new BufferedImage(imgWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setStroke(new BasicStroke(1f));
        graphics.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        graphics.setColor(new Color(229, 239, 245));
        //设置字体划线平滑
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_DEFAULT);
        //计算y轴最大值以及x轴刻度的最大长度
        Number max = 0;
        int maxWidth = 0;
        for (ColumnarVO c : columnarVos) {
            if (max.intValue() < c.getValue().intValue()) {
                max = c.getValue().intValue();
            }
            if (maxWidth < graphics.getFontMetrics().stringWidth(c.getValue().toString())) {
                maxWidth = graphics.getFontMetrics().stringWidth(c.getValue().toString());
            }
        }
        //每个刻度的最小像素数
        float singleMinHeight = 50;
        //图片宽度减去x轴距离底部的距离
        int realHeight = imageHeight - 30;
        //y轴距离左侧的距离(除去y轴刻度的宽度)
        int left = 20;
        //平分x轴,y轴的值
        double singleWidth = (imageHeight - maxWidth - left - marginLeft - columnarVos.size() * marginLeft) / (double) columnarVos.size();
        float singleHeight = (float) Math.floor((realHeight - top) / (max.doubleValue() + 1));
        //刻度值默认是数组value最大值
        int markNumber = max.intValue();
        //刻度值默认是1
        int markValue = 1;
        //如果y轴1个刻度的距离小于最小刻度值重新计算刻度以及刻度的最新值
        if (singleMinHeight > singleHeight) {
            singleHeight = singleMinHeight;
            markNumber = (int) Math.floor((realHeight - top) / singleHeight);
            markValue = (int) Math.ceil(max.intValue() / ((double) markNumber - 1));
        } else {
            markNumber = markNumber + 1;
        }
        graphics.fillRect(0, 0, imgWidth, imageHeight);
        graphics.setColor(Color.black);
        int x = 0;
        //x轴数据以及填充的柱状
        for (ColumnarVO c : columnarVos) {
            graphics.setColor(c.getColor());
            float y = singleHeight * (c.getValue().floatValue() / markValue);
            Rectangle2D rectangle2D = new Rectangle2D.Float(60, maxWidth + left + (float) singleWidth * x + (x + 1) * marginLeft, y, (float) 20.0);
            graphics.fill(rectangle2D);
            graphics.setColor(Color.black);
            int index = strWidthIndex(c.getKeyName(), (int) singleWidth, 0, graphics.getFontMetrics());
            graphics.drawString(index < c.getKeyName().length() ? c.getKeyName().substring(0, index) + "..." : c.getKeyName(), 10, maxWidth + left + (int) singleWidth * x + (x + 1) * marginLeft + 16);
            graphics.drawString(c.getValue() + "", imageHeight - 10, maxWidth + left + (int) singleWidth * x + (x + 1) * marginLeft + 14);
            x++;
        }
        try {
            File outFile = new File(path);
            if (!outFile.getParentFile().exists()) {
                outFile.getParentFile().mkdirs();
            }
            ImageIO.write(image, "jpg", new File(path));
            String result = JfreeUtil.getImageBase(path);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 指定字体宽度的计算
     *
     * @param cont
     * @param width
     * @param i
     * @param metrics
     * @return
     */
    private static int strWidthIndex(String cont, int width, int i, FontMetrics metrics) {
        if (width > metrics.stringWidth(cont.substring(0, cont.length() - i))) {
            return cont.length() - i;
        } else {
            return strWidthIndex(cont, width, i + 1, metrics);
        }
    }


    /**
     * 生成水平柱状图柱状图
     *
     * @param barChartVOList
     * @param tempFilePath
     * @param width
     * @param height
     * @return
     */
    public static String spBarChart(List<BarChartVO> barChartVOList, String tempFilePath, Integer width, Integer height) {
        JFreeChart chart = ChartFactory.createBarChart("",
                "", "", createDataset2(barChartVOList), PlotOrientation.HORIZONTAL, //显示方向
                false, //是否显示图例如图一的均值、差值
                false, // 是否生成工具
                false); // 是否生成URL链接
        iSetBarSpChart(chart);
        try {
            File outFile = new File(tempFilePath);
            if (!outFile.getParentFile().exists()) {
                outFile.getParentFile().mkdirs();
            }
            ChartUtils.saveChartAsJPEG(new File(tempFilePath), chart, width, height);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String result = JfreeUtil.getImageBase(tempFilePath);
        return result;
    }

    /**
     * 数据集合
     *
     * @return
     */
    public static CategoryDataset createDataset2(List<BarChartVO> barChartVOList) {
        DefaultCategoryDataset result = new DefaultCategoryDataset();
        for (BarChartVO bc : barChartVOList) {
            result.addValue(bc.getValue(), bc.getKeyRow(), bc.getSeries());
        }
        return result;
    }

    /**
     * 水平柱状图的样式
     *
     * @param chart
     */
    public static void iSetBarSpChart(JFreeChart chart) {
        CategoryPlot categoryplot = chart.getCategoryPlot();// 图本身
        ValueAxis rangeAxis = categoryplot.getRangeAxis();
        CategoryAxis domainAxis = categoryplot.getDomainAxis();
        // 设置Y轴的提示文字样式
        rangeAxis.setLabelFont(new Font("微软雅黑", Font.PLAIN, 8));
        // 设置Y轴刻度线的长度
        rangeAxis.setTickMarkInsideLength(10f);
        // 设置X轴下的标签文字
        domainAxis.setLabelFont(new Font("微软雅黑", Font.PLAIN, 10));
        // 设置X轴上提示文字样式
        domainAxis.setTickLabelFont(new Font("微软雅黑", Font.PLAIN, 10));
        domainAxis.setAxisLineVisible(false);
        domainAxis.setTickMarksVisible(false);

        NumberAxis numAxis = (NumberAxis) categoryplot.getRangeAxis();
        numAxis.setVisible(false);
        // 自定义柱状图中柱子的样式
        BarRenderer brender = (BarRenderer) categoryplot.getRenderer();
        Paint[] paints = {
                new Color(237, 125, 49),//超危颜色
                new Color(255, 192, 0),//高危
                new Color(112, 173, 71),//中危
                new Color(158, 72, 14),//低危
                new Color(153, 115, 0)//未知
        };
        brender.setSeriesPaint(0, paints[0]);
        brender.setSeriesPaint(1, paints[1]);
        brender.setSeriesPaint(2, paints[2]);
        brender.setSeriesPaint(3, paints[3]);
        brender.setSeriesPaint(4, paints[4]);
        // 设置柱状图的顶端显示数字
        brender.setIncludeBaseInRange(true);
        brender.setDefaultItemLabelsVisible(true, true);
        brender.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        // 设置柱子为平面图不是立体的
        brender.setBarPainter(new StandardBarPainter());
        brender.setMaximumBarWidth(0.05);
        brender.setMinimumBarLength(0.05);
        // 设置柱状图之间的距离0.1代表10%；
        brender.setItemMargin(0.02);
        // 设置柱子的阴影，false代表没有阴影
        brender.setShadowVisible(false);

        categoryplot.setRenderer(brender);
        // 设置图的背景为白色
        categoryplot.setBackgroundPaint(Color.WHITE);
        // 去掉柱状图的背景边框，使边框不可见
        categoryplot.setOutlineVisible(false);
        // 设置标题的字体样式
        chart.getTitle().setFont(new Font("微软雅黑", Font.PLAIN, 10));
    }
}
