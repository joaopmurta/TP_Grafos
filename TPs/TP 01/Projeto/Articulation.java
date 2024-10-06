import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Articulation {
    private Matriz grafo; // Matriz de adjacência
    private List<List<int[]>> componentesBiconexos; // Lista para armazenar os componentes biconexos
    private Deque<int[]> pilhaArestas; // Pilha para armazenar as arestas
    private int tempo; // Variável auxiliar para manter o tempo de descoberta na DFS
    private int[] tempoDescoberta, menorTempo; // Arrays para armazenar os tempos de descoberta e menor tempo de retorno
    private boolean[] visitado; // Array para marcar vértices visitados

    public Articulation(Matriz grafo) {
        this.grafo = grafo;
        this.componentesBiconexos = new ArrayList<>();
        this.pilhaArestas = new ArrayDeque<>();
        int numVertices = grafo.getN();
        this.tempoDescoberta = new int[numVertices];
        this.menorTempo = new int[numVertices];
        this.visitado = new boolean[numVertices];
    }

    // Método inicial para detectar componentes biconexos
    public void chamadaInicial() {
        int numVertices = grafo.getN();
        int[] pais = new int[numVertices];
        Arrays.fill(pais, -1); // Inicializa todos os pais como -1

        // Inicia DFS para encontrar componentes biconexos
        for (int vertice = 0; vertice < numVertices; vertice++) {
            if (!visitado[vertice]) {
                dfs(vertice, pais);

                // Ao final da DFS de um vértice raiz, esvazie a pilha para registrar o componente biconexo restante
                List<int[]> componenteAtual = new ArrayList<>();
                while (!pilhaArestas.isEmpty()) {
                    componenteAtual.add(pilhaArestas.pop());
                }
                if (!componenteAtual.isEmpty()) {
                    componentesBiconexos.add(componenteAtual);
                }
            }
        }
    }

    // DFS para encontrar articulações e componentes biconexos
    private void dfs(int v, int[] pais) {
        visitado[v] = true;
        tempoDescoberta[v] = menorTempo[v] = ++tempo;
        int filhos = 0;

        for (int u = 0; u < grafo.getN(); u++) {
            if (grafo.temAresta(v, u)) {
                if (!visitado[u]) {
                    filhos++;
                    pais[u] = v;
                    pilhaArestas.push(new int[]{v, u}); // Armazena a aresta na pilha

                    // Recurre DFS
                    dfs(u, pais);

                    // Verifica se a subárvore enraizada em u tem uma conexão de retorno para um dos ancestrais de v
                    menorTempo[v] = Math.min(menorTempo[v], menorTempo[u]);

                    // Se u não pode alcançar um ancestral de v, então v-u é uma articulação
                    if ((pais[v] == -1 && filhos > 1) || (pais[v] != -1 && menorTempo[u] >= tempoDescoberta[v])) {
                        // Encontrou um componente biconexo, esvazia a pilha até encontrar a aresta v-u
                        List<int[]> componenteAtual = new ArrayList<>();
                        while (!pilhaArestas.isEmpty()) {
                            int[] aresta = pilhaArestas.pop();
                            componenteAtual.add(aresta);
                            if (aresta[0] == v && aresta[1] == u) {
                                break;
                            }
                        }
                        componentesBiconexos.add(componenteAtual); // Adiciona o componente biconexo à lista
                    }
                } else if (u != pais[v] && tempoDescoberta[u] < menorTempo[v]) {
                    // Atualiza menorTempo se u for um ancestral de v
                    menorTempo[v] = Math.min(menorTempo[v], tempoDescoberta[u]);
                    pilhaArestas.push(new int[]{v, u}); // Armazena a aresta na pilha
                }
            }
        }
    }

    // Método para gravar os componentes biconexos em um arquivo
    public void escreveComponentesBiconexos() {
        try {
            // Obtém o número de vértices do grafo
            int numVertices = grafo.getN() - 1;
            // Cria o nome do arquivo dinamicamente usando o número de vértices
            String nomeArquivo = "Articulation_" + numVertices + ".txt";
            
            // Cria ou sobrescreve o arquivo com o nome gerado
            FileWriter writer = new FileWriter(nomeArquivo);
            writer.write("Componentes biconexos:\n");
            for (List<int[]> componente : componentesBiconexos) {
                // Verifica se o componente contém mais de duas arestas e menos de 100
                if (componente.size() > 2 && componente.size() < 100) {
                    for (int[] aresta : componente) {
                        writer.write(Arrays.toString(aresta) + "\n");
                    }
                    writer.write("\n");  // Adiciona uma linha em branco para separar os componentes
                }
            }
            
            // Fecha o writer para garantir que todos os dados sejam gravados no arquivo
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }   
}
