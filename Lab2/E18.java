package Lab2;

/*. Progettare e implementare un DFA che riconosca il linguaggio delle costanti numeriche in virgola mobile utilizzando la notazione scientifica dove il simbolo e indica la funzione esponenziale con base 10. L’alfabeto del DFA contiene i seguenti elementi: le cifre numeriche
0, 1, . . . , 9, il segno . (punto) che precede una eventuale parte decimale, i segni + (piu) e ` - (meno)
per indicare positivita o negativit ` a, e il simbolo ` e.
Le stringhe accettate devono seguire le solite regole per la scrittura delle costanti numeriche.
In particolare, una costante numerica consiste di due segmenti, il secondo dei quali e opzionale: `
il primo segmento e una sequenza di cifre numeriche che (1) pu ` o essere preceduta da un segno `
+ o meno -, (2) puo essere seguita da un segno punto ` ., che a sua volta deve essere seguito da
una sequenza non vuota di cifre numeriche; il secondo segmento inizia con il simbolo e, che a
sua volta e seguito da una sequenza di cifre numeriche che soddisfa i punti (1) e (2) scritti per il `
primo segmento. Si nota che, sia nel primo segmento, sia in un eventuale secondo segmento, un
segno punto . non deve essere preceduto per forza da una cifra numerica.
Esempi di stringhe accettate: “123”, “123.5”, “.567”, “+7.5”, “-.7”, “67e10”, “1e-2”,
“-.7e2”, “1e2.3” */
public class E18 {
    public static void main(String[] args) {
        for (String string : args) {
            System.out.println(first(string) == true ? "OK: " + string : "NOPE: " + string);
        }
    }

    public static boolean first(String s) {
        int state = 0;
        int i = 0;
        while (state >= 0 && i < s.length()) {
            char c = s.charAt(i++);
            switch (state) {
                case 0:
                    if (c == '+' || c == '-') {
                        state = 2;
                    } else if (Character.isDigit(c)) {
                        state = 2;
                    } else if (c == '.') {
                        state = 1;// .
                    } else {
                        state = -1;
                    }
                    break;
                case 1:// .
                    return second(s.substring(i));
                case 2:// num
                    if (Character.isDigit(c)) {
                        state = 2;
                    } else if (c == 'e') {
                        state = 3;
                    } else if (c == '.') {
                        state = 1;
                    } else {
                        state = -1;
                    }
                    break;
                case 3:
                    if (Character.isDigit(c) || c == '+' || c == '-') {
                        state = 4;
                    }else if (c == '.') {
                        state = 1;
                    } else {
                        state = -1;
                    }
                    break;
                case 4:
                    if (Character.isDigit(c)) {
                        state = 3;
                    } else {
                        state = -1;
                    }
            }
        }
        return state == 2 || state == 3;
    }

    public static boolean second(String s) {
        int state = 0;
        int i = 0;
        while (state >= 0 && i < s.length()) {
            char c = s.charAt(i++);
            switch (state) {
                case 0:// number
                    if (Character.isDigit(c)) {
                        state = 0;
                    } else if (c == 'e') {
                        state = 1;
                    } else {
                        state = -1;
                    }
                    break;
                case 1:// e
                    if (Character.isDigit(c) || c == '+' || c == '-') {
                        state = 2;
                    } else {
                        state = -1;
                    }
                    break;
                case 2:
                    if (Character.isDigit(c)) {
                        state = 2;
                    } else {
                        state = -1;
                    }
                    break;
                
            }

        }
        return state == 2 || state == 0;
    }
}
