package ca.on.conec.iplan;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import ca.on.conec.iplan.activity.MainActivity;
import ca.on.conec.iplan.adapter.ItemTouchHelperCallback;
import ca.on.conec.iplan.adapter.RecyclerViewAdapter;
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
        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");

        return null;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {

        Log.d("DEBUG" , ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> SErvice Create??");

        final Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel("notiChannel", "channelName", importance);

        final NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);

        final Notification notificationFail = new Notification.Builder(getApplicationContext(), "notiChannel")
                .setSmallIcon(R.drawable.baseline_alarm_black_24dp)
                .setContentTitle("Daytodo Alarm")
                .setContentText("null obj")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();


        //todo find the dayTodo which isAlarm is true, then send notification of it

        LocalTime timeNow = LocalTime.now();
        LocalTime timeHrStart = timeNow.minusHours(1);
        LocalTime timeHrLater = timeNow.plusHours(1);

        Log.d("DEBUG" , "timeHrStart : " + timeHrStart);
        Log.d("DEBUG" , "timeHrLater : " + timeHrLater);

        String timeS = timeHrStart.getHour() + ":" + timeHrStart.getMinute();
        String timeE = timeHrLater.getHour() + ":" + timeHrLater.getMinute();
        int days = 5; // Fri

        Log.d("DEBUG" , "timeS : " + timeS);
        Log.d("DEBUG" , "timeE : " + timeE);
        Log.d("DEBUG" , "days : " + days + "");

        // It is for LiveData method in iPlanRepository.java
        todoViewModel = new ViewModelProvider.AndroidViewModelFactory(
                this.getApplication()).create(TodoViewModel.class);


//        AsyncTask.execute(() -> todosWithAlarm = todoViewModel.getTodosWithAlarm(timeNow, timeHrLater, days));
//        todoViewModel.getTodosWithAlarm(timeNow, timeHrLater, days).observe(lifecycleOwner, todos -> {
//            todosWithAlarm = todos;
//        });



        final Notification notification = new Notification.Builder(getApplicationContext(), "notiChannel")
                .setSmallIcon(R.drawable.baseline_alarm_black_24dp)
                .setContentTitle("Daytodo Alarm")
                .setContentText("alarm content text")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();


        todoViewModel.getTodosWithAlarm(timeS, timeE, days).observeForever(todos -> {
            todosWithAlarm = todos;

            if (todosWithAlarm != null ) {
                notificationManager.notify(1, notification);
                stopSelf();
                Toast.makeText(this, "todosWithAlarm exists", Toast.LENGTH_LONG).show();
            } else {
                notificationManager.notify(1, notificationFail);
                stopSelf();
                Toast.makeText(this, "todosWithAlarm is null", Toast.LENGTH_LONG).show();
            }

        });





//        Toast.makeText(this, "Service onCreate called", Toast.LENGTH_LONG).show();
        super.onCreate();
    }

    @Override
    public void onDestroy() {
//        Toast.makeText(this, "Service onDestroy called", Toast.LENGTH_LONG).show();
        super.onDestroy();
    }
}