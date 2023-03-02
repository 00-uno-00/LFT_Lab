import java.io.*;

//fix stuff
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

    private void prog() throws Exception {
        switch (look.tag) {
            case Tag.ASSIGN:
            case Tag.PRINT:
            case Tag.READ:
            case Tag.WHILE:
            case Tag.COND:
            case '{':
                statlist();
                match(Tag.EOF);
                break;
            default:
                error("start error");
                break;
        }

    }

    private void statlist() throws Exception {
        switch (look.tag) {
            case Tag.ASSIGN:
            case Tag.PRINT:
            case Tag.READ:
            case Tag.WHILE:
            case Tag.COND:
            case '{':
                stat();
                statlistp();
                break;
            default:
                error("staatlist error");
                break;
        }
    }

    private void statlistp() throws Exception {
        switch (look.tag) {
            case ';':
                match(';');
                stat();
                statlistp();
                break;
            case Tag.EOF:
            case '}':
                break;
            default:
                error("statlistp error");
                break;
        }
    }

    private void stat() throws Exception {
        switch (look.tag) {
            case Tag.ASSIGN:
                match(Tag.ASSIGN);
                expr();
                match(Tag.TO);
                idlist();
                break;
            case Tag.PRINT:
                match(Tag.PRINT);
                match('[');
                exprlist();
                match(']');
                break;
            case Tag.READ:
                match(Tag.READ);
                match('[');
                idlist();
                match(']');
                break;
            case Tag.WHILE:
                match(Tag.WHILE);
                match('[');
                bexpr();
                match(']');
                stat();
                break;
            case Tag.COND:
                match(Tag.COND);
                match('[');
                optlist();
                match(']');
                stat_p();
                break;
            case '{':
                match('{');
                statlist();
                match('}');
                break;
            default:
                error("stat error");
                break;
        }
    }

    private void stat_p() throws Exception {
        switch (look.tag) {
            case Tag.END:
                match(Tag.END);
                break;
            case Tag.ELSE:
                match(Tag.ELSE);
                stat();
                match(Tag.END);
            default:
                error("stat_p error");
                break;
        }
    }

    private void idlist() throws Exception {
        switch (look.tag) {
            case Tag.ID:
                match(Tag.ID);
                idlist_p();
                break;

            default:
                error("idlist error");
                break;
        }
    }

    private void idlist_p() throws Exception {
        switch (look.tag) {
            case ',':
                match(',');
                match(Tag.ID);
                idlist_p();
                break;
            case ']':
            case ';':
            case Tag.EOF:
                break;
            default:
                error("idlist error");
                break;
        }
    }

    private void optlist() throws Exception {
        switch (look.tag) {
            case Tag.OPTION:
                optitem();
                optlist_p();
                break;

            default:
                error("optlist error");
                break;
        }
    }

    private void optlist_p() throws Exception {
        switch (look.tag) {
            case Tag.OPTION:
                optitem();
                optlist_p();
                break;

            default:
                error("optlist_p error");
                break;
        }
    }

    private void optitem() throws Exception {
        switch (look.tag) {
            case Tag.OPTION:
                match(Tag.OPTION);
                match('(');
                bexpr();
                match(')');
                match(Tag.DO);
                stat();
                break;

            default:
                error("optitem error");
                break;
        }
    }

    private void bexpr() throws Exception {
        switch (look.tag) {
            case Tag.RELOP:
                match(Tag.RELOP);
                expr();
                expr();
                break;
            default:
                error("bexpr error");
                break;
        }
    }

    private void expr() throws Exception {
        switch (look.tag) {
            case '+':
                match('+');
                match('(');
                expr();
                match(')');
                break;
            case '-':
                match('-');
                expr();
                expr();
                break;
            case '*':
                match('*');
                match('(');
                expr();
                match(')');
                break;
            case '/':
                match('/');
                expr();
                expr();
                break;
            case Tag.NUM:
                match(Tag.NUM);
                break;
            case Tag.ID:
                match(Tag.ID);
                break;
            default:
                error("expr error");
                break;
        }
    }

    private void exprlist() throws Exception {
        switch (look.tag) {
            case '+':
            case '-':
            case '*':
            case '/':
            case Tag.NUM:
            case Tag.ID:
                expr();
                exprlist_p();
                break;
            default:
                error("exprlist error");
                break;
        }
    }

    private void exprlist_p() throws Exception {
        switch (look.tag) {
            case ',':
                match(',');
                expr();
                exprlist_p();
                break;
            case ')':
            case ']':
                break;
            default:
                error("exprlist_p error");
                break;
        }
    }

    public static void main(String[] args) throws Exception {
        Lexer lex = new Lexer();
        String path = "/home/edoardo/Progs/UniProjects/LFT/Lab6/data.txt";
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Parser parser = new Parser(lex, br);
            parser.prog();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}