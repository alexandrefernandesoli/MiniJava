package compiler;

/**
 * @author Bianca de Almeida Dantas
 */
//Classe que especifica uma entrada da tabela de símbolos. Ela contém uma 
//string que representa o lexema reconhecido no processo de análise sintática (ou
//as palavras chaves inseridas no início da compilação.
public class STEntry 
{
    public EnumToken tokName;
    public String lexeme;//Lexema.
    public boolean reserved;//Indica se é palavra reservada.
    
    public STEntry()
    {}
    
    public STEntry(EnumToken name, String lex)
    {
        tokName = name;
        lexeme = lex;
        reserved = false;
    }
    
    public STEntry(EnumToken name, String lex, boolean res)
    {
        tokName =  name;
        lexeme = lex;
        reserved = res;
    }
}
