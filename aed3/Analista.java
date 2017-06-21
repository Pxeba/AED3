package aed3;
import java.io.*;
import java.text.NumberFormat;

public class Analista implements Registro, Comparable {
    
    protected int codigo;
    protected String nome;
    protected String email;
    
    public Analista() 
    {
        codigo = -1;
        nome = "";
        email = "";

    }//fim construtor()
    
    public Analista( int codigo , String nome, String email )
    {
        this.codigo = codigo;
        this.nome = nome;
        this.email = email;
        
    
    }//fim construtor()
    
    public String toString() 
    {
        return "\nCódigo.: "+codigo+"\n"+
                "Título: " + nome+"\n"+
                "email.: " + email+ "\n";
    
    }//fim toString()
    
    public String getChaveSecundaria() 
    {
        return this.nome;
    }
    
    public void setCodigo( int codigo )
    {
        this.codigo = codigo;
    }
    public int getCodigo()
    {
        return codigo;
    }
    
    public byte[] getRegistro() throws IOException 
    {
        ByteArrayOutputStream ba = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(ba);
       
        out.writeInt(codigo);
        out.writeUTF(nome);
        out.writeUTF(email);

        return ba.toByteArray();
    
    }//fim getRegistro()
    
    public void setRegistro(byte[] registro) throws IOException 
    {
        ByteArrayInputStream ba = new ByteArrayInputStream(registro);
        DataInputStream in = new DataInputStream(ba);
        
        codigo = in.readInt();
        nome = in.readUTF();
        email = in.readUTF();
    
    }//fim setRegistro()
  
    public int compareTo( Object  obj  ) 
    {   
        if( codigo < ((Analista)obj).codigo )
        {
            return -1;
        }
        else if( codigo == ((Analista)obj).codigo )
        {
            return 0;
        }
        else
        {
            return 0;
        } 
    
    }//fim compareTo()
        
    public Object clone() throws CloneNotSupportedException 
    {    
        return super.clone();
    
    }//fim clone()

}//fim class Analista
