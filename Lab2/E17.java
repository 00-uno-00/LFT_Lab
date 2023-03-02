package Lab2;

public class E17 {
    public static void main(String[] args) {
        for (String string : args) {
            System.out.println(name(string) == true ? "OK: " + string : "NOPE: " + string);
        }
    }

    public static boolean name(String s) {
        int state = 0;
        int i = 0;
        while (state >= 0 && i < s.length()) {
            char c = s.charAt(i++);
            switch (state) {
                case 0:
                    if (c == 'E') {
                        state = 1;
                    } else {
                        state += 8;
                    }
                    break;
                case 1:
                    if (c == 'd') {
                        state = 2;
                    } else {
                        state += 8;
                    }
                    break;
                case 2:
                    if (c == 'o') {
                        state = 3;
                    } else {
                        state += 8;
                    }
                    break;
                case 3:
                    if (c == 'a') {
                        state = 4;
                    } else {
                        state += 8;
                    }
                    break;
                case 4:
                    if (c == 'r') {
                        state = 5;
                    } else {
                        state += 8;
                    }
                    break;
                case 5:
                    if (c == 'd') {
                        state = 6;
                    } else {
                        state += 8;
                    }
                    break;
                case 6:
                    if (c == 'o') {
                        state = 7;
                    } else {
                        state = 14;
                    }
                    break;
                case 7:
                    break;
                case 8:
                    if (c == 'd') {
                        state -= 6;
                    } else {
                        state = -1;
                    }
                    break;
                case 9:
                    if (c == 'o') {
                        state -= 6;
                    } else {
                        state = -1;
                    }
                    break;
                case 10:
                    if (c == 'a') {
                        state -= 6;
                    } else {
                        state = -1;
                    }
                    break;
                case 11:
                    if (c == 'r') {
                        state -= 6;
                    } else {
                        state = -1;
                    }
                    break;
                case 12:
                    if (c == 'd') {
                        state -= 6;
                    } else {
                        state = -1;
                    }
                    break;
                case 13:
                    if (c == 'o') {
                        state -= 6;
                    } else {
                        state = -1;
                    }
                    break;
                case 14:

                    break;

            }
        }
        return state == 7 || state == 14;
    }
}
