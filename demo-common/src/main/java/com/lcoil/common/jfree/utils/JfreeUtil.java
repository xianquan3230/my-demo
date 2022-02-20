package com.lcoil.common.jfree.utils;

import lombok.extern.slf4j.Slf4j;
import org.drools.core.io.impl.ClassPathResource;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.kie.api.io.Resource;
import sun.misc.BASE64Encoder;

import java.awt.*;
import java.io.*;
import java.util.Map;
/**
 * @Classname JfreeUtil
 * @Description TODO
 * @Date 2022/2/18 8:48 PM
 * @Created by l-coil
 */
@Slf4j
public class JfreeUtil {

    /**
     * 将图片转化为字节数组
     *
     * @return 字节数组
     */
    private static byte[] imgToByte(String tempImgPath) {
        File file = new File(tempImgPath);
        byte[] buffer = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        //删除临时文件
        file.delete();
        return buffer;
    }

    public static String pieChart2(String title, Map<String, Integer> datas, int width, int height, String tempImgPath) {

        //创建主题样式
        StandardChartTheme standardChartTheme = new StandardChartTheme("CN");
        //设置标题字体
        standardChartTheme.setExtraLargeFont(new Font("宋体", Font.BOLD, 20));
        //设置图例的字体
        standardChartTheme.setRegularFont(new Font("宋体", Font.PLAIN, 15));
        //设置轴向的字体
        standardChartTheme.setLargeFont(new Font("宋体", Font.PLAIN, 15));
        //设置主题样式
        ChartFactory.setChartTheme(standardChartTheme);

        //根据jfree生成一个本地饼状图
        DefaultPieDataset pds = new DefaultPieDataset();
        datas.forEach(pds::setValue);
        //图标标题、数据集合、是否显示图例标识、是否显示tooltips、是否支持超链接
        JFreeChart chart = ChartFactory.createPieChart(title, pds, true, false, false);
        //设置抗锯齿
        chart.setTextAntiAlias(false);
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setNoDataMessage("暂无数据");
        //忽略无值的分类
        plot.setIgnoreNullValues(true);
        plot.setBackgroundAlpha(0f);
        //设置标签阴影颜色
        plot.setShadowPaint(new Color(255, 255, 255));
        //设置标签生成器(默认{0})
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}({1})/{2}"));
        try {
            File outFile = new File(tempImgPath);
            if (!outFile.getParentFile().exists()) {
                outFile.getParentFile().mkdirs();
            }
            ChartUtils.saveChartAsJPEG(new File(tempImgPath), chart, width, height);
        } catch (IOException e1) {
            log.error("生成饼状图失败！");
        }
        String result = getImageBase(tempImgPath);
        return result;
    }

    //获得图片的base64码
    public static String getImageBase(String src) {
        File file = new File(src);
        if (!file.exists()) {
            return "";
        }
        InputStream in = null;
        byte[] data = null;
        try {
            in = new FileInputStream(file);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        try {
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BASE64Encoder encoder = new BASE64Encoder();
        //删除临时文件
        file.delete();
        return encoder.encode(data);
    }

    public static String getLogoBase64() {
        Resource resource = new ClassPathResource("template/logo.png");
        InputStream in = null;
        byte[] data = null;
        try {
            in = resource.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);
    }
}