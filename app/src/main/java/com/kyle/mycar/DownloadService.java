package com.kyle.mycar;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class DownloadService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_DOWNLOAD = "com.kyle.mycar.action.FOO";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.kyle.mycar.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.kyle.mycar.extra.PARAM2";

    public DownloadService() {
        super("DownloadService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startDownload(Context context, String param1, String param2) {
        Intent intent = new Intent(context, DownloadService.class);
        intent.setAction(ACTION_DOWNLOAD);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String param1 = intent.getStringExtra(EXTRA_PARAM1);
            final String param2 = intent.getStringExtra(EXTRA_PARAM2);
            handleActionFoo(param1, param2);

        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {


    }

    /**
     * 删除之前的apk
     *
     * @param apkName apk名字
     * @return
     */
    public static File clearApk(Context context, String apkName) {
        File apkFile = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), apkName);
        if (apkFile.exists()) {
            apkFile.delete();
        }
        return apkFile;
    }

}
