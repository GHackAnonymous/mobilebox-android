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

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;


public class AcercaDe extends ActionBarActivity {

    // Referencias a los controles
    private TextView etiquetaLicenciaImagenes = null;
    private TextView etiquetaGitHub = null;
    private TextView etiquetaGPL = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acerca_de);

        // Cargar HTML desde un @string y activar los enlaces
        etiquetaLicenciaImagenes = (TextView) findViewById(R.id.etiquetaLicenciaImagenes);
        etiquetaLicenciaImagenes.setText(Html.fromHtml(getString(R.string.texto_licencia_imagenes)));
        etiquetaLicenciaImagenes.setMovementMethod(LinkMovementMethod.getInstance());

        // Cargar HTML desde un @string y activar los enlaces
        etiquetaGitHub = (TextView) findViewById(R.id.etiquetaGitHub);
        etiquetaGitHub.setText(Html.fromHtml(getString(R.string.texto_github)));
        etiquetaGitHub.setMovementMethod(LinkMovementMethod.getInstance());

        // Cargar HTML desde un @string
        etiquetaGPL = (TextView) findViewById(R.id.etiquetaGPL);
        etiquetaGPL.setText(Html.fromHtml(getString(R.string.texto_gpl)));
    }

}
