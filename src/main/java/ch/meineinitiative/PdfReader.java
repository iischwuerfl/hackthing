package ch.meineinitiative;

import ch.meineinitiative.service.impl.InitiativeServiceImpl;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PdfReader {

    public static void main(String[] args) {
        System.out.println(InitiativeServiceImpl.tanimoto("Haus Gott", "Gott Haus"));

        PDFTextStripper pdfStripper = null;
        PDDocument pdDoc = null;
        COSDocument cosDoc = null;
        File file = new File("C:\\Users\\mhan\\Downloads\\2521.pdf");
        try {
            PDFParser parser = new PDFParser(new RandomAccessFile(file, "r"));
            parser.parse();
            cosDoc = parser.getDocument();
            pdfStripper = new PDFTextStripper();
            pdDoc = new PDDocument(cosDoc);
            pdfStripper.setStartPage(1);
            pdfStripper.setEndPage(5);

            String parsedText = pdfStripper.getText(pdDoc);


            System.out.println(parsedText);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
