package Lab2;

public class E19 {

    public static void main(String[] args) {
        String string = "/*a**a**a*/";
        System.out.println(comments(string) == true ? "OK: " + string : "NOPE: " + string);
    }

    public static boolean comments(String s) {
        int state = 0;
        int i = 0;
        while (state >= 0 && i < s.length()) {
            char c = s.charAt(i++);
            switch (state) {
                case 0:
                    if (c == '/') {
                        state = 1;
                    } else {
                        state = 0;
                    }
                    break;
                case 1:
                    if (c == '*') {
                        state = 2;
                    } else {
                        state = -1;
                    }
                    break;
                case 2:
                    if (c == '*') {
                        state = 3;
                    } else {
                        state = 2;
                    }
                    break;
                case 3:
                    if (c == '/') {
                        state = 4;
                    } else {
                        state = 2;
                    }
                    break;
                case 4:

                    break;
            }
        }
        return state == 4;
    }

}
