package compiler;

public enum EnumToken {
    /**
     * TOKEN NAMES
     */
    ID,
    INTEGER_LITERAL,
    RELOP,
    SEP,
    OP,
    RESERVED,

    /**
     *  OP ATTRIBUTES
     */
    
    PLUS,
    MINUS,
    MULT,
    DIV,
    
    
    /**
     * RELOP ATTRIBUTES
     */
    AND,
    LT,
    GT,
    ATT,
    EQ,
    NEQ,
    NEG,

    /**
     * SEP ATTRIBUTES
     */
    LPARENTHESE,
    RPARENTHESE,
    LBRACKET,
    RBRACKET,
    LBRACES,
    RBRACES,
    COLON,
    SCOLON,
    DOT,

    /**
     * RESERVED ATTRIBUTES
     */
    CLASS,
    BOOLEAN,
    ELSE,
    EXTENDS,
    FALSE,
    IF,
    INT,
    LENGTH,
    MAIN,
    NEW,
    PUBLIC,
    RETURN,
    STATIC,
    STRING,
    SYSOUT,
    THIS,
    TRUE,
    VOID,
    WHILE,

    /**
     * TRASHCAN
     */

    UNDEF,
    EOF,
}
