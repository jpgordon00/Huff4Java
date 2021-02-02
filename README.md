# Huff4Java
Huffman compression in Java and a cross-platform UI

![](https://i.gyazo.com/3f801a2d0dbaa9ed7e55e1f3239102c7.png)

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
- Deployment of OpenJFX applications requires more than just a JAR file. In the context of a production application, it is not desirable to have your users have a complicated launch proccess. It is preferable to deploy a native application to each platform, despite a single Java code base. To achieve a native application from a single JAR file you must wrap it for your specific platform.
> In adressing this issue, I used several applications. These applications produce executables from JAR files and allow the use of additional scripts. As more complicated libraries and additional resources are used, it becomes increasingly linkely that the use of system-specific scripts are used. In the case of this application, additional run commands are dependencies.
- Data structures and discrete math concepts are applicable to programming and application development. Trees and traversal algoriths that I have been tought in college level discrete mathematics are used in this Java application. 
> Building a Huffman tree from a requires an implementation tree travel, to which I found several discrete math concepts relevant. Recursion and iteration are two common choices for tree traversal implementations and my discrete math background provided me context for which choice is better for this application.
- The use of developer reccomended development tools can save a lot of time. While I am usually eager to follow the proccess that I am most comfortable with, I read and tested development tools reccomended by the Java and OpenJFX developers. By following their best practices, I found my self using less boiler-plate code and doing less redundant tasks than if I was to write an application without that insight.
> Just like in my EasyDrop repo that similarily used OpenJFX, deployment of the application needed several additional steps. To reduce the frequency of these errors, I carefully followed the newest documentation from each library I used. Additionally, I individually tested each new library before referencing to it in the main code base.
