/* Classe Articulation
 * Implementação da procura por articulações removendo sucessivamente oss vértices em um grafo para determinar os compontentes biconexos de um grafo não direcionado G
 */
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Articulation {
    private Matriz grafo; // Matriz de adjacência
    private List<List<int[]>> componentesBiconexos; // Lista para armazenar os componentes biconexos

    public Articulation(Matriz grafo) {
        this.grafo = grafo;
        this.componentesBiconexos = new ArrayList<>();
    }

    // Método para identificar articulações e componentes biconexos
    public void chamadaInicial() {
        int numVertices = grafo.getN();
        List<Integer> articulacoes = new ArrayList<>();

        for (int vertice = 0; vertice < numVertices; vertice++) {
            List<int[]> componenteAtual = new ArrayList<>(); // Armazena o componente atual

            if (!grafoConectadoSemVertice(vertice, componenteAtual)) {
                articulacoes.add(vertice);
                componentesBiconexos.add(componenteAtual); // Adiciona o componente biconexo à lista
            }
        }
    }

    // Testa a conectividade após a remoção de um vértice e armazena as arestas dos componentes biconexos
    private boolean grafoConectadoSemVertice(int verticeRemovido, List<int[]> componenteAtual) {
        boolean[] visitado = new boolean[grafo.getN()];  // Reinicializa o array de visitados
        int verticeInicial = (verticeRemovido == 0) ? 1 : 0;  // Escolhe outro vértice para iniciar a DFS
        dfs(verticeInicial, visitado, verticeRemovido, componenteAtual);

        // Verifica se há algum vértice que não foi visitado (exceto o vértice removido)
        for (int i = 0; i < grafo.getN(); i++) {
            if (i != verticeRemovido && !visitado[i]) {
                return false;  // O grafo não está conectado
            }
        }
        return true;  // O grafo está conectado
    }

    // DFS para testar conectividade sem um vértice específico e armazenar as arestas do componente biconexo
    private void dfs(int v, boolean[] visitado, int verticeRemovido, List<int[]> componenteAtual) {
        visitado[v] = true;
        for (int u = 0; u < grafo.getN(); u++) {
            if (grafo.temAresta(v, u) && !visitado[u] && u != verticeRemovido) {
                componenteAtual.add(new int[] { v, u }); // Adiciona a aresta ao componente biconexo
                dfs(u, visitado, verticeRemovido, componenteAtual);
            }
        }
    }

    // Método para gravar os componentes biconexos em um arquivo
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
