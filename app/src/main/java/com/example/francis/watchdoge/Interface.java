package com.example.francis.watchdoge;

import android.os.AsyncTask;
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

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.InetSocketAddress;
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
    private String IP;
    private Integer port;
    private Integer IDPlage;
    private Integer IFPlage;
    private Boolean Pause;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
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
        Pause =false;

        BT_Demarrer.setEnabled(true);
        BT_Suspendre.setEnabled(false);
        TV_IP.setText("");
        PB_Progress.setProgress(0);


        BT_Demarrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Pause) {
                    Pause = false;
                    BT_Suspendre.setEnabled(true);
                    BT_Demarrer.setEnabled(false);

                }
                else {
                    if (isIP(TB_Addr.getText().toString())) {
                        try {
                            IDPlage = Integer.parseInt(TB_DPlage.getText().toString());
                        } catch (NumberFormatException e) {
                            IDPlage = null;
                        }
                        if (IDPlage != null && IDPlage <= 254 && IDPlage > 1) {
                            try {
                                IFPlage = Integer.parseInt(TB_FPlage.getText().toString());
                            } catch (NumberFormatException e) {
                                IFPlage = null;
                            }
                            if (IFPlage != null && IFPlage > 4 && IFPlage < 255) {
                                if (IFPlage >= IDPlage) {
                                    try {
                                        port = Integer.parseInt(TB_Port.getText().toString());
                                    } catch (NumberFormatException e) {
                                        port = null;
                                    }
                                    if (port != null && port <= 65535 && port >= 0) {
                                        IP = TB_Addr.getText().toString();
                                        TV_IP.setText("");

                                        BT_Suspendre.setEnabled(true);
                                        BT_Demarrer.setEnabled(false);

                                        PingPong qwe = new PingPong();
                                        qwe.execute();

                                    } else {
                                        if (port == null) {
                                            Toast.makeText(getApplicationContext(), "Le port n'est pas valide", Toast.LENGTH_LONG).show();
                                        } else if (!(port <= 65535)) {
                                            Toast.makeText(getApplicationContext(), "Le port est trop haut", Toast.LENGTH_LONG).show();
                                        } else if (!(port >= 0)) {
                                            Toast.makeText(getApplicationContext(), "Le port est trop bas", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "Le début de la plage d'adresse est plus grand que la fin!!!", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                if (IFPlage == null) {
                                    Toast.makeText(getApplicationContext(), "La fin de la Plage d'adresse n'est pas un nombre valide", Toast.LENGTH_LONG).show();
                                } else if (!(IFPlage > 4)) {
                                    Toast.makeText(getApplicationContext(), "La fin de la Plage d'adresse est trop basse", Toast.LENGTH_LONG).show();
                                } else if (!(IFPlage < 255)) {
                                    Toast.makeText(getApplicationContext(), "La fin de la Plage d'adresse est trop Haute", Toast.LENGTH_LONG).show();
                                }
                            }

                        } else {
                            if (IDPlage == null)
                                Toast.makeText(getApplicationContext(), "Le début de la Plage d'adresse n'est pas un nombre valide", Toast.LENGTH_LONG).show();
                            else if (!(IDPlage <= 254))
                                Toast.makeText(getApplicationContext(), "Le début de la Plage d'adresse est trop haut", Toast.LENGTH_LONG).show();
                            else if (!(IDPlage > 1))
                                Toast.makeText(getApplicationContext(), "Le début de la Plage d'adresse est trop basse", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "L'adresse de sous-réseau n'est pas valide!!!", Toast.LENGTH_LONG).show();

                    }
                }


                }
            }

            );

        BT_Suspendre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pause = true;
                BT_Demarrer.setEnabled(true);
                BT_Suspendre.setEnabled(false);
            }
        });

        }

    private boolean isIP(String IP){
        /*Character[] Values = {'1','2','3','4','5','6','7','8','9','0','.'};
        boolean result = true;
        int pointCount=0;
        //vérifie que l'ip ne commence pas et ne fini pas par un point
        if(IP.startsWith(".")||IP.endsWith(".") ) {
            result = false
        }
        //Vérifie chaque character
        for (int i = 0; i<IP.length();i++){
            if(!(Arrays.asList(Values).contains(IP.charAt(i)))) {
                result = false;
            }
            else if(IP.charAt(i) == '.'){
                pointCount++;
            }
        }
        //Vérifie qu'il y a le bont nombre de point (séparateur)
        if(pointCount!=2) {
            result = false;
        }
        if(! IP.matches("^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?).(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?).(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?).(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$"))
            result=false;

        return result;*/
        
        //Beaucoup plus simple comme sa :)
        return IP.matches("^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?).(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?).(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");
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


    private class PingPong extends AsyncTask<Void,Integer, Void> {
    /*@Override
    protected void onPreExecute() {
        super.onPreExecute();
        Toast.makeText(getApplicationContext(),
                "Début du traitement asynchrone",
                Toast.LENGTH_SHORT).show();
    }*/

    @Override
    protected Void doInBackground(Void... args) {
        int progres;
        /*int debut = Integer.parseInt(args[0]);
        int fin = Integer.parseInt(args[1]);
        int port = Integer.parseInt(args[2]);
        String ip = args[3];*/
        for (progres = IDPlage; progres <= IFPlage ; progres++)
        {
            while (Pause)
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            // this is bullshit calculation :)
            Socket s = new Socket();
            try {
                s.connect(new InetSocketAddress(IP+"."+Integer.toString(progres),port),500);
                publishProgress(progres);
            } catch (IOException e) {
                publishProgress(-1,progres);
            }


            // la méthode publishProgress met à jour l'IUG en
            // invoquant indirectement la méthode onProgressUpdate

        }
        // résultat envoyé à onPostExecute
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... valeurs) {
        super.onProgressUpdate(valeurs);
        if(valeurs[0].equals(-1)) {
            int a = valeurs[1] - IDPlage;
            int b = IFPlage - IDPlage;
            if(b != 0) {
                int c = Math.round((Float.intBitsToFloat(a) / Float.intBitsToFloat(b)) * 100);
                PB_Progress.setProgress(c);
            }
            else{
                PB_Progress.setProgress(100);
            }
        }
        else {
            int a = valeurs[0] - IDPlage;
            int b = IFPlage - IDPlage;
            if(b != 0) {
                int c = Math.round((Float.intBitsToFloat(a) / Float.intBitsToFloat(b)) * 100);
                PB_Progress.setProgress(c);
            }
            else{
                PB_Progress.setProgress(100);
            }
            TV_IP.append(IP+"."+Integer.toString(valeurs[0])+"\n");
        }
        // mise à jour de la barre de progression


    }

    @Override
    protected void onPostExecute(Void resultat) {
        PB_Progress.setProgress(100);
        BT_Demarrer.setEnabled(true);
        BT_Suspendre.setEnabled(false);
    }
}
}
