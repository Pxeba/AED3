package aed3;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArquivoIndexadoTarefa<T extends Registro> {
   
   String nomeArquivo, nomeIndicePrimario, nomeIndiceSecundario, nomeIndiceProjeto;
   RandomAccessFile arquivoDados;
   ArvoreB indicePrimario;
   ArvoreB indiceProjeto;
   ArvoreB_Sec indiceSecundario;
   boolean listagem = false;
   
   public ArquivoIndexadoTarefa(String na, String n1, String n2, String n3) throws Exception {
      nomeArquivo = na;
      nomeIndicePrimario = n1;
      nomeIndiceSecundario = n2;
      nomeIndiceProjeto = n3;
      arquivoDados = new RandomAccessFile(nomeArquivo,"rw");
      indicePrimario = new ArvoreB(20, nomeIndicePrimario);
      indiceSecundario = new ArvoreB_Sec(20, nomeIndiceSecundario);
      indiceProjeto = new ArvoreB(20,nomeIndiceProjeto);
      if( arquivoDados.length()<4 ) {
         arquivoDados.writeInt(0); // último código usado
         indicePrimario.apagar();
         indiceProjeto.apagar();
         indiceSecundario.apagar();
      }
   }
   
   public int incluir( T obj ) throws IOException {
      arquivoDados.seek(0);
      int codigo = arquivoDados.readInt()+1;
      
      obj.setCodigo( codigo );
      int a = obj.getCodigoProjeto();
      String b = obj.getChaveSecundaria();
      
      byte[] registro = obj.getRegistro();
      
      arquivoDados.seek(arquivoDados.length());
      long endereco = arquivoDados.getFilePointer();
      arquivoDados.writeInt(registro.length);
      arquivoDados.write(' ');
      arquivoDados.write(registro);
      indicePrimario.inserir(codigo, endereco);
      indiceProjeto.inserir(a,endereco);
      indiceSecundario.inserir(b, codigo);
      
      arquivoDados.seek(0);
      arquivoDados.writeInt(codigo);
   
      listagem = false;
      return codigo;
   }
   
   public int incluir2( Tarefa tarefa ) throws IOException {
      arquivoDados.seek(0);
      int codigo = arquivoDados.readInt()+1;
      
      tarefa.setCodigo( codigo );
      int a = tarefa.getCodigoProjeto();
      String b = tarefa.getChaveSecundaria();
      int c = Integer.parseInt(b)%100;
      String d = ""+c;
      tarefa.setCodigoProjeto(a%100);
      tarefa.setCodigoAnalista(d);
      
      byte[] registro = tarefa.getRegistro();
      
      arquivoDados.seek(arquivoDados.length());
      long endereco = arquivoDados.getFilePointer();
      arquivoDados.writeInt(registro.length);
      arquivoDados.write(' ');
      arquivoDados.write(registro);
      indicePrimario.inserir(codigo, endereco);
      indiceProjeto.inserir(a,endereco);
      indiceSecundario.inserir(b, codigo);
      
      arquivoDados.seek(0);
      arquivoDados.writeInt(codigo);
   
      listagem = false;
      return codigo;
   }


   
   
   public boolean buscar(int codigo, T obj) throws Exception {
      
      long endereco = localiza(codigo,obj);
      return endereco!=-1;
   }
   
   public long localiza(int codigo,T obj) throws Exception {
      
      int tamanho;
      byte lapide;
      byte[] registro;
      listagem = false;
      
      long endereco = indicePrimario.buscar(codigo);
      if(endereco!=-1) {
         arquivoDados.seek(endereco);
         tamanho = arquivoDados.readInt();
         lapide = arquivoDados.readByte();
         registro = new byte[tamanho];
         arquivoDados.read(registro);
         obj.setRegistro(registro);
         arquivoDados.seek(endereco);
         if(lapide==' ')
            return endereco;
      }
      return -1;
   }
   
   
   public boolean buscar2(int codigo, T obj) throws Exception {
      
      long endereco = localiza2(codigo,obj);
      return endereco!=-1;
   }
   
   public long localiza2(int codigo,T obj) throws Exception {
      
      int tamanho;
      byte lapide;
      byte[] registro;
      listagem = false;
      
      long endereco = indiceProjeto.buscar(codigo);
      if(endereco!=-1) {
         arquivoDados.seek(endereco);
         tamanho = arquivoDados.readInt();
         lapide = arquivoDados.readByte();
         registro = new byte[tamanho];
         arquivoDados.read(registro);
         obj.setRegistro(registro);
         arquivoDados.seek(endereco);
         if(lapide==' ')
            return endereco;
      }
      return -1;
   }

   
   public boolean buscarString(String stringDeBusca, T obj) throws Exception {
      
      int codigo = indiceSecundario.buscar(stringDeBusca);
      if(codigo==-1)
         return false;
      long endereco = localiza(codigo,obj);
      return endereco!=-1;
   }
   
   
   public boolean excluir(int codigo,T obj) throws Exception {
      long endereco = localiza(codigo,obj);
      if( endereco==-1 )
         return false;
      
      arquivoDados.skipBytes(4);
      arquivoDados.writeByte( '*' );
      indicePrimario.excluir(codigo);
      indiceProjeto.excluir(obj.getCodigoProjeto());
      indiceSecundario.excluir(obj.getChaveSecundaria());
      return true;
      
   }
   
   public boolean alterar(int codigo, T obj, T objAlterado) throws Exception {
      long endereco = localiza(codigo,obj);
      if( endereco==-1  )
         return false;
      
      int tamanhoAnterior = arquivoDados.readInt();
      byte[] registroAlterado = objAlterado.getRegistro();
      if(registroAlterado.length <= tamanhoAnterior) {
         arquivoDados.seek(endereco+5);
         arquivoDados.write(registroAlterado);
      } 
      else {
         
         // exclui registro anterior
         arquivoDados.seek(endereco+4);
         arquivoDados.writeByte('*');
         
         // insere novo registro no fim do arquivoDados
         arquivoDados.seek(arquivoDados.length());
         endereco = arquivoDados.getFilePointer();
         arquivoDados.writeInt(registroAlterado.length);
         arquivoDados.writeByte(' ');
         arquivoDados.write(registroAlterado);
         indicePrimario.atualizar(codigo, endereco);
         indiceProjeto.atualizar(codigo, endereco);
      }
      indiceSecundario.excluir(obj.getChaveSecundaria());
      indiceSecundario.inserir(objAlterado.getChaveSecundaria(),objAlterado.getCodigo());
      return true;
   }
   
   
   public void inicio() throws Exception {
      arquivoDados.seek(4);
      listagem = true;
   }
   
   public boolean proximo(T obj) throws Exception {
      
      while(listagem && arquivoDados.getFilePointer()<arquivoDados.length()) {
         int tamanho = arquivoDados.readInt();
         byte lapide = arquivoDados.readByte();
         byte[] registro = new byte[tamanho];
         arquivoDados.read(registro);
         if( lapide==' ' ) {
            obj.setRegistro(registro);
            return true;
         }
      }
      return false;
   }
   
/*
   // REORGANIZAR - VERSÃO APENAS PARA ELIMINAR OS ESPAÇOS DOS REGISTROS EXCLUÍDOS
   public void reorganizar(T obj) throws Exception {
       DataOutputStream copia = new DataOutputStream( new FileOutputStream("copia.db"));
       
       int tamanho, codigo;
       byte lapide;
       byte[] registro;
       
       // copia o cabeçalho
       arquivoDados.seek(0);
       codigo = arquivoDados.readInt();
       copia.writeInt(codigo);
       
       // copia os registros
       while(arquivoDados.getFilePointer()<arquivo.length()) {
           tamanho = arquivoDados.readInt();
           lapide = arquivoDados.readByte();
           registro = new byte[tamanho];
           arquivoDados.read(registro);
           if( lapide==' ' ) {
               obj.setRegistro(registro);
               registro = obj.getRegistro();
               copia.writeInt(registro.length);
               copia.write(lapide);
               copia.write(registro);
           }
       }
       
       // renomeia o arquivoDados
       copia.close();
       arquivoDados.close();
       
       File f = new File(nomeArquivo);
       f.delete();
       File c = new File("copia.db");
       c.renameTo(f);
       
       arquivoDados = new RandomAccessFile( nomeArquivo, "rw");
       listagem = false;
   }
*/
   
   // REORGANIZAR - VERSÃO QUE REORDENA O ARQUIVO, USANDO INTERCALAÇÃO BALANCEADA
   // Recebe um objeto vazio para auxiliar na reorganização
   public void reorganizar(T obj) throws Exception {
   
      int tamanhoBlocoMemoria = 3;
   
      // Lê o cabeçalho
      arquivoDados.seek(0);
      int cabecalho = arquivoDados.readInt();
      
      // ---------------------------------------------------------------------
      // Primeira etapa (distribuição)
      // ---------------------------------------------------------------------
      List<T> registrosOrdenados = new ArrayList<>();
      int    contador=0, seletor=0;
      int    tamanho;
      byte   lapide;
      byte[] dados;
      T r1 = (T)obj.clone(),
         r2 = (T)obj.clone(),
         r3 = (T)obj.clone();
      T rAnt1, rAnt2, rAnt3;
      
      // Abre três arquivos temporários para escrita (1º conjunto)
      DataOutputStream out1 = new DataOutputStream( new FileOutputStream("temp1.db") );
      DataOutputStream out2 = new DataOutputStream( new FileOutputStream("temp2.db") );
      DataOutputStream out3 = new DataOutputStream( new FileOutputStream("temp3.db") );
      DataOutputStream out = null;
      
      try{ 
         contador = 0;
         seletor = 0;
         while(true) {
            
            // Lê o registro no arquivoDados de dados
            tamanho = arquivoDados.readInt();
            lapide = arquivoDados.readByte();
            dados = new byte[tamanho];
            arquivoDados.read(dados);
            r1.setRegistro(dados);
         
            // Adiciona o registro ao vetor
            if(lapide == ' ') {
               registrosOrdenados.add((T)r1.clone());
               contador++;
            }
            if(contador==tamanhoBlocoMemoria){
               
               switch(seletor) {
                  case 0:  out = out1; 
                     break;
                  case 1:  out = out2; 
                     break;
                  default: out = out3; 
               }
               seletor = (seletor+1)%3;
               
               Collections.sort(registrosOrdenados);
               for( T r: registrosOrdenados ) {
                  dados = r.getRegistro();
                  out.writeInt(dados.length);
                  out.write(dados);
               }
               registrosOrdenados.clear();
               
               contador = 0;
            }
            
         }
         
      } 
      catch(EOFException eof) {
         // Descarrega os últimos registros lidos
         if(contador>0){
            switch(seletor) {
               case 0:  out = out1; 
                  break;
               case 1:  out = out2; 
                  break;
               default: out = out3; 
            }
            Collections.sort(registrosOrdenados);
            for( T r: registrosOrdenados ) {
               dados = r.getRegistro();
               out.writeInt(dados.length);
               out.write(dados);
            }
         }
      }
      out1.close();
      out2.close();
      out3.close();
   
      // ---------------------------------------------------------------------
      // Segunda etapa (intercalação)
      // ---------------------------------------------------------------------
      DataInputStream in1, in2, in3;
      boolean sentido = true; // true: conj1 -> conj2  |  false: conj2 -> conj1
      boolean maisIntercalacoes = true;
      boolean compara1, compara2, compara3;
      boolean terminou1, terminou2, terminou3;
      seletor = 0;
   
      while(maisIntercalacoes) {
         
         maisIntercalacoes = false;
         compara1 = false; compara2 = false; compara3 = false;
         terminou1 = false; terminou2 = false; terminou3 = false;
         
         // Seleciona as fontes e os destinos
         if(sentido) {
            in1  = new DataInputStream( new FileInputStream( "temp1.db"));
            in2  = new DataInputStream( new FileInputStream( "temp2.db"));
            in3  = new DataInputStream( new FileInputStream( "temp3.db"));
            out1 = new DataOutputStream(new FileOutputStream("temp4.db"));
            out2 = new DataOutputStream(new FileOutputStream("temp5.db"));
            out3 = new DataOutputStream(new FileOutputStream("temp6.db"));
         } 
         else {
            in1  = new DataInputStream( new FileInputStream( "temp4.db"));
            in2  = new DataInputStream( new FileInputStream( "temp5.db"));
            in3  = new DataInputStream( new FileInputStream( "temp6.db"));
            out1 = new DataOutputStream(new FileOutputStream("temp1.db"));
            out2 = new DataOutputStream(new FileOutputStream("temp2.db"));
            out3 = new DataOutputStream(new FileOutputStream("temp3.db"));
         }
         sentido = !sentido;
         
         // novos registros anteriores vazios
         r1 = (T)obj.clone();
         r2 = (T)obj.clone();
         r3 = (T)obj.clone();
      
         // Inicia a intercalação dos segmentos
         boolean mudou1 = true, mudou2 = true, mudou3 = true;
         while(!terminou1 || !terminou2 || !terminou3) {
            
            if(!compara1 && !compara2 && !compara3) {
               // Seleciona o próximo arquivoDados de saída
               switch(seletor) {
                  case 0:  out = out1; 
                     break;
                  case 1:  out = out2; 
                     break;
                  default: out = out3; 
               }
               seletor = (seletor+1)%3;
               
               if(!terminou1) compara1 = true; 
               if(!terminou2) compara2 = true; 
               if(!terminou3) compara3 = true;
            }
         
            // le o próximo registro da última fonte usada
            if(mudou1) {
               rAnt1 = (T)r1.clone();
               try {
                  tamanho = in1.readInt();
                  dados = new byte[tamanho];
                  in1.read(dados);
                  r1.setRegistro(dados);
                  if(r1.compareTo(rAnt1)<0)
                     compara1 = false;   
               }
               catch( EOFException e) {
                  compara1 = false;
                  terminou1 = true;
               }
               mudou1 = false;
            }
            if(mudou2) {
               rAnt2 = (T)r2.clone();
               try {
                  tamanho = in2.readInt();
                  dados = new byte[tamanho];
                  in2.read(dados);
                  r2.setRegistro(dados);
                  if(r2.compareTo(rAnt2)<0)
                     compara2 = false;                    
               }
               catch( EOFException e) {
                  compara2 = false;
                  terminou2 = true;
               }
               mudou2 = false;
            }
            if(mudou3) {
               rAnt3 = (T)r3.clone();
               try {
                  tamanho = in3.readInt();
                  dados = new byte[tamanho];
                  in3.read(dados);
                  r3.setRegistro(dados);
                  if(r3.compareTo(rAnt3)<0)
                     compara3 = false;                    
               }
               catch( EOFException e) {
                  compara3 = false;
                  terminou3 = true;
               }
               mudou3 = false;
            }
            
            // Escreve o menor registro
            if(compara1 && (!compara2||r1.compareTo(r2)<=0) && (!compara3||r1.compareTo(r3)<=0)) {
               dados = r1.getRegistro();
               out.writeInt(dados.length);
               out.write(dados);
               mudou1 = true;
            }
            else if(compara2 && (!compara1||r2.compareTo(r1)<=0) && (!compara3||r2.compareTo(r3)<=0)) {
               dados = r2.getRegistro();
               out.writeInt(dados.length);
               out.write(dados);
               mudou2 = true;
            }
            else if(compara3 && (!compara1||r3.compareTo(r1)<=0) && (!compara2||r3.compareTo(r2)<=0)) {
               dados = r3.getRegistro();
               out.writeInt(dados.length);
               out.write(dados);
               mudou3 = true;
            }
         
            // Testa se há mais intercalações a fazer
            if(seletor>1)
               maisIntercalacoes = true;
         }
      
         in1.close(); in2.close(); in3.close();
         out1.close(); out2.close(); out3.close();
      }
          
      
      //return;
      
      // copia os registros de volta para o arquivoDados original
      arquivoDados.close();
      if(sentido)
         in1  = new DataInputStream( new FileInputStream( "temp1.db"));
      else
         in1  = new DataInputStream( new FileInputStream( "temp4.db"));
      DataOutputStream ordenado = new DataOutputStream( new FileOutputStream(nomeArquivo) );
      ordenado.writeInt(cabecalho);
      
      // apaga o índice
      indicePrimario.apagar();
      indiceProjeto.apagar();
      indiceSecundario.apagar();
      
      int codigo;
      long endereco;
      try {
         while(true) {
            tamanho = in1.readInt();
            dados = new byte[tamanho];
            in1.read(dados);
            r1.setRegistro(dados);
            codigo = r1.getCodigo();
         
            endereco = ordenado.size();
            ordenado.writeInt(tamanho);
            ordenado.writeByte(' ');   // lápide
            ordenado.write(dados);
            indicePrimario.inserir(codigo, endereco);
            indiceProjeto.inserir(r1.getCodigoProjeto(), endereco);
            indiceSecundario.inserir(r1.getChaveSecundaria(),codigo);
         }
      } 
      catch(EOFException e ) {
         // saída normal
      }
      ordenado.close();
      in1.close();
      (new File("temp1.db")).delete();
      (new File("temp2.db")).delete();
      (new File("temp3.db")).delete();
      (new File("temp4.db")).delete();
      (new File("temp5.db")).delete();
      (new File("temp6.db")).delete();
      arquivoDados = new RandomAccessFile(nomeArquivo,"rw");
   }
   
}
