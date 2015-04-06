/*
 * MobileBox
 * Copyright (C) 2015 Ion Jaureguialzo Sarasola
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package com.jaureguialzo.mobilebox;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;

import java.io.IOException;


public class YouAreBoxed extends ActionBarActivity {

    // Referencias a los controles
    private TextView etiquetaCaja = null;
    private TextView etiquetaNombre = null;
    private TextView etiquetaAyuda = null;

    // Parámetros que llegan en el Intent
    private String idCaja = null;
    private String nombre = null;

    // ANDROID_ID de éste móvil
    private String androidId = null;

    // Objetos alojados en Parse.com
    private ParseObject caja = null;
    private ParseObject telefono = null;

    // Referencias para controlar la alarma
    private MediaPlayer player = null;
    private boolean playerPreparado = false;
    private int volumenAlarmaOriginal = 0;
    private AudioManager audioManager = null;

    // Para saber si hemos pulsado la tecla de retorno del móvil
    private boolean teclaBackPulsada = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_you_are_boxed);

        // Referencias a los controles
        etiquetaCaja = (TextView) findViewById(R.id.etiquetaCaja);
        etiquetaNombre = (TextView) findViewById(R.id.etiquetaNombre);
        etiquetaAyuda = (TextView) findViewById(R.id.etiquetaAyuda);

        // Extraer los parámetros desde el Intent
        Intent intent = getIntent();
        idCaja = intent.getStringExtra("ID_CAJA");
        nombre = intent.getStringExtra("NOMBRE");

        // Obtener el ANDROID_ID
        androidId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        // Mostrar los datos en pantalla
        etiquetaCaja.setText(idCaja);
        etiquetaNombre.setText(nombre);
        etiquetaAyuda.setText(androidId);

        // Obtener el tono de alarma predeterminado del sistema
        Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        // Si no hay tono de alarma, usar el de notificaciones
        if (alert == null) {
            alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            // Si sigue sin haberlo, usar el tono de llamada
            if (alert == null) {
                alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            }
        }

        // Obtener el volumen actual y guardarlo
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        volumenAlarmaOriginal = audioManager.getStreamVolume(AudioManager.STREAM_ALARM);

        // Dejar preparado el MediaPlayer
        if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
            player = new MediaPlayer();
            try {
                player.setDataSource(this, alert);
                player.setAudioStreamType(AudioManager.STREAM_ALARM);
                player.setLooping(true);
                player.prepare();
                playerPreparado = true;
            } catch (IOException e) {
                Log.e("MobileBox", e.getStackTrace().toString());
            }
        }

        // Lanzamos una tarea en segundo plano para no bloquear el interfaz de usuario
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {

                // Buscar el ID del teléfono y cargar el objeto teléfono, si existe
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Telefono");
                query.whereEqualTo("androidId", androidId);
                query.getFirstInBackground(new GetCallback<ParseObject>() {
                    public void done(ParseObject object, ParseException e) {
                        // Si existe lo copiamos en teléfono. Si no, creamos uno nuevo.
                        if (object != null) {
                            telefono = object;
                        } else {
                            telefono = new ParseObject("Telefono");
                        }

                        // Rellenamos el objeto con los datos actuales
                        telefono.put("androidId", androidId);
                        telefono.put("nombre", nombre);
                        telefono.put("conectado", true);

                        // Forzamos a que se guarde, sin importar la espera
                        try {
                            telefono.save();
                        } catch (ParseException ex) {
                            Log.e("Parse", ex.getMessage());
                        }

                        // Conectar a Parse y registrarse en el aula
                        ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Caja");
                        query2.whereEqualTo("caja", idCaja);
                        query2.getFirstInBackground(new GetCallback<ParseObject>() {
                            public void done(ParseObject object, ParseException e) {
                                if (object != null) {
                                    caja = object;

                                    // Añadimos el teléfono a la lista de esta caja
                                    ParseRelation<ParseObject> relation = caja.getRelation("telefonos");
                                    relation.add(telefono);

                                    caja.saveInBackground();

                                    Log.d("Parse", "Alumno " + androidId + " registrado en la caja " + idCaja + " correctamente");
                                } else {
                                    Log.e("Parse", "La caja " + idCaja + " no existe");
                                    etiquetaAyuda.setText(getResources().getString(R.string.error_caja_no_existe));
                                }
                            }
                        });
                    }
                });

                return null;
            }
        }.execute();
    }

    // Nos permite controlar si el usuario ha pulsado o no la tecla retorno del móvil
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        teclaBackPulsada = true;
    }

    /*
        Controlamos 3 eventos del ciclo de vida de la actividad.

        1. onStop(): Si el usuario abandona la ventana, activamos la alarma
        2. onStart(): Cuando retorna, la desactivamos
        3. onDestroy(): Si la aplicación se está cerrando, eliminamos el teléfono de la caja
    */

    // Gestión del evento onStop()
    @Override
    protected void onStop() {
        super.onStop();

        Log.d("MobileBox", "onStop()");

        // Comprobamos si la pantalla está apagada. En ese caso no pitamos
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        boolean pantallaApagada = !pm.isScreenOn();
        //boolean pantallaApagada = !pm.isInteractive();  // API 16 o superior

        if (pantallaApagada)
            Log.d("MobileBox", "Pantalla apagada");

        if (telefono != null && !pantallaApagada) {

            // Pitar
            if (player != null && !teclaBackPulsada) {
                // Si ya ha sonado antes, hay que volver al estado "prepared"
                // http://developer.android.com/reference/android/media/MediaPlayer.html#StateDiagram
                if (!playerPreparado) {
                    try {
                        player.prepare();
                        player.seekTo(0);
                        playerPreparado = true;
                    } catch (IOException e) {
                        Log.e("MobileBox", e.getStackTrace().toString());
                    }
                }

                // Subimos el volumen al máximo
                audioManager.setStreamVolume(
                        AudioManager.STREAM_ALARM,
                        audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM),
                        0);

                player.start();
                Log.d("MobileBox", "Alarma iniciada");
            }

            // Actualizamos el estado del teléfono
            telefono.put("conectado", false);
            telefono.saveInBackground();
        }
    }

    // Gestión del evento onStart()
    @Override
    protected void onStart() {
        super.onStart();

        Log.d("MobileBox", "onStart()");

        if (telefono != null) {
            // Actualizamos el estado
            telefono.put("conectado", true);
            telefono.saveInBackground();

            // Dejar de pitar
            if (player != null) {
                Log.d("MobileBox", "Alarma detenida");

                player.stop();
                playerPreparado = false;

                // Dejamos el volumen como estaba
                audioManager.setStreamVolume(
                        AudioManager.STREAM_ALARM,
                        volumenAlarmaOriginal,
                        0);
            }
        }
    }

    // Gestión del evento onDestroy()
    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.d("MobileBox", "onDestroy()");

        // Dejar de pitar
        if (player != null) {
            Log.d("MobileBox", "Alarma detenida");

            player.stop();
            playerPreparado = false;

            // Dejamos el volumen como estaba
            audioManager.setStreamVolume(
                    AudioManager.STREAM_ALARM,
                    volumenAlarmaOriginal,
                    0);
        }

        // Eliminamos el teléfono de la caja
        if (caja != null && telefono != null) {
            ParseRelation<ParseObject> relation = caja.getRelation("telefonos");
            relation.remove(telefono);
            caja.saveInBackground();
        }
    }
}
