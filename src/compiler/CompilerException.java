package compiler;

public class CompilerException extends RuntimeException
{
    private String msg;

    public CompilerException() 
    {       
        msg = "Unexpected";
    }
    
    public CompilerException(String str)
    {
        super(str);
        msg = str;
    }
    
    @Override
    public String toString()
    {
        return msg;
    }
}
