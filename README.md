# Huff4Java
Huffman compression in Java and a cross-platform UI

## What is Huffman?
- Huffman is loseless compression and decompression algorithm for compressible files. It works by placing data from a file into a binary search tree, where the most used characters are placed higher on the tree (further from the root). Because most file formats are already compressed, this algorithm will only shorten "raw" files that are compressible. While all files can be compressed with the algorithm, non-compressible files show no decrease in size.
> Huff files are stored in binary with the following consecutive data: Number of bytes, Tree data, Traversal Data.

## What technologies/frameworks are involved?
- Java
- JavaFX/[OpenJFX](https://openjfx.io/) for cross-platform UI development
- [OpenJFXBuilder](https://openjfx.io/javadoc/11/javafx.base/javafx/util/Builder.html) Dev Tool for building JavaFX GUI's 
- Binary Search Trees, Trees and Tree Traversal 

## How do I run this?
- Download the JAR file in the root directory of this repo.
- Download the folder javafx-sdk'' in this repo (assume $SDK is a valid path to this folder)
- Run the JAR file with the following command.
> java --module-path=$SDK --add-modules=javafx.controls,javafx.base -jar Huff4Java.jar
These commands and why they are required are explained more in the 'What did I learn' section.


## What did I learn
- Deployment of OpenJFX applications require more than just a JAR file. If deploying a production application to the real world, you would most likely not want your users to have to run any sort of command to start your application. In fact, it is likely that you would like to deploy a native application to each platform, despite a single Java code base. To achieve a native application from a single JAR file you must wrap it for your specific platform: some of the tools I tried were this and this, however there are many available tools to wrap a JAR file.
- Real-world uses of data structures and discrete math concepts. Trees and traversal algoriths that I have been tought in college level discrete mathematics is directly applicable to Huffman compression.
- Use of of reccomended development tools. Instead of jumping straight into the code or my favorite program to construct the GUI for this application, I read and tested some development tools reccomended by the OpenJFX developers. By following their best practices, including using their reccomended programs for layout generation, I built this application with minimal time spent with boilerplate code. 
- How to deal with deployment issues and debugging for cross-platform deployment. Just like in my EasyDrop project that similarily used OpenJFX for a cross-platform GUI, deployment of the application needed additional steps.
> An example of an error I encountered with this project is when running the JAR fails with specific stacktraces on any machine. By reading the documentation directly from the OpenJFX developers, I found out that my error in specific was caused by missing module dependencies. JavaFX was a library bundled in with Java for many years until a recent Java update removed the library and offered it externally via modular components. These errors taught me how Java's modular components work and operate, and how they differ from traditional JAR libraries.

![](https://i.gyazo.com/3f801a2d0dbaa9ed7e55e1f3239102c7.png)
