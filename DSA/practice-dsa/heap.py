''' Heap Practice'''

# Library standard used for this
import heapq

# Min heap
heap = []

# Min heap operation - inserting values
heapq.heappush(heap, 5)
heapq.heappush(heap, 2)
heapq.heappush(heap, 8)

# Printing the smallest element (stored in index 0 for min heap)
print(heap[0])  # Smallest element (2)

# Removing the smallest element
print(heapq.heappop(heap))  # Removes smallest element (2)

# Max Heap
heap_max = []

# Max heap - inserting
heapq.heappush_max(heap_max, 5)
# ... and so on

# Printing then removing largest element
print(heap_max[0])
print(heapq.heappop_max(heap_max))
