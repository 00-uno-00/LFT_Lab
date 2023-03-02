import java.io.*;

public class Parser {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    public Parser(Lexer l, BufferedReader br) throws Exception {
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
        switch (look.tag) {
            case '(':
            case Tag.NUM:
                expr();
                match(Tag.EOF);
                break;
            default:
                error("start error");
                break;
        }
        
    }

    private void expr() throws Exception {
        //GUIDA(expr -> termexprp) = {(,+,-}
        //GUIDA(expr -> termexprp) = {(,+,-}
        switch (look.tag) {
            case '(':
            case Tag.NUM:
                term();
                exprp();
                break;
            default:
                error("expr error");
                break;
        }
    }

    private void exprp() throws Exception {
        //GUIDA(exprp -> +termexprp) = {+}
        //GUIDA(exprp -> -termexprp) = {-}
        //GUIDA(exprp -> epsilon) = {)}
        switch (look.tag) {
            case '+':
                match('+');
                term();
                exprp();
                break;
            case '-':
                match('-');
                term();
                exprp();
                break;
            case ')':
            case Tag.EOF:
                break;
            default:
                error("exprp error");
                break;
        }
    }

    private void term() throws Exception {
        //GUIDA(term -> facttermp) = {(,NUM}
        switch (look.tag) {
            case '(':
            case Tag.NUM:
                fact();
                termp();    
                break;
            default:
                error("Missing term");
                break;
        }
    }

    private void termp() throws Exception {
        //GUIDA(termp -> *facttermp) = {*}
        //GUIDA(termp -> /facttermp) = {/}
        //GUIDA(termp -> epsilon) = {+,-,)}
        switch (look.tag) {
            case '*':
                match('*');
                fact();
                termp();
                break;
            case '/':
                match('/');
                fact();
                termp();
                break;
            case '+':
            case '-':
            case ')':
            case Tag.EOF:
                break;
            default:
                error("Termp error");
                break;
        }
    }

    private void fact() throws Exception {
          switch (look.tag) {
            case '(':
                match('(');
                expr();
                match(')');
                break;
            case Tag.NUM:
                match(look.tag);
                break;
            default:
                error("Fact error");
                break;
        }
    }

    public static void main(String[] args) throws Exception {
        Lexer lex = new Lexer();
        String path = "/home/edoardo/Progs/UniProjects/LFT/Lab5/data.txt"; 
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Parser parser = new Parser(lex, br);
            parser.start();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}