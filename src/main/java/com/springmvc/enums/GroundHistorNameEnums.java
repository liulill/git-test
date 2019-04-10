package com.springmvc.enums;

/**
 * @author maliang
 * @create 2018-11-06 14:17
 */
public enum GroundHistorNameEnums {

    /**
     * key-名称
     */
    GroundHistor1("year","年"),
    GroundHistor2("month","月"),
    GroundHistor3("day","日"),
    GroundHistor4("hour","时"),
    GroundHistor5("province","省"),
    GroundHistor6("city","市、州"),
    GroundHistor7("county","县"),
    GroundHistor8("town","乡"),
    GroundHistor9("tem","温度"),
    GroundHistor10("pre_24h","24小时雨量"),
    GroundHistor11("rhu","湿度"),
    GroundHistor12("k1","平均气温低于20℃时"),
    GroundHistor13("k2","平均气温大于22℃时"),
    GroundHistor14("k3","气温23-25℃时"),
    GroundHistor15("k4","气温25-32℃时"),
    GroundHistor16("k5","在适温条件下，雨量>40mm的雨后约7d"),
    GroundHistor17("k6","雨后相对湿度保持在80%以上3～5d"),
    GroundHistor18("severity","发病程度");

    private String key;
    private String name;

    GroundHistorNameEnums(String key, String name) {
        this.key = key;
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
