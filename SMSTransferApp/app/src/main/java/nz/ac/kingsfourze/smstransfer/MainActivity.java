package nz.ac.kingsfourze.smstransfer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public EditText editToken, editHost, editPort, editUser, editPassword, editDBname;
    public Button btnStart, btnStop, btnSave;
    public Spinner spinnerDatabaseType;
    public String[] dbType = {"MariaDB","MySQL"};

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(getApplicationContext(),"未提供SMS接收權限，請開啟後再使用本App", Toast.LENGTH_SHORT).show();
                finish();
            } else
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get Permission
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[] { Manifest.permission.RECEIVE_SMS }, 1);
        }

        SharedPreferences setting = getSharedPreferences("Setting", MODE_PRIVATE);
        setupUI();

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = spinnerDatabaseType.getSelectedItemPosition();
                saveSetting(setting,true);
                btnStart.setVisibility(View.INVISIBLE);
                btnStop.setVisibility(View.VISIBLE);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSetting(setting,false);
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setting.edit().putBoolean("runStatus", false).commit();
                btnStop.setVisibility(View.INVISIBLE);
                btnStart.setVisibility(View.VISIBLE);
            }
        });
    }

    private void saveSetting(SharedPreferences setting, Boolean withRunStatus){
        int position = spinnerDatabaseType.getSelectedItemPosition();
        if (editPort.getText().toString() == ""){
            editPort.setText("3306", TextView.BufferType.EDITABLE);
        }
        setting.edit()
                .putString("host", editHost.getText().toString())
                .putString("port",editPort.getText().toString())
                .putString("user", editUser.getText().toString())
                .putString("password", editPassword.getText().toString())
                .putString("dbName", editDBname.getText().toString())
                .putString("userToken", editToken.getText().toString())
                .putInt("dbType", position).commit();
        if (withRunStatus){
            setting.edit().putBoolean("runStatus", true).commit();
        }
    }

    private void setupUI(){
        //Setup View
        editToken = findViewById(R.id.editToken);
        editHost = findViewById(R.id.editHost);
        editPort = findViewById(R.id.editPort);
        editUser = findViewById(R.id.editUser);
        editPassword = findViewById(R.id.editPassword);
        editDBname = findViewById(R.id.editDBname);
        btnStart = findViewById(R.id.btnStart);
        btnStop = findViewById(R.id.btnStop);
        btnSave = findViewById(R.id.btnSave);
        spinnerDatabaseType = findViewById(R.id.spinnerDataBaseType);

        //Spinner Setup
        ArrayAdapter<String> dbTpyeList = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, dbType);
        spinnerDatabaseType.setAdapter(dbTpyeList);

        //Read Setting
        SharedPreferences setting = getSharedPreferences("Setting", MODE_PRIVATE);
        Integer dbtype = setting.getInt("dbType",0);
        String host = setting.getString("host","localhost");
        String port = setting.getString("port","3306");
        String dbname = setting.getString("dbName","PSMST");
        String user = setting.getString("user","root");
        String password = setting.getString("password","PSMSTPassword");
        String userToken = setting.getString("userToken","");
        Boolean runStatus = setting.getBoolean("runStatus",false);

        //check userToken and setup editToken
        spinnerDatabaseType.setSelection(dbtype);
        editHost.setText(host, TextView.BufferType.EDITABLE);
        editPort.setText(port, TextView.BufferType.EDITABLE);
        editDBname.setText(dbname, TextView.BufferType.EDITABLE);
        editUser.setText(user, TextView.BufferType.EDITABLE);
        editPassword.setText(password, TextView.BufferType.EDITABLE);
        if (userToken != ""){
            editToken.setText(userToken, TextView.BufferType.EDITABLE);
        }
        //check runStatus and setup btn
        if (runStatus == false){
            btnStop.setVisibility(View.INVISIBLE);
        }else{
            btnStart.setVisibility(View.INVISIBLE);
        }
    }
}