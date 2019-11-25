package compiler;

public class Parser {
    private Token lToken;
    private Scanner scanner;
    private SymbolTable globalST;
    private SymbolTable currentST;
    
    public Parser(String inputFile){
        globalST = new SymbolTable<STEntry>();
        initSymbolTable();
        currentST = globalST;
        scanner = new Scanner(globalST, inputFile);
        advance();
    }
    
    public String execute(){
        try
        {
            program();
        }
        catch(CompilerException e)
        {
            return e.toString();        
        }
        return "Compilado com sucesso!";
    }
    
    private void initSymbolTable(){
        globalST.add(new STEntry(EnumToken.BOOLEAN, "boolean", true));
        globalST.add(new STEntry(EnumToken.CLASS, "class", true));
        globalST.add(new STEntry(EnumToken.ELSE, "else", true));
        globalST.add(new STEntry(EnumToken.EXTENDS, "extends", true));
        globalST.add(new STEntry(EnumToken.FALSE, "false", true));
        globalST.add(new STEntry(EnumToken.IF, "if", true));
        globalST.add(new STEntry(EnumToken.INT, "int", true));
        globalST.add(new STEntry(EnumToken.LENGTH, "lenght", true));
        globalST.add(new STEntry(EnumToken.MAIN, "main", true));
        globalST.add(new STEntry(EnumToken.NEW, "new", true));
        globalST.add(new STEntry(EnumToken.PUBLIC, "public", true));
        globalST.add(new STEntry(EnumToken.RETURN, "return", true));
        globalST.add(new STEntry(EnumToken.STATIC, "static", true));
        globalST.add(new STEntry(EnumToken.STRING, "String", true));
        globalST.add(new STEntry(EnumToken.THIS, "this", true));
        globalST.add(new STEntry(EnumToken.TRUE, "true", true));
        globalST.add(new STEntry(EnumToken.VOID, "void", true));
        globalST.add(new STEntry(EnumToken.WHILE, "while", true));
    }
    
    private void advance(){
        System.out.println(lToken);
        lToken = scanner.nextToken();
    }
    
    private void match(EnumToken nameToken){ 
        if(nameToken == lToken.name)
            advance();
        else if(lToken.attribute != null){
            if(nameToken == lToken.attribute)
                advance();
            else if(nameToken == EnumToken.SCOLON){
                throw new CompilerException("Erro na linha "+lToken.lineNumber+". Token ';' era esperado.");
            }
            else
                throw new CompilerException("Erro na linha "+lToken.lineNumber+". Token '"+lToken.lexeme+"' inesperado. O esperado era '"+nameToken+"'.");
        }
        else
        {            
            if(lToken.name != EnumToken.EOF)
                throw new CompilerException("Erro na linha "+lToken.lineNumber+". Token '"+ lToken.lexeme+"' inesperado.");
            else
                throw new CompilerException("Token " + lToken.name + " inesperado.");
        } 
    }
    
    private void program() throws CompilerException{
        mainClass();
        classDeclLinha();
    }
    
    private void mainClass(){
        if(lToken.name == EnumToken.CLASS){
            advance();
            if(lToken.name == EnumToken.ID)
                advance();
            else
                throw new CompilerException("Erro na linha "+lToken.lineNumber+". Identificador esperado.");
            match(EnumToken.LBRACES);
            match(EnumToken.PUBLIC);
            match(EnumToken.STATIC);
            match(EnumToken.VOID);
            match(EnumToken.MAIN);
            match(EnumToken.LPARENTHESE);
            match(EnumToken.STRING);
            match(EnumToken.LBRACKET);
            match(EnumToken.RBRACKET);
            if(lToken.name == EnumToken.ID)
                advance();
            else
                throw new CompilerException("Erro na linha "+lToken.lineNumber+". Identificador esperado.");
            match(EnumToken.RPARENTHESE);
            match(EnumToken.LBRACES);
            statement();
            match(EnumToken.RBRACES);
            match(EnumToken.RBRACES);
        }else{
            throw new CompilerException("Erro na linha 1. Classe main esperada.");
        }
    }
    
