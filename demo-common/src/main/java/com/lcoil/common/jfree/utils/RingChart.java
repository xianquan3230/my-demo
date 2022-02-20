package com.lcoil.common.jfree.utils;

import com.lcoil.common.jfree.vo.BarChartVO;
import lombok.extern.slf4j.Slf4j;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.DefaultDrawingSupplier;
import org.jfree.chart.plot.RingPlot;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.data.general.DefaultPieDataset;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
/**
 * @Classname RingChart
 * @Description TODO
 * @Date 2022/2/18 8:48 PM
 * @Created by l-coil
 */
@Slf4j
public class RingChart {
    /**
     * 生成环状图
     *
     * @param barChartVOList
     * @param tempImgPath
     * @param width
     * @param height
     * @return
     */
    public static String generateRingChart(List<BarChartVO> barChartVOList, String tempImgPath, Integer width, Integer height) {
        DefaultPieDataset dataset = getDataSet2(barChartVOList);
        setTheme();
        JFreeChart chart = ChartFactory.createRingChart(
                "",
                dataset,
                true,
                false,
                false
        );
        RingPlot plot = (RingPlot) chart.getPlot();//获得图形面板
        plot.setIgnoreNullValues(true);  //忽略null值
        plot.setIgnoreZeroValues(false); //不忽略0值
        plot.setBackgroundPaint(Color.WHITE);//设置画布背景颜色
        plot.setOutlineVisible(false);//设置绘图区边框是否可见
        plot.setLegendLabelGenerator(new StandardPieSectionLabelGenerator("{0}      {1} 个", NumberFormat.getNumberInstance(),
                new DecimalFormat("0.00%"))); //设置图例数据格式
        plot.setBackgroundPaint(new Color(253, 253, 253));
        //设置标签样式
        plot.setLabelFont(new Font("宋体", Font.BOLD, 15));
        plot.setSimpleLabels(false);
        plot.setLabelLinkPaint(Color.WHITE);
        plot.setLabelOutlinePaint(Color.WHITE);
        plot.setLabelLinksVisible(false);
        plot.setLabelShadowPaint(null);
        plot.setLabelOutlinePaint(new Color(0, true));
        plot.setLabelBackgroundPaint(new Color(0, true));
        plot.setLabelPaint(Color.WHITE);
        plot.setSeparatorPaint(Color.WHITE);
        plot.setShadowPaint(new Color(253, 253, 253));
        plot.setSectionDepth(0.35);
        plot.setStartAngle(90);
        plot.setDrawingSupplier(new DefaultDrawingSupplier(
                new Paint[]{
                        new Color(237, 125, 49),//超危颜色
                        new Color(255, 192, 0),//高危
                        new Color(112, 173, 71),//中危
                        new Color(158, 72, 14),//低危
                        new Color(153, 115, 0),//未知
                        new Color(67, 104, 43)//无
                },
                DefaultDrawingSupplier.DEFAULT_OUTLINE_PAINT_SEQUENCE,
                DefaultDrawingSupplier.DEFAULT_STROKE_SEQUENCE,
                DefaultDrawingSupplier.DEFAULT_OUTLINE_STROKE_SEQUENCE,
                DefaultDrawingSupplier.DEFAULT_SHAPE_SEQUENCE));
        plot.setSectionDepth(0.35);
        //图例样式的设定,图例含有M 和P 方法
        LegendTitle legendTitle = chart.getLegend();//获得图例标题
        legendTitle.setPosition(RectangleEdge.LEFT);//图例右边显示
        legendTitle.setBorder(0, 0, 0, 0);//设置图例上下左右线
        legendTitle.setPadding(0, 0, 0, 50);
        //标题的距离的设定
        TextTitle title = chart.getTitle();    //设置标题居左的距离
        title.setMargin(0, -20, 0, 0);//标题距离上下左右的距离

        FileOutputStream fos_jpg = null;
        try {
            try {
                File outFile = new File(tempImgPath);
                if (!outFile.getParentFile().exists()) {
                    outFile.getParentFile().mkdirs();
                }
                fos_jpg = new FileOutputStream(tempImgPath);
                ChartUtils.writeChartAsJPEG(fos_jpg, 1.0f, chart, width, height, null);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        } finally {
            try {
                fos_jpg.close();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        String result = JfreeUtil.getImageBase(tempImgPath);
        return result;
    }

    /**
     * <p>样式设定</p>
     */
    private static void setTheme() {
        //创建主题样式
        StandardChartTheme standardChartTheme = new StandardChartTheme("CN");
        //设置标题
        standardChartTheme.setExtraLargeFont(new Font("隶书", Font.BOLD, 20));
        //设置图例的字体
        standardChartTheme.setRegularFont(new Font("宋书", Font.PLAIN, 15));
        //设置轴向的字体
        standardChartTheme.setLargeFont(new Font("宋书", Font.PLAIN, 15));
        ChartFactory.setChartTheme(standardChartTheme);
    }

    /*
     * 基本数据集
     */
    private static DefaultPieDataset getDataSet2(List<BarChartVO> barChartVOList) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        for (BarChartVO bc : barChartVOList) {
            dataset.setValue(bc.getSeries(), bc.getValue());
        }
        return dataset;
    }
}
