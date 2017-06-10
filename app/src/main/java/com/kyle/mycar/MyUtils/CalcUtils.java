package com.kyle.mycar.MyUtils;

import java.math.BigDecimal;

/**
 * 复制他人，未修改
 * Created by Kyle on 2017/2/8.
 */

public class CalcUtils {

    // TODO: 2017/2/9 负数运算
    /**
     * 两个字符类型的小数进行相加为a+b;
     *
     * @param a
     * @param b
     * @return
     */
    public static BigDecimal appendBigDecimal(BigDecimal a, String b) {
        BigDecimal b1 = new BigDecimal(b);
        BigDecimal c = a.add(b1);
        c.setScale(2,BigDecimal.ROUND_HALF_UP);
        return c;
    }
    /**
//     * 两个字符类型的小数进行相加为a+b;
//     *
//     * @param a
//     * @param b
//     * @return
//     */
//    public static String addBigDecimal(String a, String b) {
//        BigDecimal a1 = new BigDecimal(a);
//        BigDecimal b1 = new BigDecimal(b);
//        BigDecimal c1 = a1.add(b1);
//        c1.setScale(2);
//        return c1.toString();
//    }

    /**
     * 两个字符类型的小数进行相减为a-b;
     *
     * @param a
     * @param b
     * @return BigDecimal类型
     */
    public static BigDecimal reduceBigDecimal(String a, String b) {
        BigDecimal a1 = new BigDecimal(a);
        BigDecimal b1 = new BigDecimal(b);
        BigDecimal c1 = a1.subtract(b1);
        c1.setScale(2);
        return c1;
    }

    /**
     * 两个字符类型的数相乘 a*b=c；
     *
     * @param a
     * @param b
     * @return
     */
//    public static String multipliedString(String a, String b) {
//        BigDecimal a1 = new BigDecimal(a);
//        BigDecimal b1 = new BigDecimal(b);
//        BigDecimal c1 = a1.multiply(b1);
//        c1.setScale(2);
//        return c1.toString();
//    }

    /**
     * 两个字符类型的数相除 a/b=c；
     *
     * @param a
     * @param b
     * @return
     */
//    public static String divideString(String a, String b) {
//        BigDecimal a1 = new BigDecimal(a);
//        BigDecimal b1 = new BigDecimal(b);
//        BigDecimal c1 = a1.divide(b1);
//        c1.setScale(2);
//        return c1.toString();
//    }

//    public static String Calc(String s) {
//        String r = "";
//        int p = 0;
//        for (int i = 0; i < s.length(); i++) {
//            if (s.charAt(i) == '+' || s.charAt(i) == '-' || s.charAt(i) == '×'
//                    || s.charAt(i) == '÷') {
//                p++;
//            }
//        }
//        if (p==0){
//            return s;
//        }
//        String k[] = new String[2 * p + 1];
//        int k1 = 0;
//        int first = 0;
//        for (int i = 0; i < s.length(); i++) {
//            if (s.charAt(i) == '+' || s.charAt(i) == '-' || s.charAt(i) == '×'
//                    || s.charAt(i) == '÷') {
//                k[k1] = s.substring(first, i);
//                k1++;
//                k[k1] = "" + s.charAt(i);
//                k1++;
//                first = i + 1;
//            }
//        }
//        k[k1] = s.substring(first, s.length());
//        int kp = p;
//        while (kp > 0) {
//            for (int i = 0; i < k.length; i++) {
//                if (k[i].equals("×") || k[i].equals("÷")) {
//                    int l;
//                    for (l = i - 1; l > -1; l--) {
//                        if (!(k[l].equals("p")))
//                            break;
//                    }
//                    int q;
//                    for (q = i + 1; q < k.length; q++) {
//                        if (!(k[l].equals("p")))
//                            break;
//                    }
//                    if (k[i].equals("×")) {
//                        int j = 0;
//                        if (k[l].contains("-")) {
//                            k[l] = k[l].replace("-", "");
//                            j++;
//                        }
//                        if (k[q].contains("-")) {
//                            k[q] = k[q].replace("-", "");
//                            j++;
//                        }
//                        if (j % 2 == 1) {
//                            k[i] = "-" + multipliedString(k[l], k[q]);
//                        } else {
//                            k[i] = "" + multipliedString(k[l], k[q]);
//                        }
//                        k[l] = "p";
//                        k[q] = "p";
//                        kp--;
//                    } else {
//                        int j = 0;
//                        if (k[l].contains("-")) {
//                            k[l] = k[l].replace("-", "");
//                            j++;
//                        }
//                        if (k[q].contains("-")) {
//                            k[q] = k[q].replace("-", "");
//                            j++;
//                        }
//                        if (j % 2 == 1) {
//                            k[i] = "-" + divideString(k[l], k[q]);
//                        } else {
//                            k[i] = "" + divideString(k[l], k[q]);
//                        }
//                        k[l] = "p";
//                        k[q] = "p";
//                        kp--;
//                    }
//                    break;
//                }
//            }
//
//
//            for (int i = 0; i < 2 * p + 1; i++) {
//                if (k[i].equals("+") || k[i].equals("-")) {
//                    int l;
//                    for (l = i - 1; l > -1; l--) {
//                        if (!(k[l].equals("p")))
//                            break;
//                    }
//                    int q;
//                    for (q = i + 1; q < k.length; q++) {
//                        if (!(k[q].equals("p")))
//                            break;
//                    }
//                    if (k[i].equals("+")) {
//                        k[i] = "" + addBigDecimal(k[l], k[q]);
//                        k[l] = "p";
//                        k[q] = "p";
//                        kp--;
//                    } else {
//                        k[i] = "" + reduceBigDecimal(k[l], k[q]);
//                        k[l] = "p";
//                        k[q] = "p";
//                        kp--;
//                    }
//                    break;
//                }
//            }
//            for (int i = 0; i < k.length; i++) {
//                if (!(k[i].equals("p"))) {
//                    r = k[i];
//                    break;
//                }
//            }
//        }
//        return r;
//    }

//        public String sizeyunsuan(String s) {
//            while (true) {
//                int first = 0;
//                int last = 0;
//                for (int i = 0; i < s.length(); i++) {
//                    if (s.charAt(i) == '(')
//                        first = i;
//                    if (s.charAt(i) == ')') {
//                        last = i;
//                        break;
//                    }
//                }
//                if (last == 0) {
//                    return yunsuanjibie(s);
//                } else {
//                    String s1 = s.substring(0, first);
//                    String s2 = s.substring(first + 1, last);
//                    String s3 = s.substring(last + 1, s.length());
//                    s = s1 + yunsuanjibie(s2) + s3;
//                }
//            }
//        }
}

