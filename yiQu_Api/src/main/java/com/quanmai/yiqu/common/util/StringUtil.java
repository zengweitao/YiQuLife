package com.quanmai.yiqu.common.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhanjinj on 16/3/29.
 */
public class StringUtil {


    /**
     * 计算字符串字符长度
     *
     * @param content
     * @return
     */
    public static int getCharacterNum(String content) {
        if (null == content || content.equals("")) {
            return 0;
        } else {
            return content.length() + getChineseNum(content);
        }

    }


    /**
     * 计算字符串的中文长度
     *
     * @param str
     * @return
     */
    public static int getChineseNum(String str) {
        int num = 0;
        char[] myChar = str.toCharArray();
        for (int i = 0; i < myChar.length; i++) {
            if ((char) (byte) myChar[i] != myChar[i]) {
                num++;
            }
        }
        return num;
    }

    /**
     * 设置过滤字符函数
     * 只允许大小写字母、阿拉伯数字、中文以及下划线
     *
     * @param str
     * @return
     */
    public static String stringFilter(String str) {
        String reg = "[^a-zA-Z0-9\u4E00-\u9FA5_]";
        if (str != null && !TextUtils.isEmpty(str)) {
            return str.replaceAll(reg, "");
        }
        return "";
    }

    /**
     * 设置过滤字符函数
     * 只允许数字
     * @param str
     * @return
     */
    public static String numberFilter(String str){
        String reg ="[^0-9]";
        if (!TextUtils.isEmpty(str)){
            return str.replaceAll(reg,"");
        }
        return "";
    }

    /**
     * 去除字符串中的空格
     * @param str
     * @return
     */
    public static String removeAllSpace(String str)
    {
        String tmpstr=str.replace(" ","");
        return tmpstr;
    }


