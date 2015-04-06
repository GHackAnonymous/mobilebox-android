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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;


public class MobileBox extends ActionBarActivity {

    // Referencias a los controles
    private EditText campoCaja = null;
    private EditText campoNombre = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_box);

        // Referencias a los controles
        campoCaja = (EditText) findViewById(R.id.campoCaja);
        campoNombre = (EditText) findViewById(R.id.campoNombre);

        // Boton "Conectar"
        findViewById(R.id.botonConectar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String idCaja = campoCaja.getText().toString();
                String nombre = campoNombre.getText().toString();

                // Si hay texto, pasamos a la siguiente actividad
                if (idCaja.length() >= 5 && nombre.length() >= 2) {

                    Intent intent = new Intent(MobileBox.this, YouAreBoxed.class);
                    intent.putExtra("ID_CAJA", idCaja);
                    intent.putExtra("NOMBRE", nombre);

                    startActivity(intent);
                }
            }
        });

        // Ocultar el teclado en pantalla cuando se pulsa fuera de él
        campoCaja.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(view);
                }
            }
        });

        campoNombre.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(view);
                }
            }
        });

    }

    // Este método oculta el teclado en pantalla
    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    // Crear el menú "Acerca de..."
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mobile_box, menu);
        return true;
    }

    // Abrir la actividad "Acerca de..."
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean result = super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.menu_acerca_de).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                startActivity(new Intent(MobileBox.this, AcercaDe.class));
                return true;
            }
        });
        return result;
    }
}
