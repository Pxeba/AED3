package aed3;
import java.util.Scanner;

public class ArqInd02 {

   private static ArquivoIndexado<Analista> arquivo;
   private static ArquivoIndexado<Projeto> arquivo2;
   private static ArquivoIndexadoTarefa<Tarefa> arquivo3;
   private static Scanner console;
       
   public static void main(String[] args) {
   
      try {
          
         arquivo = new ArquivoIndexado<>("Analista.db", "Analista1.idx", "Analista2.idx");
         arquivo2 = new ArquivoIndexado<>("Projeto.db", "Projeto1.idx", "Projeto2.idx");
         arquivo3 = new ArquivoIndexadoTarefa<>("Tarefa.db", "Tarefa1.idx", "Tarefa2.idx","Tarefa3.idx");
      
         System.out.println( "1 - Analistas" );
         System.out.println( "2 - Projetos" );
         System.out.println( "3 - Tarefas" );
         System.out.println( "0 - Sair" );
          
         // menu
         int opcao;
         console = new Scanner(System.in);
         opcao = Integer.valueOf(console.nextLine());
         do {
            
            switch(opcao) {
               case 1:
                  System.out.println( "CADASTRO DE ANALISTAS" );
                  System.out.println();
                  System.out.println( "1 - Incluir" );
                  System.out.println( "2 - Alterar" );
                  System.out.println( "3 - Excluir" );
                  System.out.println( "4 - Consultar por codigo" );
                  System.out.println( "5 - Consultar por nome" );
                  System.out.println( "6 - Listar" );
                  System.out.println( "7 - Reorganizar" );
                  System.out.println( "9 - Popular" );                
                  System.out.println( "0 - Sair" );
               
                  System.out.print("\nOpção: ");
                  opcao = Integer.valueOf(console.nextLine());
                  switch(opcao) 
                  {
                     case 1: incluirAnalista(); 
                        break;
                     case 2: alterarAnalista(); 
                        break;
                     case 3: excluirAnalista(); 
                        break;
                     case 4: consultarCodigoAnalista(); 
                        break;
                     case 5: consultarNomeAnalista(); 
                        break;
                     case 6: listarAnalista(); 
                        break;
                     case 7: reorganizarAnalista(); 
                        break;
                     case 9: popularAnalista(); 
                        break;
                     case 0: 
                        break;
                     default: System.out.println( "Opção inválida!" );
                        break;
                  }
                  System.out.println( "1 - Analistas" );
                  System.out.println( "2 - Projetos" );
                  System.out.println( "3 - Tarefas" );
                  System.out.println( "0 - Sair" );
                  opcao = Integer.valueOf(console.nextLine());
                  break;
               case 2:
                  System.out.println( "CADASTRO DE PROJETOS" );
                  System.out.println();
                  System.out.println( "1 - Incluir" );
                  System.out.println( "2 - Alterar" );
                  System.out.println( "3 - Excluir" );
                  System.out.println( "4 - Consultar por codigo" );
                  System.out.println( "5 - Consultar por nome" );
                  System.out.println( "6 - Listar" );
                  System.out.println( "7 - Reorganizar" );
                  System.out.println( "9 - Popular" );                
                  System.out.println( "0 - Sair" );
               
                  System.out.print("\nOpção: ");
                  opcao = Integer.valueOf(console.nextLine());
                  switch(opcao) 
                  {
                     case 1: incluirProjeto(); 
                        break;
                     case 2: alterarProjeto(); 
                        break;
                     case 3: excluirProjeto(); 
                        break;
                     case 4: consultarCodigoProjeto(); 
                        break;
                     case 5: consultarNomeProjeto(); 
                        break;
                     case 6: listarProjeto(); 
                        break;
                     case 7: reorganizarProjeto(); 
                        break;
                     case 9: popularProjeto(); 
                        break;
                     case 0: 
                        break;
                     default: System.out.println( "Opção inválida!" );
                        break;
                  }
                  System.out.println( "1 - Analistas" );
                  System.out.println( "2 - Projetos" );
                  System.out.println( "3 - Tarefas" );
                  System.out.println( "0 - Sair" );
                  opcao = Integer.valueOf(console.nextLine());
                  break;
               case 3:
                  
                  System.out.println( "CADASTRO DE TAREFAS" );
                  System.out.println();
                  System.out.println( "1 - Incluir" );
                  System.out.println( "2 - Alterar" );
                  System.out.println( "3 - Excluir" );
                  System.out.println( "4 - Consultar por Cod.Proj" );
                  System.out.println( "5 - Consultar por Cod.Analista" );                
                  System.out.println( "0 - Sair" );
               
                  System.out.print("\nOpção: ");
                  opcao = Integer.valueOf(console.nextLine());
                  switch(opcao) 
                  {
                     case 1: incluirTarefa(); 
                        break;
                     case 2: alterarTarefa(); 
                        break;
                     case 3: excluirTarefa(); 
                        break;
                     case 4: consultarCodigoTarefa(); 
                        break;
                     case 5: consultarTarefaAnalista(); 
                        break;
                     case 0: 
                        break;
                     default: System.out.println( "Opção inválida!" );
                        break;
                  }
                  System.out.println( "1 - Analistas" );
                  System.out.println( "2 - Projetos" );
                  System.out.println( "3 - Tarefas" );
                  System.out.println( "0 - Sair" );
                  opcao = Integer.valueOf(console.nextLine());
               
                  break;
                  
               case 0:
                  break;
                  
               default:
                  System.out.println("opcao invalida");
                  break;
                  
                  
            }
            
            
         } while(opcao!=0);
         
         
      } 
      catch( Exception e ) {
         System.out.println("ERROR - Numero Digitado Invalido");
      }
      
   }
   
