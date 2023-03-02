
public class E12 {
    public static void main(String[] args) {
        for (String string : args) {
            System.out.println(lexer(string) == true ? "OK" : "NOPE");
        }
    }

    public static boolean lexer(String s) {
        int state = 0;

        for (char c : s.toCharArray()) {
            switch (state) {
                case 0:
                    if (isletter(c)) {
                        state = 1;
                    } else if (c == 95) {
                        state = 0;
                    }
                    break;
                case 1:
                    if (isletter(c) || isnumber(c) || c == 95) {
                        state = 1;
                    }
                    break;
            }
        }
        return state == 1;
    }

    public static boolean isnumber(char c) {
        return Character.isDigit(c);
    }

    public static boolean isletter(char c) {
        return Character.isLetter(c);
    }
}
