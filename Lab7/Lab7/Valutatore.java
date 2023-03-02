import java.io.*;

public class Valutatore {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    public Valutatore(Lexer l, BufferedReader br) throws Exception {
        lex = l;
        pbr = br;
        move();
    }

    void move() throws Exception {
        look = lex.lexical_scan(pbr);
        System.out.println("token = " + look);
    }

    void error(String s) {
        throw new Error("Near line " + lex.line + ": " + s);
    }

    void match(int t) throws Exception {
        if (look.tag == t) {
            if (look.tag != Tag.EOF)
                move();
        } else
            error("syntax error");
    }

    public void start() throws Exception {
        int expr_val;
        switch (look.tag) {
            case '(':
            case Tag.NUM:
                expr_val = expr();
                match(Tag.EOF);
                System.out.println(expr_val);
                break;
            default:
                error("start error");
                break;
        }
    }

    private int expr() throws Exception {
        int term_val, exprp_val = 0;

        switch (look.tag) {
            case '(':
            case Tag.NUM:
                term_val = term();
                exprp_val = exprp(term_val);
                break;
            default:
                error("expr error");
                break;
        }
        return exprp_val;
    }

    private int exprp(int exprp_i) throws Exception {
        int term_val, exprp_val = 0;
        switch (look.tag) {
            case '+':
                match('+');
                term_val = term();
                exprp_val = exprp(exprp_i + term_val);
                break;
            case '-':
                match('-');
                term_val = term();
                exprp_val = exprp(exprp_i - term_val);
                break;
            case ')':
            case Tag.EOF:
                return exprp_i;//senza questo azzera
            default:
                error("exprp error");
                break;
        }
        return exprp_val;
    }

    private int term() throws Exception {
        int fact_val, termp_val = 0;
        switch (look.tag) {
            case '(':
            case Tag.NUM:
                fact_val = fact();
                termp_val = termp(fact_val);
                break;
            default:
                error("Missing term");
                break;
        }
        return termp_val;
    }

    private int termp(int termp_i) throws Exception {
        int fact_val, termp_val = 0;
        switch (look.tag) {
            case '*':
                match('*');
                fact_val = fact();
                termp_val = termp(termp_i * fact_val);
                break;
            case '/':
                match('/');
                fact_val = fact();
                termp_val = termp(termp_i / fact_val);
                break;
            case '+':
            case '-':
            case ')':
            case Tag.EOF:
                return termp_i;//senza questo azzera 
            default:
                error("Termp error");
                break;
        }
        return termp_val;
    }

    private int fact() throws Exception{
        int fact_val = 0;
        switch (look.tag) {
            case '(':
                match('(');
                fact_val = expr();
                match(')');
                break;
            case Tag.NUM:
                fact_val = ((NumberTok)look).value;
                match(look.tag);
                break;
            default:
                error("Fact error");
                break;
        }
        return fact_val;
    }

    public static void main(String[] args) throws Exception {
        Lexer lex = new Lexer();
        String path = "../LFT/Lab7/data.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Valutatore valutatore = new Valutatore(lex, br);
            valutatore.start();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
