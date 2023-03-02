public class E15 {
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
                    c = Character.toLowerCase(c);
                    if (97 <= c && c <= 107) {// t2
                        state = 1;
                    } else if (108 <= c && c <= 122) {// t3
                        state = 2;
                    } else {
                        state = -1;
                    }
                    break;
                case 1:// ak
                    if (Character.isLetter(c)) {// t2
                        state = 1;
                    } else if (Character.isDigit(c) && c % 2 != 0) {
                        state = 3;
                    } else if (Character.isDigit(c) && c % 2 == 0) {
                        state = 5;
                    } else {
                        state = -1;
                    }
                    break;
                case 2:// lz
                    if (Character.isLetter(c)) {// t3
                        state = 2;
                    } else if (Character.isDigit(c) && c % 2 == 0) {
                        state = 4;
                    } else if (Character.isDigit(c) && c % 2 != 0) {
                        state = 6;
                    } else {
                        state = -1;
                    }
                    break;
                case 3:// disp ak
                    if (Character.isDigit(c) && c % 2 != 0) {
                        state = 3;
                    } else if (Character.isDigit(c) && c % 2 == 0) {
                        state = 5;
                    } else {
                        state = -1;
                    }
                    break;
                case 4:// pari lz
                    if (Character.isDigit(c) && c % 2 == 0) {
                        state = 4;
                    } else if (Character.isDigit(c) && c % 2 != 0) {
                        state = 6;
                    } else {
                        state = -1;
                    }
                    break;
                case 5:// pari ak
                    if (Character.isDigit(c) && c % 2 == 0) {
                        state = 5;
                    } else if (Character.isDigit(c) && c % 2 != 0) {
                        state = 3;
                    } else {
                        state = -1;
                    }
                    break;
                case 6:// disp lz
                    if (Character.isDigit(c) && c % 2 == 0) {
                        state = 4;
                    } else if (Character.isDigit(c) && c % 2 != 0) {
                        state = 6;
                    } else {
                        state = -1;
                    }
                    break;
            }
        }
        return state == 5 || state == 6;
    }

}
