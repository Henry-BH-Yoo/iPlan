/**
 * FileName : TodayDecorator.java
 * Purpose : For today bold
 * Revision History :
 *          2021 04 23  Henry   Create Decorator
 */

package ca.on.conec.iplan.calendar;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Calendar;
import java.util.Date;


public class TodayDecorator implements DayViewDecorator {
    //private final Drawable drawable;
    private final Calendar calendar = Calendar.getInstance();
    private CalendarDay date;

    public TodayDecorator() {
      //  drawable = null;
        date = CalendarDay.today();
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return date != null && day.equals(date);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new StyleSpan(Typeface.BOLD));
        view.addSpan(new RelativeSizeSpan(1.4f));
        //view.setSelectionDrawable(drawable);
    }

}
