package aed3;
import java.io.*;
import java.text.NumberFormat;

public class Tarefa implements Registro, Comparable {
   protected int codigo;
   protected String descricao;
   protected int codigoProjeto;
   protected String codigoAnalista;
   protected String dataInicio;
   protected String dataLimite;
   
   public Tarefa() 
   {
      codigo = -1;
      descricao = "";
      codigoProjeto = 0;
      codigoAnalista = ""+0;
      dataInicio = "";
      dataLimite = "";
   
   }//fim construtor()
   
   public Tarefa( int codigo , String descricao, int codigoProjeto, String codigoAnalista , String dataInicio , String dataLimite )
   {
      this.codigo = codigo;
      this.descricao = descricao;
      this.codigoProjeto = codigoProjeto;
      this.codigoAnalista = codigoAnalista;
      this.dataInicio = dataInicio;
      this.dataLimite = dataLimite;
   
   }//fim construtor()
   
   public String toString() 
   {
      return  "Codigo da Tarefa: " +codigo +"\n"+
             "Descricao: " + descricao +"\n"+
             "codigoProjeto.: " + codigoProjeto +"\n"+
             "codigoAnalista: " + codigoAnalista +"\n"+
              "dataInicio:" + dataInicio +"\n"+
              "dataLimite:" + dataLimite +"\n";
   
   }//fim toString()
   
   public String toString(int a) 
   {
      if(a == 1)
      {
         return  "Codigo da Tarefa: " +codigo +"\n"+
            "codigoAnalista: " + codigoAnalista +"\n";
      }
      else
      {
         return "Codigo da Tarefa: " +codigo +"\n"+
            "codigoProjeto.: " + codigoProjeto +"\n";
      
      
      }
   }
   
     
   public String getChaveSecundaria() 
   {
      return this.codigoAnalista;
   }
   
   public int getCodigoProjeto()
   {
      return this.codigoProjeto;
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
      out.writeUTF(descricao);
      out.writeInt(codigoProjeto);
      out.writeUTF(codigoAnalista);
      out.writeUTF(dataInicio);
      out.writeUTF(dataLimite);
   
      return ba.toByteArray();
   
   }//fim getRegistro()
   
   public void setRegistro(byte[] registro) throws IOException 
   {
      ByteArrayInputStream ba = new ByteArrayInputStream(registro);
      DataInputStream in = new DataInputStream(ba);
      
      codigo = in.readInt();
      descricao = in.readUTF();
      codigoProjeto = in.readInt();
      codigoAnalista = in.readUTF();
      dataInicio = in.readUTF();
      dataLimite = in.readUTF();
   
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
   
   public void setCodigoProjeto(int a)
   {
      this.codigoProjeto = a;
   }
   
   public void setCodigoAnalista(String a)
   {
      this.codigoAnalista = a;
   }

}//fim class Projeto