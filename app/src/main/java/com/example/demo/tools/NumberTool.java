package com.example.demo.tools;

import java.math.BigDecimal;

/**
 * Herramientas digitales
 * @author WuShengjun
 * @date 2017年2月22日
 */
public class NumberTool {

    /**
     * Un valor doble que conserva un número específico de decimales.
     * @param point ¿Cuántos decimales se deben conservar?
     * @param val Necesidad de convertir valor
     * @return
     */

    public static double getPointDouble(int point, double val) {
        BigDecimal bd = new BigDecimal(val);
        return bd.setScale(point, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * Un valor doble que conserva un número específico de decimales.
     * @param point ¿Cuántos decimales se deben conservar?
     * @param val Necesidad de convertir valor
     * @return
     */
    public static double getPointDouble(int point, int val) {
        BigDecimal bd = new BigDecimal(val);
        return bd.setScale(point, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    /**
     * Un valor doble que conserva un número específico de decimales.
     * @param point ¿Cuántos decimales se deben conservar?
     * @param val Necesidad de convertir valor
     * @return
     */

    public static double getPointDouble(int point, long val) {
        BigDecimal bd = new BigDecimal(val);
        return bd.setScale(point, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * Un valor doble que conserva un número específico de decimales.
     * @param point ¿Cuántos decimales se deben conservar?
     * @param val Necesidad de convertir valor
     * @return
     */
    public static double getPointDouble(int point, String val) {
        BigDecimal bd = new BigDecimal(val);
        return bd.setScale(point, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static float getPointFloat(int point, float val) {
        BigDecimal b = new BigDecimal(val);
        return b.setScale(point, BigDecimal.ROUND_HALF_UP).floatValue();
    }





}
