package com.kyle.mycar.temp;

import java.util.List;

/**
 * Created by Zhang on 2017/6/24.
 */

public class CarInfo {
    /**
     * msg : success
     * result : [{"name":"AC Schnitzer","son":[{"car":"AC Schnitzer","type":"AC Schnitzer X5"}]}]}]
     * retCode : 200
     */

    public String msg;
    public String retCode;
    public List<ResultBean> result;

    public static class ResultBean {
        /**
         * name : AC Schnitzer
         * son : [{"car":"AC Schnitzer","type":"AC Schnitzer X5"}]
         */

        public String name;
        public List<SonBean> son;

        public static class SonBean {
            /**
             * car : AC Schnitzer
             * type : AC Schnitzer X5
             */

            public String car;
            public String type;
        }
    }
}