   public static int ultimoCodigoProjeto(int a)
   {
      try
      {
         while(arquivo3.indiceProjeto.buscar(a) != -1)
         {
            a = a+100;
         }
      }
      catch(Exception e){
         System.out.println("Erro no metodo ultimoCodigoProjeto");
      }
      return a;
   }
   
   public static String ultimoCodigoAnalista(String a)
   {
      try
      {
         int b = Integer.parseInt(a);
         while(arquivo3.indiceSecundario.buscar(a) != -1)
         {
            b = b+100;
            a = ""+b;
         }
      }
      catch(Exception e){
         System.out.println("Erro no metodo ultimoCodigoProjeto");
      }
      return a;
   }
   
   public static int numeroProjetos(int a)
   {
      int b = ultimoCodigoProjeto(a);
      b = (b - b%100)/100;
      return b;
   }
   
   
   public static void incluirAnalista() throws Exception {
      
      System.out.println("\nINCLUSÃO");
      System.out.print("Nome: ");
      String nome = console.nextLine();
      System.out.print("Email: ");
      String email = console.nextLine();
      
      System.out.print( "\nConfirma inclusão? ");
      char confirma = console.nextLine().charAt(0);
      if(confirma=='S'||confirma=='s') {
         Analista analista = new Analista(-1,nome,email) ;
         int codigo = arquivo.incluir(analista);
         System.out.println( "Analista incluido com o código "+codigo+".");
      }
      
   }
   
   public static void incluirProjeto() throws Exception {
      
      System.out.println("\nINCLUSÃO");
      System.out.print("Nome do Projeto: ");
      String nomeProjeto = console.nextLine();
      System.out.print("Nome do Cliente: ");
      String nomeCliente = console.nextLine();
      System.out.print("Data de Inicio: ");
      String dataInicio = console.nextLine();
      System.out.print("Data do Fim: ");
      String dataFim = console.nextLine();
      
      System.out.print( "\nConfirma inclusão? ");
      char confirma = console.nextLine().charAt(0);
      if(confirma=='S'||confirma=='s') {
         Projeto projeto = new Projeto(-1,nomeProjeto,nomeCliente,dataInicio,dataFim);
         int codigo = arquivo2.incluir(projeto);
         System.out.println( "Projeto incluido com o código "+codigo+".");
      }
      
   }
   
