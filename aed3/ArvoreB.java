package aed3;
import java.io.*;

public class ArvoreB {

   private int  ordem;
   private RandomAccessFile arquivo;
   private String nomeArquivo;
   
   // Variáveis usadas nas funções recursivas (já que não é possível passar valores por referência)
   private int  chaveExtra;
   private long enderecoExtra;
   private long paginaExtra;
   private boolean cresceu;
   private boolean diminuiu;
   
   
   class ArvoreB_Pagina {
   
      protected int    ordem;
      protected int    n;
      protected int[]  chaves;
      protected long[] enderecos;
      protected long[] filhos;
      private int TAMANHO_REGISTRO = 12;
      protected int TAMANHO_PAGINA;
   
      public ArvoreB_Pagina(int o) {
         n = 0;
         ordem = o;
         chaves = new int[ordem*2];
         enderecos = new long[ordem*2];
         filhos = new long[ordem*2+1];
         for(int i=0; i<ordem*2; i++) {  
            chaves[i] = -1;
            enderecos[i] = -1;
            filhos[i] = -1;
         }
         filhos[ordem*2] = -1;
         TAMANHO_PAGINA = 4 + (ordem*2)*20 + 8;
      }
      
      protected byte[] getBytes() throws IOException {
         ByteArrayOutputStream ba = new ByteArrayOutputStream();
         DataOutputStream out = new DataOutputStream(ba);
         out.writeInt(n);
         int i=0;
         while(i<n) {
            out.writeLong(filhos[i]);
            out.writeInt(chaves[i]);
            out.writeLong(enderecos[i]);
            i++;
         }
         out.writeLong(filhos[i]);
         byte[] registroVazio = new byte[TAMANHO_REGISTRO];
         while(i<ordem*2){
            out.write(registroVazio);
            out.writeLong(filhos[i+1]);
            i++;
         }
         return ba.toByteArray();
      }
   
      public void setBytes(byte[] buffer) throws IOException {
         ByteArrayInputStream ba = new ByteArrayInputStream(buffer);
         DataInputStream in = new DataInputStream(ba);
         n = in.readInt();
         int i=0;
         while(i<n) {
            filhos[i] = in.readLong();
            chaves[i] = in.readInt();
            enderecos[i] = in.readLong();
            i++;
         }
         filhos[i] = in.readLong();
      }
   }
   
   public ArvoreB(int o, String na) throws IOException {
      ordem = o;
      nomeArquivo = na;
      arquivo = new RandomAccessFile(nomeArquivo,"rw");
      if(arquivo.length()<8)
         arquivo.writeLong(-1);  // raiz vazia
   }
   
   // Testa se a árvore está vazia
   public boolean vazia() throws IOException {
      long raiz;
      arquivo.seek(0);
      raiz = arquivo.readLong();
      return raiz == -1;
   }
   
   
   // Método para buscar
   public long buscar(int c) throws IOException {
      long raiz;
      arquivo.seek(0);
      raiz = arquivo.readLong();
      if(raiz!=-1)
         return buscar1(c,raiz);
      else
         return -1;
   }
   
   private long buscar1(int chaveBusca, long pagina) throws IOException {
      if(pagina==-1)
         return -1;
      arquivo.seek(pagina);
      ArvoreB_Pagina pa = new ArvoreB_Pagina(ordem);
      byte[] buffer = new byte[pa.TAMANHO_PAGINA];
      arquivo.read(buffer);
      pa.setBytes(buffer);
   
      // Encontra (recursivamente) a página para inserção
      int i=0;
      while(i<pa.n && chaveBusca>pa.chaves[i]) {
         i++;
      }
      if(i<pa.n) {
         if(chaveBusca==pa.chaves[i]) { // registro encontrado
            if((buscar1(chaveBusca,pa.filhos[i])) != -1)
            {
               if(chaveBusca==pa.filhos[i+1])
               {
                  System.out.println("AXEI OTRO");
               }
            }
            return pa.enderecos[i];
         }
         if(chaveBusca<pa.chaves[i])
            return buscar1(chaveBusca, pa.filhos[i]);
         else
            return buscar1(chaveBusca, pa.filhos[i+1]);
      } 
      else {
         return buscar1(chaveBusca, pa.filhos[i]);
      }
   }

        // Método para atualizar o campo de endereço
   public boolean atualizar(int c, long e) throws IOException {
      long raiz;
      arquivo.seek(0);
      raiz = arquivo.readLong();
      if(raiz!=-1)
         return atualizar1(c,e,raiz);
      else
         return false;
   }
   
