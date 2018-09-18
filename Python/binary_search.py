from difflib import SequenceMatcher
import bisect

def find_le(a, x):
    'Find rightmost value less than or equal to x'
    i = bisect.bisect_right(a, x)
    if i:
        return i-1
    return 0

def binarySearch(data, val):
    return find_le(data, val)