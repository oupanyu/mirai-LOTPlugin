package top.oupanyu.Functions;

public class FormatInteger {
    public static String formatToDouble(Long integer){
        double formattednumber = integer;
        String result = "";
        if (integer / 1000 < 1){
            return String.valueOf(integer);
        }
        if (integer / 1000 > 1){
            formattednumber = formattednumber / 1000;
            result = "k";
        }
        if (integer / 1000000 > 1){
            formattednumber = formattednumber / 1000000;
            result = "M";
        }

        return String.format("%.2f",formattednumber) + result;
    }
}
