/* Classe Articulation
 * Implementação da procura por articulações removendo sucessivamente oss vértices em um grafo para determinar os compontentes biconexos de um grafo não direcionado G
 */

 import java.io.FileWriter;
 import java.io.IOException;
 import java.util.ArrayList;
 import java.util.Arrays;
 import java.util.List;

public class Articulation {
    private Matriz grafo; // Matriz de adjacência
    private List<List<int[]>> componentesBiconexos; // Armazena os componentes biconexos  private boolean[] visitado; // Array para marcar vértices visitados
    private boolean[] visitado; // Array para marcar vértices visitados

    public Articulation(Matriz grafo) {
        this.grafo = grafo;
        componentesBiconexos = new ArrayList<>();
       
    }

    // Executa o algoritmo para encontrar componentes biconexos
    public void chamadaInicial() {

    }

    public void escreveComponentesBiconexos() {
        try {
            int numVertices = grafo.getN() - 1; // Obtém o número de vértices
            String nomeArquivo = "Articulation_" + numVertices + ".txt"; // Nome do arquivo
            
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
