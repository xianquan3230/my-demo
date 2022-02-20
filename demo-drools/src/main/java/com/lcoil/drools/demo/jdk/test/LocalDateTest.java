package com.lcoil.drools.demo.jdk.test;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;

/**
 * @Classname LocalDateTest
 * @Description TODO
 * @Date 2022/2/17 11:24 PM
 * @Created by l-coil
 */
public class LocalDateTest {

    public static void main(String[] args) {
        LocalDate date = LocalDate.of(2021, 1, 26);
        LocalDate.parse("2021-01-26");

        LocalDateTime dateTime = LocalDateTime.of(2021, 1, 26, 12, 12, 22);
        LocalDateTime.parse("2021-01-26 12:12:22");

        LocalTime time = LocalTime.of(12, 12, 22);
        LocalTime.parse("12:12:22");
    }

    public void pushWeek(){
        //一周后的日期
        LocalDate localDate = LocalDate.now();
        //方法1
        LocalDate after = localDate.plus(1, ChronoUnit.WEEKS);
        //方法2
        LocalDate after2 = localDate.plusWeeks(1);
        System.out.println("一周后日期：" + after);

        //算两个日期间隔多少天，计算间隔多少年，多少月
        LocalDate date1 = LocalDate.parse("2021-02-26");
        LocalDate date2 = LocalDate.parse("2021-12-23");
        Period period = Period.between(date1, date2);
        System.out.println("date1 到 date2 相隔："
                + period.getYears() + "年"
                + period.getMonths() + "月"
                + period.getDays() + "天");
        //打印结果是 “date1 到 date2 相隔：0年9月27天”
        //这里period.getDays()得到的天是抛去年月以外的天数，并不是总天数
        //如果要获取纯粹的总天数应该用下面的方法
        long day = date2.toEpochDay() - date1.toEpochDay();
        System.out.println(date2 + "和" + date2 + "相差" + day + "天");
        //打印结果：2021-12-23和2021-12-23相差300天
    }

    public void getDayNew() {
        LocalDate today = LocalDate.now();
        //获取当前月第一天：
        LocalDate firstDayOfThisMonth = today.with(TemporalAdjusters.firstDayOfMonth());
        // 取本月最后一天
        LocalDate lastDayOfThisMonth = today.with(TemporalAdjusters.lastDayOfMonth());
        //取下一天：
        LocalDate nextDay = lastDayOfThisMonth.plusDays(1);
        //当年最后一天
        LocalDate lastday = today.with(TemporalAdjusters.lastDayOfYear());
        //2021年最后一个周日，如果用Calendar是不得烦死。
        LocalDate lastMondayOf2021 = LocalDate.parse("2021-12-31").with(TemporalAdjusters.lastInMonth(DayOfWeek.SUNDAY));
    }


}
