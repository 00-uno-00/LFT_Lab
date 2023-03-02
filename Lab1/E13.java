

/**
 * E13
 */
public class E13 {

    public static void main(String[] args) {
        for (String string : args) {
            System.out.println(T2T3(string) == true ? "OK" : "NOPE");
        }
    }

    public static boolean T2T3(String s) {
        int state = 0;
        int i = 0;

        while (state >= 0 && i < s.length()) {
            char c = s.charAt(i++);
            switch (state) {
                case 0:
                    if (Character.isDigit(c) && c % 2 == 0) {// pari
                        state = 1;
                    } else if (Character.isDigit(c) && c % 2 != 0) {// dispari
                        state = 2;
                    } else {
                        state = -1;
                    }
                    break;
                case 1:// pari
                    if (Character.isDigit(c) && c % 2 == 0) {// pari
                        state = 1;
                    } else if (Character.isDigit(c) && c % 2 != 0) {// pari
                        state = 2;
                    } else if (65 <= c && c <= 75) {
                        state = 3;
                    } else {
                        state = -1;
                    }
                    break;
                case 2:// dispari
                    if (Character.isDigit(c) && c % 2 != 0) {// dispari
                        state = 2;
                    } else if (Character.isDigit(c) && c % 2 == 0) {// pari
                        state = 1;
                    } else if (66 <= c && c <= 90) {
                        state = 4;
                    } else {
                        state = -1;
                    }
                    break;
                case 3:
                    if (Character.isLetter(c)) {
                        state = 3;
                    } else {
                        state = -1;
                    }
                    break;
                case 4:
                    if (Character.isLetter(c)) {
                        state = 4;
                    } else {
                        state = -1;
                    }
                    break;
            }
        }
        return state == 3 || state == 4;
    }
}