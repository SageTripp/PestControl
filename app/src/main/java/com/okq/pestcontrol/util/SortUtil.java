package com.okq.pestcontrol.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * 为list对象排序的工具类
 * Created by Administrator on 2015/12/16.
 */
public class SortUtil {

    /**
     * 对List对象按照某个成员变量进行排序
     *
     * @param list      List对象
     * @param sortField 排序的属性名称 如果不是基本类型,写入内部属性名称,如:pestKind.kindName
     * @param isASC     是否按照ASC排序
     */
    public static <T> void sortList(List<T> list, final String sortField, final boolean isASC) {
        if (list == null || list.size() < 2) {
            return;
        }
        Collections.sort(list, new Comparator<T>() {
            @Override
            public int compare(T o1, T o2) {
                try {
                    Object[] field1 = getField(o1, sortField);
                    Object[] field2 = getField(o2, sortField);

                    Field field = (Field) field1[0];

                    String typeName = (String) field1[2];

                    Object v1 = field1[1];
                    Object v2 = field2[1];

//                    boolean ASC_order = (sortMode == null || "ASC".equalsIgnoreCase(sortMode));
                    boolean ASC_order = isASC;

                    //判断字段数据类型，并比较大小
                    if (typeName.endsWith("string")) {
                        String value1 = v1.toString();
                        String value2 = v2.toString();
                        return ASC_order ? value1.compareTo(value2) : value2.compareTo(value1);
                    } else if (typeName.endsWith("short")) {
                        Short value1 = Short.parseShort(v1.toString());
                        Short value2 = Short.parseShort(v2.toString());
                        return ASC_order ? value1.compareTo(value2) : value2.compareTo(value1);
                    } else if (typeName.endsWith("byte")) {
                        Byte value1 = Byte.parseByte(v1.toString());
                        Byte value2 = Byte.parseByte(v2.toString());
                        return ASC_order ? value1.compareTo(value2) : value2.compareTo(value1);
                    } else if (typeName.endsWith("char")) {
                        Integer value1 = (int) (v1.toString().charAt(0));
                        Integer value2 = (int) (v2.toString().charAt(0));
                        return ASC_order ? value1.compareTo(value2) : value2.compareTo(value1);
                    } else if (typeName.endsWith("int") || typeName.endsWith("integer")) {
                        Integer value1 = Integer.parseInt(v1.toString());
                        Integer value2 = Integer.parseInt(v2.toString());
                        return ASC_order ? value1.compareTo(value2) : value2.compareTo(value1);
                    } else if (typeName.endsWith("long")) {
                        Long value1 = Long.parseLong(v1.toString());
                        Long value2 = Long.parseLong(v2.toString());
                        return ASC_order ? value1.compareTo(value2) : value2.compareTo(value1);
                    } else if (typeName.endsWith("float")) {
                        Float value1 = Float.parseFloat(v1.toString());
                        Float value2 = Float.parseFloat(v2.toString());
                        return ASC_order ? value1.compareTo(value2) : value2.compareTo(value1);
                    } else if (typeName.endsWith("double")) {
                        Double value1 = Double.parseDouble(v1.toString());
                        Double value2 = Double.parseDouble(v2.toString());
                        return ASC_order ? value1.compareTo(value2) : value2.compareTo(value1);
                    } else if (typeName.endsWith("boolean")) {
                        Boolean value1 = Boolean.parseBoolean(v1.toString());
                        Boolean value2 = Boolean.parseBoolean(v2.toString());
                        return ASC_order ? value1.compareTo(value2) : value2.compareTo(value1);
                    } else if (typeName.endsWith("date")) {
                        Date value1 = (Date) (v1);
                        Date value2 = (Date) (v2);
                        return ASC_order ? value1.compareTo(value2) : value2.compareTo(value1);
                    } else if (typeName.endsWith("timestamp")) {
                        Timestamp value1 = (Timestamp) (v1);
                        Timestamp value2 = (Timestamp) (v2);
                        return ASC_order ? value1.compareTo(value2) : value2.compareTo(value1);
                    } else {
                        //调用对象的compareTo()方法比较大小
                        Method method = field.getType().getDeclaredMethod("compareTo", new Class[]{field.getType()});
                        method.setAccessible(true); //设置可访问权限
                        int result = (Integer) method.invoke(v1, new Object[]{v2});
                        return ASC_order ? result : result * (-1);
                    }
                } catch (Exception e) {
                    String err = e.getLocalizedMessage();
                    System.out.println(err);
                    e.printStackTrace();
                }

                return 0; //未知类型，无法比较大小
            }
        });
    }


    private static <T> Object[] getField(T t, String sortField) throws NoSuchFieldException {
        String[] split = sortField.split("\\.");
        Field returnField = null;
        Object value = null;
        String typeName = "";
        try {
            if (split.length > 1) {
                Class clazz = t.getClass();
                Field field = clazz.getDeclaredField(split[0]);
                field.setAccessible(true);
                returnField = (Field) getField(field.get(t), sortField.substring(split[0].length() + 1, sortField.length()))[0];
                returnField.setAccessible(true);
                typeName = returnField.getType().getName().toLowerCase(); //转换成小写
                value = returnField.get(field.get(t));
            } else {
                Class clazz = t.getClass();
                returnField = clazz.getDeclaredField(sortField);
                returnField.setAccessible(true);
                typeName = returnField.getType().getName().toLowerCase(); //转换成小写
                value = returnField.get(t);
            }
            //获取field的值
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return new Object[]{returnField, value, typeName};
    }

}
