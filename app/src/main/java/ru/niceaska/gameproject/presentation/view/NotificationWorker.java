package ru.niceaska.gameproject.presentation.view;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import ru.niceaska.gameproject.R;

public class NotificationWorker extends Worker {

    static final String PLAYER_AWAY_TEXT = "longAwayText";
    static final String PLAYER_AWAY_TITLE = "longAwayTitle";
    private final String CHANNEL_ID = "1";
    private final int NOTIFICATION_ID = 1;

    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        String title = getInputData().getString(PLAYER_AWAY_TITLE);
        String message = getInputData().getString(PLAYER_AWAY_TEXT);
        createNotification(title, message);
        return Result.success();
    }

    private void createNotification(String title, String text) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        String channelName = getApplicationContext().getResources().getString(R.string.app_name);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new
                    NotificationChannel(CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }


        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(text)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_email_black_24dp);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}
