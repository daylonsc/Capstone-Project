package br.android.com.mevenda.Utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.Realm;

/**
 * Created by daylo on 17/03/2018.
 */

public class Utils {
    private final static String CHAVE_PRIMARIA_TABELA = "id";
    private final static int VALOR_INICIAL_ID = 1;
    private static final SimpleDateFormat sdf = (SimpleDateFormat)SimpleDateFormat.getInstance();
    private static String defaultDatePattern = "dd/MM/yyyy";

    public static int getNextId( Class classe){
        Realm realm = Realm.getDefaultInstance();
        int prodximoId = VALOR_INICIAL_ID;
        if(realm.where(classe).max(CHAVE_PRIMARIA_TABELA) != null)
            prodximoId = realm.where(classe).max(CHAVE_PRIMARIA_TABELA).intValue() + VALOR_INICIAL_ID;

        return prodximoId;
    }

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
}
