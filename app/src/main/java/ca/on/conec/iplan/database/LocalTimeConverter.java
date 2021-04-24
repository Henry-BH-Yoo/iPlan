package ca.on.conec.iplan.database;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.room.TypeConverter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

// Type converter for Room DB, or general use
public class LocalTimeConverter  {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @TypeConverter
    public static LocalTime toTime(String timeString) {
        if (timeString == null) {
            return null;
        } else {
            return LocalTime.parse(timeString, DateTimeFormatter.ofPattern("HH:m"));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @TypeConverter
    public static String toTimeString(LocalTime time) {
        if (time == null) {
            return null;
        } else {
            return time.format(DateTimeFormatter.ofPattern("HH:m")); // ex 22:40 or 07:01?
        }
    }
}
