import java.io.*;

public class Translator {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    SymbolTable st = new SymbolTable();
    CodeGenerator code = new CodeGenerator();
    int count = 0;

    public Translator(Lexer l, BufferedReader br) throws Exception {
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

    public void prog() throws Exception {
        switch (look.tag) {// Guida(prog -> statlist$)={Assign, print, read, while, conditional, { }
            case Tag.ASSIGN:
            case Tag.PRINT:
            case Tag.READ:
            case Tag.WHILE:
            case Tag.COND:
            case '{':
                int lnext_prog = code.newLabel();
                statlist(lnext_prog);
                code.emitLabel(lnext_prog);
                match(Tag.EOF);
                try {
                    code.toJasmin();
                } catch (java.io.IOException e) {
                    System.out.println("IO error\n");
                }
                break;
            default:
                error("prog error");
                break;
        }
    }

    private void statlist(int lnext_prog) throws Exception {
        switch (look.tag) {
            case Tag.ASSIGN:
            case Tag.PRINT:
            case Tag.READ:
            case Tag.WHILE:
            case Tag.COND:
            case '{':
                stat(lnext_prog);
                statlistp(lnext_prog);
                break;

            default:
                error("statlist error");
                break;
        }
    }

    private void statlistp(int lnext_statlistp) throws Exception {
        switch (look.tag) {
            case ';':
                match(';');
                stat(lnext_statlistp);
                statlistp(lnext_statlistp);
                break;
            case Tag.EOF:
            case '}':
                break;
            default:
                error("statlistp error");
                break;
        }
    }

    public void stat(int lnext_stat) throws Exception {
        switch (look.tag) {
            case Tag.ASSIGN:
                match(Tag.ASSIGN);
                expr(lnext_stat);
                match(Tag.TO);
                idlist(lnext_stat);
                break;
            case Tag.PRINT:
                match(Tag.PRINT);
                match('[');
                exprlist(lnext_stat);
                match(']');
                code.emit(OpCode.invokestatic, 1);
                break;
            case Tag.READ:
                match(Tag.READ);
                code.emit(OpCode.invokestatic, 0);
                match('[');
                idlist(lnext_stat);
                match(']');
                break;
            case Tag.WHILE:
                int loop = code.newLabel();
                int endloop = code.newLabel();
                match(Tag.WHILE);
                match('(');
                code.emitLabel(loop);
                bexpr(endloop);
                match(')');
                stat(lnext_stat);
                code.emit(OpCode.GOto, loop);
                code.emit(OpCode.label, endloop);
                break;
            case Tag.COND:
                match(Tag.COND);
                match('[');
                int final_label = code.newLabel();
                optlist(final_label);
                match(']');
                stat_prime(final_label);
                code.emitLabel(final_label);
                break;
            case '{':
                match('{');
                statlist(lnext_stat);
                match('}');
                break;
            default:
                error("stat error");
                break;
        }
    }

    private void stat_prime(int lnext_stat_prime) throws Exception {
        switch (look.tag) {
            case Tag.END:
                match(Tag.END);
                break;
            case Tag.ELSE:
                match(Tag.ELSE);
                stat(lnext_stat_prime);
                match(Tag.END);
                break;
            default:
                error("stat_prime error");
                break;
        }
    }

    private void idlist(int lnext_idlist) throws Exception {
        switch (look.tag) {
            case Tag.ID:
                int id_addr = st.lookupAddress(((Word) look).lexeme);
                if (id_addr == -1) {
                    id_addr = count;
                    st.insert(((Word) look).lexeme, count++);
                }
                match(Tag.ID);
                code.emit(OpCode.istore, id_addr);
                idlist_p(lnext_idlist);
                break;
            default:
                error("idlist error");
                break;
        }
    }

    private void idlist_p(int lnext_idlist_p) throws Exception {
        switch (look.tag) {
            case ',':
                match(',');
                code.emit(OpCode.iload, (count - 1));//al posto di dup
                int id_addr = st.lookupAddress(((Word) look).lexeme);
                if (id_addr == -1) {
                    id_addr = count;
                    st.insert(((Word) look).lexeme, count++);
                }
                match(Tag.ID);
                code.emit(OpCode.istore, id_addr);
                idlist_p(lnext_idlist_p);
                break;
            case ']':
            case '}':
            case ';':
            case Tag.EOF:
                break;
            default:
                error("idlist_p error");
                break;
        }
    }

    private void exprlist(int lnext_exprlist) throws Exception {
        switch (look.tag) {
            case '+':
            case '-':
            case '*':
            case '/':
            case Tag.NUM:
            case Tag.ID:
                expr(lnext_exprlist);
                exprlist_p(lnext_exprlist);
                break;
            default:
                error("exprlist error");
                break;
        }
    }

    private void exprlist_p(int lnext_exprlistp) throws Exception {
        switch (look.tag) {
            case ',':
                match(',');
                expr(lnext_exprlistp);
                exprlist_p(lnext_exprlistp);
                break;
            case ')':
            case ']':
                break;
            default:
                error("exprlist_p error");
                break;
        }
    }

    private void expr(int lnext_expr) throws Exception {
        switch (look.tag) {
            case '+':
                match('+');
                match('(');
                exprlist(lnext_expr);
                match(')');
                code.emit(OpCode.iadd);
                break;
            case '-':
                match('-');
                expr(lnext_expr);
                expr(lnext_expr);
                code.emit(OpCode.isub);
                break;
            case '*':
                match('*');
                match('(');
                exprlist(lnext_expr);
                match(')');
                code.emit(OpCode.imul);
                break;
            case '/':
                match('/');
                expr(lnext_expr);
                expr(lnext_expr);
                code.emit(OpCode.idiv);
                break;
            case Tag.NUM:
                code.emit(OpCode.ldc, ((NumberTok) look).value);
                match(Tag.NUM);
                break;
            case Tag.ID:
                code.emit(OpCode.iload, st.lookupAddress(((Word) look).lexeme));
                match(Tag.ID);
                break;
            default:
                error("expr error");
                break;
        }
    }

    private void bexpr(int lnext_bexpr) throws Exception {
        switch (look.tag) {
            case Tag.RELOP:
                switch (((Word) look).lexeme) {
                    case ">":
                        match(Tag.RELOP);
                        expr(lnext_bexpr);
                        expr(lnext_bexpr);
                        code.emit(OpCode.if_icmple, lnext_bexpr);
                        break;
                    case ">=":
                        match(Tag.RELOP);
                        expr(lnext_bexpr);
                        expr(lnext_bexpr);
                        code.emit(OpCode.if_icmplt, lnext_bexpr);
                        break;
                    case "<":
                        match(Tag.RELOP);
                        expr(lnext_bexpr);
                        expr(lnext_bexpr);
                        code.emit(OpCode.if_icmpge, lnext_bexpr);
                        break;
                    case "<=":
                        match(Tag.RELOP);
                        expr(lnext_bexpr);
                        expr(lnext_bexpr);
                        code.emit(OpCode.if_icmpgt, lnext_bexpr);
                        break;
                    case "<>":// !=
                        match(Tag.RELOP);
                        expr(lnext_bexpr);
                        expr(lnext_bexpr);
                        code.emit(OpCode.if_icmpeq, lnext_bexpr);
                        break;
                    case "==":
                        match(Tag.RELOP);
                        expr(lnext_bexpr);
                        expr(lnext_bexpr);
                        code.emit(OpCode.if_icmpne, lnext_bexpr);
                        break;
                    default:
                        error("incorrect boolean operand");
                        break;
                }
                break;
            case 33:
                match(33);
                switch (((Word) look).lexeme) {
                    case ">":
                        match(Tag.RELOP);
                        expr(lnext_bexpr);
                        expr(lnext_bexpr);
                        code.emit(OpCode.if_icmpgt, lnext_bexpr);
                        break;
                    case ">=":
                        match(Tag.RELOP);
                        expr(lnext_bexpr);
                        expr(lnext_bexpr);
                        code.emit(OpCode.if_icmpge, lnext_bexpr);
                        break;
                    case "<":
                        match(Tag.RELOP);
                        expr(lnext_bexpr);
                        expr(lnext_bexpr);
                        code.emit(OpCode.if_icmplt, lnext_bexpr);
                        break;
                    case "<=":
                        match(Tag.RELOP);
                        expr(lnext_bexpr);
                        expr(lnext_bexpr);
                        code.emit(OpCode.if_icmple, lnext_bexpr);
                        break;
                    case "<>":// !=
                        match(Tag.RELOP);
                        expr(lnext_bexpr);
                        expr(lnext_bexpr);
                        code.emit(OpCode.if_icmpne, lnext_bexpr);
                        break;
                    case "==":
                        match(Tag.RELOP);
                        expr(lnext_bexpr);
                        expr(lnext_bexpr);
                        code.emit(OpCode.if_icmpeq, lnext_bexpr);
                        break;
                    default:
                        error("incorrect boolean operand");
                        break;
                }
                break;
            case Tag.AND:
                match(Tag.AND);
                bexpr(lnext_bexpr);
                bexpr(lnext_bexpr);
                break;
            case Tag.OR:
                match(Tag.OR);
                int ftrue = code.newLabel();
                int ffalse = code.newLabel();
                bexpr(ffalse);
                code.emit(OpCode.GOto, ftrue);
                code.emitLabel(ffalse);
                bexpr(lnext_bexpr);
                code.emitLabel(ftrue);
                break;
            default:
                error("bexpr error");
                break;
        }
    }

    private void optlist(int final_label) throws Exception {
        switch (look.tag) {
            case Tag.OPTION:
                optitem(final_label);
                optlist_p(final_label);
                break;
            default:
                error("optlist error");
                break;
        }
    }

    private void optlist_p(int final_label) throws Exception {
        switch (look.tag) {
            case Tag.OPTION:

                optitem(final_label);
                optlist_p(final_label);
                break;
            case ']':
                break;
            default:
                error("optlist_p error");
                break;
        }
    }

    private void optitem(int final_label) throws Exception {
        switch (look.tag) {
            case Tag.OPTION:
                int lnext_opititem = code.newLabel();
                match(Tag.OPTION);
                match('(');
                bexpr(lnext_opititem);
                match(')');
                match(Tag.DO);
                stat(lnext_opititem);
                code.emit(OpCode.GOto, final_label);// jump to end if
                code.emitLabel(lnext_opititem);
                break;
            default:
                error("optitem error");
                break;
        }
    }

    public static void main(String[] args) throws Exception {
        Lexer lex = new Lexer();
        String path = "/home/edoardo/Progs/UniProjects/LFT/Lab9/5.2-5.3/testorale.lft";
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Translator translator = new Translator(lex, br);
            translator.prog();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}