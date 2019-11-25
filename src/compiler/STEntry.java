package compiler;

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
