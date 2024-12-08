import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Exact {

    private Matriz adjacencyMatrix; // Matriz de adjacência representando o grafo
    private int numVertices;        // Número de vértices no grafo

    // Construtor que inicializa a matriz de adjacência e o número de vértices
    public Exact(Matriz adjacencyMatrix) {
        this.adjacencyMatrix = adjacencyMatrix;
        this.numVertices = adjacencyMatrix.getN(); // Obtém o número de vértices
        System.out.println("Número de vértices: " + this.numVertices);
    }

    // Método principal para encontrar os k-centros no grafo
    public List<Integer> findKCenters(int k) {
        // Lista com todos os vértices do grafo
        List<Integer> vertices = new ArrayList<>();
        for (int i = 1; i <= numVertices; i++) {
            vertices.add(i);
        }

        // Inicializa listas para os centros escolhidos e a melhor solução encontrada
        List<Integer> centers = new ArrayList<>();
        List<Integer> bestCenters = new ArrayList<>();

        // Assume uma solução inicial escolhendo os primeiros k vértices
        bestCenters.addAll(vertices.subList(0, Math.min(k, vertices.size())));
        System.out.println("Vértices iniciais: " + vertices);

        // Recursivamente encontra os melhores centros
        findKCentersUtil(vertices, centers, bestCenters, k);
        return bestCenters;
    }

    // Método recursivo para explorar todas as combinações de centros possíveis
    private void findKCentersUtil(List<Integer> vertices, List<Integer> currentCenters, List<Integer> bestCenters, int k) {
        // Se a combinação atual possui k centros, avalia a solução
        if (currentCenters.size() == k) {
            System.out.println("Testando centros: " + currentCenters);

            // Atualiza a melhor solução se a distância máxima for menor
            if (calculateMaxDistance(currentCenters) < calculateMaxDistance(bestCenters)) {
                bestCenters.clear();
                bestCenters.addAll(currentCenters);
                System.out.println("Atualizou melhores centros: " + bestCenters);
            }
            return;
        }

        // Gera combinações de centros recursivamente
        for (int i = 0; i < vertices.size(); i++) {
            int vertex = vertices.get(i); // Seleciona um vértice como centro
            currentCenters.add(vertex);  // Adiciona à lista de centros atuais
            vertices.remove(i);          // Remove temporariamente o vértice da lista

            findKCentersUtil(vertices, currentCenters, bestCenters, k);

            // Restaura o estado original após explorar a combinação
            vertices.add(i, vertex);
            currentCenters.remove(currentCenters.size() - 1);
        }
    }

    // Calcula a maior distância entre qualquer vértice e o centro mais próximo
    public int calculateMaxDistance(List<Integer> centers) {
        int maxDistance = 0;

        // Itera por todos os vértices do grafo
        for (int i = 1; i <= numVertices; i++) {
            int minDistance = Integer.MAX_VALUE;

            // Calcula a menor distância entre o vértice atual e um dos centros
            for (int center : centers) {
                int distance = adjacencyMatrix.dist[i][center];
                minDistance = Math.min(minDistance, distance);
            }

            // Atualiza a maior distância mínima encontrada
            maxDistance = Math.max(maxDistance, minDistance);

            // Exibe informações de depuração para o vértice atual
            System.out.println("Distância mínima para vértice " + i + ": " + minDistance);
        }

        System.out.println("Distância máxima entre centros e vértices: " + maxDistance);
        return maxDistance;
    }

    // Método principal do programa
    public static void main(String[] args) {
        try {
            // Lê o arquivo de entrada com os dados do grafo
            File file = new File("../inst/pmed1.txt");
            Scanner scanner = new Scanner(file);

            // Lê os parâmetros principais: número de vértices, arestas e centros
            int n = scanner.nextInt(); // Número de vértices
            int m = scanner.nextInt(); // Número de arestas
            int k = scanner.nextInt(); // Número de centros

            Matriz matriz = new Matriz(n); // Cria uma matriz para o grafo

            // Lê as arestas do grafo
            for (int i = 0; i < m; i++) {
                int u = scanner.nextInt(); // Vértice de origem
                int v = scanner.nextInt(); // Vértice de destino
                int peso = scanner.nextInt(); // Peso da aresta
                matriz.adicionarAresta(u, v, peso);
            }

            scanner.close(); // Fecha o arquivo de entrada

            // Exibe a matriz de adjacência para depuração
            System.out.println("Matriz de adjacência:");
            matriz.imprimirMatriz();

            // Executa o algoritmo para encontrar os k-centros
            Exact exact = new Exact(matriz);
            List<Integer> centers = exact.findKCenters(k);

            // Exibe os centros escolhidos
            System.out.println("Centros escolhidos: " + centers);
        } catch (FileNotFoundException e) {
            // Trata erro caso o arquivo não seja encontrado
            System.out.println("Arquivo não encontrado: " + e.getMessage());
        }
    }
}

// Classe auxiliar para representar a matriz de adjacência
class Matriz {
    int[][] dist; // Matriz de distâncias
    private int n; // Número de vértices

    // Construtor que inicializa a matriz
    public Matriz(int n) {
        this.n = n;
        dist = new int[n + 1][n + 1]; // Índices de 1 a n
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= n; j++) {
                dist[i][j] = (i == j) ? 0 : Integer.MAX_VALUE; // Inicializa com 0 ou infinito
            }
        }
    }

    public int getN() {
        return n;
    }

    // Adiciona uma aresta ao grafo
    public void adicionarAresta(int u, int v, int peso) {
        dist[u][v] = peso; // Define a distância entre os vértices
        dist[v][u] = peso; // Define a distância inversa (grafo não direcionado)
    }

    // Exibe a matriz de adjacência
    public void imprimirMatriz() {
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                System.out.print((dist[i][j] == Integer.MAX_VALUE ? "INF" : dist[i][j]) + " ");
            }
            System.out.println();
        }
    }
}
