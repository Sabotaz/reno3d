package core;

import java.util.Random;

/**
 * Created by christophe on 18/02/16.
 */
public class GUID {

    private static final Random random = new Random();
    private static final char[] symbols;
    private final static char[] buf = new char[22];

    static {
        StringBuilder tmp = new StringBuilder();
        for (char ch = '0'; ch <= '9'; ++ch)
            tmp.append(ch);
        for (char ch = 'a'; ch <= 'z'; ++ch)
            tmp.append(ch);
        for (char ch = 'A'; ch <= 'Z'; ++ch)
            tmp.append(ch);
        tmp.append('_');
        tmp.append('$');
        symbols = tmp.toString().toCharArray();
    }

    public static String uid() {
        for (int idx = 0; idx < buf.length; ++idx)
            buf[idx] = symbols[random.nextInt(symbols.length)];
        return new String(buf);
    }
}
