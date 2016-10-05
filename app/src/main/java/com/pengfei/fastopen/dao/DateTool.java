package com.pengfei.fastopen.dao;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by mengfei on 2016/8/14.
 */
public class DateTool {

    private static final String TOADY = DateFormat.getDateInstance().format(new Date());//获取今天的时间

    public static String getDateStr(Date date) {
        String lastUseDate = DateFormat.getDateInstance().format(date);
        String lastUseTime = DateFormat.getTimeInstance().format(date);
        if (lastUseDate.equals(TOADY)) {
            return lastUseTime;
        } else {
            return lastUseDate;
        }
    }

}
