package com.workshop;

import org.joda.money.BigMoney;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

public class CatalanNumberGenerator {

    private final Map<Long, BigMoney> catsR2 = new HashMap<Long, BigMoney>();
    private final Map<Long, String> facts = new HashMap<Long, String>();

    public static final BigMoney ONE = BigMoney.parse("EUR 1");

    public CatalanNumberGenerator() {
        catsR2.put(0L, ONE);

        facts.put(1L, "1");
    }

    public static void main(String[] args) throws IOException {
        final CatalanNumberGenerator generator = new CatalanNumberGenerator();

        generator.generateCatalanNumbers(99, "target/outputfile.txt");

        generator.saveLatexAsPng("\\frac{(2n)!}{(n+1)!n!}", "target/catalan-common-formula.png");

        for (int i = 1; i <= 14; i++) {
            final String math = "\\frac{(2\\cdot" + i + ")!}{(" + i + "+1)!" + i + "!}" +
                                " = " +
                                generator.generateFormula(i);

            generator.saveLatexAsPng(math, "target/catalan " + i + ".png");
        }
    }

    private void saveLatexAsPng(final String math, final String pathToFile) throws IOException {
        final TeXFormula formula = new TeXFormula(math);
        final TeXIcon ti = formula.createTeXIcon(TeXConstants.STYLE_TEXT, 400);
        final BufferedImage b = new BufferedImage(ti.getIconWidth(), ti.getIconHeight(), BufferedImage.TYPE_4BYTE_ABGR);
        ti.paintIcon(new JLabel(), b.getGraphics(), 0, 0);

        final File file = new File(pathToFile);
        ImageIO.write(b, "png", file);
    }

    private String fact(final long n) {
        if (facts.containsKey(n)) {
            return facts.get(n);
        }
        String fact = "1";
        for (long i = 2; i <= n; i++) {
            fact = fact + "\\cdot" + i;
        }
        facts.put(n, fact);
        return fact;
    }

    private String generateFormula(final int n) {
        return "\\frac{(" + fact(2*n) +")}{(" + fact(n + 1) +")\\cdot(" + fact(n) + ")}";
    }

    private BigMoney catalan(final long n) {
        if (!catsR2.containsKey(n)) {
            final BigMoney previousCatalan = catalan(n - 1);
            final BigMoney catalan = previousCatalan.multipliedBy(2).multipliedBy((2 * (n - 1) + 1)).dividedBy(n + 1, RoundingMode.FLOOR);
            catsR2.put(n, catalan);
        }
        return catsR2.get(n);
    }

    private void generateCatalanNumbers(final int numberOfNumbers, final String outputFilePath) throws IOException {
        FileWriter outputStream = null;
        try {
            outputStream = new FileWriter(outputFilePath);
            for (int i = 0; i <= numberOfNumbers; i++) {
                final String catalanNumber = catalan(i).rounded(0, RoundingMode.HALF_UP).toString().replaceAll("EUR ", "");
                outputStream.write(i + " = " + catalanNumber + "\n");
            }
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }
}
