''' DFS (Depth-First Search) & BFS (Breadth-First Search):
Use:
    DFS -> paths, recursion, backtracking
    BFS -> shortest path (for unweighted graphs)
'''

# Recursive DFS Algorithm
def dfs(graph, node, visited):
    
    # Base case - return when node is found
    if node in visited:
        return
    
    # Add the current node to the visited set
    visited.add(node)
    print(node)

    # Run dfs recursively on each neighbor
    for neighbor in graph[node]:
        dfs(graph, neighbor, visisted)

# BFS Iterative Algorithm
from collections import deque
def bfs(graph, start):
    # Initialize
    visited = set()  # Keep track of nodes we've already seen
    queue = deque([start])  # Initialize queue with starting node

    # Begin loop until there are no remaining nodes
    while queue:
        node = queue.popleft()  # Remoe the front node (FIFO)

        # Skip if we have already processed the node
        if node in visited: continue

        # Mark the current node as visited and process it
        visited.add(node)
        print(node)  # Or whatever other processing

        # Explore all neighbors of the current node
        for neighbor in graph[node]:
            queue.append(neighbor)  # Add neighbors to the queue for future iterations

