package org.tud.imir.ex2.core.descriptor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class Mpeg7Descriptor {

    public int[] computeDescriptor(String filename) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(filename));
        }
        catch (IOException e) {
            System.out.println(e);
        }

        ImagePartitioner partitioner = new ImagePartitioner();
        ZickZackReader reader = new ZickZackReader();
        CosinusTransformator transformer = new CosinusTransformator();
        Quantizise quant = new Quantizise();

        partitioner.setImage(img);

        transformer.setBlockColorMatrix(partitioner.getColorMatrix());

        // set factor to 1 (so nothing happened, because the quantisize is marked as voluntary in the slides)
        Integer quantisizeFactor = 1;

        int[] coeffs = new int[12];
        int currentIndex = 0;

        // the integer and list names are the same as in the slides inside the XML-code on slide 8
        reader.setMatrix(quant.quantisizeMatrix(transformer.getValue(colorValue.Y), quantisizeFactor));
        Integer YDCCoeff = reader.getDCValue();
        List<Integer> YACCoeff5 = reader.get5ACValues();

        reader.setMatrix(quant.quantisizeMatrix(transformer.getValue(colorValue.Cb),quantisizeFactor));
        Integer CbDCCoeff = reader.getDCValue();
        List<Integer> CbACCoeff2 = reader.get2ACValues();

        reader.setMatrix(quant.quantisizeMatrix(transformer.getValue(colorValue.Cr),quantisizeFactor));
        Integer CrDCCoeff = reader.getDCValue();
        List<Integer> CrACCoeff2 = reader.get2ACValues();

        coeffs[currentIndex++] = YDCCoeff;
        for (Integer coeff : YACCoeff5) {
            coeffs[currentIndex++] = coeff;
        }
        coeffs[currentIndex++] = CbDCCoeff;
        for (Integer coeff : CbACCoeff2) {
            coeffs[currentIndex++] = coeff;
        }
        coeffs[currentIndex++] = CrDCCoeff;
        for (Integer coeff : CrACCoeff2) {
            coeffs[currentIndex++] = coeff;
        }

        return coeffs;
    }

}