/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.megagitel.sigecu.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.krysalis.barcode4j.impl.upcean.EAN13Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.tools.UnitConv;

/**
 *
 * @author jorgemalla
 */
public class BarCodeService {

    public static void generarImagenBarCode(String code, String path) {
        try {
            EAN13Bean bean = new EAN13Bean();

            final int dpi = 200;
            bean.setModuleWidth(UnitConv.in2mm(2.8f / dpi));
            bean.doQuietZone(false);
            File outputFile = new File(path + code + ".png");
            FileOutputStream out = new FileOutputStream(outputFile);
            BitmapCanvasProvider canvas = new BitmapCanvasProvider(
                    out, "image/x-png", dpi, BufferedImage.TYPE_BYTE_GRAY, false, 0);
            bean.generateBarcode(canvas, code);
            canvas.finish();
        } catch (IOException ex) {
            try {
                throw ex;
            } catch (IOException ex1) {
                Logger.getLogger(BarCodeService.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }

    public static Boolean verifiyCodeEan13(String ean13Digit) {
        if (ean13Digit.length() != 13) {
            return Boolean.FALSE;
        }
        String eanWithoutDigit = ean13Digit.substring(0, 12);
        String verifyDigit = ean13Digit.substring(12,13);
        int sum = 0;
        for (int i = 0; i < eanWithoutDigit.length(); i++) {
            sum = sum + (Integer.parseInt(String.valueOf(eanWithoutDigit.charAt(i))) * (i % 2 == 0 ? 1 : 3));
        }
        int checkDigit = sum % 10 == 0 ? 0 : (10 - (sum % 10));
        return Integer.parseInt(verifyDigit)==checkDigit;
    }
}
