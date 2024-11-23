/* Classe Main
 * Implementação da classe principal que lê o arquivo txt com os dados e orquestra as tarefas a serem realizadas
 * João Pedro de Melo Murta
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
             int E = Integer.parseInt(firstLine[1]);
             int P = Integer.parseInt(firstLine[2]);
     
             A = new Matriz(V, E, P); // Cria o grafo em forma de matriz
     
             // Lê os pares de vértices e os insere no grafo até o fim do arquivo
             String line;
             while ((line = br.readLine()) != null) {
                 String[] vertices = line.trim().split("\\s+"); // expressão regular para garantir que apenas números sejam capturados
     
                 int v = Integer.parseInt(vertices[0]);
                 int w = Integer.parseInt(vertices[1]);
                 int u = Integer.parseInt(vertices[2]); // custo
     
                 A.adicionarAresta(v, w, u);
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
         String arqName = "inst/pmed1.txt";
         
         // Leitura do arquivo de entrada
         lerArq(arqName);
         
         // Inicia a contagem do tempo
         //long startTime = System.nanoTime();
         A.floydWarshall();
         //A.imprimirMatriz();
         //int raio = 0;
       
         A.definirCentros();
         
         A.imprimirCentros();
         // Calcula o tempo de execução em segundos
         //long endTime = System.nanoTime();
         //double durationInSeconds = (endTime - startTime) / 1_000_000_000.0;
 
         A.definirConjuntos();  // Cria os conjuntos
         A.imprimirConjuntos(); // Imprime os conjuntos de vértices
         A.calcularRaios();
     }
  }
  
 