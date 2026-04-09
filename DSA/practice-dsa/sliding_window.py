''' Sliding Window - Common DSA '''

# Fixed Window Example
def max_sum_subarray(nums: list(int), k: int):
    window_sum = sum(nums[:k])  # Get the sum of the entire window
    max_sum = window_sum

    # Loop through the array wiht a standard fixed window
    for i in range(k, len(nums)):
        window_sum += nums[i] - nums[i - k]
        max_sum = max(max_sum, window_sum)

    return max_sum


# Variable Window Example
def longest_substring(s):
    seen = set()
    left_ptr = 0
    max_len = 0

    # Start variable sliding window
    for right_ptr in range(len(s)):
        while s[right_ptr] in seen:
            seen.remove(s[left_ptr])
            left_ptr += 1

        seen.add(s[right_ptr])
        max_len = max(max_len, right_ptr - left_ptr + 1)

    return max_len
