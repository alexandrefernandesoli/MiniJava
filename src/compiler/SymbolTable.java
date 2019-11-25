/*  
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package compiler  ;

import java.util.Iterator;
import java.util.TreeMap;


public class SymbolTable<T extends STEntry> implements Iterable<T>
{
    SymbolTable<T> parent;//Referência à tabela pai (escopo imediatamente anterior).
    
    TreeMap<String, T> symbols;//Armazena os símbolos do escopo corrente.

    //Construtor que instancia uma nova tabela sem especificar uma tabela pai.
    SymbolTable()
    {
        symbols = new TreeMap<String, T>();
    }
    
    //Construtor que instancia uma nova tabela especificando uma tabela pai.
    SymbolTable(SymbolTable<T> p)
    {
        symbols = new TreeMap<String, T>();
        
        parent = p;
    }

    //Tenta adicionar um novo símbolo à tabela corrente. Primeiramente, verifica
    //se já existe uma entrada para o lexema na tabela corrente e, caso haja,
    //a inserção não é realizada e o método retorna false; caso contrário, a 
    //inserção é efetivada e true é retornado.
    public boolean add(T t)
    {
        //System.out.print(t.lexeme);
        
        if (symbols.containsKey(t.lexeme))
            return false;
        
        symbols.put(t.lexeme, t);
        return true;
    }

    //Tenta remover um símbolo da tabela.
    public boolean remove(String name)
    {
        return symbols.remove(name) != null;
    }

    //Limpa a tabela.
    public void clear()
    {
        symbols.clear();
    }

    //Verifica se a tabela está vazia.
    public boolean isEmpty()
    {
        return symbols.isEmpty();
    }

    //Busca uma entrada equivalente ao lexema passado como parâmetro. A busca se
    //inicia no escopo corrente e sobe na hierarquia enquanto não encontrar o
    //símbolo, possivelmente até o escopo global. Retorna uma referência à 
    //entrada encontrada ou null, caso o símbolo não esteja presente.
    public T get(String name)
    {
        T s;
        SymbolTable<T> table = this;

        do
        {
            s = table.symbols.get(name);
        } while (s == null && (table = table.parent) != null);
        
        return s;
    }

    //Retorna um objeto iterador da tabela.
    public Iterator<T> iterator()
    {
        return symbols.values().iterator();
    } 
}
    