''' Binary Search - Fundemental, used when trying to find a value in a sorted array - O(logn) '''

def binary_search(nums, target):
    left, right = 0, len(nums) - 1  # Keep track of the left-most and right-most to begin

    # Begin loop to condense window
    while left <= right:
        mid = (left + right) // 2  # Integer division to get middle index

        # Determine whether current value is selected, or which way to shrink the window
        if nums[mid] == target:
            return mid
        elif nums[mid] < target:
            left = mid + 1
        else:
            right = mid - 1

    # If not found during iterations, return -1 (DNE)
    return -1
