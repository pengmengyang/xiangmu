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
	/* �ϴ��ļ���Server�ķ��� */
    public void uploadFile(String ur,String file,String newFileName) {
            String end = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            try {
                    URL url = new URL(ur);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    /* ����Input��Output����ʹ��Cache */
                    con.setDoInput(true);
                    con.setDoOutput(true);
                    con.setUseCaches(false);
                    /* ���ô��͵�method=POST */
                    con.setRequestMethod("POST");
                    /* setRequestProperty */
                    con.setRequestProperty("Connection", "Keep-Alive");
                    con.setRequestProperty("Charset", "UTF-8");
                    con.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                    /* ����DataOutputStream */
                    DataOutputStream ds = new DataOutputStream(con.getOutputStream());
                    ds.writeBytes(twoHyphens + boundary + end);
                    ds.writeBytes("Content-Disposition: form-data; " + "name=\"file1\";filename=\"" + newFileName + "\"" + end);
                    ds.writeBytes(end);
                    /* ȡ���ļ���FileInputStream */
                    FileInputStream fStream = new FileInputStream(file);
                    /* ����ÿ��д��1024bytes */
                    int bufferSize = 1024;
                    byte[] buffer = new byte[bufferSize];
                    int length = -1;
                    /* ���ļ���ȡ������������ */
                    while ((length = fStream.read(buffer)) != -1) {
                            /* ������д��DataOutputStream�� */
                            ds.write(buffer, 0, length);
                    }
                    ds.writeBytes(end);
                    ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
                    /* close streams */
                    fStream.close();
                    ds.flush();
                    /* ȡ��Response���� */
                    InputStream is = con.getInputStream();
                    int ch;
                    StringBuffer b = new StringBuffer();
                    while ((ch = is.read()) != -1) {
                            b.append((char) ch);
                    }
                    /* ��Response��ʾ��Dialog */
//                    showDialog("�ϴ��ɹ�" );
                    Toast.makeText(context, "�ϴ��ɹ�",Toast.LENGTH_SHORT).show();
                    /* �ر�DataOutputStream */
                    ds.close();
            } catch (Exception e) {
                    showDialog("�ϴ�ʧ��" + e);
            }
    }

    /* ��ʾDialog��method */
    private void showDialog(String mess) {
            new AlertDialog.Builder(context).setTitle("Message").setMessage(mess)
                            .setNegativeButton("ȷ��", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                            }).show();
    }
}