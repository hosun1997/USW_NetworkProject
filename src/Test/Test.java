package Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Test {

    private final static String UNSECURED_CHAR_REGULAR_EXPRESSION =
            "select|delete|update|insert|create|alter|drop";
    private static Pattern unsecuredCharPattern;

    public static void initialize(){
        unsecuredCharPattern = Pattern.compile(UNSECURED_CHAR_REGULAR_EXPRESSION,Pattern.CASE_INSENSITIVE);
    }
    private static String makeSecureString(final String str, int maxLength)
    {
        String secureStr = str.substring(0, maxLength);
        Matcher matcher = unsecuredCharPattern.matcher(secureStr);
        if(matcher.find()){
            return matcher.replaceAll("");
        }
        else{
            return secureStr;
        }
    }

    public static String makeSecureString2(String str){
        String match = "[^\uAC00-\uD7A30-9a-zA-Z\\s]";
        str = str.replaceAll(match, "");
        return str;
    }

    public static void main(String[] args) {
//        WebCrawlingTest webCrawlingTest = new WebCrawlingTest("어벤져스");
        initialize();
        String str2 = "어벤져스''<><>@#$%^&*(''";
        str2 = makeSecureString2(str2);
        System.out.println(str2);
//        System.out.println(str2);
//        str2 =makeSecureString(str2,str2.length());
//        System.out.println(str2);

    }
}
