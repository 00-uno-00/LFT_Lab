package Lab2;

public class E16 {
    public static void main(String[] args) {
        for (String string : args) {
            System.out.println(DFA(string) == true ? "OK" : "NOPE");
        }
    }

    public static boolean DFA(String s) {
        int state = 0;
        int i = 0;
        while (state >= 0 && i < s.length()) {
            char c = s.charAt(i++);
            switch (state) {
                case 0:
                    if (c == 'b') {
                        state = 0;
                    } else if (c == 'a') {
                        state += 1;
                    } else {
                        state = -1;
                    }
                    break;
                case 1:
                    if (c == 'a' || c == 'b') {
                        state += 1;
                    } else {
                        state = -1;
                    }
                    break;
                case 2:
                    if (c == 'a' || c == 'b') {
                        state += 1;
                    } else {
                        state = -1;
                    }
                    break;
                case 3:
                    if (c == 'a' ) {
                        state = 1;
                    } else {
                        state = 0;
                    }
                    break;
            }
        }
        return state >= 1;
    }
}
