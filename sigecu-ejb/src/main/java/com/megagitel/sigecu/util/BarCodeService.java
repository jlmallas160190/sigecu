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
import org.krysalis.barcode4j.impl.upcean.UPCABean;
import org.krysalis.barcode4j.impl.upcean.UPCEANBean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.tools.UnitConv;

/**
 *
 * @author jorgemalla
 */
public class BarCodeService {

    public static void generarImagenBarCode(String code, String path) {
        try {
            UPCEANBean bean = new UPCABean();
            final int dpi = 200;
            bean.setModuleWidth(UnitConv.in2mm(2.8f / dpi));
            bean.doQuietZone(false);
            File outputFile = new File(path + code + ".png");
            FileOutputStream out = new FileOutputStream(outputFile);
            BitmapCanvasProvider canvas = new BitmapCanvasProvider(
                    out, "image/x-png", dpi, BufferedImage.TYPE_BYTE_BINARY, false, 0);
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
}
