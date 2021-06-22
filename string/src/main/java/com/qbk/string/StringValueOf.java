package com.qbk.string;

/**
 *  String.valueOf 和 null
 */
public class StringValueOf {

    public static void main(String[] args) {
        Object o = null;
        /*
         public static String valueOf(Object obj) {
            return (obj == null) ? "null" : obj.toString();
         }
         */
        String nullStr = String.valueOf(o);
        System.out.println(nullStr);
        System.out.println(nullStr.length()); //4

        /*
          public static String valueOf(char data[]) {
               return new String(data);
            }

         java.lang.NullPointerException
	        at java.lang.String.<init>(String.java:166)
	        at java.lang.String.valueOf(String.java:3008)
	        at com.qbk.string.StringValueOf.main(StringValueOf.java:24)
         */
        String s = String.valueOf(null); //java.lang.NullPointerException



    }
}
