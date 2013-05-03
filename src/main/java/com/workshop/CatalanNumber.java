package com.workshop;

import org.joda.money.BigMoney;

import java.io.FileWriter;
import java.io.IOException;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

public class CatalanNumber {

    private static final Map<Long, BigMoney> catsR2 = new HashMap<Long, BigMoney>();

    public static final BigMoney ONE = BigMoney.parse("EUR 1");

    static{
        catsR2.put(0L, ONE);
    }

    private static BigMoney catalan(long n) {
        if(!catsR2.containsKey(n)) {
            final BigMoney previousCatalan = catalan(n - 1);
            final BigMoney catalan = previousCatalan.multipliedBy(2).multipliedBy((2 * (n - 1) + 1)).dividedBy(n + 1, RoundingMode.FLOOR);
            catsR2.put(n, catalan);
        }
        return catsR2.get(n);
    }

    public static void main(String[] args) throws IOException {
        FileWriter outputStream = null;
        try {
            outputStream = new FileWriter("target/outputfile.txt");
            for (int i = 0; i <= 9999; i++) {
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
