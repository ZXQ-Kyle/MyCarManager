package com.kyle.mycar;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import com.kyle.mycar.MyUtils.MyConstant;
import com.kyle.mycar.MyUtils.SpUtils;

import java.io.File;
import java.io.IOException;

public class DownloadFinishReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //下载完成的广播
        long completeDownloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
        long id = SpUtils.getLong(context, MyConstant.DOWNLOAD_ID);
        if (completeDownloadId == id) {
            String path = SpUtils.getString(context, MyConstant.DOWNLOAD_PATH);
            if (!TextUtils.isEmpty(path)) {
                setPermission(path);//提升读写权限,否则可能出现解析异常
                install(context, path);
            }
        }
    }

    /**
     * 提升读写权限
     *
     * @param filePath 文件路径
     * @return
     * @throws IOException
     */
    public static void setPermission(String filePath) {
        String command = "chmod " + "777" + " " + filePath;
        Runtime runtime = Runtime.getRuntime();
        try {
            runtime.exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //普通安装
    private static void install(Context context, String apkPath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //版本在7.0以上是不能直接通过uri访问的
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            File file = (new File(apkPath));
            // 由于没有在Activity环境下启动Activity,设置下面的标签
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
            Uri apkUri = FileProvider.getUriForFile(context, "com.kyle.mycar", file);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(new File(apkPath)), "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }
}