    private void statement(){
        if(lToken.attribute == EnumToken.LBRACES){
            advance();
            statementNotNecessary();
            match(EnumToken.RBRACES);
        }else if(lToken.name == EnumToken.IF){
            advance();
            match(EnumToken.LPARENTHESE);
            expression();
            match(EnumToken.RPARENTHESE);
            statement();
            match(EnumToken.ELSE);
            statement();
        }else if(lToken.name == EnumToken.WHILE){
            advance();
            match(EnumToken.LPARENTHESE);
            expression();
            match(EnumToken.RPARENTHESE);
            statement();
        }else if(lToken.name == EnumToken.ID){
            if(lToken.lexeme.equals("System")){
                advance();
                if(lToken.attribute == EnumToken.DOT){
                    advance();
                    if(lToken.name == EnumToken.ID && lToken.lexeme.equals("out")){
                        advance();
                        if(lToken.attribute == EnumToken.DOT){
                            advance();
                            if(lToken.name == EnumToken.ID && lToken.lexeme.equals("println")){
                                advance();
                                match(EnumToken.LPARENTHESE);
                                expression();
                                match(EnumToken.RPARENTHESE);
                                match(EnumToken.SCOLON);
                            }
                        }
                    }
                }
            }else{
                advance();
            }
            if(lToken.attribute == EnumToken.ATT){
                advance();
                expression();
                match(EnumToken.SCOLON);
            }else if(lToken.attribute == EnumToken.LBRACKET){
                advance();
                expression();
                match(EnumToken.RBRACKET);
                match(EnumToken.ATT);
                expression();
                match(EnumToken.SCOLON);
            }
        }else{
            throw new CompilerException("Erro na linha "+lToken.lineNumber+". Declaração esperada.");
        }
    }
    
    private void statementNotNecessary(){  
        if(lToken.attribute == EnumToken.LBRACES || lToken.name == EnumToken.ID || lToken.name == EnumToken.IF || lToken.name == EnumToken.WHILE){
            statement();
            statementNotNecessary();
        }
    }
    
    private void expression(){
        if(lToken.name == EnumToken.INTEGER_LITERAL ||
           lToken.name == EnumToken.TRUE ||
           lToken.name == EnumToken.FALSE ||
           lToken.name == EnumToken.ID ||
           lToken.name == EnumToken.THIS){
            advance();
            expressionLine();
        }else if(lToken.name == EnumToken.NEW){
            advance();
            if(lToken.name == EnumToken.INT){
                advance();
                match(EnumToken.LBRACKET);
                expression();
                match(EnumToken.RBRACKET);
                expressionLine();
            }else if(lToken.name == EnumToken.ID){
                advance();
                match(EnumToken.LPARENTHESE);
                match(EnumToken.RPARENTHESE);
                expressionLine();
            }
        }else if(lToken.attribute == EnumToken.NEG){
            advance();
            expression();
            expressionLine();
        }else if(lToken.attribute == EnumToken.LPARENTHESE){
            advance();
            expression();
            match(EnumToken.RPARENTHESE);
            expressionLine();
        }else{
            throw new CompilerException("Erro na linha "+lToken.lineNumber+". Expressão esperada.");
        }
    }
    
    private void firstExpression(){
        if(lToken.name == EnumToken.INTEGER_LITERAL ||
           lToken.name == EnumToken.TRUE ||
           lToken.name == EnumToken.FALSE ||
           lToken.name == EnumToken.ID ||
           lToken.name == EnumToken.THIS ||
           lToken.name == EnumToken.NEW ||
           lToken.attribute == EnumToken.NEG ||
           lToken.attribute == EnumToken.LPARENTHESE){
            expression();
            if(lToken.attribute == EnumToken.COLON){
                advance();
                multiExpression();
            }
        }
    }
    
    private void multiExpression(){
        if(lToken.name == EnumToken.INTEGER_LITERAL ||
           lToken.name == EnumToken.TRUE ||
           lToken.name == EnumToken.FALSE ||
           lToken.name == EnumToken.ID ||
           lToken.name == EnumToken.THIS ||
           lToken.name == EnumToken.NEW ||
           lToken.attribute == EnumToken.NEG ||
           lToken.attribute == EnumToken.LPARENTHESE){
            expression();
            if(lToken.attribute == EnumToken.COLON){
                advance();
                multiExpression();
            }
        }else{
            throw new CompilerException("Erro na linha "+lToken.lineNumber+". Expressão esperada.");
        }
    }
    
