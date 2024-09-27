import networkx as nx
import random

def generate_component(size):
    G = nx.cycle_graph(size)  # Cria um ciclo simples com 'size' vértices
    return G

def connect_components(G, components):
    for i in range(len(components) - 1):
        # Seleciona um nó aleatório do componente atual (C1)
        node_from_component1 = random.choice(list(components[i].nodes()))
        # Seleciona um nó aleatório do próximo componente (C2)
        node_from_component2 = random.choice(list(components[i + 1].nodes()))
        # Adiciona uma aresta entre os dois componentes, criando uma ponte
        G.add_edge(node_from_component1, node_from_component2)
    return G

def generate_graph(V, num_components):
    G = nx.Graph()
    
    # Calcula o tamanho médio dos componentes
    avg_component_size = V // num_components
    components = []
    start_node = 0

    for _ in range(num_components):
        # Determina o tamanho do componente atual, ajustando para não exceder o número total de vértices
        size = random.randint(max(3, avg_component_size - 2), avg_component_size + 2)
        if start_node + size > V:  # Ajusta o tamanho se ultrapassar o limite de vértices
            size = V - start_node
        # Gera um componente com um ciclo simples
        component = generate_component(size)
        
        # Reposiciona os nós do componente para um range adequado
        mapping = {node: node + start_node for node in component.nodes()}
        component = nx.relabel_nodes(component, mapping)
        
        # Adiciona o componente ao grafo total
        G = nx.compose(G, component)
        components.append(component)
        
        # Atualiza o ponto de partida para o próximo componente
        start_node += size

        # Verifica se todos os nós foram utilizados
        if start_node >= V:
            break
    
    # Conecta todos os componentes com pontes
    G = connect_components(G, components)
    
    return G

# Parâmetros
V = 100000  # Número total de vértices
num_components = 1000  # Número de componentes biconexos

# Gera o grafo
G = generate_graph(V, num_components)

# Escreve o grafo em um arquivo com cabeçalho |V| |E|
with open('graph' + str(V) + '.txt', 'w') as f:
    f.write(f"{V}  {G.number_of_edges()}\n")
    
    # Escreve a lista de arestas com espaçamento adequado
    for edge in G.edges():
        f.write(f"       {edge[0]}      {edge[1]}\n")
