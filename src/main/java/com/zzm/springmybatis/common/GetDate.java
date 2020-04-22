package com.zzm.springmybatis.common;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @author zzm
 * @data 2020/3/10 18:26
 */
public class GetDate {

    static String getDate() {
        Date date = new Date();   //这里的时util包下的
        SimpleDateFormat temp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  //这是24时
        String strDate = temp.format(date);
        return strDate;
    }
}
