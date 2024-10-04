/* Classe Cycle
 * Implementação da procura por ciclos em um grafo para determinar os compontentes biconexos de um grafo não direcionado G
 */
import java.util.*;
import java.io.FileWriter;
import java.io.IOException;

public class Cycle {
    private Matriz grafo; // Matriz de adjacência
    private List<List<int[]>> componentesBiconexos; // Armazena os componentes biconexos
    private boolean[][] visitadas; // Marca se as arestas já foram usadas
    private Deque<int[]> pilhaArestas; // Pilha para armazenar as arestas do caminho
    private Deque<Integer> pilhaVertices; // Pilha para armazenar os vértices no caminho

    public Cycle(Matriz grafo) {
        this.grafo = grafo;
        this.componentesBiconexos = new ArrayList<>();
        this.visitadas = new boolean[grafo.getN() + 1][grafo.getN() + 1]; // Inicializa com o número de vértices
        this.pilhaArestas = new ArrayDeque<>();
        this.pilhaVertices = new ArrayDeque<>();
    }

    // Método principal para encontrar componentes biconexos
    public void chamadaInicial() {
        for (int v = 0; v < grafo.getN(); v++) { // Para cada vértice v
            boolean[] visitados = new boolean[grafo.getN() + 1];
            dfs(v, -1, visitados); // Chama DFS para procurar ciclos a partir de v
        }
    }

    // Implementação da DFS que procura ciclos
    private void dfs(int v, int pai, boolean[] visitados) {
        visitados[v] = true;
        pilhaVertices.push(v); // Adiciona o vértice atual na pilha de vértices

        // Lista de vizinhos de v ordenados lexicograficamente
        List<Integer> vizinhos = new ArrayList<>();
        for (int w = 0; w < grafo.getN(); w++) {
            if (grafo.temAresta(v, w)) {
                vizinhos.add(w);
            }
        }
        Collections.sort(vizinhos); // Ordena os vizinhos em ordem crescente

        for (int w : vizinhos) {
            if (!visitados[w]) { // Se w não foi visitado, continua a DFS
                if (!arestaJaEmComponente(v, w)) { // Verifica se a aresta já está em um componente
                    pilhaArestas.push(new int[]{v, w}); // Adiciona a aresta na pilha
                    visitadas[v][w] = visitadas[w][v] = true; // Marca a aresta como usada
                    dfs(w, v, visitados); // Chama recursivamente a DFS
                }
            } else if (w != pai && !visitadas[v][w]) { // Se encontra um ciclo (aresta de retorno)
                // Ciclo encontrado entre v e w, salvar componente biconexo
                List<int[]> componenteBiconexo = new ArrayList<>();

                // Percorrer o caminho na pilha de vértices até voltar ao vértice w
                Iterator<int[]> iterArestas = pilhaArestas.iterator();
                while (iterArestas.hasNext()) {
                    int[] aresta = iterArestas.next();
                    componenteBiconexo.add(aresta);
                    if ((aresta[0] == w || aresta[1] == w)) {
                        componenteBiconexo.add(new int[]{w, v}); // Aresta que fecha o ciclo
                        break; // Ciclo completo
                    }
                }

                // Adiciona o componente biconexo completo
                componentesBiconexos.add(componenteBiconexo);
                limpaPilhaArestas(w); // Limpa arestas até w
            }
        }

        // Limpa as pilhas se o ciclo não for encontrado ou ao voltar da chamada
        pilhaVertices.pop();
        if (!pilhaArestas.isEmpty()) {
            pilhaArestas.pop();
        }
    }

    // Método para limpar a pilha de arestas até um vértice específico
    private void limpaPilhaArestas(int w) {
        while (!pilhaArestas.isEmpty()) {
            int[] aresta = pilhaArestas.peek();
            if (aresta[0] == w || aresta[1] == w) {
                break; // Chegou ao vértice w, parar de remover
            }
            pilhaArestas.pop(); // Remove aresta
        }
    }

    // Método que verifica se uma aresta já pertence a um componente biconexo
    private boolean arestaJaEmComponente(int v, int w) {
        for (List<int[]> componente : componentesBiconexos) {
            for (int[] aresta : componente) {
                if ((aresta[0] == v && aresta[1] == w) || (aresta[0] == w && aresta[1] == v)) {
                    return true; // Aresta já pertence a um componente
                }
            }
        }
        return false; // Aresta não foi encontrada em nenhum componente
    }

    // Escreve os componentes biconexos encontrados em um arquivo
    public void escreveComponentesBiconexos() {
        try {
            int numVertices = grafo.getN() - 1; // Obtém o número de vértices
            String nomeArquivo = "Cycle_" + numVertices + ".txt"; // Nome do arquivo

            FileWriter writer = new FileWriter(nomeArquivo);
            writer.write("Componentes biconexos:\n");
            for (List<int[]> componente : componentesBiconexos) {
                if (componente.size() > 2) { // Limita o tamanho do componente
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
