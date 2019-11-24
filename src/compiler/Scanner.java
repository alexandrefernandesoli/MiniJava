package compiler;

public class Scanner {

    private int pos;
    private final String entrada;
    private int lineNumber;
    private final SymbolTable st;

    public Scanner(SymbolTable globalST, String inputText) {
        st = globalST;
        entrada = inputText;
        pos = 0;
        lineNumber = 1;
    }

    public Token nextToken() {
        Token tok;
        String lexema = "";
        int state;
        char charAtual;

        if (pos == entrada.length()) {
            tok = new Token(EnumToken.EOF);
            return tok;
        }

        state = 0;
        charAtual = entrada.charAt(pos);

        while (true) {
            if (pos < entrada.length()) {
                charAtual = entrada.charAt(pos);
            }

            switch (state) {
                case 0:
                    if (pos >= entrada.length()) {
                        tok = new Token(EnumToken.EOF);
                        return tok;
                    }

                    if (pos < entrada.length()) {
                        if (java.lang.Character.isLetter(charAtual)) {
                            lexema += charAtual;
                            state = 1;

                        } else if (charAtual == '<') {
                            lexema += charAtual;
                            state = 4;
                        } else if (charAtual == '>') {
                            lexema += charAtual;
                            state = 7;
                        } else if (charAtual == '+') {
                            lexema += charAtual;
                            state = 10;
                        } else if (charAtual == '-') {
                            lexema += charAtual;
                            state = 11;
                        } else if (charAtual == '*') {
                            lexema += charAtual;
                            state = 12;
                        } else if (charAtual == '/') {
                            state = 13;
                        } else if (charAtual == '=') {
                            lexema += charAtual;
                            state = 15;
                        } else if (charAtual == '!') {
                            lexema += charAtual;
                            state = 18;
                        } else if (charAtual == '&') {
                            lexema += charAtual;
                            state = 3;
                        } else if (java.lang.Character.isWhitespace(charAtual)) {
                            state = 21;
                        } else if (charAtual == '(') {
                            lexema += charAtual;
                            state = 24;
                        } else if (charAtual == ')') {
                            lexema += charAtual;
                            state = 25;
                        } else if (charAtual == '[') {
                            lexema += charAtual;
                            state = 26;
                        } else if (charAtual == ']') {
                            lexema += charAtual;
                            state = 27;
                        } else if (charAtual == '{') {
                            lexema += charAtual;
                            state = 28;
                        } else if (charAtual == '}') {
                            lexema += charAtual;
                            state = 29;
                        } else if (charAtual == ';') {
                            lexema += charAtual;
                            state = 30;
                        } else if (charAtual == '.') {
                            lexema += charAtual;
                            state = 31;
                        } else if (charAtual == ',') {
                            lexema += charAtual;
                            state = 32;
                        } else if (java.lang.Character.isDigit(charAtual)) {
                            lexema += charAtual;
                            state = 34;
                        } else {
                            throw new CompilerException("Erro na linha " + lineNumber + ". Token mal formado. Símbolo '" + charAtual + "' não esperado");
                        }

                    }
                    pos++;
                    break;

                case 1:
                    while (pos < entrada.length() && (java.lang.Character.isLetterOrDigit(charAtual) || charAtual == '_')) {
                        lexema += charAtual;
                        pos++;
                        if (pos < entrada.length()) {
                            charAtual = entrada.charAt(pos);
                        }
                    }
                    state = 2;
                    break;

                case 2:    //Reconhecimento de ID
                    STEntry entry;
                    entry = st.get(lexema);

                    if (entry != null) {
                        tok = new Token(entry.tokName);
                    } else {
                        tok = new Token(EnumToken.ID);
                    }
                    tok.lexeme = lexema;
                    tok.lineNumber = lineNumber;
                    return tok;

                case 3: // Reconhecimento and
                    if (pos < entrada.length()) {
                        if (charAtual == '&') {
                            lexema += charAtual;
                            pos++;
                            tok = new Token(EnumToken.RELOP, EnumToken.AND);
                            tok.lexeme = lexema;
                            tok.lineNumber = lineNumber;
                            return tok;
                        }
                    }
                case 4:
                    tok = new Token(EnumToken.RELOP, EnumToken.LT);
                    tok.lexeme = lexema;
                    tok.lineNumber = lineNumber;
                    return tok;

                case 7:
                    tok = new Token(EnumToken.RELOP, EnumToken.GT);
                    tok.lexeme = lexema;
                    tok.lineNumber = lineNumber;
                    return tok;

                case 10:    //Reconhecimento do op add
                    tok = new Token(EnumToken.OP, EnumToken.PLUS);
                    tok.lexeme = lexema;
                    tok.lineNumber = lineNumber;
                    return tok;

                case 11:    //Reconhecimento do op SUB
                    tok = new Token(EnumToken.OP, EnumToken.MINUS);
                    tok.lexeme = lexema;
                    tok.lineNumber = lineNumber;
                    return tok;

                case 12:    //Reconhecimento do op MUL
                    tok = new Token(EnumToken.OP, EnumToken.MULT);
                    tok.lexeme = lexema;
                    tok.lineNumber = lineNumber;
                    return tok;

                case 13:
                    if (pos < entrada.length()) {
                        if (charAtual == '*') {
                            state = 36;
                            pos++;
                        } else if (charAtual == '/') {
                            state = 39;
                            pos++;
                        } else {
                            state = 41;
                        }
                    } else if (pos == entrada.length()) {
                        if (charAtual == '/') {
                            lexema += charAtual;
                            state = 41;
                        }
                    }
                    break;

                case 15:
                    if (pos < entrada.length()) {
                        if (charAtual == '=') {      //Nesse if foi verificado se pos!=entrada.length() porque caso pos fosse igual ao tamanho da entrada
                            lexema += charAtual;
                            state = 16;
                            pos++;
                        } else {
                            state = 17;
                        }
                    } else if (pos == entrada.length()) {
                        state = 17;
                    }
                    break;

                case 16:    //Reconhecimento do relop EQ
                    tok = new Token(EnumToken.RELOP, EnumToken.EQ);
                    tok.lexeme = lexema;
                    tok.lineNumber = lineNumber;
                    return tok;

                case 17:    //Reconhecimento do op ATT
                    tok = new Token(EnumToken.OP, EnumToken.ATT);
                    tok.lexeme = lexema;
                    tok.lineNumber = lineNumber;
                    return tok;

                case 18:
                    if (pos <= entrada.length()) {
                        if (charAtual == '=') {
                            lexema += charAtual;
                            state = 19;
                        } else {
                            tok = new Token(EnumToken.RELOP, EnumToken.NEG);
                            tok.lexeme = lexema;
                            tok.lineNumber = lineNumber;
                            return tok;
                        }
                    }
                    pos++;
                    break;

                case 19:    //Reconhecimento do relop NE
                    tok = new Token(EnumToken.RELOP, EnumToken.NEQ);
                    tok.lexeme = lexema;
                    tok.lineNumber = lineNumber;
                    return tok;

                case 21:
                    while (pos < entrada.length() && java.lang.Character.isWhitespace(charAtual)) {
                        if (charAtual == '\n') {
                            lineNumber++;
                        }
                        pos++;
                        if (pos < entrada.length()) {
                            charAtual = entrada.charAt(pos);
                        }
                    }
                    state = 22;
                    break;

                case 22:
                    state = 0;
                    break;

                case 24:    //Reconhecimento do SEP '('
                    tok = new Token(EnumToken.SEP, EnumToken.LPARENTHESE);
                    tok.lexeme = lexema;
                    tok.lineNumber = lineNumber;
                    return tok;

                case 25:    //Reconhecimento do SEP ')' 
                    tok = new Token(EnumToken.SEP, EnumToken.RPARENTHESE);
                    tok.lexeme = lexema;
                    tok.lineNumber = lineNumber;
                    return tok;

                case 26:    //Reconhecimento do SEP '['
                    tok = new Token(EnumToken.SEP, EnumToken.LBRACKET);
                    tok.lexeme = lexema;
                    tok.lineNumber = lineNumber;
                    return tok;

                case 27:    //Reconhecimento do SEP ']'
                    tok = new Token(EnumToken.SEP, EnumToken.RBRACKET);
                    tok.lexeme = lexema;
                    tok.lineNumber = lineNumber;
                    return tok;

                case 28:    //Reconhecimento do SEP '{'
                    tok = new Token(EnumToken.SEP, EnumToken.LBRACES);
                    tok.lexeme = lexema;
                    tok.lineNumber = lineNumber;
                    return tok;

                case 29:    //Reconhecimento do SEP '}'
                    tok = new Token(EnumToken.SEP, EnumToken.RBRACES);
                    tok.lexeme = lexema;
                    tok.lineNumber = lineNumber;
                    return tok;

                case 30:    //Reconhecimento do SEP ';'
                    tok = new Token(EnumToken.SEP, EnumToken.SCOLON);
                    tok.lexeme = lexema;
                    tok.lineNumber = lineNumber;
                    return tok;

                case 31:    //Reconhecimento do SEP '.'
                    tok = new Token(EnumToken.SEP, EnumToken.DOT);
                    tok.lexeme = lexema;
                    tok.lineNumber = lineNumber;
                    return tok;

                case 32:    //Reconhecimento do SEP ','
                    tok = new Token(EnumToken.SEP, EnumToken.COLON);
                    tok.lexeme = lexema;
                    tok.lineNumber = lineNumber;
                    return tok;

                case 34:
                    while (pos < entrada.length() && java.lang.Character.isDigit(charAtual)) {
                        lexema += charAtual;
                        pos++;
                        if (pos < entrada.length()) {
                            charAtual = entrada.charAt(pos);
                        }
                    }
                    state = 35;
                    break;

                case 35:    //Reconhecimento de um inteiro
                    tok = new Token(EnumToken.INTEGER_LITERAL);
                    tok.lexeme = lexema;
                    tok.lineNumber = lineNumber;
                    return tok;

                case 36:
                    while (pos < entrada.length() && (charAtual != '*')) {
                        if (charAtual == '\n') {
                            lineNumber++;
                        }
                        pos++;
                        if (pos < entrada.length()) {
                            charAtual = entrada.charAt(pos);
                        }
                    }
                    if (pos < entrada.length()) {    //Verificando se o laço parou porque o '*' foi encontrado
                        pos++;
                    } else {
                        throw new CompilerException(String.format("Erro na linha %d. Comentário não fechado.", lineNumber));
                    }
                    state = 37;
                    break;

                case 37:
                    if (pos < entrada.length()) {
                        if (charAtual == '/') {
                            state = 38;
                        } else {
                            state = 36;
                        }
                    }
                    pos++;
                    break;

                case 38:
                    state = 0;
                    break;

                case 39:
                    while (pos < entrada.length() && entrada.charAt(pos) != '\n') {
                        pos++;
                        if (pos < entrada.length()) {
                            charAtual = entrada.charAt(pos);
                        }
                    }
                    if (pos < entrada.length()) {     //Verificando se o laço parou porque o '\n' foi encontrado
                        lineNumber++;
                        pos++;
                    }
                    state = 40;
                    break;

                case 40:
                    state = 0;
                    break;

                case 41:    //Reconhecimento de um op DIV
                    tok = new Token(EnumToken.OP, EnumToken.DIV);
                    tok.lexeme = lexema;
                    tok.lineNumber = lineNumber;
                    return tok;
            }
        }
    }
}
