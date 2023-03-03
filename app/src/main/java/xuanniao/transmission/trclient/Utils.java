package xuanniao.transmission.trclient;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Arrays;

public class Utils {

    public static String EINGCODE="UTF-8";

    public final static int PORT = 21000;

    public static int byteArrayToInt(byte[] b) {
        return   b[3] & 0xFF |
                (b[2] & 0xFF) << 8 |
                (b[1] & 0xFF) << 16 |
                (b[0] & 0xFF) << 24;
    }

    public static byte[] intToByteArray(int a) {
        return new byte[] {
                (byte) ((a >> 24) & 0xFF),
                (byte) ((a >> 16) & 0xFF),
                (byte) ((a >> 8) & 0xFF),
                (byte) (a & 0xFF)
        };
    }

    public static void main(String[] args) {
        int num = Integer.MAX_VALUE;
        byte[] intToByteArray = intToByteArray(num);
        System.out.println(Arrays.toString(intToByteArray));
        System.out.println(byteArrayToInt(intToByteArray));
    }


    /**
     * 占比计算保留小数的位数方法
     * 转成百分数
     * 当前数除以总数
     * @param  num1 ,num2  num1/num2
     * @return  rate  保留2位小数的
     */
    public static String  division(long num1,long num2){
        String rate="0.00%";
        //定义格式化起始位数
        String format="0.00";
        if(num2 != 0 && num1 != 0){
            DecimalFormat dec = new DecimalFormat(format);
            rate =  dec.format((double) num1 / num2*100)+"%";
            while(true){
                if(rate.equals(format+"%")){
                    format=format+"0";
                    DecimalFormat dec1 = new DecimalFormat(format);
                    rate =  dec1.format((double) num1 / num2*100)+"%";
                }else {
                    break;
                }
            }
        }else if(num1 != 0 && num2 == 0){
            rate = "100%";
        }
        return rate;
    }


    /**
     * 把上面得到的百分比转为字符串类型的小数  保留两位小数
     * @author  shw
     */
    public static BigDecimal perToDecimal(String percent){
        String decimal = percent.substring(0,percent.indexOf("%"));
        BigDecimal bigDecimal = new BigDecimal(decimal);
        bigDecimal.divide(new BigDecimal("100"), 4, BigDecimal.ROUND_HALF_UP);
        return bigDecimal;
    }

}
