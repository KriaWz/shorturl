package com.jiuzhang.url.util;

import java.util.Random;

/**
 * @auther: WZ
 * @Date: 2020/9/20 13:21
 * @Description: 随机生成6位短网址
 */
public class RandomUtil {

    public static String BaseTrans(String url){
        String str="zxcvbnmlkjhgfdsaqwertyuiopQWERTYUIOPASDFGHJKLZXCVBNM1234567890";
        //2.  由Random生成随机数
        Random random=new Random();
        StringBuffer sb =new StringBuffer();
        //3.  长度为几就循环几次
        for(int i=0; i<6; ++i) {
            //从62个的数字或字母中选择
            int number = random.nextInt(62);
            //将产生的数字通过length次承载到sb中
            sb.append(str.charAt(number));
        }
        //将承载的字符转换成字符串
        return sb.toString();
    }

}
