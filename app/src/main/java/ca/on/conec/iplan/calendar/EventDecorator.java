/**
 * FileName : EventDecorator.java
 * Purpose : For monthly plan calendar display event
 * Revision History :
 *          2021 04 23  Henry   Create Decorator
 */


package ca.on.conec.iplan.calendar;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;


public class EventDecorator implements DayViewDecorator {
    private int color;
    private HashSet<CalendarDay> dates;

    public EventDecorator() {

    }

    public EventDecorator(int color, Collection<CalendarDay> dates) {
        this.color = color;
        this.dates = new HashSet<>(dates);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new DotSpan(5, color)); // 날자밑에 점
    }
}
