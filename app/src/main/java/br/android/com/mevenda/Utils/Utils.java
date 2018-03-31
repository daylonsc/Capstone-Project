package br.android.com.mevenda.Utils;

import android.util.Base64;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by daylo on 17/03/2018.
 */

public class Utils {
    private final static String CHAVE_PRIMARIA_TABELA = "id";
    private final static int VALOR_INICIAL_ID = 1;
    private static final SimpleDateFormat sdf = (SimpleDateFormat)SimpleDateFormat.getInstance();
    private static String defaultDatePattern = "dd/MM/yyyy";


    public static String converterDoubleToMonetario(double valor){
        String valorString = "0,00";
        if(valor > 0){
            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
            symbols.setGroupingSeparator('.');
            symbols.setDecimalSeparator(',');
            DecimalFormat decimalFormat = new DecimalFormat("#,###.00", symbols);
            valorString = decimalFormat.format(valor);
        }
        return valorString;
    }

    public static synchronized String format(Date date, String pattern) {
        sdf.applyPattern(pattern == null ? defaultDatePattern : pattern);
        return sdf.format(date);
    }

    public static String codificarBase64(String texto) {
        return Base64.encodeToString(texto.getBytes(), Base64.DEFAULT).replaceAll("(\\n|\\r)", "");
    }

    public static String decodificarBase64(String textoCodificado) {
        return new String(Base64.decode(textoCodificado, Base64.DEFAULT));
    }
}
