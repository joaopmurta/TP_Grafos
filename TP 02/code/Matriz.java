import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Matriz {
    protected int N; // Número de vértices |V|
    protected int[][] matriz; // Matriz de adjacência
    protected int E;
    protected int k; // k ou p centro
    protected int[][] dist;
 protected int[] centros; // Vetor para armazenar os K centros

    protected List<Integer>[] conjuntos; // Lista de conjuntos para armazenar os vértices associados a cada centro
    // Valor máximo para representar "infinito" no Floyd-Warshall
    private static final int INFINITO = 1000000;

    // CONSTRUCTOR
    public Matriz(int N, int E, int k) {
        this.N = N + 1;
        this.matriz = new int[this.N][this.N]; // Inicializa a matriz com zeros
        this.E = E;
        this.k = k;
        this.dist = new int[this.N][this.N];
        this.centros = new int[this.N];
   
        // Inicializa a matriz de distâncias com valores "infinito", exceto a diagonal principal
        for (int i = 0; i < this.N; i++) {
            for (int j = 0; j < this.N; j++) {
                if (i == j) {
                    dist[i][j] = 0; // Distância para si mesmo é 0
                } else {
                    dist[i][j] = INFINITO; // Distância inicial é "infinita"
                }
            }
        }

        // Inicializa a lista de conjuntos
        conjuntos = new ArrayList[k];
        for (int i = 0; i < k; i++) {
            conjuntos[i] = new ArrayList<>();
        }
    }

    // Adiciona uma aresta entre dois vértices com um peso específico
    public void adicionarAresta(int v, int w, int u) {
        if (v >= 0 && v < getN() && w >= 0 && w < getN()) {
            matriz[v][w] = u;
            matriz[w][v] = u;
        } else {
            throw new IllegalArgumentException("Vértice inválido.");
        }
    }

    // GETTER para o número de vértices
    public int getN() {
        return N;
    }

    // Imprime a matriz de adjacência
    public void imprimirMatriz() {
        for (int i = 1; i < getN(); i++) {
            for (int j = 1; j < getN(); j++) {
                MyIO.print((dist[i][j] == INFINITO ? "INF" : dist[i][j]) + " ");
            }
            MyIO.println();
        }
    }

    // Implementação do algoritmo de Floyd-Warshall
    public void floydWarshall() {
        // Inicializa a matriz de distâncias com os valores da matriz original
        for (int i = 1; i < N; i++) {
            for (int j = 1; j < N; j++) {
                if (matriz[i][j] != 0 && matriz[i][j] != INFINITO) {
                    dist[i][j] = matriz[i][j];
                }
            }
        }

        // Atualiza a matriz de distâncias usando Floyd-Warshall
        for (int k = 1; k < N; k++) {
            for (int i = 1; i < N; i++) {
                for (int j = 1; j < N; j++) {
                    if (dist[i][k] != INFINITO && dist[k][j] != INFINITO) {
                        dist[i][j] = Math.min(dist[i][j], dist[i][k] + dist[k][j]);
                        dist[j][i] = dist[i][j];
                    }
                }
            }
        }
    }

    public void definirCentros() {
        // Lista para armazenar os centros definidos
        List<Integer> centrosDefinidos = new ArrayList<>();
    
        // Encontra o vértice inicial que minimiza a soma das distâncias
        int melhorCentroInicial = -1;
        int menorSomaDistancias = INFINITO;
    
        for (int i = 1; i < N; i++) {
            int somaDistancias = 0;
            for (int j = 1; j < N; j++) {
                somaDistancias += dist[i][j];
            }
            if (somaDistancias < menorSomaDistancias) {
                menorSomaDistancias = somaDistancias;
                melhorCentroInicial = i;
            }
        }
        centrosDefinidos.add(melhorCentroInicial);
    
        // Iterativamente escolhe os próximos centros
        while (centrosDefinidos.size() < k) {
            int novoCentro = -1;
            int menorCustoAdicional = INFINITO;
    
            for (int candidato = 1; candidato < N; candidato++) {
                if (centrosDefinidos.contains(candidato)) continue; // Ignorar já escolhidos
    
                int custoAdicional = 0;
    
                // Calcula o custo adicional se o candidato for um centro
                for (int i = 1; i < N; i++) {
                    int menorDistanciaAtual = INFINITO;
    
                    // Verifica a distância atual até os centros existentes
                    for (int centro : centrosDefinidos) {
                        menorDistanciaAtual = Math.min(menorDistanciaAtual, dist[i][centro]);
                    }
    
                    // Compara com a distância para o novo candidato
                    int distanciaParaCandidato = dist[i][candidato];
                    custoAdicional += Math.min(menorDistanciaAtual, distanciaParaCandidato);
                }
    
                // Atualiza o melhor candidato
                if (custoAdicional < menorCustoAdicional) {
                    menorCustoAdicional = custoAdicional;
                    novoCentro = candidato;
                }
            }
    
            // Adiciona o melhor candidato como novo centro
            centrosDefinidos.add(novoCentro);
        }
    
        // Salva os centros definidos no array `centros`
        for (int i = 0; i < k; i++) {
            centros[i] = centrosDefinidos.get(i);
        }
    }
    
    
    // Imprime os K centros
    public void imprimirCentros() {
        MyIO.println("K Centros:");
        for (int i = 0; i < k; i++) {
            MyIO.println("Centro " + (i + 1) + ": " + centros[i]);
        }
    }

    // Método corrigido para associar vértices aos conjuntos mais próximos
    public void definirConjuntos() {
        // Limpa os conjuntos antes de preenchê-los
        for (int i = 0; i < k; i++) {
            conjuntos[i].clear();
        }

        // Itera por todos os vértices
        for (int i = 1; i < N; i++) {
            // Verifica se o vértice atual é um centro
            boolean isCentro = false;
            for (int j = 0; j < k; j++) {
                if (i == centros[j]) {
                    isCentro = true;
                    break;
                }
            }
            if (isCentro) continue;

            // Encontra o centro mais próximo para o vértice
            int centroMaisProximo = -1;
            int menorDistancia = INFINITO;

            for (int j = 0; j < k; j++) {
                int centroAtual = centros[j];
                if (dist[i][centroAtual] != INFINITO && dist[i][centroAtual] < menorDistancia) {
                    menorDistancia = dist[i][centroAtual];
                    centroMaisProximo = j; // Índice do centro na lista "centros"
                }
            }

            // Adiciona o vértice ao conjunto do centro mais próximo
            if (centroMaisProximo != -1) {
                conjuntos[centroMaisProximo].add(i);
            }
        }
    }


    // Imprime os conjuntos de vértices associados aos centros
    public void imprimirConjuntos() {
        for (int i = 0; i < k; i++) {
            MyIO.println("Conjunto para o centro " + centros[i] + ": " + conjuntos[i]);
        }
    }

    public int calcularRaios() {
        int raioGlobal = 0;
    
        // Itera sobre cada conjunto
        for (int i = 0; i < k; i++) {
            int centro = centros[i];
            int raioLocal = 0;
    
            // Verifica as distâncias do centro atual para todos os vértices do conjunto
            for (int vertice : conjuntos[i]) {
                raioLocal = Math.max(raioLocal, dist[centro][vertice]);
            }
    
            // Atualiza o raio global
            raioGlobal = Math.max(raioGlobal, raioLocal);
    
            // Exibe o raio local para o conjunto atual
            MyIO.println("Raio do conjunto para o centro " + centro + ": " + raioLocal);
        }
    
        // Exibe o raio global
        MyIO.println("Raio global: " + raioGlobal);
    
        return raioGlobal;
    }
    
}

