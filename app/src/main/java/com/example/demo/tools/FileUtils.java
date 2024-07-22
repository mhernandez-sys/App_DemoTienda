package com.example.demo.tools;

import android.os.Environment;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class FileUtils {
    static String TAG = "FileUtils";
    public static String ADDR = "btaddress";
    public static String NAME = "btname";
    public static String filePath = Environment.getExternalStorageState();

    public static void saveXmlList(List<String[]> data) {
        try{
            File file = new File(filePath);
            if(file.exists()){
                file.delete();
            }
            FileOutputStream fos = new FileOutputStream(file);
            //Obtenga una herramienta de serialización
            XmlSerializer serializer = Xml.newSerializer();
            serializer.setOutput(fos, "Lector-C5");
            //Establecer encabezado de archivo
            serializer.startDocument("Lector-C5", true);
            serializer.startTag(null, "root");
            for (int i = 0; i < data.size(); i++) {
            serializer.startTag(null, "bt");
            //dirección bluetooth
            serializer.startTag(null, ADDR);
            serializer.text(data.get(i)[0]);
            serializer.endTag(null, ADDR);
            //nombre de Bluetooth
                serializer.startTag(null, NAME);
                serializer.text(data.get(i)[1]);
                serializer.endTag(null, NAME);
                serializer.endTag(null, "bt");
            }
            serializer.endTag(null, "root");
            serializer.endDocument();
            fos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static ArrayList<String[]> readXmlList() {
        ArrayList<String[]> list = new ArrayList<String[]>();
        try {
            File path = new File(filePath);
            if (!path.exists()) {
                return list;
            }
            FileInputStream fis = new FileInputStream(path);

            // Obtener el objeto del analizador de extracción

            XmlPullParser parser = Xml.newPullParser();
            // Especificar archivos analizados y formatos de codificación
            parser.setInput(fis, "utf-8");
            int eventType = parser.getEventType(); // Obtener tipo de evento
            String addr = null;
            String name = null;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = parser.getName(); // Obtener el nombre del nodo actual
                switch (eventType) {
                    case XmlPullParser.START_TAG: // La corriente es igual al nodo inicial.
                        if ("root".equals(tagName)) { //
                        } else if ("bt".equals(tagName)) { //
                        } else if (ADDR.equals(tagName)) { // <nombre>
                            addr = parser.nextText();
                        } else if (NAME.equals(tagName)) { // <edad>
                            name = parser.nextText();
                        }
                        break;
                    case XmlPullParser.END_TAG: // </personas>
                        if ("bt".equals(tagName)) {
                            Log.i(TAG, "addr---" + addr);
                            Log.i(TAG, "name---" + name);
                            String[] str = new String[2];
                            str[0] = addr;
                            str[1] = name;
                            list.add(str);
                        }
                        break;
                    default:
                        break;
                }
                eventType = parser.next(); // Obtener el siguiente tipo de evento
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return list;
    }

    public static void clearXmlList(){
        List<String[]> list =readXmlList();
        list.clear();
        saveXmlList(list);
    }

    public static void writeFile(String fileName, List<String> data, boolean append) {
        /*int index = fileName.lastIndexOf(File.separator);
        Log.e("TEST", "index = " + index);
        if (index != -1) {
            File filePath = new File(fileName.substring(0, index));
            Log.e("TEST", "filePath = " + filePath);
            if (!filePath.exists()) {
                filePath.mkdirs();
            }
        }

        File file = new File(fileName);
        try {
            boolean created = file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
                Runtime runtime = Runtime.getRuntime();
                runtime.exec("chmod 0666 " + file);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        FileOutputStream fileOutputStream = null;
        try {
            Log.e("TEST", "文件路径");
            // Log.e("TEST", "data.size() = " + data.size());
            fileOutputStream = new FileOutputStream(file, append);
            *//*String epc = "epc data\t";
            String tid = "tid data\t";
            String user = "user data\t";
            fileOutputStream.write(epc.getBytes());*//*
            Log.e("TEST", "data.size() = " + data.size());
            for (int i = 0; i < data.size(); i++) {
                String datas = data.get(i);
                Label label1 = new Label(i%3, i/3 + 1, data.get(i));
                fileOutputStream.write(label1.getBytes());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (Exception e) {
            }
        }*/
        Log.e("TEST", "Ingrese la ruta del archivo de escritura");
        try {
            WritableWorkbook workbook = Workbook.createWorkbook(new File(fileName));
            WritableSheet sheet = workbook.createSheet("Sheet1", 0);
            for (int i = 0; i < data.size(); i++) {
                if (i == 0) {
                    Label label = new Label(0, 0, "EPC Data");
                    sheet.addCell(label);
                    Label label1 = new Label(1, 0, "TID Data");
                    sheet.addCell(label1);
                    Label label2 = new Label(2, 0, "USER Data");
                    sheet.addCell(label2);
                }
                Label label = new Label(i%3, i/3+1, data.get(i));
                sheet.addCell(label);
            }
            workbook.write();
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (RowsExceededException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        }
    }

    private static String readInStream(FileInputStream inStream) {
        if(inStream==null)
            return null;
        try {
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[512];
            int length = -1;
            while ((length = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, length);
            }

            outStream.close();
            inStream.close();
            return outStream.toString();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    /**
     * Leer el contenido del archivo
     *
     * @param path
     * @return
     */
    public static String readFile(String path) {

        FileInputStream fin = null;
        try {
            fin = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }

        return readInStream(fin);
    }



}
