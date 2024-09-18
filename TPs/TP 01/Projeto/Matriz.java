/* Classe Matriz
 * Representa uma Matriz de adjacência |V| x |V| para um determinado grafo
 */

 public class Matriz {
    protected int N; // Número de vértices |V|
    protected int[][] matriz; // Matriz de adjacência

    // CONSTRUCTOR
    public Matriz(int N) {
        this.N = N+1;
        this.matriz = new int[this.N][this.N]; // Inicializa a matriz com zeros
    }

    // GETTER
    public int getN() {
        return N;
    }

    // SETTER
    public void setN(int N) {
        this.N = N;
    }

    // Adiciona uma aresta entre dois vértices
    public void adicionarAresta(int v, int w) {
        if (v >= 0 && v <= getN() && w >= 0 && w <= getN()) {
            matriz[v][w] = 1;
            matriz[w][v] = 1; // Para grafos não direcionados, a aresta é inclusa duas vezes
        } else {
            throw new IllegalArgumentException("Vértice inválido.");
        }
    }

    // Verifica se há uma aresta entre dois vértices
    public boolean temAresta(int v, int w) {
        if (v >= 0 && v <= getN() && w >= 0 && w <= getN()) {
            return matriz[v][w] == 1;
        } else {
            throw new IllegalArgumentException("Vértice inválido.");
        }
    }

    // Imprime a matriz de adjacência
    public void imprimirMatriz() {
        for (int i = 0; i < getN(); i++) {
            for (int j = 0; j < N; j++) {
                System.out.print(matriz[i][j] + " ");
            }
            System.out.println();
        }
    }
}
