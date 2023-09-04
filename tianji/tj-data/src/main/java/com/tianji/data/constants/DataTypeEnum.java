package com.tianji.data.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName DataTypeEnum
 * @Author wusongsong
 * @Date 2022/10/10 15:28
 * @Version
 **/
@AllArgsConstructor
@Getter
public enum DataTypeEnum {

    VISITS(1, "访问量", "次","bar"),
    ORDER_AMOUNT(2, "订单金额", "元","bar"),
    ORDER_NUM(3, "订单笔数", "笔","line"),
    STU_NEW_NUM(4, "新增学员", "人","line"),
    CUSTOMER_UNIT_PRICE(5, "客单价", "元","bar"),
    STU_TOTAL_NUM(6, "学员总数", "人", "bar"),
    DAILY_LIVING_NUM(7, "日活用户", "个", "bar"),
    VISITOR_NUM(8, "访客数", "个", "bar"),
    PURCHASE_NUM(9, "购买量", "笔", "line"),
    NULL(null, null, null,null);

    private Integer type;
    private String name;
    private String unit;
    private String axisType;

    public static DataTypeEnum get(Integer type){
        for (DataTypeEnum dataTypeEnum : values()){
            if(dataTypeEnum.getType().equals(type)){
                return dataTypeEnum;
            }
        }
        return NULL;
    }

    public String nameWithUnit(){
        return String.format("%s (%s)", name, unit);
    }

    public static void main(String[] args) {
        System.out.println(DataTypeEnum.ORDER_AMOUNT.nameWithUnit());
    }
}
