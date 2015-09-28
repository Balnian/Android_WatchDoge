package com.example.francis.watchdoge;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.CharacterPickerDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class Interface extends AppCompatActivity {

    private EditText TB_Addr;
    private EditText TB_DPlage;
    private EditText TB_FPlage;
    private EditText TB_Port;
    private ProgressBar PB_Progress;
    private Button BT_Demarrer;
    private Button BT_Suspendre;
    private TextView TV_IP;
    private Thread Scanner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interface);

        TB_Addr = (EditText)findViewById(R.id.TB_Addr);
        TB_DPlage = (EditText)findViewById(R.id.TB_DPlage);
        TB_FPlage = (EditText) findViewById(R.id.TB_FPlage);
        TB_Port = (EditText) findViewById(R.id.TB_Port);
        PB_Progress = (ProgressBar) findViewById(R.id.PB_Progress);
        BT_Demarrer = (Button) findViewById(R.id.BT_Demarrer);
        BT_Suspendre = (Button) findViewById(R.id.BT_Suspendre);
        TV_IP = (TextView) findViewById(R.id.TV_IP);

        BT_Demarrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isIP(TB_Addr.getText().toString())){
                    if(Scanner != null)
                    {
                        Scanner = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                int i = 1+1;

                               // Socket = new Socket
                            }
                        });
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(),"L'adresse de sous-réseau n'est pas valide!!!",Toast.LENGTH_LONG);
                }



            }
        });



    }

    private boolean isIP(String IP){
        Character[] Values = {'1','2','3','4','5','6','7','8','9','0','.'};
        for (int i = 0; i<IP.length();i++){
            if(!Arrays.asList(Values).contains(IP.charAt(i)))
                return false;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_interface, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
