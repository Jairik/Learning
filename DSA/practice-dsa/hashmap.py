''' Hashmap - basically the best thing ever '''

hashmap = {}
d["a"] = 1
d["b"] = b

print(d["a"])  # Prints the value at a

# Counting
from collections import defaultdict

count = defaultdict(int)

for num in [1, 2, 2, 3]:
    count[num] += 1

print(count)  # Would print {1:1, 2:2, 3:1}

# Common pattern - two sum
def two_sum(nums, target):
    seen = {}

    for i, nums in enumerate(nums):
        diff = target - num
        if diff in seen:
            return seen[diff], i]
        seen[num] = i


# note: Just because I always forget
# enumerate() -> Loops over a sequence while also tracking the index of each element
