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

import android.app.Application;

import com.parse.Parse;

/**
 * Created by widemos on 1/4/15.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        /*
            Activar Parse con las claves de aplicación y cliente

            Para que la aplicación funcione, hay que crear el fichero res/values/parse.xml y
            rellenar el valor de las dos claves con los de nuestra aplicación de https://parse.com

            Plantilla del fichero parse.xml:

            <?xml version="1.0" encoding="utf-8"?>
            <resources>
                <string translatable="false" name="PARSE_APPLICATION_ID"></string>
                <string translatable="false" name="PARSE_CLIENT_ID"></string>
            </resources>
        */

        // Datastore local (en el teléfono) de Parse
        Parse.enableLocalDatastore(this);

        // Clave de la aplicación para que el servidor permita el acceso
        Parse.initialize(this, getString(R.string.PARSE_APPLICATION_ID), getString(R.string.PARSE_CLIENT_ID));
    }
}
