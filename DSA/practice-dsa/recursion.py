''' Recursion - Fundementally just calls itself to break up a problem into smaller subproblems '''

# Factorial example
def factorial(n):
    if n = 0:
        return 1
    return n * factorial(n - 1)

# Tree DFS example
def inorder(root):
    # Base case - return at the end of tree
    if not root:
        return

    inorder(root.left)
    print(root.val)
    inorder(root.right)
