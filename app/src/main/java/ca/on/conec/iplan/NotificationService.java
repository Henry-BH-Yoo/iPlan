/**
 * FileName : NotificationService.java
 * Purpose
 * Revision History :
 *      2021.04.23 Sean    Create
 *      2021.04.24 Sean    Modify notification service
 *      2021.04.24 Sean    Modify notification service icon
 */
package ca.on.conec.iplan;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.icu.util.LocaleData;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import ca.on.conec.iplan.activity.MainActivity;
import ca.on.conec.iplan.adapter.ItemTouchHelperCallback;
import ca.on.conec.iplan.adapter.RecyclerViewAdapter;
import ca.on.conec.iplan.database.LocalTimeConverter;
import ca.on.conec.iplan.database.iPlanRepository;
import ca.on.conec.iplan.entity.Todo;
import ca.on.conec.iplan.fragment.DailyFragment;
import ca.on.conec.iplan.viewmodel.TodoViewModel;



public class NotificationService extends Service {

    TodoViewModel todoViewModel;
    List<Todo> todosWithAlarm;

    public NotificationService() {
    }

    // This is for outside components
    @Override
    public IBinder onBind(Intent intent) {
        // Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");

        return null;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        final Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel("notiChannel", "channelName", importance);

        final NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);

        LocalTime timeNow = LocalTime.now();
        LocalTime timeHrLater = timeNow.plusHours(1);

        String timeS = LocalTimeConverter.toTimeString(timeNow);
        String timeE = LocalTimeConverter.toTimeString(timeHrLater);

        // get day of week
        LocalDateTime now = LocalDateTime.now();
        int days = now.getDayOfWeek().getValue();

        // It is for LiveData method in iPlanRepository.java
        todoViewModel = new ViewModelProvider.AndroidViewModelFactory(
                this.getApplication()).create(TodoViewModel.class);


        todoViewModel.getTodosWithAlarm(timeS, timeE, days).observeForever(todos -> {
            todosWithAlarm = todos;

            if (todosWithAlarm != null && todosWithAlarm.size() > 0 ) {

                todosWithAlarm.forEach(a -> {
                    final Notification notification = new Notification.Builder(getApplicationContext(), "notiChannel")
                            .setSmallIcon(R.drawable.new_ic_alarm)
                            .setContentTitle(a.name + " at " + LocalTimeConverter.toTimeString(a.startTime))
                            .setContentText("Tap to open the iPlan")
                            .setAutoCancel(true)
                            .setContentIntent(pendingIntent)
                            .build();

                    notificationManager.notify(1, notification);
                    stopSelf();
                });
            } else {
//                Toast.makeText(this, "todosWithAlarm list is null or empty", Toast.LENGTH_LONG).show();
            }
        });

        super.onCreate();
    }

    @Override
    public void onDestroy() {
//        Toast.makeText(this, "Service onDestroy called", Toast.LENGTH_LONG).show();
        super.onDestroy();
    }
}