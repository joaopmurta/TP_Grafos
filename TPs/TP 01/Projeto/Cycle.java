/* Classe Cycle
 * Implementação da procura por ciclos em um grafo para determinar os compontentes biconexos de um grafo não direcionado G
 */

 import java.io.FileWriter;
 import java.io.IOException;
 import java.util.ArrayList;
 import java.util.Arrays;
 import java.util.List;
 
 public class Cycle {
     private Matriz grafo; // Matriz de adjacência
     private List<List<int[]>> componentesBiconexos; // Armazena os componentes biconexos

     public Cycle(Matriz grafo) {
         this.grafo = grafo;
         this.componentesBiconexos = new ArrayList<>();
     }

     // Escreve os componentes biconexos encontrados em um arquivo
     public void escreveComponentesBiconexos() {
         try {
             int numVertices = grafo.getN() - 1; // Obtém o número de vértices
             String nomeArquivo = "Cycle_" + numVertices + ".txt"; // Nome do arquivo
     
             FileWriter writer = new FileWriter(nomeArquivo);
             writer.write("Componentes biconexos:\n");
             for (List<int[]> componente : componentesBiconexos) {
                 if (componente.size() > 2 && componente.size() < 100) { // Limita o tamanho do componente
                     for (int[] aresta : componente) {
                         writer.write(Arrays.toString(aresta) + "\n");
                     }
                     writer.write("\n"); // Linha em branco para separar componentes
                 }
             }
             writer.write("Fim!\n");
             writer.close(); // Fecha o writer
         } catch (IOException e) {
             e.printStackTrace();
         }
     }
 }
 