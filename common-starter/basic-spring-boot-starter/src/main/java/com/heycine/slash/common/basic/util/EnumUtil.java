package com.heycine.slash.common.basic.util;

import com.heycine.slash.common.basic.BasicEnum;

/**
 * @desc: 枚举工具类，通过Code返回枚举 T extends CodeEnum 定义泛型的上限 enumClass.getEnumConstants() 通过反射取出Enum所有常量的属性值
 * 
 * @author: ZhiJi.Zhou
 */
public class EnumUtil {

    /**
     * 获取枚举，根据code
     *
     * @param code
     * @param enumClass
     * @param <T>
     * 
     * @return
     */
    public static <T extends BasicEnum> T getByCode(Integer code, Class<T> enumClass) {
        // 通过反射取出Enum所有常量的属性值
        for (T each : enumClass.getEnumConstants()) {
            // 利用code进行循环比较，获取对应的枚举
            if (code.equals(each.getCode())) {
                return each;
            }
        }
        return null;
    }

    /**
     * 获取枚举，根据code
     *
     * @param code
     * @param enumClass
     * @param <T>
     *
     * @return
     */
    public static <T extends BasicEnum> T getByCode(String code, Class<T> enumClass) {
        // 通过反射取出Enum所有常量的属性值
        for (T each : enumClass.getEnumConstants()) {
            // 利用code进行循环比较，获取对应的枚举
            if (code.equals(each.getCode())) {
                return each;
            }
        }
        return null;
    }

    /**
     * 获取枚举，根据info
     *
     * @param info
     * @param enumClass
     * @param <T>
     * 
     * @return
     */
    public static <T extends BasicEnum> T getByInfo(String info, Class<T> enumClass) {
        // 通过反射取出Enum所有常量的属性值
        for (T each : enumClass.getEnumConstants()) {
            // 利用info进行循环比较，获取对应的枚举
            if (info.equals(each.getInfo())) {
                return each;
            }
        }
        return null;
    }

    /**
     * 获取枚举的所有属性
     *
     * @param enumClass
     * @param <T>
     * 
     * @return
     */
    public static <T extends BasicEnum> T[] getAll(Class<T> enumClass) {
        // 通过反射取出Enum所有常量的属性值
        return enumClass.getEnumConstants();
    }

}
