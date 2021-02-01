# Huff4Java
Huffman compression in Java and a cross-platform UI

![](https://i.gyazo.com/3f801a2d0dbaa9ed7e55e1f3239102c7.png)

## What is Huffman?
- Huffman is loseless compression and decompression algorithm for compressible files. It works by placing data from a file into a binary search tree, where the most used characters are placed higher on the tree (further from the root). Because most file formats are already compressed, this algorithm will only shorten "raw" files that are compressible. While all files can be compressed with the algorithm, non-compressible files show no decrease in size.
> Huff files are stored in binary with the following consecutive data: Number of bytes, Tree data, Traversal Data.

## What technologies/frameworks are involved?
- Java
- JavaFX/[OpenJFX](https://openjfx.io/) for cross-platform UI development
- Binary Search Trees, Trees and Tree Traversal 



## What did I learn
- Real-world uses of data structures and discrete math concepts. Trees and traversal algoriths that I have been tought in college level discrete mathematics is directly applicable to Huffman compression.
- Use of of reccomended development tools. Instead of jumping straight into the code or my favorite program to construct the GUI for this application, I read and tested some development tools reccomended by the OpenJFX developers. By following their best practices, including using their reccomended programs for layout generation, I built this application with minimal time spent with boilerplate code. 
