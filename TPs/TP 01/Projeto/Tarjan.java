/* Classe Tarjan
 * Implementação do método proposto por Tarjan para determinar os compontentes biconexos de um grafo não direcionado G
 */

 import java.util.ArrayList;
 import java.util.Arrays;
 import java.util.List;
 import java.util.Stack;

 public class Tarjan {
     private Matriz grafo; // Matriz de adjacência
     private int[] TD; // Vetor para armazenar o número de descoberta de cada vértice
     private int[] lowpt; // Vetor para armazenar o menor ponto de alcance de cada vértice
     private boolean[] marcado; // Vetor para marcar vértices visitados
     private int i; // Contador para o número de descoberta
     private Stack<int[]> pilha; // Pilha de arestas
     private List<List<int[]>> componentesBiconexos = new ArrayList<>(); // Armazena os componentes biconexos
 
     public Tarjan(Matriz grafo) {
         this.grafo = grafo;
         int N = grafo.getN(); // Número de vértices
         i = 1;
         // Cria vetores e pilha 
         TD = new int[N+1];
         lowpt = new int[N+1];
         marcado = new boolean[N+1];
         pilha = new Stack<>();
 
         // Inicializa os arrays com valores padrão
         for (int v = 0; v < N; v++) {
             TD[v] = 0; // Vértice não foi numerado ainda
             lowpt[v] = 0;
             marcado[v] = false;
         }
     }
    // Executa o algoritmo de Tarjan para encontrar componentes biconexos
    public void chamadaInicial() {
        for (int v = 0; v < grafo.getN(); v++) {
            if (TD[v] == 0) {
                biconnect(v, -1); // Começa com vértice v e pai não existe pois v é uma raiz na busca
            }
        }
    }

    // Função recursiva que encontra os componentes biconexos
    private void biconnect(int v, int u) {
        TD[v] = lowpt[v] = i++;
        marcado[v] = true; // Marca V

        // Percorre todos os vértices adjacentes a v
        for (int w = 1; w < grafo.getN(); w++) {
            // Verifica se há aresta {v, w} no grafo
            if (grafo.temAresta(v, w)) {
                if (TD[w] == 0) { // Se w ainda não foi numerado
                    pilha.push(new int[]{v, w}); // Adiciona aresta (v, w) na pilha
                    biconnect(w, v); // Chamada recursiva para vértice w

                    // Atualiza o valor de lowpt de v
                    lowpt[v] = Math.min(lowpt[v], lowpt[w]);

                    // Verifica se w forma um novo componente biconexo
                    if (lowpt[w] >= TD[v]) {
                        List<int[]> componente = new ArrayList<>(); // Novo componente biconexo
                        int[] aresta;
                        do {
                            aresta = pilha.pop(); // Remove arestas da pilha
                            componente.add(aresta); // Adiciona ao componente
                        } while (aresta[0] != v || aresta[1] != w);                         
                        componentesBiconexos.add(componente); // Salva o componente biconexo
                    }

                } else if (w != u && TD[w] < TD[v]) {
                    // Se w já foi numerado e não é o pai de v, é uma aresta de retorno
                    pilha.push(new int[]{v, w}); // Adiciona aresta de retorno à pilha
                    lowpt[v] = Math.min(lowpt[v], TD[w]);
                }
            }
        }
    }

    // Exibe os componentes biconexos encontrados
    public void exibirComponentesBiconexos() {
        for (List<int[]> componente : componentesBiconexos) {
            // Verifica se o componente contém mais de duas arestas
            if (componente.size() > 2 && componente.size() < 100) {
                MyIO.println("Componente biconexo:");
                for (int[] aresta : componente) {
                    MyIO.println(Arrays.toString(aresta));
                }
            }
        }
    }
 }
 