   public static void incluirTarefa() throws Exception {
      
      System.out.println("\nINCLUSÃO");;
      System.out.print("Descricao da Tarefa: ");
      String descricao = console.nextLine();
      System.out.print("Codigo do Analista: ");
      String codigoAnalista = console.nextLine();
      System.out.print("Codigo do Projeto: ");
      String codigoProjeto = console.nextLine();
      System.out.print("Data de Inicio: ");
      String dataInicio = console.nextLine();
      System.out.print("Data do Fim: ");
      String dataFim = console.nextLine();
      try
      {
         int codigoProj = Integer.parseInt(codigoProjeto);
         int codigoAnal = Integer.parseInt(codigoAnalista);
         if(arquivo.indicePrimario.buscar(codigoProj) != -1  && arquivo2.indicePrimario.buscar(codigoAnal) != -1)
         {
            System.out.print( "\nConfirma inclusão? ");
            char confirma = console.nextLine().charAt(0);
            if(confirma=='S'||confirma=='s') {
               if(arquivo3.indiceProjeto.buscar(codigoProj) != -1)
               {
                  codigoProj = ultimoCodigoProjeto(codigoProj);
               }
               if(arquivo3.indiceSecundario.buscar(codigoAnalista) != -1)
               {
                  codigoAnalista = ultimoCodigoAnalista(codigoAnalista);
               }
            
               Tarefa tarefa = new Tarefa(-1,descricao,codigoProj,codigoAnalista,dataInicio,dataFim);
               int codigo = arquivo3.incluir2(tarefa);
               System.out.println( "Tarefa incluida com o código "+codigo+".\n");
            }
         }
         else
         {
            System.out.println("\nAnalista ou Projeto invalido(s)\n");
         }
      }
      catch(Exception e)
      {
         System.out.println("\nErro em algum dos c�digos\n");
      }
      
   }
   
   
   
   
   public static void consultarCodigoAnalista() throws Exception {
      int codigo;
      Analista analista = new Analista();
      System.out.println("\nCONSULTA POR CÓDIGO");
      System.out.print("Código: ");
      codigo = Integer.valueOf(console.nextLine());
      
      if(arquivo.buscar(codigo,analista)) 
         System.out.println(analista);
      else
         System.out.println("Analista não encontrado!");
   }
   
   public static void consultarCodigoProjeto() throws Exception {
      int codigo;
      Projeto projeto = new Projeto();
      System.out.println("\nCONSULTA POR CÓDIGO");
      System.out.print("Código: ");
      codigo = Integer.valueOf(console.nextLine());
      
      if(arquivo2.buscar(codigo,projeto)) 
         System.out.println(projeto);
      else
         System.out.println("Projeto não encontrado!");
   }
   
   public static void consultarCodigoTarefa() throws Exception {
      int codigo;
      Tarefa tarefa = new Tarefa();
      System.out.println("\nCONSULTA POR CÓDIGO");
      System.out.print("Código: ");
      codigo = Integer.valueOf(console.nextLine());
      
      if(arquivo3.indiceProjeto.buscar(codigo)!= -1)
      {
         for(codigo = codigo;arquivo3.buscar2(codigo,tarefa);codigo = codigo+100)
         {
            System.out.println(tarefa);
         }
      }
      else
         System.out.println("Tarefa não encontrado!");
   }
   
   
   
   
   
   
   public static void consultarNomeAnalista() throws Exception {
      String nome;
      Analista analista = new Analista();
      System.out.println("\nCONSULTA POR NOME");
      System.out.print("Título: ");
      nome = console.nextLine();
      
      if(arquivo.buscarString(nome,analista)) 
         System.out.println(analista);
      else
         System.out.println("Analista não encontrado!");
   }
   
