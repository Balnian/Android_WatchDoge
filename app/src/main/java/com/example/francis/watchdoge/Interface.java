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

        //Pars ou repars la tâche si toutes les données son conforme
        BT_Demarrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Vérifie si il est en pause ... si oui redémare la tâche
                if(Pause) {
                    Pause = false;
                    BT_Suspendre.setEnabled(true);
                    BT_Demarrer.setEnabled(false);

                }
                else {      //si n'est pas en pause on fait les vérification sur le data et on part la tâche
                    if (isIP(TB_Addr.getText().toString())) { // Vérifie si le sous-réseau est valide
                        try {
                            IDPlage = Integer.parseInt(TB_DPlage.getText().toString());
                        } catch (NumberFormatException e) {
                            IDPlage = null;
                        }
                        if (IDPlage != null && IDPlage <= 254 && IDPlage > 1) {  // Vérifie si le Début de la plage entrer est correcte
                            try {
                                IFPlage = Integer.parseInt(TB_FPlage.getText().toString());
                            } catch (NumberFormatException e) {
                                IFPlage = null;
                            }
                            if (IFPlage != null && IFPlage > 4 && IFPlage < 255) {  // Vérifie si la Fin de la plage entrer est correcte
                                if (IFPlage >= IDPlage) {  //Vérifie que le début de la plage est plus petit que la fin
                                    try {
                                        port = Integer.parseInt(TB_Port.getText().toString());
                                    } catch (NumberFormatException e) {
                                        port = null;
                                    }
                                    if (port != null && port <= 65535 && port >= 0) {  // Vérifie si le Port entrer est correcte
                                        IP = TB_Addr.getText().toString();
                                        TV_IP.setText("");

                                        BT_Suspendre.setEnabled(true);
                                        BT_Demarrer.setEnabled(false);

                                        PingPong qwe = new PingPong();
                                        qwe.execute();

                                    } else {   // si le port entrer n'est pas correct on affiche un message personnaliser selon l'erreur
                                        if (port == null) {
                                            Toast.makeText(getApplicationContext(), "Le port n'est pas valide", Toast.LENGTH_LONG).show();
                                        } else if (!(port <= 65535)) {
                                            Toast.makeText(getApplicationContext(), "Le port est trop haut", Toast.LENGTH_LONG).show();
                                        } else if (!(port >= 0)) {
                                            Toast.makeText(getApplicationContext(), "Le port est trop bas", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                } else {  // On informe ue le début de la plage entrer n,est pas plus petit que la fin
                                    Toast.makeText(getApplicationContext(), "Le début de la plage d'adresse est plus grand que la fin!!!", Toast.LENGTH_LONG).show();
                                }
                            } else {   // si la fin de la plage entrer n'est pas correct on affiche un message personnaliser selon l'erreur
                                if (IFPlage == null) {
                                    Toast.makeText(getApplicationContext(), "La fin de la Plage d'adresse n'est pas un nombre valide", Toast.LENGTH_LONG).show();
                                } else if (!(IFPlage > 4)) {
                                    Toast.makeText(getApplicationContext(), "La fin de la Plage d'adresse est trop basse", Toast.LENGTH_LONG).show();
                                } else if (!(IFPlage < 255)) {
                                    Toast.makeText(getApplicationContext(), "La fin de la Plage d'adresse est trop Haute", Toast.LENGTH_LONG).show();
                                }
                            }

                        } else {  // si le début de la plage entrer n'est pas correct on affiche un message personnaliser selon l'erreur
                            if (IDPlage == null)
                                Toast.makeText(getApplicationContext(), "Le début de la Plage d'adresse n'est pas un nombre valide", Toast.LENGTH_LONG).show();
                            else if (!(IDPlage <= 254))
                                Toast.makeText(getApplicationContext(), "Le début de la Plage d'adresse est trop haut", Toast.LENGTH_LONG).show();
                            else if (!(IDPlage > 1))
                                Toast.makeText(getApplicationContext(), "Le début de la Plage d'adresse est trop basse", Toast.LENGTH_LONG).show();
                        }
                    } else {  // si l'adresse de sous-réseau n'est pas valide on informe l'usager
                        Toast.makeText(getApplicationContext(), "L'adresse de sous-réseau n'est pas valide!!!", Toast.LENGTH_LONG).show();

                    }
                }


                }
            }

            );

        //Suspend la tâche asynchrone
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


    @Override
    protected Void doInBackground(Void... args) {
        int progres;

        for (progres = IDPlage; progres <= IFPlage ; progres++)
        {
            while (Pause)  // si en pause on sleep 100 puis on reverifie
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            // on essaie de se connecter si sa réussie pas on met -1 dans le progress suivi de la valeur que l'on vien dessayer si non ont met seulement la valeur
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

        // Si le premier nombre est -1 on vérifie le deuxième et on update la progresse bar sans rajouter l'adresse au textview
        if(valeurs[0].equals(-1)) {
            int a = valeurs[1] - IDPlage;
            int b = IFPlage - IDPlage;
            if(b != 0) { // si b = 0 ces une division par 0 donc pas bon mais sa veux aussi dire que on avait seulement 1 élément à essayer donc on peux juste mettre 100% "and call it a day"
                int c = Math.round((Float.intBitsToFloat(a) / Float.intBitsToFloat(b)) * 100);
                PB_Progress.setProgress(c);
            }
            else{ //ont met le 100% ici quand ces 0 :)
                PB_Progress.setProgress(100);
            }
        }
        else {
            int a = valeurs[0] - IDPlage;
            int b = IFPlage - IDPlage;
            if(b != 0) { // si b = 0 ces une division par 0 donc pas bon mais sa veux aussi dire que on avait seulement 1 élément à essayer donc on peux juste mettre 100% "and call it a day"
                int c = Math.round((Float.intBitsToFloat(a) / Float.intBitsToFloat(b)) * 100);
                PB_Progress.setProgress(c);
            }
            else{ //ont met le 100% ici quand ces 0 :)
                PB_Progress.setProgress(100);
            }
            TV_IP.append(IP+"."+Integer.toString(valeurs[0])+"\n");
        }
        // mise à jour de la barre de progression


    }

    @Override
    protected void onPostExecute(Void resultat) {
        //en finissant on met le progress a 100% au cas où il y aillent des erreurs d'arrondie
        PB_Progress.setProgress(100);
        //lorsque le processu fini on met les boutons dans un état pour pouvoir relancer une nouvelle tache avec de nouveaux paramêtre
        BT_Demarrer.setEnabled(true);
        BT_Suspendre.setEnabled(false);
    }
}
}
