import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Exact {

    private Matriz adjacencyMatrix; // Matriz de adjacência
    private int numVertices;        // Número de vértices

    public Exact(Matriz adjacencyMatrix) {
        this.adjacencyMatrix = adjacencyMatrix;
        this.numVertices = adjacencyMatrix.getN(); // Número de vértices
        System.out.println("Número de vértices: " + this.numVertices);
    }

    public List<Integer> findKCenters(int k) {
        List<Integer> vertices = new ArrayList<>();
        for (int i = 1; i <= numVertices; i++) { // Índices de 1 a n
            vertices.add(i);
        }

        List<Integer> centers = new ArrayList<>();
        List<Integer> bestCenters = new ArrayList<>();

        // Inicializa o melhor resultado com um valor alto
        bestCenters.addAll(vertices.subList(0, Math.min(k, vertices.size())));
        System.out.println("Vértices iniciais: " + vertices);
        findKCentersUtil(vertices, centers, bestCenters, k);
        return bestCenters;
    }

    private void findKCentersUtil(List<Integer> vertices, List<Integer> currentCenters, List<Integer> bestCenters, int k) {
        if (currentCenters.size() == k) {
            System.out.println("Testando centros: " + currentCenters);
            if (calculateMaxDistance(currentCenters) < calculateMaxDistance(bestCenters)) {
                bestCenters.clear();
                bestCenters.addAll(currentCenters);
                System.out.println("Atualizou melhores centros: " + bestCenters);
            }
            return;
        }

        for (int i = 0; i < vertices.size(); i++) {
            int vertex = vertices.get(i);
            currentCenters.add(vertex);
            vertices.remove(i);

            findKCentersUtil(vertices, currentCenters, bestCenters, k);

            vertices.add(i, vertex);
            currentCenters.remove(currentCenters.size() - 1);
        }
    }

    public int calculateMaxDistance(List<Integer> centers) {
        int maxDistance = 0;

        for (int i = 1; i <= numVertices; i++) { // Índices de 1 a n
            int minDistance = Integer.MAX_VALUE;

            for (int center : centers) {
                int distance = adjacencyMatrix.dist[i][center];
                minDistance = Math.min(minDistance, distance);
            }

            maxDistance = Math.max(maxDistance, minDistance);

            System.out.println("Distância mínima para vértice " + i + ": " + minDistance);
        }

        System.out.println("Distância máxima entre centros e vértices: " + maxDistance);
        return maxDistance;
    }

    public static void main(String[] args) {
        try {
            // Lê o arquivo
            File file = new File("teste.txt");
            Scanner scanner = new Scanner(file);

            // Lê os parâmetros principais
            int n = scanner.nextInt(); // Número de vértices
            int m = scanner.nextInt(); // Número de arestas
            int k = scanner.nextInt(); // Número de centros

            Matriz matriz = new Matriz(n);

            // Lê as arestas
            for (int i = 0; i < m; i++) {
                int u = scanner.nextInt();
                int v = scanner.nextInt();
                int peso = scanner.nextInt();
                matriz.adicionarAresta(u, v, peso);
            }

            scanner.close();

            System.out.println("Matriz de adjacência:");
            matriz.imprimirMatriz();

            Exact exact = new Exact(matriz);
            List<Integer> centers = exact.findKCenters(k);

            System.out.println("Centros escolhidos: " + centers);
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo não encontrado: " + e.getMessage());
        }
    }
}

// Classe auxiliar Matriz
class Matriz {
    int[][] dist;
    private int n;

    public Matriz(int n) {
        this.n = n;
        dist = new int[n + 1][n + 1]; // Índices de 1 a n
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= n; j++) {
                dist[i][j] = (i == j) ? 0 : Integer.MAX_VALUE; // Inicializa com infinito
            }
        }
    }

    public int getN() {
        return n;
    }

    public void adicionarAresta(int u, int v, int peso) {
        dist[u][v] = peso;
        dist[v][u] = peso; // Se for grafo não direcionado
    }

    public void imprimirMatriz() {
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                System.out.print((dist[i][j] == Integer.MAX_VALUE ? "INF" : dist[i][j]) + " ");
            }
            System.out.println();
        }
    }
}
