package nz.ac.kingsfourze.smstransfer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static android.content.Context.MODE_PRIVATE;

public class SMSReceiver extends BroadcastReceiver {
    private String host, port, user, password, dbname, dbtype, url, userToken, phoneNum, smsText;

    public void connectTest(){
        try{
            Class.forName("org.mariadb.jdbc.Driver");
            Log.v("DB","Driver Load Success");
        }catch (ClassNotFoundException e){
            Log.e("DB","Driver Load Error");
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Connection conn = DriverManager.getConnection(url);
                    conn.close();
                    Log.v("DB","SQL Connect Success");
                }catch (SQLException e){
                    Log.e("DB","SQL Connect Error");
                }
            }
        }).start();
    }

    Runnable uploadSMS = new Runnable() {
        @Override
        public void run() {
            try{
                Connection conn = DriverManager.getConnection(url);
                String data = "FROM: " + phoneNum + "\n" + smsText;
                String sql = "INSERT INTO sms (userToken, smsText) VALUES ('" + userToken + "','" + data + "')";
                Statement st = conn.createStatement();
                st.executeQuery(sql);
                st.close();
                conn.close();
            }catch (SQLException e){
                Log.e("DB","SQL Connect Error");
            }
        }
    };

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences setting = context.getSharedPreferences("Setting", MODE_PRIVATE);
        Boolean runStatus = setting.getBoolean("runStatus",false);
        String[] dbTypeList = {"mariadb","mysql"};
        dbtype = dbTypeList[setting.getInt("dbType", 0)];
        host = setting.getString("host","localhost");
        port = setting.getString("port","3306");
        dbname = setting.getString("dbName","PSMST");
        user = setting.getString("user","root");
        password = setting.getString("password","PSMSTPassword");
        userToken = setting.getString("userToken","");

        url = "jdbc:"+ dbtype +"://" + host + ":" + port + "/" + dbname + "?user="+ user + "&password=" + password;
        if (runStatus == false)
            return;

        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
            Bundle bundle = intent.getExtras();
            SmsMessage[] messages = null;
            String strMessage = "";
            connectTest();

            if (bundle != null){
                Object[] pdus = (Object[]) bundle.get("pdus");
                if (pdus.length == 0)
                    return;

                messages = new SmsMessage[pdus.length];
                StringBuilder sb = new StringBuilder();
                smsText = "";
                for (int i = 0; i < pdus.length; i++){
                    String format = bundle.getString("format");
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i],format);
                    phoneNum = messages[i].getOriginatingAddress();
                    smsText += messages[i].getMessageBody();
                }
                new Thread(uploadSMS).start();
            }
        }
    }
}