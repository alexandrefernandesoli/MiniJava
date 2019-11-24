package compiler;

public class Token {
    public EnumToken name;
    public EnumToken attribute;
    public String lexeme;
    public int lineNumber;

    public Token(EnumToken name) {
        this.name = name;
        lineNumber = -1;
    }

    public Token(EnumToken name, EnumToken att) {
        this.name = name;
        attribute = att;
    }
    
    @Override
    public String toString(){
        return "<" + name + ", " + attribute + ", " + lexeme + ">" ;
    }
}
