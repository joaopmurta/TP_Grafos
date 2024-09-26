import networkx as nx
import random

# Número de vértices
V = 100000

# Cria um grafo vazio
G = nx.Graph()

# Adiciona V vértices ao grafo
G.add_nodes_from(range(V))

# Número de componentes biconexos desejados
num_biconnected_components = 20

# Gera componentes biconexos pequenos sem adicionar novos vértices
for _ in range(num_biconnected_components):
    # Seleciona um tamanho aleatório para o componente biconexo entre 3 e 6
    size = random.randint(3, 6)
    
    # Seleciona vértices disponíveis para formar o componente biconexo
    available_nodes = [node for node in G.nodes() if G.degree[node] < 3]
    if len(available_nodes) >= size:
        # Escolhe vértices que ainda não estão em componentes biconexos
        cycle_nodes = random.sample(available_nodes, size)
        
        # Cria um ciclo fechado para formar o componente biconexo
        for i in range(size):
            G.add_edge(cycle_nodes[i], cycle_nodes[(i + 1) % size])
        
        # Conecta o componente biconexo ao grafo original usando apenas um vértice
        anchor_node = random.choice(cycle_nodes)
        other_node = random.choice([node for node in G.nodes() if node not in cycle_nodes])
        G.add_edge(anchor_node, other_node)

# Adiciona mais arestas aleatórias para aumentar a densidade do grafo
num_additional_edges = random.randint(10000, 50000)
while len(G.edges()) < num_additional_edges:
    u = random.randint(0, V - 1)
    v = random.randint(0, V - 1)
    if u != v and not G.has_edge(u, v):  # Evita auto-conexões e arestas duplicadas
        G.add_edge(u, v)

# Escreve o grafo no arquivo
with open('graph' + str(V) + '.txt', 'w') as f:
    f.write(f"{V}  {len(G.edges())}\n")  
    for edge in G.edges():
        f.write(f"       {edge[0]}      {edge[1]}\n")
