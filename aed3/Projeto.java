package aed3;
import java.io.*;
import java.text.NumberFormat;

public class Projeto implements Registro, Comparable {
    
    protected int codigo;
    protected String nomeProjeto;
    protected String nomeCliente;
    protected String dataInicio;
    protected String dataFim;
    
    public Projeto() 
    {
        codigo = -1;
        nomeProjeto = "";
        nomeCliente = "";
        dataInicio = "";
        dataFim = "";
    
    }//fim construtor()
    
    public Projeto( int codigo , String nomeProjeto, String nomeCliente, String dataInicio , String dataFim )
    {
        this.codigo = codigo;
        this.nomeProjeto = nomeProjeto;
        this.nomeCliente = nomeCliente;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
    
    }//fim construtor()
    
    public String toString() 
    {
        return "\nCódigo.: "+codigo+"\n"+
                "Título: " + nomeProjeto+"\n"+
                "nomeCliente.: " + nomeCliente+ "\n" +
                "DataInicio: " + dataInicio + "\n" +
                "DataFim:" + dataFim + "\n";
    
    }//fim toString()
    
    public String getChaveSecundaria() 
    {
        return this.nomeProjeto;
    }
    
    public void setCodigo( int codigo )
    {
        this.codigo = codigo;
    }
    public int getCodigo()
    {
        return codigo;
    }
    
    public int getCodigoProjeto()
    {
      return getCodigo();
    }
    
    public byte[] getRegistro() throws IOException 
    {
        ByteArrayOutputStream ba = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(ba);
       
        out.writeInt(codigo);
        out.writeUTF(nomeProjeto);
        out.writeUTF(nomeCliente);
        out.writeUTF(dataInicio);
        out.writeUTF(dataFim);

        return ba.toByteArray();
    
    }//fim getRegistro()
    
    public void setRegistro(byte[] registro) throws IOException 
    {
        ByteArrayInputStream ba = new ByteArrayInputStream(registro);
        DataInputStream in = new DataInputStream(ba);
        
        codigo = in.readInt();
        nomeProjeto = in.readUTF();
        nomeCliente = in.readUTF();
        dataInicio = in.readUTF();
        dataFim = in.readUTF();
    
    }//fim setRegistro()
  
    public int compareTo( Object  obj  ) 
    {   
        if( codigo < ((Projeto)obj).codigo )
        {
            return -1;
        }
        else if( codigo == ((Projeto)obj).codigo )
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

}//fim class Projeto