   public static void consultarNomeProjeto() throws Exception {
      String nome;
      Projeto projeto = new Projeto();
      System.out.println("\nCONSULTA POR NOME");
      System.out.print("Título: ");
      nome = console.nextLine();
      
      if(arquivo2.buscarString(nome,projeto)) 
         System.out.println(projeto);
      else
         System.out.println("Projeto não encontrado!");
   }
   
   public static void consultarTarefaAnalista() throws Exception {
      String codAnalista;
      Tarefa tarefa = new Tarefa();
      System.out.println("\nCONSULTA POR COD. ANALISTA");
      System.out.print("Codigo: ");
      codAnalista = console.nextLine();
      int b = Integer.parseInt(codAnalista);
      if(arquivo3.indiceSecundario.buscar(codAnalista)!= -1)
      {
         while(arquivo3.buscarString(codAnalista,tarefa))
         {  
            System.out.println(tarefa);
            b = b+100;
            codAnalista = ""+b;
         }
      }
      else
         System.out.println("Tarefa não encontrado!");
   }
   
   
   
   
   
   
   
   public static void excluirAnalista() throws Exception {
      int codigo;
      Analista analista = new Analista();
      System.out.println("\nEXCLUSÃO");
      System.out.print("Código: ");
      codigo = Integer.valueOf(console.nextLine());
      
      if(arquivo.buscar(codigo,analista)) {
         System.out.println(analista);
         System.out.print( "\nConfirma Exclusão? ");
         char confirma = console.nextLine().charAt(0);
         if(confirma=='S'||confirma=='s') {
            if(arquivo.excluir(codigo,analista)){
               System.out.println("Analista excluído!");
            }
         }
      }
      else
         System.out.println("Analista não encontrado!");
      
   }
   
   
   public static void excluirProjeto() throws Exception {
      int codigo;
      Projeto projeto = new Projeto();
      System.out.println("\nEXCLUSÃO");
      System.out.print("Código: ");
      codigo = Integer.valueOf(console.nextLine());
      
      if(arquivo2.buscar(codigo,projeto)) {
         System.out.println(projeto);
         System.out.print( "\nConfirma Exclusão? ");
         char confirma = console.nextLine().charAt(0);
         if(confirma=='S'||confirma=='s') {
            if(arquivo2.excluir(codigo,projeto)){
               System.out.println("Projeto excluído!");
            }
         }
      }
      else
         System.out.println("Projeto não encontrado!");
      
   }



   public static void excluirTarefa() throws Exception {
      int codigo;
      Tarefa tarefa = new Tarefa();
      System.out.println("\nEXCLUSÃO");
      System.out.print("Código: ");
      codigo = Integer.valueOf(console.nextLine());
      
      if(arquivo3.buscar(codigo,tarefa)) {
         System.out.println(tarefa);
         System.out.print( "\nConfirma Exclusão? ");
         char confirma = console.nextLine().charAt(0);
         if(confirma=='S'||confirma=='s') {
            if(arquivo3.excluir(codigo,tarefa)){
               System.out.println("Tarefa excluído!");
            }
         }
      }
      else
         System.out.println("Tarefa não encontrado!");
      
   }



   
   
   
   
   
   
   public static void alterarAnalista() throws Exception {
      int codigo;
      String scan = "";
      Analista analista = new Analista();
      System.out.println("\nALTERACAO");
      System.out.print("Codigo: ");
   
      try
      {
         codigo = Integer.valueOf(console.nextLine());
         if(arquivo.buscar(codigo,analista)) {
            System.out.println(analista);
         
            System.out.println("\nINCLUSÃO");
            System.out.print("Nome: ");
            String nome = console.nextLine();
            System.out.print("Email: ");
            String email = console.nextLine();
         
            if(!nome.equals("") || !email.equals("")) {
            
               System.out.print( "\nConfirma alteração? ");
               char confirma = console.nextLine().charAt(0);
               if(confirma=='S'||confirma=='s') {
               
                  if(!nome.equals("")) 
                     analista.nome = nome;
                  if(!email.equals("")) 
                     analista.email = email;
               
                  if(arquivo.alterar(codigo,new Analista(),analista))
                     System.out.println("\nAnalista alterado!\n");
                  else
                     System.out.println("Atualização não completa!\n");
               
               
               
               }   
            }
            else
               System.out.println("Analista não encontrado!");
         }
      }
      catch(Exception e)
      {
         System.out.println("\nCODIGO INVALIDO");
      }
      
      
   }
   
