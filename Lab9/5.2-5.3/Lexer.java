import java.io.*;

public class Lexer {

    public static int line = 1;
    private char peek = ' ';

    private void readch(BufferedReader br) {
        try {
            peek = (char) br.read();
        } catch (IOException exc) {
            peek = (char) -1; // ERROR
        }
    }
    
    public Token lexical_scan(BufferedReader br) throws Exception {
        while (peek == ' ' || peek == '\t' || peek == '\n' || peek == '\r') {
            if (peek == '\n')
                line++;
            readch(br);
        }

        switch (peek) {
            case '!':
                peek = ' ';
                return Token.not;

            // ... gestire i casi di ( ) [ ] { } + - * / ; , ... //
            case '(':
                peek = ' ';
                return Token.lpt;
            case ')':
                peek = ' ';
                return Token.rpt;
            case '[':
                peek = ' ';
                return Token.lpq;
            case ']':
                peek = ' ';
                return Token.rpq;
            case '{':
                peek = ' ';
                return Token.lpg;
            case '}':
                peek = ' ';
                return Token.rpg;
            case '+':
                peek = ' ';
                return Token.plus;
            case '-':
                peek = ' ';
                return Token.minus;
            case '*':
                peek = ' ';
                return Token.mult;
            case '/':
                readch(br);
                if (peek == '/'){
                    while (peek != '\n') {
                        readch(br);
                    }
                    peek = ' ';
                    return lexical_scan(br);
                } else if (peek == '*') {// comment start
                    int state = 0;
                    int eline = line;
                    while (state >= 0) {
                        readch(br);
                        switch (state) {
                            case 0:
                                if (peek == '*'  ) {
                                    state = 1;
                                } else if (peek == (char) -1) {
                                    state = -1;
                                } else {
                                    state = 0;
                                }
                                break;
                            case 1:
                                if (peek == '/') {
                                    peek = ' ';
                                    return lexical_scan(br);
                                } else if (peek == (char) -1) {
                                    state = -1;
                                } else {
                                    state = 0;
                                }
                                break;
                        }
                    }
                    throw new Exception("Unxpected end of comment at line:" + eline);
                } else {
                    return Token.div;
                }
            case ';':
                peek = ' ';
                return Token.semicolon;
            case ',':
                peek = ' ';
                return Token.comma;
            case '&':
                readch(br);
                if (peek == '&') {
                    peek = ' ';
                    return Word.and;
                } else {
                    throw new Exception("Erroneous character"
                    + " after & : " + peek);
                }

                // ... gestire i casi di || < > <= >= == <> ... //
            case '|':
                readch(br);
                if (peek == '|') {
                    peek = ' ';
                    return Word.or;
                } else {
                    throw new Exception("Erroneous character"
                    + " after | : " + peek);
                }
            case '<':
                readch(br);
                if (peek == '=') {
                    peek = ' ';
                    return Word.le;
                } else if (peek == '>') {
                    peek = ' ';
                    return Word.ne;
                } else {
                    return Word.lt;
                }
            case '>':
                readch(br);
                if (peek == '=') {
                    peek = ' ';
                    return Word.ge;
                } else {
                    return Word.gt;
                }
            case '=':
                readch(br);
                if (peek == '=') {
                    peek = ' ';
                    return Word.eq;
                } else {
                    return Word.assign;
                }
            case (char) -1:
                return new Token(Tag.EOF);

            default:
                if (Character.isLetter(peek) || peek == '_') {

                    // ... gestire il caso degli identificatori e delle parole chiave //
                    String s = "";
                    do {
                        s += peek;
                        readch(br);
                    } while (Character.isLetterOrDigit(peek) || peek == '_');
                    if (s.equals("assign")) {
                        return Word.assign;
                    } else if (s.equals("to")) {
                        return Word.to;
                    } else if (s.equals("conditional")) {
                        return Word.conditional;
                    } else if (s.equals("option")) {
                        return Word.option;
                    } else if (s.equals("do")) {
                        return Word.dotok;
                    } else if (s.equals("else")) {
                        return Word.elsetok;
                    } else if (s.equals("while")) {
                        return Word.whiletok;
                    } else if (s.equals("begin")) {
                        return Word.begin;
                    } else if (s.equals("end")) {
                        return Word.end;
                    } else if (s.equals("print")) {
                        return Word.print;
                    } else if (s.equals("read")) {
                        return Word.read;
                    } else {
                        String ss = s.replaceAll("_", "");
                        if (ss == "") {
                            throw new Exception("Invalid seqence:" + s);
                        } else {
                            return new Word(Tag.ID, s);
                        }
                    }
                } else if (Character.isDigit(peek)) {

                    // ... gestire il caso dei numeri ... //
                    String s = "";
                    do {
                        s += peek;
                        readch(br);
                    } while (Character.isLetterOrDigit(peek));
                    try {
                        return new NumberTok(Integer.parseInt(s));
                    } catch (Exception e) {
                        return new Word(Tag.ID, s);
                    }
                } else {
                    throw new Exception("Erroneous charachter");
                }
        }
    }

    public static void main(String[] args) throws Exception {
        Lexer lex = new Lexer();
        String path = "/home/edoardo/Progs/UniProjects/LFT/Lab8/data.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Token tok;
            do {
                tok = lex.lexical_scan(br);
                System.out.println("Scan: " + tok);
            } while (tok.tag != Tag.EOF);
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
