/* Classe Main
 * Implementação da classe principal que lê o arquivo txt com os dados e orquestra as tarefas a serem realizadas
 * João Pedro de Melo Murta e Danilo Paris
 */

 import java.io.BufferedReader;
 import java.io.FileNotFoundException;
 import java.io.FileReader;
 import java.io.IOException;
  
  public class Main {
      public static Matriz A;
  
      public static void lerArq(String arqName) {
         BufferedReader br = null;
         try {
             br = new BufferedReader(new FileReader(arqName));
             String[] firstLine = br.readLine().trim().split("\\s+"); // expressão regular para dividir pelos espaços em branco
     
             int V = Integer.parseInt(firstLine[0]); // Converte para int
     
             A = new Matriz (V); // Cria o grafo
     
             // Lê os pares de vértices e os insere no grafo até o fim do arquivo
             String line;
             while ((line = br.readLine()) != null) {
                 String[] vertices = line.trim().split("\\s+"); // expressão regular para garantir que apenas números sejam capturados
     
                 int v = Integer.parseInt(vertices[0]);
                 int w = Integer.parseInt(vertices[1]);
     
                 A.adicionarAresta(v, w);
             }
     
         } catch (FileNotFoundException e) {
             System.err.println("Erro: Arquivo não encontrado: " + arqName);
             e.printStackTrace();
         } catch (IOException e) {
             System.err.println("Erro ao ler o arquivo: " + arqName);
             e.printStackTrace();
         } catch (NumberFormatException e) {
             System.err.println("Erro ao converter string para número. Verifique o formato do arquivo.");
             e.printStackTrace();
         } finally {
             try {
                 if (br != null) {
                     br.close();
                 }
             } catch (IOException e) {
                 System.err.println("Erro ao fechar o arquivo.");
                 e.printStackTrace();
             }
         }
     }    
 
     public static void main(String[] args) {
        // Escolha do Grafo
        int tam = 20;
        String arqName ="Projeto/Grafos/grafo" + tam + ".txt";
        lerArq(arqName);
        
        // Escolha do Algoritmo
        //Cycle biconexo = new Cycle();
        //Articulation biconexo = new Articulation(A);
        Tarjan biconexo = new Tarjan(A);
        
        biconexo.chamadaInicial();
        biconexo.exibirComponentesBiconexos();
     }    
  }
  