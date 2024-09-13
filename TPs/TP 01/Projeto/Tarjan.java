/* Classe Tarjan
 * Implementação do método proposto por Tarjan para determinar os compontentes biconexos de um grafo não direcionado G
 */

 import java.util.Stack;

 public class Tarjan {
     private Matriz grafo; // Matriz de adjacência
     private int[] TD; // Vetor para armazenar o número de descoberta de cada vértice
     private int[] lowpt; // Vetor para armazenar o menor ponto de alcance de cada vértice
     private boolean[] marcado; // Vetor para marcar vértices visitados
     private int i; // Contador para o número de descoberta
     private Stack<int[]> pilha; // Pilha de arestas
 
     public Tarjan(Matriz grafo) {
         this.grafo = grafo;
         int N = grafo.getN(); // Número de vértices
         i = 0;
         // Cria vetores e pilha 
         TD = new int[N];
         lowpt = new int[N];
         marcado = new boolean[N];
         pilha = new Stack<>();
 
         // Inicializa os arrays com valores padrão
         for (int v = 0; v < N; v++) {
             TD[v] = -1; // Vértice não foi numerado ainda
             lowpt[v] = -1;
             marcado[v] = false;
         }
     }

     // Executa o algoritmo Tarjan para encontrar componentes biconexos
     public void chamadaInicial(){
        for (int v = 0; v < grafo.getN(); v++) {
            if (TD[v] == -1) {
                biconnect(v, -1); // Começa com vértice v e pai não existe pois v é uma raiz na busca
            }
        }
     }
 
     // Função recursiva que encontra os componentes biconexos
     private void biconnect(int v, int u) {
         TD[v] = lowpt[v] = i++;
         marcado[v] = true; // Marca V
 
         // Percorre todos os vértices adjacentes a v
         for (int w = 0; w < grafo.getN(); w++) {
            // Verifica se há aresta {v, w} no grafo
             if (grafo.temAresta(v, w)) {
                 if (TD[w] == -1) { // Se w ainda não foi numerado
                     pilha.push(new int[]{v, w}); // Adiciona aresta (v, w) na pilha
                     biconnect(w, v); // Chamada recursiva para vértice w
 
                     // Atualiza o valor de lowpt de v
                     lowpt[v] = Math.min(lowpt[v], lowpt[w]);
 
                     // Verifica se w forma um novo componente biconexo
                     if (lowpt[w] >= TD[v]) {
                         int[] aresta;
                         do {
                             aresta = pilha.pop(); // Remove arestas da pilha
                         } while (!(aresta[0] == v && aresta[1] == w));
                     }
                 } else if (w != u && TD[w] < TD[v]) {
                     // Se w já foi numerado e não é o pai de v, é uma aresta de retorno
                     pilha.push(new int[]{v, w}); // Adiciona aresta de retorno à pilha
                     lowpt[v] = Math.min(lowpt[v], TD[w]);
                 }
             }
         }
         i = 0;
         // esvaziar pilha
         for(int w = 0; w < grafo.getN(); w++){
            if(marcado[w] == false){
                biconnect(w, -1);
            }
         }
     }
 }
 