package com.kyle.mycar;

import android.app.DownloadManager;
import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;

import com.kyle.mycar.MyUtils.MyConstant;
import com.kyle.mycar.MyUtils.SpUtils;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.net.URI;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class DownloadService extends IntentService {

    private static final String APK_NAME = "MyCarManager.apk";
//    private static final String APK_URL = "http://fir.im/carM";
    private static final String APK_URL = "https://raw.githubusercontent.com/ZhangXiaoQing-Kyle/MyCar/fun_chart/app/app-debug.apk";

    public DownloadService() {
        super("DownloadService");
    }


    public static void startDownload(Context context) {
        Intent intent = new Intent(context, DownloadService.class);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            handleActionFoo();
        }
    }

    private void handleActionFoo() {
        //删除原有的APK
        File file = clearApk(this, APK_NAME);
        //使用DownLoadManager来下载
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(APK_URL));
        //将文件下载到自己的Download文件夹下,必须是External的
        //这是DownloadManager的限制
        request.setDestinationUri(Uri.fromFile(file));

        //添加请求 开始下载
        DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        long downloadId = downloadManager.enqueue(request);
        Logger.d("DownloadBinder"+file.getAbsolutePath());

        SpUtils.putLong(this, MyConstant.DOWNLOAD_ID,downloadId);
        SpUtils.putSring(this, MyConstant.DOWNLOAD_PATH,file.getAbsolutePath());

    }

    /**
     * 获取进度信息
     *
     * @param downloadId 要获取下载的id
     * @return 进度信息 max-100
     */
//    public int getProgress(long downloadId) {
//        //查询进度
//        DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadId);
//        Cursor cursor = null;
//        int progress = 0;
//        try {
//            cursor = mDownloadManager.query(query);//获得游标
//            if (cursor != null && cursor.moveToFirst()) {
//                //当前的下载量
//                int downloadSoFar = cursor.getInt(cursor.getColumnIndex(DownloadManager
//                        .COLUMN_BYTES_DOWNLOADED_SO_FAR));
//                //文件总大小
//                int totalBytes = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
//
//                progress = (int) (downloadSoFar * 1.0f / totalBytes * 100);
//            }
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//        }
//
//        return progress;
//    }

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
