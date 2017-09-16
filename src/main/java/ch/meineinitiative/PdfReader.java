package ch.meineinitiative;

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

    public static double tanimoto(String string1, String string2) {
        List<Pair<String, String>> pairsString1 = createPairs(string1);
        List<Pair<String, String>> pairsString2 = createPairs(string2);

        if (pairsString1.size() < pairsString2.size()) {
            List<Pair<String, String>> back = pairsString1;
            pairsString1 = pairsString2;
            pairsString2 = back;
        }

        int intersection = 0;
        int unionDelta = 0;

        for (Pair<String, String> stringStringPair : pairsString1) {
            boolean remove = pairsString2.remove(stringStringPair);
            if (remove) {
                intersection++;
                System.out.println(pairsString2);
            } else {
                unionDelta++;
            }
        }

        double unionSize = intersection + unionDelta;

        return (unionSize - intersection) / unionSize;
    }

    private static List<Pair<String, String>> createPairs(String string1) {
        List<Pair<String, String>> toReturn = new ArrayList<>(string1.length());
        for (int i = 0; i < string1.length() - 1; i++) {
            toReturn.add(Pair.of(string1.substring(i, i + 1), string1.substring(i + 1, i + 2)));
        }
        return toReturn;
    }

    public static void main(String[] args) {
        System.out.println(tanimoto("Haus Gott", "Gott Haus"));

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
