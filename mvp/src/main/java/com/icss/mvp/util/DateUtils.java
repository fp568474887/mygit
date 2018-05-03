package com.icss.mvp.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

public class DateUtils
{
	private final static Logger LOG = Logger.getLogger(DateUtils.class);
    public static final SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
    public static final SimpleDateFormat format3 = new SimpleDateFormat("yyyy-MM-dd");
    
    public static final SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
    
    public static final SimpleDateFormat format2 = new SimpleDateFormat("yyyyMM");
    
    private DateUtils()
    {
    }
    
    /**
     * 获得系统当前日期，并以YYYYMM格式返回
     * 
     * @return yyyyMM
     */
    public static String getSystemMonth()
    {
        Calendar c = Calendar.getInstance();
        String year = c.get(Calendar.YEAR) + "";
        String month = c.get(Calendar.MONTH) + 1 + "";
        if (month.length() == 1)
        {
            month = "0" + month;
        }
        String reckonMonth = year + month;
        return reckonMonth;
    }
    
    /**
     * 获得系统当前月前后n月，并以YYYYMM格式返回
     * @param n 1=下月 -1=上月
     * 
     * @return yyyyMM
     */
    public static String getSystemFewMonth(int n)
    {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, n);
        String year = c.get(Calendar.YEAR) + "";
        String month = c.get(Calendar.MONTH) + 1 + "";
        if (month.length() == 1)
        {
            month = "0" + month;
        }
        String reckonMonth = year + month;
        return reckonMonth;
    }
    
    /**
     * 获得系统当前日期，并以YYYYMM格式返回
     * 
     * @return yyyyMM
     */
    public static String getMonth()
    {
        Calendar c = Calendar.getInstance();
        String year = c.get(Calendar.YEAR) + "";
        String month = c.get(Calendar.MONTH) + 1 + "";
        if (month.length() == 1)
        {
            month = "0" + month;
        }
        String reckonMonth = year + "-" + month;
        return reckonMonth;
    }

    /**
     * 获得系统当前日期，并以YYYYMMDD格式返回
     * 
     * @return yyyyMMdd
     */
	public static String getToday() {
		Calendar c = Calendar.getInstance();
        String year = c.get(Calendar.YEAR) + "";
        String month = c.get(Calendar.MONTH) + 1 + "";
        String day = c.get(Calendar.DAY_OF_MONTH) + "";
        if (month.length() == 1)
        {
            month = "0" + month;
        }
        if (day.length() == 1)
        {
        	day = "0" + day;
        }
        String reckon = year + month + day;
        return reckon;
	}
	
	 /**
     * 获得系统当前月前后n天，并以YYYYMM格式返回
     * @param n 1=明天 -1=昨天
     * 
     * @return yyyyMMdd
     */
    public static String getSystemFewDay(String str,int n)
    {	
	    Date date;
		try {
			date = format.parse(str);
			Calendar calendar = Calendar.getInstance();
		    calendar.setTime(date);
		    calendar.add(Calendar.DAY_OF_MONTH, n);
	        String dateStr = format.format(calendar.getTime());
	        return dateStr;
		} catch (ParseException e) {
			LOG.error("日期格式错误");
			return null;
		}
	    
    }
    /**
     * 比较两个日期的大小，类型为yyyyMMdd  end》beg true
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * 
     * @return boolean
     */
    public static boolean comparisonDateSize(String beginTime,String endTime) {
		Date bt; 
		Date et;
		try {
			bt = format.parse(beginTime);
			et = format.parse(endTime);
			if (bt.before(et)){ 
				return true; 
			}else{ 
				return false; 
			} 
		} catch (Exception e) {
			LOG.error("日期格式错误");
			return false;
		} 
    }
    /**
     * 比较两个日期的大小，类型为yyyyMMdd  end》beg true
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * 
     * @return boolean
     */
    public static Integer calculationTimeDifference(String beginTime,String endTime) {
		Date bt; 
		Date et;
		try {
			bt = format.parse(beginTime);
			et = format.parse(endTime);
			Long time = bt.getTime() - et.getTime();
			time = time/(1000*24*60*60);
			return time.intValue();
		} catch (Exception e) {
			LOG.error("日期格式错误");
			return null;
		} 
    }
    public static void main(String[] args) {
		System.out.println(getSystemFewDay("20180413",1));
		System.out.println(comparisonDateSize("20180413","20180413"));
	}
}
