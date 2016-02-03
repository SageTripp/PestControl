package com.okq.pestcontrol.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * 为list对象排序的工具类
 * Created by zst on 2015/12/16.
 */
public class SortUtil1 {

    private static String[] strings = new String[]{
            "asd",
            "人",
            "的",
            "sfe",
            "asd",
            "ew",
            "htr",
            "dg",
            "kyt",
            "是",
            "r5",
            "sjh5",
            "asd",
            "546h",
            "fgfe4",
            "这个",
            "儿童",
            "虽然太热",
            "瑞特人",
            "帆哥",
            "谁惹他",
            "而儿童",
            "金太阳",
            "好",
            "飒飒",
            "土壤",
            "瑞特",
            "人人",
            "鬼地方个突然",
            "a热特然sd",
            "asd",
            "asd",
            "色",
    };

    public static void main(String[] args) {

        List<A> as = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            B b = new B((int) (Math.random() * 20), strings[(int) (Math.random() * 20)]);
            A a = new A((int) (Math.random() * 20), strings[(int) (Math.random() * 20)], b);
            as.add(a);
        }

        sortList(as, true, "aB.ba", "aB.bb");
        for (A a : as) {
            System.out.println(a.toString());
        }

    }


    /**
     * 对List对象按照某个成员变量进行排序
     *
     * @param list       List对象
     * @param sortFields 排序的属性名称 如果不是基本类型,写入内部属性名称,如:pestKind.kindName
     * @param isASC      是否按照ASC排序
     */
    public static <T> void sortList(List<T> list, final boolean isASC, final String... sortFields) {
        if (list == null || list.size() < 2) {
            return;
        }
        Collections.sort(list, new Comparator<T>() {

            public int compareByField(T o1, T o2, int fieldIndex) {
                try {
                    Object[] field1 = getField(o1, sortFields[fieldIndex]);
                    Object[] field2 = getField(o2, sortFields[fieldIndex]);

                    Field field = (Field) field1[0];

                    String typeName = (String) field1[2];

                    Object v1 = field1[1];
                    Object v2 = field2[1];

//                    boolean ASC_order = (sortMode == null || "ASC".equalsIgnoreCase(sortMode));
                    boolean ASC_order = isASC;

                    int result;

                    //判断字段数据类型，并比较大小
                    if (typeName.endsWith("string")) {
                        String value1 = v1.toString();
                        String value2 = v2.toString();
                        result = ASC_order ? value1.compareTo(value2) : value2.compareTo(value1);
                        if (result == 0 && fieldIndex < sortFields.length - 1) {
                            return compareByField(o1, o2, ++fieldIndex);
                        }
                        return result;
                    } else if (typeName.endsWith("short")) {
                        Short value1 = Short.parseShort(v1.toString());
                        Short value2 = Short.parseShort(v2.toString());
                        result = ASC_order ? value1.compareTo(value2) : value2.compareTo(value1);
                        if (result == 0 && fieldIndex < sortFields.length - 1) {
                            return compareByField(o1, o2, ++fieldIndex);
                        }
                        return result;
                    } else if (typeName.endsWith("byte")) {
                        Byte value1 = Byte.parseByte(v1.toString());
                        Byte value2 = Byte.parseByte(v2.toString());
                        result = ASC_order ? value1.compareTo(value2) : value2.compareTo(value1);
                        if (result == 0 && fieldIndex < sortFields.length - 1) {
                            return compareByField(o1, o2, ++fieldIndex);
                        }
                        return result;
                    } else if (typeName.endsWith("char")) {
                        Integer value1 = (int) (v1.toString().charAt(0));
                        Integer value2 = (int) (v2.toString().charAt(0));
                        result = ASC_order ? value1.compareTo(value2) : value2.compareTo(value1);
                        if (result == 0 && fieldIndex < sortFields.length - 1) {
                            return compareByField(o1, o2, ++fieldIndex);
                        }
                        return result;
                    } else if (typeName.endsWith("int") || typeName.endsWith("integer")) {
                        Integer value1 = Integer.parseInt(v1.toString());
                        Integer value2 = Integer.parseInt(v2.toString());
                        result = ASC_order ? value1.compareTo(value2) : value2.compareTo(value1);
                        if (result == 0 && fieldIndex < sortFields.length - 1) {
                            return compareByField(o1, o2, ++fieldIndex);
                        }
                        return result;
                    } else if (typeName.endsWith("long")) {
                        Long value1 = Long.parseLong(v1.toString());
                        Long value2 = Long.parseLong(v2.toString());
                        result = ASC_order ? value1.compareTo(value2) : value2.compareTo(value1);
                        if (result == 0 && fieldIndex < sortFields.length - 1) {
                            return compareByField(o1, o2, ++fieldIndex);
                        }
                        return result;
                    } else if (typeName.endsWith("float")) {
                        Float value1 = Float.parseFloat(v1.toString());
                        Float value2 = Float.parseFloat(v2.toString());
                        result = ASC_order ? value1.compareTo(value2) : value2.compareTo(value1);
                        if (result == 0 && fieldIndex < sortFields.length - 1) {
                            return compareByField(o1, o2, ++fieldIndex);
                        }
                        return result;
                    } else if (typeName.endsWith("double")) {
                        Double value1 = Double.parseDouble(v1.toString());
                        Double value2 = Double.parseDouble(v2.toString());
                        result = ASC_order ? value1.compareTo(value2) : value2.compareTo(value1);
                        if (result == 0 && fieldIndex < sortFields.length - 1) {
                            return compareByField(o1, o2, ++fieldIndex);
                        }
                        return result;
                    } else if (typeName.endsWith("boolean")) {
                        Boolean value1 = Boolean.parseBoolean(v1.toString());
                        Boolean value2 = Boolean.parseBoolean(v2.toString());
                        result = ASC_order ? value1.compareTo(value2) : value2.compareTo(value1);
                        if (result == 0 && fieldIndex < sortFields.length - 1) {
                            return compareByField(o1, o2, ++fieldIndex);
                        }
                        return result;
                    } else if (typeName.endsWith("date")) {
                        Date value1 = (Date) (v1);
                        Date value2 = (Date) (v2);
                        result = ASC_order ? value1.compareTo(value2) : value2.compareTo(value1);
                        if (result == 0 && fieldIndex < sortFields.length - 1) {
                            return compareByField(o1, o2, ++fieldIndex);
                        }
                        return result;
                    } else if (typeName.endsWith("timestamp")) {
                        Timestamp value1 = (Timestamp) (v1);
                        Timestamp value2 = (Timestamp) (v2);
                        result = ASC_order ? value1.compareTo(value2) : value2.compareTo(value1);
                        if (result == 0 && fieldIndex < sortFields.length - 1) {
                            return compareByField(o1, o2, ++fieldIndex);
                        }
                        return result;
                    } else {
                        //调用对象的compareTo()方法比较大小
                        Method method = field.getType().getDeclaredMethod("compareTo", new Class[]{field.getType()});
                        method.setAccessible(true); //设置可访问权限
                        result = (Integer) method.invoke(v1, new Object[]{v2});
                        return ASC_order ? result : result * (-1);
                    }
                } catch (Exception e) {
                    String err = e.getLocalizedMessage();
                    System.out.println(err);
                    e.printStackTrace();
                }

                return 0; //未知类型，无法比较大小
            }

            @Override
            public int compare(T o1, T o2) {
                return compareByField(o1, o2, 0);
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


    public static class A {
        public int aa;
        public String ab;
        public B aB;

        public A(int aa, String ab, B aB) {
            this.aa = aa;
            this.ab = ab;
            this.aB = aB;
        }

        @Override
        public String toString() {
            return "A{" +
                    "aa=" + aa +
                    "\t ab='" + ab + '\'' +
                    "\t aB=" + aB +
                    '}';
        }
    }

    public static class B {
        public int ba;
        public String bb;

        public B(int ba, String bb) {
            this.ba = ba;
            this.bb = bb;
        }

        @Override
        public String toString() {
            return "B{" +
                    "ba=" + ba +
                    "\t bb='" + bb + '\'' +
                    '}';
        }
    }

}