   public static void alterarProjeto() throws Exception {
      int codigo;
      Projeto projeto = new Projeto();
      System.out.println("\nALTERAÇÃO");
      System.out.print("Código: ");
      try
      {
         codigo = Integer.valueOf(console.nextLine());
         if(arquivo2.buscar(codigo,projeto)) {
            System.out.println(projeto);
         
            System.out.println("\nINCLUSÃO");
            System.out.print("Nome do Projeto: ");
            String nomeProjeto = console.nextLine();
            System.out.print("Nome do Cliente: ");
            String nomeCliente = console.nextLine();
            System.out.print("Data de Inicio: ");
            String dataInicio = console.nextLine();
            System.out.print("Data do Fim: ");
            String dataFim = console.nextLine();
         
            if(!nomeProjeto.equals("") || !nomeCliente.equals("") || !dataInicio.equals("" ) || !dataFim.equals("" )) {
            
               System.out.print( "\nConfirma alteração? ");
               char confirma = console.nextLine().charAt(0);
               if(confirma=='S'||confirma=='s') {
               
                  if(!nomeProjeto.equals("")) 
                     projeto.nomeProjeto = nomeProjeto;
                  if(!nomeCliente.equals("")) 
                     projeto.nomeCliente = nomeCliente;
                  if(!dataInicio.equals("")) 
                     projeto.dataInicio = dataInicio;
                  if(!dataFim.equals("")) 
                     projeto.dataFim = dataFim;
               
                  if(arquivo2.alterar(codigo,new Projeto(),projeto))
                     System.out.println("Projeto alterado!");
                  else
                     System.out.println("Atualização não completa!");
               
               
               
               
               }
            }
         }
         else
            System.out.println("Projeto não encontrado!");
      }
      catch(Exception e)
      {
         System.out.println("CODIGO INVALIDO");
      }
   }
   
   public static void alterarTarefa() throws Exception {
      int codigo;
      Tarefa tarefa = new Tarefa();
      System.out.println("\nALTERAÇÃO");
      System.out.print("Código: ");
      try
      {
         codigo = Integer.valueOf(console.nextLine());
      
         if(arquivo3.buscar(codigo,tarefa)) {
            System.out.println(tarefa);
         
            System.out.println("\nINCLUSÃO");;
            System.out.print("Descricao da Tarefa: ");
            String descricao = console.nextLine();
            System.out.print("Codigo do Analista: ");
            String codigoAnalista = console.nextLine();
            System.out.print("Codigo do Projeto: ");
            String codigoProjeto = console.nextLine();
            System.out.print("Data de Inicio: ");
            String dataInicio = console.nextLine();
            System.out.print("Data do Fim: ");
            String dataFim = console.nextLine();
         
            if(arquivo3.indiceSecundario.buscar(codigoAnalista) != -1 && 
            arquivo3.indiceProjeto.buscar(Integer.parseInt(codigoProjeto)) != -1)
            {
            
               if(!descricao.equals("") || !codigoAnalista.equals("") || !codigoProjeto.equals("" )
               || !dataInicio.equals("" )|| !dataFim.equals("" )) {
               
                  int codProj = Integer.parseInt(codigoProjeto);
                  System.out.print( "\nConfirma alteração? ");
                  char confirma = console.nextLine().charAt(0);
                  if(confirma=='S'||confirma=='s') {
                  
                     if(!descricao.equals("")) 
                        tarefa.descricao = descricao;
                     if(!codigoAnalista.equals("")) 
                        tarefa.codigoAnalista = codigoAnalista;
                     if(!codigoProjeto.equals("")) 
                        tarefa.codigoProjeto = codProj;
                     if(!dataInicio.equals("")) 
                        tarefa.dataInicio = dataInicio;
                     if(!dataFim.equals("")) 
                        tarefa.dataLimite = dataFim;
                  
                     if(arquivo3.alterar(codigo,new Tarefa(),tarefa))
                        System.out.println("Tarefa alterado!");
                     else
                        System.out.println("Atualização não completa!");
                  }
               
               
               
               }
            }
            else
            {
               System.out.println("Analista ou Projeto invalido(s)");  
            }
         }
         else
            System.out.println("Tarefa não encontrado!");
      }
      catch(Exception e)
      {
         System.out.println("CODIGO INVALIDO");
      }
   }
   
   
   
   
   
   
   public static void reorganizarAnalista() throws Exception {
      System.out.println("\nREORGANIZAÇÃO");
      Analista analista = new Analista();
      arquivo.reorganizar(analista);
      System.out.println("Arquivo reorganizado!");
   }
   