    private void expressionLine(){
        if(operation()){
            expression();
            expressionLine();
        }else if(lToken.attribute == EnumToken.LBRACKET){
            advance();
            expression();
            match(EnumToken.RBRACKET);
            expressionLine();
        }else if(lToken.name == EnumToken.SEP && lToken.attribute == EnumToken.DOT){
            advance();
            if(lToken.name == EnumToken.LENGTH){
                advance();
                expressionLine();
            }else if(lToken.name == EnumToken.ID){
                advance();
                match(EnumToken.LPARENTHESE);
                multiExpression();
                match(EnumToken.RPARENTHESE);
            }
        }
    }
    
    private boolean operation(){
        if(lToken.name == EnumToken.OP || lToken.name == EnumToken.RELOP){
            advance();
            System.out.println("Entroue");
            return true;
        }
        return false;
    }
    
    private void classDeclLinha(){
        if(lToken.name == EnumToken.CLASS){
            classDecl();
            classDeclLinha();
        }
        else if(lToken.name != EnumToken.EOF){
            throw new CompilerException("Erro na linha "+lToken.lineNumber+". Token '"+lToken.lexeme+"' inesperado.");
        }
    }
    
    private void classDecl(){
        if(lToken.name == EnumToken.CLASS){
            advance();
            if(lToken.name == EnumToken.ID){
                advance();
            }
            else
                throw new CompilerException("Erro na linha "+lToken.lineNumber+". Identificador esperado.");
            if(lToken.name == EnumToken.EXTENDS){
                advance();
                if(lToken.name == EnumToken.ID)
                    advance();
                else
                    throw new CompilerException("Erro na linha "+lToken.lineNumber+". Identificador esperado.");
            }
            match(EnumToken.LBRACES);
            varDeclarationLine();
            methodDeclarationLine();
            match(EnumToken.RBRACES);   
        }
    }
    
    private void methodDeclarationLine(){
        if(lToken.name == EnumToken.PUBLIC){
            methodDeclaration();
            methodDeclarationLine();
        }
    }
    
    private void methodDeclaration(){      
        if(lToken.name == EnumToken.PUBLIC){
            advance();
            type();
            if(lToken.name == EnumToken.ID){
                advance();
            }else{
                throw new CompilerException("Erro na linha "+lToken.lineNumber+". Identificador esperado.");
            }
            match(EnumToken.LPARENTHESE);
            methodParameters();
            match(EnumToken.RPARENTHESE);
            match(EnumToken.LBRACES);
            varDeclarationLine();
            statementNotNecessary();
            match(EnumToken.RETURN);
            expression();
            match(EnumToken.SCOLON);
            match(EnumToken.RBRACES);
        }
    }
    
    private void methodParameters(){
        if(lToken.name == EnumToken.INT || lToken.name == EnumToken.BOOLEAN || lToken.name == EnumToken.ID){
            type();
            if(lToken.name == EnumToken.ID){
                advance();
                if(lToken.attribute == EnumToken.COLON){
                    advance();
                    methodParameters();
                }
            }else{
                throw new CompilerException("Erro na linha "+lToken.lineNumber+". Identificador esperado.");
            }
        }
    }
    
    private void varDeclarationLine(){
        if(lToken.name == EnumToken.INT || lToken.name == EnumToken.BOOLEAN || lToken.name == EnumToken.ID){
            varDeclaration();
            varDeclarationLine();
        }
    }
    
    private void varDeclaration(){
        type();
        if(lToken.name == EnumToken.ID){    
            advance(); 
        }else{
            throw new CompilerException("Erro na linha "+lToken.lineNumber+". Identificador esperado.");
        }
        match(EnumToken.SCOLON);
    }
    
    private void type(){
        if(lToken.name == EnumToken.INT){
            advance();
            if(lToken.attribute == EnumToken.LBRACKET){
                advance();
                match(EnumToken.RBRACKET);
            }
        }else if(lToken.name == EnumToken.BOOLEAN || lToken.name == EnumToken.ID){
            advance();
        }else{
            throw new CompilerException("Erro na linha "+lToken.lineNumber+". Tipo esperado.");
        }
    } 
}
