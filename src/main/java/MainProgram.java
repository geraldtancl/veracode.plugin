import com.veracode.apiwrapper.wrappers.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class MainProgram {

    private static final String API_ID = "";
    private static final String API_KEY = "";

    public static void main(String[] args) {
        //2019-09-03 01:34:50 UTC
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date;
        try {
            date = isoFormat.parse("2019-09-03 01:34:50 UTC");
            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

}