   public static void reorganizarProjeto() throws Exception {
      System.out.println("\nREORGANIZAÇÃO");
      Projeto projeto = new Projeto();
      arquivo2.reorganizar(projeto);
      System.out.println("Arquivo reorganizado!");
   }
   
   
   
   
   
   
   
   
   public static void listarAnalista() throws Exception {
   
      System.out.println("\nLISTAGEM\n");
      Analista analista = new Analista();
      
      arquivo.inicio();
      while(arquivo.proximo(analista))
         System.out.println(analista);
      
   }
   
   public static void listarProjeto() throws Exception {
   
      System.out.println("\nLISTAGEM\n");
      Projeto projeto = new Projeto();
      
      arquivo2.inicio();
      while(arquivo2.proximo(projeto))
         System.out.println(projeto);
      
   }
   
   public static void listarTarefa() throws Exception {
   
      System.out.println("\nLISTAGEM\n");
      Tarefa tarefa = new Tarefa();
      
      arquivo3.inicio();
      while(arquivo3.proximo(tarefa))
         System.out.println(tarefa);
      
   }
   
   
   
   
   
   
   
   public static void popularAnalista() throws Exception {
      System.out.println(arquivo.incluir(new Analista(-1,"O Pequeno Príncipe","Antoine de Saint-Exupéry"))); 
      System.out.println(arquivo.incluir(new Analista(-1,"O Pequeno Príncipe2","Antoine de Saint-Exupéry2"))); 
      System.out.println(arquivo.incluir(new Analista(-1,"O Pequeno Príncipe3","Antoine de Saint-Exupéry3")));        
   }
   
   public static void popularProjeto() throws Exception {
      System.out.println(arquivo2.incluir(new Projeto(-1,"O Pequeno Príncipe","Antoine de Saint-Exupéry","5","10"))); 
      System.out.println(arquivo2.incluir(new Projeto(-1,"O Pequeno Príncipe2","Antoine de Saint-Exupéry2","6","11"))); 
      System.out.println(arquivo2.incluir(new Projeto(-1,"O Pequeno Príncipe3","Antoine de Saint-Exupéry3","7","12")));        
   }
   
   public static void popularTarefa() throws Exception {
      System.out.println(arquivo3.incluir(new Tarefa(-1,"O Pequeno Príncipe",1,"1","1","4"))); 
      System.out.println(arquivo3.incluir(new Tarefa(-1,"O Pequeno Príncipe2",2,"2","2","5"))); 
      System.out.println(arquivo3.incluir(new Tarefa(-1,"O Pequeno Príncipe3",3,"3","3","6")));        
   }
   
}