   private boolean atualizar1(int chaveBusca, long enderecoAtualizado, long pagina) throws IOException {
      if(pagina==-1)
         return false;
      arquivo.seek(pagina);
      ArvoreB_Pagina pa = new ArvoreB_Pagina(ordem);
      byte[] buffer = new byte[pa.TAMANHO_PAGINA];
      arquivo.read(buffer);
      pa.setBytes(buffer);
   
      // Encontra (recursivamente) a página para inserção
      int i=0;
      while(i<pa.n && chaveBusca>pa.chaves[i]) {
         i++;
      }
      if(i<pa.n) {
         if(chaveBusca==pa.chaves[i]) { // registro encontrado
            pa.enderecos[i] = enderecoAtualizado;
            arquivo.seek(pagina);
            arquivo.write(pa.getBytes());
            return true;
         }
         if(chaveBusca<pa.chaves[i])
            return atualizar1(chaveBusca, enderecoAtualizado, pa.filhos[i]);
         else
            return atualizar1(chaveBusca, enderecoAtualizado, pa.filhos[i+1]);
      } 
      else {
         return atualizar1(chaveBusca, enderecoAtualizado, pa.filhos[i]);
      }
   }
   
   
   // Método para inclusão -> transforma a chamada em um função recursiva a partir da raoz
   public void inserir(int c, long e) throws IOException {
   
      arquivo.seek(0);       
      long pagina;                           // carrega a raiz como primeira página
      pagina = arquivo.readLong();
   
      chaveExtra = c;         // converte chave e endereco para tipos de referência, para que possam ser atualizados pela função recursiva
      enderecoExtra = e;
      paginaExtra = -1;       // ponteiro para a página filho direito do registro promovido
      cresceu = false;        // controla se houve crescimento da árvore
              
      // Inclui o registro (na chaveExtra e no enderecoExtra) na página
      inserir1(pagina);
      
      // Testa a necessidade de criação de uma nova raiz
      if(cresceu) {
         ArvoreB_Pagina novaPagina = new ArvoreB_Pagina(ordem);
         novaPagina.n = 1;
         novaPagina.chaves[0] = chaveExtra;
         novaPagina.enderecos[0] = enderecoExtra;
         novaPagina.filhos[0] = pagina;
         novaPagina.filhos[1] = paginaExtra;
         
         // Achar o espaço em disco....
         arquivo.seek(arquivo.length());
         long raiz = arquivo.getFilePointer();
         arquivo.write(novaPagina.getBytes());
         arquivo.seek(0);
         arquivo.writeLong(raiz);
      }
   }
   
