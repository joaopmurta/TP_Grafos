/* Classe Main
 * Implementação da classe principal que lê o arquivo txt com os dados e orquestra as tarefas a serem realizadas
 * João Pedro de Melo Murta e Danilo Paris
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
  
  public class Main {
      public static Matriz A;
  
      public static void lerArq(String arqName) {
         BufferedReader br = null;
         try {
             br = new BufferedReader(new FileReader(arqName));
             String[] firstLine = br.readLine().trim().split("\\s+"); // expressão regular para dividir pelos espaços em branco
     
             int V = Integer.parseInt(firstLine[0]); // Converte para int
     
             A = new Matriz (V); // Cria o grafo
     
             // Lê os pares de vértices e os insere no grafo até o fim do arquivo
             String line;
             while ((line = br.readLine()) != null) {
                 String[] vertices = line.trim().split("\\s+"); // expressão regular para garantir que apenas números sejam capturados
     
                 int v = Integer.parseInt(vertices[0]);
                 int w = Integer.parseInt(vertices[1]);
     
                 A.adicionarAresta(v, w);
             }
     
         } catch (FileNotFoundException e) {
             System.err.println("Erro: Arquivo não encontrado: " + arqName);
             e.printStackTrace();
         } catch (IOException e) {
             System.err.println("Erro ao ler o arquivo: " + arqName);
             e.printStackTrace();
         } catch (NumberFormatException e) {
             System.err.println("Erro ao converter string para número. Verifique o formato do arquivo.");
             e.printStackTrace();
         } finally {
             try {
                 if (br != null) {
                     br.close();
                 }
             } catch (IOException e) {
                 System.err.println("Erro ao fechar o arquivo.");
                 e.printStackTrace();
             }
         }
     }    

    public static void escreveTempoExecucao(double duracaoSegundos, int tamanhoGrafo, String nomeMetodo) {
        try {
            // Abre o arquivo em modo de append (não sobrescreve o conteúdo existente)
            FileWriter writer = new FileWriter("performance_test.txt", true); 

            // Escreve as informações no arquivo
            writer.write(" " + nomeMetodo + " " + tamanhoGrafo + " " + String.format("%.5f segundos\n", duracaoSegundos));
            
            writer.close(); // Fecha o arquivo
        } catch (IOException e) {
            e.printStackTrace(); // Mostra erro caso ocorra problema na escrita do arquivo
        }
    }

 
    public static void main(String[] args) {
        // Array com os nomes das classes a serem testadas
        String[] nomesClasses = {"Cycle", "Tarjan"};
        
        // Array com os tamanhos dos grafos
        int[] tamanhos = {100, 1000, 10000, 100000};

        // Itera sobre cada tamanho de grafo
        for (int tam : tamanhos) {
            // Itera sobre cada nome de classe
            for (String nomeClasse : nomesClasses) {
                // Cria o nome do arquivo de grafo
                String arqName = "graph" + tam + ".txt";
                lerArq(arqName);

                // Instancia o algoritmo de acordo com o nome da classe
                Object biconexo = null;
                if (nomeClasse.equals("Cycle")) {
                    biconexo = new Cycle(A);
                } else if (nomeClasse.equals("Tarjan")) {
                    biconexo = new Tarjan(A);
                }

                if (biconexo != null) {
                    // Medindo o tempo de execução de chamadaInicial()
                    long inicio = System.nanoTime(); // Início da contagem
                    
                    // Chama o método chamadaInicial()
                    if (biconexo instanceof Cycle) {
                        ((Cycle) biconexo).chamadaInicial();
                    } else if (biconexo instanceof Tarjan) {
                        ((Tarjan) biconexo).chamadaInicial();
                    }

                    long fim = System.nanoTime(); // Fim da contagem

                    // Calcula o tempo em segundos
                    double duracaoSegundos = (fim - inicio) / 1_000_000_000.0;

                    // Chama a função para escrever o tempo no arquivo
                    escreveTempoExecucao(duracaoSegundos, tam, nomeClasse);

                    // Chama o método para escrever os componentes biconexos
                    if (biconexo instanceof Cycle) {
                        ((Cycle) biconexo).escreveComponentesBiconexos();
                    } else if (biconexo instanceof Tarjan) {
                        ((Tarjan) biconexo).escreveComponentesBiconexos();
                    }
                }
            }
        }
    }
  }
  