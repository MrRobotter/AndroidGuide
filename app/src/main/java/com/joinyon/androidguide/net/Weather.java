package com.joinyon.androidguide.net;

/**
 * 作者： JoinYon on 2018/6/25.
 * 邮箱：2816886869@qq.com
 */

public class Weather {
    public WeatherInfo weatherinfo;

    public class WeatherInfo {
        public String city;
        public String cityid;
        public String WD;

        public WeatherInfo(String city, String cityid, String WD) {
            this.city = city;
            this.cityid = cityid;
            this.WD = WD;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCityid() {
            return cityid;
        }

        public void setCityid(String cityid) {
            this.cityid = cityid;
        }

        public String getWD() {
            return WD;
        }

        public void setWD(String WD) {
            this.WD = WD;
        }

        @Override
        public String toString() {
            return "WeatherInfo{" +
                    "city='" + city + '\'' +
                    ", cityid='" + cityid + '\'' +
                    ", WD='" + WD + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Weather{" +
                "weatherinfo=" + weatherinfo +
                '}';
    }
}
