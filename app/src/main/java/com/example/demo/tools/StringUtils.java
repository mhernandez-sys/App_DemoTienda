package com.example.demo.tools;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StringUtils {

    public static String replaceUrlWithPlus(String url) {
        // 1. Manejar caracteres especiales
        // 2. Elimine la vista desordenada del explorador de archivos causada por el nombre del sufijo(Especialmente las imágenes se procesan de esta manera; de lo contrario, algunas bases de datos de teléfonos móviles son todas nuestras imágenes almacenadas en caché.
        if (url != null) {
            return url.replaceAll("http://(.)*?/", "")
                    .replaceAll("[.:/,%?&=]", "+").replaceAll("[+]+", "+");
        }
        return null;
    }

    /**
     * Verificar si la IP es legal
     *
     * @param text dirección IP
     * @return verificar mensaje
     */
    public static Boolean isIP(String text) {
        if (text != null && !text.isEmpty()) {
            // Definir expresión regular
            String regex = "^((25[0-5])|(2[0-4]\\d)|(1\\d\\d)|([1-9]\\d)|\\d)(\\.((25[0-5])|(2[0-4]\\d)|(1\\d\\d)|([1-9]\\d)|\\d)){3}$";
            // Determinar si la dirección IP coincide con una expresión regular
            if (text.matches(regex)) {
                // Devolver información del juicio
                return true;
            } else {
                // Devolver información del juicio
                return false;
            }
        }
        // Devolver información del juicio
        return false;
    }

    /**
     * Verificar si el nombre de dominio es legal
     *
     * @param text nombre de dominio
     * @return verificar mensaje
     */
    public static Boolean isDomain(String text) {
        if (text != null && !text.isEmpty()) {
            // Definir expresión regular
            String regex = "^([a-zA-Z0-9\\.\\-]+(\\:[a-zA-Z0-9\\.&amp;%\\$\\-]+)*@)*((25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])|localhost|([a-zA-Z0-9\\-]+\\.)*[a-zA-Z0-9\\-]+\\.(com|edu|gov|int|mil|net|org|biz|arpa|info|name|pro|aero|coop|museum|[a-zA-Z]{2}))$";
            // Determinar si el nombre de dominio coincide con la expresión regular
            if (text.matches(regex)) {
                // Devolver información del juicio
                return true;
            } else {
                // Devolver información del juicio
                return false;
            }
        }
        // Devolver información del juicio
        return false;
    }

    public static boolean isEmpty(CharSequence cs) {

        return cs == null || cs.length() == 0;

    }

    public static boolean isNotEmpty(CharSequence cs) {
        return !StringUtils.isEmpty(cs);
    }

    public static String trim(String str) {

        return str == null ? null : str.trim();

    }

    /**
     * Convertir cadena a número entero
     *
     * @param str
     * @param defValue
     * @return
     */
    public static int toInt(String str, int defValue) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
        }
        return defValue;
    }

    /**
     * Objeto a entero
     *
     * @param obj
     * @return Devolución de excepción de conversión 0
     */
    public static int toInt(Object obj) {
        if (obj == null)
            return 0;
        return toInt(obj.toString(), 0);
    }

    /**
     * Objeto a entero
     *
     * @param obj
     * @return Conversion exception return 0
     */
    public static long toLong(String obj) {
        try {
            return Long.parseLong(obj);
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * Convertir carácter a double
     *
     * @param obj
     * @return Devolución de excepción de conversión 0
     */
    public static double toDouble(String obj) {
        try {
            return Double.parseDouble(obj);
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * Convertir cadena a booleana
     *
     * @param b
     * @return Conversion exception return false
     */
    public static boolean toBool(String b) {
        try {
            return Boolean.parseBoolean(b);
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * Determinar si es un número entero. INT
     *
     * @param val
     * @return
     */
    public static Boolean isInt(String val) {
        try {
            Integer.parseInt(val);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String getTimeString() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        return formatter.format(curDate);
    }

    public static String getTimeFormat(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy MM dd HH:mm:ss");
        Date curDate = new Date(time);// 获取当前时间
        return formatter.format(curDate);
    }

    /**
     * Determinar si es hexadecimal
     *
     * @param str
     * @return
     */
    public static boolean isHexNumber(String str) {
        boolean flag = false;
        for (int i = 0; i < str.length(); i++) {
            char cc = str.charAt(i);
            if (cc == '0' || cc == '1' || cc == '2' || cc == '3' || cc == '4'
                    || cc == '5' || cc == '6' || cc == '7' || cc == '8'
                    || cc == '9' || cc == 'A' || cc == 'B' || cc == 'C'
                    || cc == 'D' || cc == 'E' || cc == 'F' || cc == 'a'
                    || cc == 'b' || cc == 'c' || cc == 'c' || cc == 'd'
                    || cc == 'e' || cc == 'f') {
                flag = true;
            }
        }
        return flag;
    }

    /**
     * Convertir una cadena hexadecimal a una matriz de caracteres
     *
     * @param s
     * @return
     */
    public static char[] HexStringToChars(String s) {
        char[] bytes;
        bytes = new char[s.length() / 2];

        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (char) Integer.parseInt(s.substring(2 * i, 2 * i + 2),
                    16);
        }

        return bytes;
    }



}