   private void inserir1(long pagina) throws IOException {
      
      // testa se atingiu uma página folha. Caso afirmativo, cria o registro e o devolve para ser incluído
      if(pagina==-1) {
         cresceu = true;
         paginaExtra = -1;
         return;
      }
      
      // Lê a página
      arquivo.seek(pagina);
      ArvoreB_Pagina pa = new ArvoreB_Pagina(ordem);
      byte[] buffer = new byte[pa.TAMANHO_PAGINA];
      arquivo.read(buffer);
      pa.setBytes(buffer);
      
      // Encontra (recursivamente) a página para inserção
      int i=0;
      while(i<pa.n && chaveExtra>pa.chaves[i]) {
         i++;
      }
      if(i<pa.n) {
         if(chaveExtra==pa.chaves[i]) { // registro já existe
            cresceu = false;
            return;
         }
         if(chaveExtra<pa.chaves[i])
            inserir1(pa.filhos[i]);
         else
            inserir1(pa.filhos[i+1]);
      } 
      else {
         inserir1(pa.filhos[i]);
      }
   
      
      // Controle o retorno das chamadas recursivas sem a inclusão de nova página (se o registro já existir ou couber em página existente)
      if(!cresceu)
         return;
      
      // Se tiver espaço na página
      if(pa.n<ordem*2) {
         for(int j=pa.n; j>i; j--) {
            pa.chaves[j] = pa.chaves[j-1];
            pa.enderecos[j] = pa.enderecos[j-1];
            pa.filhos[j+1] = pa.filhos[j];
         }
         pa.chaves[i] = chaveExtra;
         pa.enderecos[i] = enderecoExtra;
         pa.filhos[i+1] = paginaExtra;
         pa.n++;
         arquivo.seek(pagina);
         arquivo.write(pa.getBytes());
         cresceu=false;
         return;
      }
      
      // Overflow: divide a página e move metade dos registros
      ArvoreB_Pagina np = new ArvoreB_Pagina(ordem);
      for(int j=0; j<ordem; j++) {
         np.chaves[j] = pa.chaves[j+ordem];
         np.enderecos[j] = pa.enderecos[j+ordem];   
         np.filhos[j+1] = pa.filhos[j+ordem+1];  
      }
      np.filhos[0] = pa.filhos[ordem];
      np.n = ordem;
      pa.n = ordem;
      
      // Testa o lado de inserção
      if(i<=ordem) {   // novo registro deve ficar na página da esquerda
         if(i==ordem) {  // Testa se é o próprio registro que será promovido
            np.filhos[0] = paginaExtra;
         } 
         else {
            int c_aux = pa.chaves[ordem-1];
            long e_aux = pa.enderecos[ordem-1];
            for(int j=ordem; j>0 && j>i; j--) {
               pa.chaves[j] = pa.chaves[j-1];
               pa.enderecos[j] = pa.enderecos[j-1];
               pa.filhos[j+1] = pa.filhos[j];
            }
            pa.chaves[i] = chaveExtra;
            pa.enderecos[i] = enderecoExtra;
            pa.filhos[i+1] = paginaExtra;
            chaveExtra = c_aux;
            enderecoExtra = e_aux;
         }
      } 
      else {
         int c_aux = np.chaves[0];
         long e_aux = np.enderecos[0];
         int j;
         for(j=0; j<ordem-1 && np.chaves[j+1]<chaveExtra; j++) {
            np.chaves[j] = np.chaves[j+1];
            np.enderecos[j] = np.enderecos[j+1];
            np.filhos[j] = np.filhos[j+1];
         }
         np.filhos[j] = np.filhos[j+1];
         np.chaves[j] = chaveExtra;
         np.enderecos[j] = enderecoExtra;
         np.filhos[j+1] = paginaExtra;
         chaveExtra = c_aux;
         enderecoExtra = e_aux;
      }
   
      arquivo.seek(pagina);
      arquivo.write(pa.getBytes());
      arquivo.seek(arquivo.length());
      paginaExtra = arquivo.getFilePointer();
      arquivo.write(np.getBytes());
   }

   
   public boolean excluir(int chaveParaExcluir) throws IOException {
              
      arquivo.seek(0);       
      long pagina;                           // carrega a raiz como primeira página
      pagina = arquivo.readLong();
   
      diminuiu = false;        // controla se houve redução da árvore
              
      // Inclui o registro (na chaveExtra e no enderecoExtra) na página
      boolean excluido = excluir1(chaveParaExcluir, pagina);
      if(excluido && diminuiu) {
         arquivo.seek(pagina);
         ArvoreB_Pagina pa = new ArvoreB_Pagina(ordem);
         byte[] buffer = new byte[pa.TAMANHO_PAGINA];
         arquivo.read(buffer);
         pa.setBytes(buffer);
         if(pa.n == 0) {
            arquivo.seek(0);
            arquivo.writeLong(pa.filhos[0]);  // Atualiza raiz. A raiz antiga deveria ser encaixada na lista de espaços disponíveis
         }
      }
       
      return excluido;
   }
   
