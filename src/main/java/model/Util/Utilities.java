package model.Util;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Utilities {

    public static String resetEmail;

    // generate a random number of 10 digits
    public static String generateToken(){
        long number = (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;
        String strLong = String.valueOf(number);
        return strLong;
    }
    // get the time 1 hour from now
    public static Date getOneHourFutureTime(){
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        Date future = calendar.getTime();
        System.out.println(calendar.get(Calendar.HOUR_OF_DAY));

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY/MM/DD HH:mm:ss");
        System.out.println(simpleDateFormat.format(future));
        return future;
    }


    public static void main(String[] args) {
        Utilities utils = new Utilities();
        System.out.println(utils.generateToken());
            utils.getOneHourFutureTime();
    }
}
