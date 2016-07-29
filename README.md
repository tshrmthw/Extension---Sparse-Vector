# Extension - SparseVector

This Lab was developed by Tushar Mathew and Erik Wijmans for the future iterations of the CSE 247 - Data structures and Algorithms Course

# Abstract: 

In class we have talked extensively about the speed (asymptotic complexity) of various different algorithms and data structures, however we have consistently left out an important factor: memory usage.
In this assignment, you'll be implementing a sparse vector and then examining the real world performance and memory usage of your sparse vector vs. a dense vector (aka a normal array).

Goals of this lab:

Understand how binary search works. Think about why it doesn't work on a linked list.
Understand the trade-off that has to be made between memory usage and speed.
Introduction: The Sparse Vector

To represent a normal vector on a computer, an array is traditionally used, however, this storage scheme results in a lot of redundant information if the majority of the elements in the vector are zero. This type of vector is a sparse vector is used. In a sparse vector, only nonzero values are stored, thus every nonzero element is stored with both its row number and value in a tuple, i.e. (row number, value)
0     (1, 2)
2     (5, 3)
0
0
0  vs
3
0
0
0
Sparse Vectors can be implemented in a multitude of ways (array of tuples, binary search tree of tuples, hash map). However, the most common implementation is an array of tuples. This is because being able to iterate over every nonzero in row order is the most common use of a sparse vector and an array of tuples lends itself nicely to this. For this reason, and another that will become very clear soon, the array of tuples will be ordered by row number.

# Contributors:

Tushar Mathew and Erik Wijmans 

# How-To-Run

The following are the variables and methods which need to be part of the sparse vector class and a brief description of its implementation:

'Insert Method'
This method will insert a 'row'-'value' tuple into the array. Since the array is always in sorted order, a binary search is first performed on the tuple array over the 'row' values. Once the appropriate location for insert is found, the elements of the array from that location till the last element in the array need to be shifted by one location to make space for the new insert. The new element(row-value tuple) is then inserted. Increment the current non-zero elements variable. The insert should take Θ(n) time for the sparse vector class to be efficient.

'Get Method'
This method receives an integer corresponding to the 'row' it needs to look up and returns the 'value' item. First a binary search is performed over the the 'row' values. Once the 'row' has been found, the method looks up the 'value' item in that tuple and returns this. Get should take Θ(lg(n)) time.

'IsEmpty'
Returns boolean 'True' if there are no non-Zero elements in the sparse vector and returns boolean 'false' otherwise.

'Size variable'
This is an int variable that stores the virtual size of the one-dimensional array it is representing. It is initialized when a new sparse vector object is created and never changed after.

'Non-zero Elements variable'
This is an int variable that stores the maximum number of non-zero elements that the sparse vector object is designed to handle. It is initialized when a new sparse vector object is created and never changed after.

'Current Non-zero Elements variable'
This is an int variable that stores the number of non-zero elements that currently exist in the sparse vector object. It is initialized when a new sparse vector object is incremented every time a an element is added to the sparse vector.

'Tuple array'
This is an array of that will store tuples of 'row'-'value' pairs that represent the sparse vector. Its size is specified by the Non-Zero Elements variable. For this project, this array will be a VMSimulatedArray.


# Binary Search Explained:

Binary search is an algorithm that can be only be done on an ordered array and works first checking the middle element of the array, and then, if the middle element is the target element, the upper or lower half of the array is eliminated the search is done again on the remaining half.
Algorithm:

### helper(array, target, min, max)
###   if (min >= max)
###     return NOT_FOUND
### 
###   mid = (max + min)/2
###   if (array[mid] == target)
###     return mid
###   else if (target < array[mid])
###     max = mid - 1
###     return helper(array, target, min, max)
###   else
###     min = mid + 1
###     return helper(array, target, min, max)



### binarySearch(array, target)
###   min = 0
###   max = array.length - 1
###   return helper(array, target, min, max)



By using an array ordered by row number we can implement a sparse vector. Binary search allows us to do Θ(lg(number of non zeros)) look up. For insertion however, we cannot do better than Θ(number of non zeros) as in the worse case scenario, we will have to sift every element currently in the underlying array down one position.
Virtual Memory

To understand the timing results of this lab, let's briefly talk about how your computer handles too much memory being used. When the sum of all memory needed by all programs currently running is greater than the amount of physical memory, your computer will begin to write the least recently used parts of physical memory onto disk, so that space can be used by a different program. This is called "swapping" and relies on that memory not being needed again for a relatively significant period of time. Swapping takes a "very long time", about 8 milliseconds, but this is millions of CPU clock cycles.
If a program is using enough memory that the assumption of the memory written to disk not being needed again for a while becomes untrue, this system begins to "thrash" and everything will grind to a halt. In the worst case, this will crash the computer.

For this lab, we have written a piece of software that simulates this process and when too much memory is being used, simulates swapping. While we have had to severely limit the size of physical memory for the purpose of this lab, these ideas and principles of a sparse vector vs. a dense vector easily scale into n-dimensions (and 3 is generally high enough to causes problems) where even the marvel that is modern technology simply doesn't have enough memory.

When running the Sparse Vector Timer, pay special attention to n=525000, this is the tipping point where a dense vector is too large to fit into the memory we have given you, and you will notice a rather large jump in the time it takes to complete. This jump is so large, that we have had to reduce the amount of time it takes to "swap" a page from 8 to 1 milliseconds!

# VMSimulatedArray

This is how your sparse vector will interface with our virtual memory simulator. It has two very simple methods for you to use that we will outline here
at(int index)
Returns whatever is currently at index in the array
put(int index, E thing)
Puts thing at index in the array. If something is already there, it will be replaced.

# Your Assignment

Implement the insert, get and IsEmpty methods of the given SparseVector class. Be sure to properly use the VMSimulatedArray as this will ensure fair and accurate timing.
Pass all tests given in TestSparseVector.
Run SpareVectorTimmer and graph the output of the Dense Vector vs. your Sparse Vector.