   private boolean excluir1(int chaveParaExcluir, long pagina) throws IOException {
      
      boolean excluido=false;
      int diminuido;
      
      // Testa se o registro não foi encontrado na árvore
      if(pagina==-1) {
         diminuiu=false;
         return false;
      }
      
      // Lê a página no disco
      arquivo.seek(pagina);
      ArvoreB_Pagina pa = new ArvoreB_Pagina(ordem);
      byte[] buffer = new byte[pa.TAMANHO_PAGINA];
      arquivo.read(buffer);
      pa.setBytes(buffer);
   
      // Encontra (recursivamente) a página para inserção
      int i=0;
      while(i<pa.n && chaveParaExcluir>pa.chaves[i]) {
         i++;
      }
      if(i<pa.n) {
         if(chaveParaExcluir==pa.chaves[i]) { // registro encontrado
            
            // Testa se a exclusão é em uma folha
            if(pa.filhos[i]==-1){
               // "puxa" os demais registros
               int j;
               for(j=i; j<pa.n-1; j++) {
                  pa.chaves[j] = pa.chaves[j+1];
                  pa.enderecos[j] = pa.enderecos[j+1];
               }
               pa.n--;
               arquivo.seek(pagina);
               arquivo.write(pa.getBytes());
               diminuiu = pa.n<ordem;
               return true;
            }
            else { // exclusão não é em uma folha
               // encontra o maior antecessor do registro
               long paginaAux = pa.filhos[i];
               ArvoreB_Pagina paux = new ArvoreB_Pagina(ordem);
               while(paginaAux != -1) {
                  arquivo.seek(paginaAux);
                  arquivo.read(buffer);
                  paux.setBytes(buffer);
                  paginaAux = paux.filhos[paux.n];
               }
               int chaveAntecessor = paux.chaves[paux.n-1];
               long enderecoAntecessor = paux.enderecos[paux.n-1];
               
               // muda a exclusão da chave atual para exclusão na folha
               pa.chaves[i] = chaveAntecessor;
               pa.enderecos[i] = enderecoAntecessor;
               arquivo.seek(pagina);
               arquivo.write(pa.getBytes());
               excluido = excluir1(chaveAntecessor,pa.filhos[i]);
               diminuido = i;
            }
         } 
         else {
            if(chaveParaExcluir<pa.chaves[i]) {
               excluido = excluir1(chaveParaExcluir, pa.filhos[i]);
               diminuido = i;
            }
            else {
               excluido = excluir1(chaveParaExcluir, pa.filhos[i+1]);
               diminuido = i+1;
            }
         }
      } 
      else {
         excluido = excluir1(chaveParaExcluir, pa.filhos[i]);
         diminuido = i;
      }
      
      // Testa se houve redução de nós
      if(diminuiu) {
      
         long paginaFilho = pa.filhos[diminuido];
         ArvoreB_Pagina pFilho = new ArvoreB_Pagina(ordem);
         arquivo.seek(paginaFilho);
         arquivo.read(buffer);
         pFilho.setBytes(buffer);
         
         long paginaIrmao;
         ArvoreB_Pagina pIrmao;
         
         // Testa fusão com irmão esquerdo
         if(diminuido>0) {
            paginaIrmao = pa.filhos[diminuido-1];
            pIrmao = new ArvoreB_Pagina(ordem);
            arquivo.seek(paginaIrmao);
            arquivo.read(buffer);
            pIrmao.setBytes(buffer);
            
            // Testa se o irmão pode emprestar algum registro
            if(pIrmao.n>ordem) {
               
               // move todos os registros no filho para a direita
               for(int j=pFilho.n; j>0; j--) {
                  pFilho.chaves[j] = pFilho.chaves[j-1];
                  pFilho.enderecos[j] = pFilho.enderecos[j-1];
                  pFilho.filhos[j+1] = pFilho.filhos[j];
               }
               pFilho.filhos[1] = pFilho.filhos[0];
               pFilho.n++;
               
               // desce o elemento pai
               pFilho.chaves[0] = pa.chaves[diminuido-1];
               pFilho.enderecos[0] = pa.enderecos[diminuido-1];
               
               // sobe o elemento do irmão
               pa.chaves[diminuido-1] = pIrmao.chaves[pIrmao.n-1];
               pa.enderecos[diminuido-1] = pIrmao.enderecos[pIrmao.n-1];
               pFilho.filhos[0] = pIrmao.filhos[pIrmao.n];
               pIrmao.n--;
               diminuiu = false;
            }
            
            // Se não puder emprestar, faz a fusão dos dois irmãos
            else {
               // Desce o registro no pai para o irmão da esquerda
               pIrmao.chaves[pIrmao.n] = pa.chaves[diminuido-1];
               pIrmao.enderecos[pIrmao.n] = pa.enderecos[diminuido-1];
               pIrmao.filhos[pIrmao.n+1] = pFilho.filhos[0];
               pIrmao.n++;
            
               // copia todos os registros para o irmão da esquerda
               for(int j=0; j<pFilho.n; j++) {
                  pIrmao.chaves[pIrmao.n] = pFilho.chaves[j];
                  pIrmao.enderecos[pIrmao.n] = pFilho.enderecos[j];
                  pIrmao.filhos[pIrmao.n+1] = pFilho.filhos[j+1];
                  pIrmao.n++;
               }
               pFilho.n = 0;   // aqui o endereço do filho poderia ser incluido em uma lista encadeada no cabeçalho, indicando os espaços reaproveitáveis
               
               // puxa os registros no pai
               for(int j=diminuido-1; j<pa.n; j++) {
                  pa.chaves[j] = pa.chaves[j+1];
                  pa.enderecos[j] = pa.enderecos[j+1];
                  pa.filhos[j+1] = pa.filhos[j+2];
               }
               pa.n--;
               diminuiu = pa.n<ordem;  // testa se o pai também ficou sem o número mínimo de elementos
            }
         }
         
         // fusão com o irmão direito
         else {
            paginaIrmao = pa.filhos[diminuido+1];
            pIrmao = new ArvoreB_Pagina(ordem);
            arquivo.seek(paginaIrmao);
            arquivo.read(buffer);
            pIrmao.setBytes(buffer);
            
            // Testa se o irmão pode emprestar algum registro
            if(pIrmao.n>ordem) {
               
               // desce o elemento pai
               pFilho.chaves[pFilho.n] = pa.chaves[diminuido];
               pFilho.enderecos[pFilho.n] = pa.enderecos[diminuido];
               pFilho.filhos[pFilho.n+1] = pIrmao.filhos[0];
               pFilho.n++;
            
               // sobe o elemento do irmão
               pa.chaves[diminuido] = pIrmao.chaves[0];
               pa.enderecos[diminuido] = pIrmao.enderecos[0];
               
               // move todos os registros no irmão para a esquerda
               int j;
               for(j=0; j<pIrmao.n-1; j++) {
                  pIrmao.chaves[j] = pIrmao.chaves[j+1];
                  pIrmao.enderecos[j] = pIrmao.enderecos[j+1];
                  pIrmao.filhos[j] = pIrmao.filhos[j+1];
               }
               pIrmao.filhos[j] = pIrmao.filhos[j+1];
               pIrmao.n--;
               diminuiu = false;
            }
            
            else {
               // Desce o registro no pai para o filho
               pFilho.chaves[pFilho.n] = pa.chaves[diminuido];
               pFilho.enderecos[pFilho.n] = pa.enderecos[diminuido];
               pFilho.filhos[pFilho.n+1] = pIrmao.filhos[0];
               pFilho.n++;
            
               // copia todos os registros do irmão da direita
               for(int j=0; j<pIrmao.n; j++) {
                  pFilho.chaves[pFilho.n] = pIrmao.chaves[j];
                  pFilho.enderecos[pFilho.n] = pIrmao.enderecos[j];
                  pFilho.filhos[pFilho.n+1] = pIrmao.filhos[j+1];
                  pFilho.n++;
               }
               pIrmao.n = 0;   // aqui o endereço do irmão poderia ser incluido em uma lista encadeada no cabeçalho, indicando os espaços reaproveitáveis
               
               // puxa os registros no pai
               for(int j=diminuido; j<pa.n-1; j++) {
                  pa.chaves[j] = pa.chaves[j+1];
                  pa.enderecos[j] = pa.enderecos[j+1];
                  pa.filhos[j+1] = pa.filhos[j+2];
               }
               pa.n--;
               diminuiu = pa.n<ordem;  // testa se o pai também ficou sem o número mínimo de elementos
            }
         }
         
         // Atualiza todos os registros
         arquivo.seek(pagina);
         arquivo.write(pa.getBytes());
         arquivo.seek(paginaFilho);
         arquivo.write(pFilho.getBytes());
         arquivo.seek(paginaIrmao);
         arquivo.write(pIrmao.getBytes());
      }
      return excluido;
   }
   
   
   public void print() throws IOException {
      long raiz;
      arquivo.seek(0);
      raiz = arquivo.readLong();
      if(raiz!=-1)
         print1(raiz,0,0);
   }
   
   private void print1(long pagina, int nivel, int endereco) throws IOException {
      if(pagina==-1)
         return;
      int i;
   
      arquivo.seek(pagina);
      ArvoreB_Pagina pa = new ArvoreB_Pagina(ordem);
      byte[] buffer = new byte[pa.TAMANHO_PAGINA];
      arquivo.read(buffer);
      pa.setBytes(buffer);
      
      System.out.print(nivel+"."+endereco+": (");
      for(i=0; i<pa.n; i++) {
         if(pa.filhos[i]!=-1)
            System.out.print((nivel+1)+"."+((nivel*endereco*ordem*2)+i));
         System.out.print(") "+pa.chaves[i]+" (");
      }
      if(pa.filhos[i]==-1)
         System.out.println(")");
      else
         System.out.println((nivel+1)+"."+((nivel*endereco*ordem*2)+i)+")");
      for(i=0;i<=pa.n;i++)
         print1(pa.filhos[i],nivel+1,((nivel*endereco*ordem*2)+i));
   }
   

   public void apagar() throws IOException {
   
      File f = new File(nomeArquivo);
      f.delete();
   
      arquivo = new RandomAccessFile(nomeArquivo,"rw");
      arquivo.writeLong(-1);  // raiz vazia
   }
   
}
