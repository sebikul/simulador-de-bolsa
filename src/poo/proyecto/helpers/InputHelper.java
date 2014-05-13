package poo.proyecto.helpers;

import java.io.Console;
import java.util.Scanner;


public class InputHelper {

    private Console c;


    public InputHelper(Console c) {
        this.c = c;
    }

    private String ask(String m) {

        return c.readLine(m).trim();
    }

    public double getDouble(String m, String e) {
        return getDouble(m, e, false, 0);
    }

    public int getInt(String m, String e) {
        return getInt(m, e, false, 0);
    }

    public double getDouble(String msg, String err, boolean hasDef,
                            double def) {

        double ans = 0.0;
        String strIn;
        boolean ansInvalida = false;

        do {
            if (ansInvalida) {
                c.printf("\n" + err + "\n");
            }
            strIn = ask(msg + (hasDef ? " [" + (double) 0 + "]" : "") + ": ");

            if (!strIn.isEmpty()) {

                ansInvalida = false;
                try {
                    ans = Double.parseDouble(strIn);
                } catch (Exception e) {
                    ansInvalida = true;
                }
            } else if (hasDef) {
                ansInvalida = false;
                ans = 0.0;
            }

        } while (ansInvalida);

        return ans;

    }

    public int getInt(String msg, String err, boolean hasDef, int def) {

        int ans = 0;
        String strIn;
        boolean ansInvalida = false;

        do {
            if (ansInvalida) {
                c.printf("\n" + err + "\n");
            }
            strIn = ask(msg + (hasDef ? " [" + 0 + "]" : "") + ": ");
            if (!strIn.isEmpty()) {

                ansInvalida = false;
                try {
                    ans = Integer.parseInt(strIn);
                } catch (Exception e) {
                    ansInvalida = true;
                }
            } else if (hasDef) {
                ansInvalida = false;
                ans = 0;
            }

        } while (ansInvalida);

        return ans;

    }

    public String getString(String msg, String err) {
        return getString(msg, err, false, null);

    }

    public String getString(String msg, String err, boolean hasDef,
                            String def) {
        String strIn;
        boolean strInvalida = false;

        do {
            if (strInvalida) {
                c.printf("\n" + err + "\n");
            }

            strIn = ask(msg + (hasDef ? " [" + def + "]" : "") + ": ");

            if (strIn.isEmpty() && hasDef) {

                strInvalida = false;
                strIn = def;

            } else {
                strInvalida = strIn.isEmpty();
            }

        } while (strInvalida);

        return strIn;
    }

}
