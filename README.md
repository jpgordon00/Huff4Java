# Huff4Java
Huffman compression in Java and a cross-platform UI



## What is Huffman?
- Huffman is a loseless compression and decompression algorithm for compressible files. It works by placing data from a file into a binary search tree, where the most used characters are placed higher on the tree (further from the root). Because most file formats are already compressed, this algorithm will only shorten "raw" files that are compressible. While all files can be compressed with the algorithm, non-compressible files show no decrease in size.
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



## What I learned.
- Deployment of OpenJFX applications requires more than just a JAR file. In the context of a production application, it is not desirable to have your application have a complicated launch proccess. Instead, it is preferable to deploy a native application to each platform, despite a single Java code base. To achieve a native application from a java application, it must be wrapped for a specific platform.
> In adressing this issue, I used several applications that produced executables from JAR files and allowed the use of additional scripts. As more complicated libraries and additional resources were used, it became increasingly likely that system-specific scripts were needed. In the case of this application, additional run commands became neccesary.
- Data structures and discrete math concepts are applicable to programming and application development. Trees and tree traversal algorithms, concepts I learned in college, were applicable here.
> Building a Huffman tree requires an implementation of a tree travel algorithm, to which I found several discrete math concepts relevant. Recursion and iteration are two common choices, and my discrete math background provided me context for which choice is better for this application.
- The use of developer reccomended development tools can save a lot of time. While I am usually eager to follow the proccess that I am most comfortable with, I tested various tools reccomended by the Java and OpenJFX developers. By following their best practices, I found my self using less boiler-plate code and doing less redundant tasks than if I was to write an application without that insight.
> Just like in my EasyDrop repo that similarily used OpenJFX, deployment of the application needed several additional steps. To reduce the frequency of errors, I carefully followed the newest documentation from each dependent library. Additionally, I individually tested each new library before referring to it in the main code base.



![](https://i.gyazo.com/3f801a2d0dbaa9ed7e55e1f3239102c7.png)
