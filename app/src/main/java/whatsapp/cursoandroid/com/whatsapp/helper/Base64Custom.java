package whatsapp.cursoandroid.com.whatsapp.helper;

import android.util.Base64;

/**
 * Created by gabriel on 7/18/17.
 */

public class Base64Custom {

    public static String encoderBase64(String texto) {
        return Base64.encodeToString(texto.getBytes(), Base64.DEFAULT).replaceAll("(\\n|\\r)", "");
    }

    public static String decodeBase64(String textoCodificado) {
        return new String (Base64.decode(textoCodificado.getBytes(), Base64.DEFAULT));
    }
}
