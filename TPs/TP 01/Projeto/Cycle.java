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
     private List<List<int[]>> componentesBiconexos; // Armazena os componentes biconexos  private boolean[] visitado; // Array para marcar vértices visitados
     private boolean[] visitado; // Array para marcar vértices visitados

     public Cycle(Matriz grafo) {
         this.grafo = grafo;
         componentesBiconexos = new ArrayList<>();
         visitado = new boolean[grafo.getN() +1]; // Inicializa o array de visitados
     }
 
     public void chamadaInicial() {
        int n = grafo.getN();
    
        for (int v = 0; v < n; v++) {
            for (int w = v + 1; w < n; w++) { // Verifica pares de vértices
                if (temCiclo(v, w)) {
                    List<int[]> componente = new ArrayList<>();
                    componente.add(new int[]{v, w});
    
                    // Adiciona outros vértices que fazem parte do mesmo componente
                    for (int u = 0; u < n; u++) {
                        // Verifica se u não é adjacente a v e não é igual a w
                        if (u != w && grafo.temAresta(v, u) && !grafo.temAresta(w, u) && temCiclo(v, u)) {
                            componente.add(new int[]{v, u});
                        }
                    }
    
                    // Verifica se o componente tem pelo menos 3 vértices
                    if (componentesBiconexos.stream().noneMatch(c -> c.equals(componente) && componente.size() >= 3)) {
                        componentesBiconexos.add(componente);
                    }
                }
                // Limpa o array de visitados para o próximo par
                Arrays.fill(visitado, false);
            }
        }
    }
     
    // Método para verificar se existe um ciclo entre v e w
    private boolean temCiclo(int v, int w) {
        return dfs(v, w, -1); // Chama DFS passando -1 como pai
    }

    // Busca em profundidade (DFS)
    private boolean dfs(int v, int w, int pai) {
        visitado[v] = true;

        for (int vizinho = 0; vizinho < grafo.getN(); vizinho++) {
            // Verifique se a matriz de adjacência indica que v é adjacente a vizinho
            if (grafo.temAresta(v, vizinho)) {
                // Se encontramos w e não é o pai, temos um ciclo
                if (vizinho == w && pai != -1) {
                    return true;
                }
                if (!visitado[vizinho]) {
                    // Recursão DFS
                    if (dfs(vizinho, w, v)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void escreveComponentesBiconexos() {
         try {
             int numVertices = grafo.getN() - 1; // Obtém o número de vértices
             String nomeArquivo = "Cycle_" + numVertices + ".txt"; // Nome do arquivo
             
             FileWriter writer = new FileWriter(nomeArquivo);
             writer.write("Componentes biconexos:\n");
             for (List<int[]> componente : componentesBiconexos) {
                 if (componente.size() > 2 && componente.size() < 100) {
                     for (int[] aresta : componente) {
                         writer.write(Arrays.toString(aresta) + "\n");
                     }
                     writer.write("\n"); // Linha em branco para separar componentes
                 }
             }
             writer.close(); // Fecha o writer
         } catch (IOException e) {
             e.printStackTrace();
         }
     }
 }
 