    /**
     * 更改局部字体颜色
     *
     * @param str
     * @param color
     * @param start
     * @param end
     * @return
     */
    public static SpannableString changeLocalTextColor(String str, String color, int start, int end) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return changeLocalTextColor(str, color, start, end, SpannableString.SPAN_EXCLUSIVE_INCLUSIVE);
    }

    /**
     * 更改局部字体颜色
     *
     * @param str
     * @param color
     * @param start
     * @param end
     * @param flags
     * @return
     */
    public static SpannableString changeLocalTextColor(String str, String color, int start, int end, int flags) {
        if (TextUtils.isEmpty(str) || start < 0 || end < 0 ||
                str.length() < start || str.length() < end || start >= end) {
            return new SpannableString("");
        }

        SpannableString string = new SpannableString(str);
        string.setSpan(new ForegroundColorSpan(Color.parseColor(color)), start, end, flags);
        return string;
    }

    /**
     * 处理空字符串
     *
     * @param string
     * @return
     */
    public static String stringNullFilter(String string) {
        String mString = "";
        if (string == null || "".equals(string) || "null".equals(string)) {
            return mString;
        }
        return string;
    }

    /**
     * 处理空数字字符串
     *
     * @param string
     * @return
     */
    public static String stringNumFilter(String string) {
        String mString = "0";
        if (string == null || "".equals(string) || "null".equals(string)) {
            return mString;
        }
        return string;
    }


    /**
     * 处理空整数类型
     *
     * @param integer
     * @return
     */
    public static int intNullFilter(Integer integer) {
        if (integer == null) {
            return 0;
        }
        return integer;
    }

    /**
     * 将字符串转换成Bitmap类型
     *
     * @param string
     * @return
     */
    public static Bitmap stringToBitmap(String string) {
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(string, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 是否匹配正则表达式
     *
     * @param strRegex 正则表达式规则
     * @param strText  匹配字符串
     * @return
     */
    public static boolean isRegexMatches(String strRegex, String strText) {
        if (TextUtils.isEmpty(strRegex) || TextUtils.isEmpty(strText)) {
            return false;
        }
        Pattern r = Pattern.compile(strRegex);
        Matcher m = r.matcher(strText);

        return m.find();
    }

    /**
     * 过滤
     *
     * @param strText
     * @return
     */
    public static String stringFilter2(String strText) {
        String strResult = "";
        if (TextUtils.isEmpty(strText) || TextUtils.isEmpty(strText)) {
            return strResult;
        }
        String strRegex = "\\b\\w{2,}\\b";
        Pattern pattern = Pattern.compile(strRegex);
        Matcher matcher = pattern.matcher(strText);
        if (matcher.find()) {
            strResult = matcher.group();
        }
        return strResult;
    }

    /**
     * 是否姓名,包括普通汉族名、少数民族姓名（阿沛·阿旺晋美），不支持英文、空格和特殊符号
     *
     * @param name
     * @return
     */
    public static boolean isRealName(String name) {
        //是否匹配空格
        if (isRegexMatches("\\s+", name.trim())) {
            return false;
        }
        //是否匹配阿拉伯数字
        if (isRegexMatches("[0-9]", name.trim())) {
            return false;
        }
        //是否匹配特殊字符（半角、全角）
        if (isRegexMatches("[`~!@#$%^&*()+=|{}':;',\\[\\]\\\\.<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？']", name)) {
            return false;
        }
        //是否匹配英文
        if (isRegexMatches("[a-zA-Z]", name)) {
            return false;
        }
        return isRegexMatches("[\\u4E00-\\u9FA5]{2,5}(?:·[\\u4E00-\\u9FA5]{2,5})*", name.trim());
    }

    /**
     * 是否电话号码
     *
     * @param phone
     * @return
     */
    public static boolean isPhoneNum(String phone) {
        return isRegexMatches("0?1[3|4|5|7|8][0-9]{9}", phone);
    }

    /**
     * 名字加密。如樊书振--》*书振
     * 替换第一个字符
     *
     * @param name
     * @return
     */
    public static String nameEncrypt(String name) {
        String strResult = "";
        if (TextUtils.isEmpty(name)) {
            return strResult;
        }
        if (isRegexMatches("[\\u4E00-\\u9FA5]{2,10}", name)) {
            StringBuilder stringBuilder = new StringBuilder(name);
            stringBuilder.replace(0, 1, "*");
            strResult = stringBuilder.toString();
        } else {
            strResult = name;
        }

        return strResult;
    }

    /**
     * 手机号码加密。如13212345678-->132****3217
     * 替换第4-8个数字
     *
     * @param phone
     * @return
     */
    public static String phoneEncrypt(String phone) {
        String strResult = "";
        if (TextUtils.isEmpty(phone)) {
            return strResult;
        }
        if (isRegexMatches("0?(13|14|15|17|18)[0-9]{9}", phone)) {
            StringBuilder stringBuilder = new StringBuilder(phone);
            stringBuilder.replace(3, 7, "****");
            strResult = stringBuilder.toString();
        } else {
            strResult = phone;
        }

        return strResult;
    }

    /**
     * 当前app版本是否最新版本
     *
     * @param currentversion 当前版本字符串 格式为**.**.**
     * @param latestversion  最新版本字符串 同上
     * @return
     */
    public static Boolean versionCompare(String currentversion, String latestversion) {
        Boolean isLatestVersion = true;
        if (TextUtils.isEmpty(currentversion) || TextUtils.isEmpty(latestversion)) {
            return true;
        }
        currentversion = currentversion.replaceAll("[^0-9\\.]", ""); //过滤除数字、“.”以外的字符
        latestversion = latestversion.replaceAll("[^0-9\\.]", ""); //同上

        String[] strsCurrent = currentversion.split("\\."); //将版本号分割为[**,**,**]的形式
        String[] strsLatest = latestversion.split("\\.");
        List<String> listCurrent = new ArrayList<>();
        List<String> listLatest = new ArrayList<>();
        for (int i = 0; i < strsCurrent.length; i++) {
            listCurrent.add(strsCurrent[i]);
        }
        for (int i = 0; i < strsLatest.length; i++) {
            listLatest.add(strsLatest[i]);
        }

        if (listCurrent.size() == 0 && listLatest.size() == 0) { //两个版本号字符串不包含“.”，直接对比。
            return Double.valueOf(currentversion) >= Double.valueOf(latestversion);
        }

//        int maxLength = strsCurrent.length >= strsLatest.length ? strsCurrent.length : strsLatest.length; //两个数组中的最大长度
        int maxLength = Math.max(listCurrent.size(), listLatest.size());
        for (int i = 1; i < maxLength + 1; i++) {
            if (listCurrent.size() < i) {
                listCurrent.add("0");
            }
            if (listLatest.size() < i) {
                listLatest.add("0");
            }
        }

        String currentV = "",lastV = "";
        for (int i = 0; i < maxLength; i++) { //逐一对比版本号
//            if (Integer.valueOf(listCurrent.get(i)) < Integer.valueOf(listLatest.get(i))) {
//                isLatestVersion = false;
//            }
            currentV+=listCurrent.get(i);
            lastV += listLatest.get(i);
        }

        if ((Integer.parseInt(currentV) < Integer.parseInt(lastV))){
            isLatestVersion = false;
        }
        return isLatestVersion;
    }

    /**
     * 过滤掉换行符
     *
     * @param str
     * @return
     */
    public static String lineFeedFilter(String str) {
        if (str == null || TextUtils.isEmpty(str)) {
            return "";
        }
        return str.trim().replace("\r|\n", "");
    }

    /**
     * 是否网址URL
     *
     * @param str
     * @return
     */
    public static Boolean isURL(String str) {
        Boolean isURL = false;
        if (!TextUtils.isEmpty(str)) {
            return false;
        }
        isURL = isRegexMatches("^((https|http|ftp|rtsp|mms)?:\\/\\/)[^\\s]+", str);
        return isURL;
    }

    /**
     * 当日期为单数在前面加‘0’
     * @param s
     * @param num
     * @return
     */
    public static String getDaTaAdd0(String s,int num){
        String ss;
        if (s.trim().length()<num){
            ss=0+s;
        }else {
            ss=s;
        }
        return ss;
    }

}
