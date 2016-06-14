package com.example.expressscanningsystem.tools;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

public class UploadUtil {
	private Context context;
	public UploadUtil(Context context) {
		this.context=context;
	}
	/* 上传文件至Server的方法 */
    public void uploadFile(String ur,String file,String newFileName) {
            String end = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            try {
                    URL url = new URL(ur);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    /* 允许Input、Output，不使用Cache */
                    con.setDoInput(true);
                    con.setDoOutput(true);
                    con.setUseCaches(false);
                    /* 设置传送的method=POST */
                    con.setRequestMethod("POST");
                    /* setRequestProperty */
                    con.setRequestProperty("Connection", "Keep-Alive");
                    con.setRequestProperty("Charset", "UTF-8");
                    con.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                    /* 设置DataOutputStream */
                    DataOutputStream ds = new DataOutputStream(con.getOutputStream());
                    ds.writeBytes(twoHyphens + boundary + end);
                    ds.writeBytes("Content-Disposition: form-data; " + "name=\"file1\";filename=\"" + newFileName + "\"" + end);
                    ds.writeBytes(end);
                    /* 取得文件的FileInputStream */
                    FileInputStream fStream = new FileInputStream(file);
                    /* 设置每次写入1024bytes */
                    int bufferSize = 1024;
                    byte[] buffer = new byte[bufferSize];
                    int length = -1;
                    /* 从文件读取数据至缓冲区 */
                    while ((length = fStream.read(buffer)) != -1) {
                            /* 将资料写入DataOutputStream中 */
                            ds.write(buffer, 0, length);
                    }
                    ds.writeBytes(end);
                    ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
                    /* close streams */
                    fStream.close();
                    ds.flush();
                    /* 取得Response内容 */
                    InputStream is = con.getInputStream();
                    int ch;
                    StringBuffer b = new StringBuffer();
                    while ((ch = is.read()) != -1) {
                            b.append((char) ch);
                    }
                    /* 将Response显示于Dialog */
//                    showDialog("上传成功" );
                    Toast.makeText(context, "上传成功",Toast.LENGTH_SHORT).show();
                    /* 关闭DataOutputStream */
                    ds.close();
            } catch (Exception e) {
                    showDialog("上传失败" + e);
            }
    }

    /* 显示Dialog的method */
    private void showDialog(String mess) {
            new AlertDialog.Builder(context).setTitle("Message").setMessage(mess)
                            .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                            }).show();
    }
}