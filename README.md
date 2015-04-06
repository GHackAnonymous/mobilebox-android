# MobileBox

Aplicación experimental para controlar el uso del móvil en clase.

MobileBox permite crear cajas virtuales en las que los alumnos dejan el móvil mientras están en clase. Una vez registrados en la caja, deben mantener abierta la aplicación. Si cambian a otra, sonará la alarma (hasta que vuelvan a la aplicación) y se registrará en la aplicación del profesor.

Se compone de dos aplicaciones separadas:

- Profesor: Permite crear cajas y controlar qué móviles hay en ellas.

  Descarga en la [Play Store](https://play.google.com/store/apps/details?id=com.jaureguialzo.mobileboxprofesor) y repositorio de [GitHub](https://github.com/ijaureguialzo/mobilebox-profesor-android).

- Alumno: Se conecta a una caja y debe permanecer en ella hasta que no se le indique lo contrario.

    Descarga en la [Play Store](https://play.google.com/store/apps/details?id=com.jaureguialzo.mobilebox) y repositorio de [GitHub](https://github.com/ijaureguialzo/mobilebox-android).

Se admiten sugerencias, críticas y todo tipo de ideas que ayuden a mejorarla.

Esta aplicación es un experimento y no se ofrece garantía alguna de su funcionamiento :)

## Notas sobre el código fuente
Para que la aplicación funcione, hay que crear el fichero `res/values/parse.xml` y rellenar el valor de las dos claves con los de nuestra aplicación de [https://parse.com](https://parse.com).

Plantilla del fichero parse.xml:

    <?xml version="1.0" encoding="utf-8"?>
    <resources>
        <string translatable="false" name="PARSE_APPLICATION_ID"></string>
        <string translatable="false" name="PARSE_CLIENT_ID"></string>
    </resources>
