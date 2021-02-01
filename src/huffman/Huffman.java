package huffman;

import javax.xml.soap.Node;
import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Models a Huffman tree and provides both Huffman coding and decoding services.
 * <p>This Huffman tree is not generic; it only works with bytes. All Huffman instances are
 * full trees (meaning every node has 0 or 2 children). They have at least one internal
 * node. That is, the root node may never also be a leaf node.</p>
 * <p>When encoding, a Huffman tree writes the following data to the output stream:
 * <ul>
 * <li>the number of bytes to be encoded (as an int, so 4 bytes, big-endian)
 * <li>the Huffman tree used for the encoding. This is specified using a pre-order
 * DFS traversal of the tree, indicating each internal nodes with a 0 bit and each leaf
 * node a 1 bit followed immediately by the byte (8 bit) value in that leaf node.
 * <li>the encoding data, which consists of variable-length prefix-free codes describing
 * paths through the given Huffman tree to the the leaf node corresponding to each
 * original data byte.
 * </ul>
 * </p>
 * When decoding, the input stream must provide the data in exactly the format as output
 * by the encoding algorithm. If not, the resulting behavior is undefined.
 *
 * @author Zach Tomaszewski
 * @since 15 Nov 2012
 */
public class Huffman {

    /**
     * Extension that will be looked for & applied to
     * compressed files.
     */
    public static final String HUFF_EXT = ".huff";

    /**
     * Root to store all data out of.
     */
    private HuffmanNode<Byte> root;

    /**
     * Number of Bytes to load if loading a .huff file.
     */
    private int head = 0;

    /**
     * BitReader assigned by the file we are loading.
     */
    private BitReader input;


    /**
     * Builds a Huffman tree suitable for encoding the given byte array.
     * <p>The tree will contain 1 leaf node for each unique byte value and each leaf
     * will contain a count of that byte value's frequency in the data.</p>
     * <p>If the byte array is of size 0, no tree is constructed (ie, the root will still
     * be null.)</p>
     * The root node cannot be a leaf node. This is because the encoding is comprised of
     * left and right movements from the root, and so no bit pattern corresponds to the root
     * node itself. Therefore, if there is only a single unique byte, an extra dummy leaf
     * node with a byte value of 0 and a count of 0 will be created but not used.
     *
     * @param bytes The complete data to be encoded using this tree.
     */
    public Huffman(byte[] bytes) {
        // map each byte value to its frequency count
        Map<Byte, Integer> values = new HashMap<Byte, Integer>();
        for (byte b : bytes) {
            if (values.containsKey(b)) {
                values.put(b, values.get(b) + 1); // add one to current count
            } else {
                values.put(b, 1);
            }
        }

        // load a priority queue with TreeNodes of all unique byte values &
        // corresponding counts
        Queue<HuffmanNode<Byte>> pq = new PriorityQueue<HuffmanNode<Byte>>();
        for (Byte b : values.keySet()) {
            pq.add(new HuffmanNode<Byte>(b, values.get(b)));
        }

        while (pq.size() < 2) {
            // add an extra dummy leaf node with count of 0
            pq.add(new HuffmanNode<Byte>((byte) 0, 0));
        }

        // build tree of nodes by grabbing next two smallest counts from front of
        // of PQ, building a parent node to connect the two, and putting the new
        // parent back into the PQ. Repeat until only one node left: root of the
        // new Huffman tree
        while (pq.size() > 1) {
            pq.offer(new HuffmanNode<Byte>(pq.poll(), pq.poll()));
        }

        // store root of finished tree (maybe to null if pq is empty)
        this.root = (pq.size() > 0) ? pq.poll() : null;
    }


    /**
     * Builds a Huffman tree as read from the given input bit stream.
     * <p>The stream must be at the start of the tree data, after the byte count header.
     * Reads from the stream until the end of the tree, which leaves the given BitReader
     * at the start of the encoding data.</p>
     *
     * @param input a BitReader at the start of the pre-order traversal of the Huffman.
     * @throws IOException If cannot read from stream.
     */
    public Huffman(BitReader input) throws IOException {
        //get the head first from the
        //first four bytes
        int n = 4;
        byte[] header = new byte[n];
        for (int i = 0; i < n; i++) header[i] = input.readByte();
        this.head = new BigInteger(header).intValue();
        this.input = input;
        //build our tree structure
        buildTree(input, root = new HuffmanNode<Byte>(null, 0));
    }

    /**
     * Recursive helper function to build our tree from binary data.
     * 0 means inside node, it will be followed by a 0 or a 1
     * 1 means leaf, it is followed by a byte = its data
     * Check left, right
     * @param input bitreader to read our data.
     * @param parent node we are currently looking at.
     */
    public void buildTree(BitReader input, HuffmanNode parent) {
        /**
         * if 0, inside node
         *       check the left child, right recursively
         * if 1, then you know you are on a life. set the data
         *
         *
         */

        if (input.read()) {//if its 1
            //leaf
            parent.setData(input.readByte());
        } else { //if its 0
            //inside
            //check left
            HuffmanNode l = new HuffmanNode();
            parent.setLeft(l);
            buildTree(input, l);

            //check right
            HuffmanNode r = new HuffmanNode();
            parent.setRight(r);
            buildTree(input, r);
        }
    }


    /**
     * Reads bits from the given reader, decoding the given number of byte values before
     * stopping. Writes decoded bytes to
     * the given output stream.
     *
     * @param bytes The number of value to decode according to this tree
     * @param in The reader to read bits from
     * @param out Where to write decode byte values
     * @throws IOException If can't read/write from/to streams
     */
    public void decode(int bytes, BitReader in, OutputStream out) throws IOException {
        int c = 0;
        //traverse data byte times.
        while (c < bytes) { decode(root, in, out); c++; }
    }

    /**
     * Recursive helper function for the decode method.
     * @param node HuffmanNode we are currently looking at
     * @param in BitReader that points to traversal data.
     * @param out OutputStream to write the new data too.
     * @throws IOException
     */
    private void decode(HuffmanNode node, BitReader in, OutputStream out) throws IOException {
        if (in.read()) {
            //go right
            if (node.getRight().isLeaf()) {
                out.write((Byte) node.getRight().getData());
                return;
            } else {
                decode(node.getRight(), in, out);
            }
        } else {
            //go left
            if (node.getLeft().isLeaf()) {
               out.write((Byte) node.getLeft().getData());
               return;
            } else {
                decode(node.getLeft(), in, out);
            }
        }
    }


    /**
     * Encodes the given bytes based on this tree's structure, writing the resulting
     * bits to the given output stream.
     *
     * @param bytes the bytes to encode.
     * @param out the BitWriter.
     * @throws IOException If there is a problem writing to stream.
     */
    public void encode(byte[] bytes, BitWriter out) throws IOException {
        if (this.root == null) {
            return; // can't encode anything
        }
        // get a dictionary mapping of byte values to bit-paths to leaf node
        Map<Byte, List<Boolean>> dict = new HashMap<Byte, List<Boolean>>();
        loadPaths(dict, this.root, new ArrayDeque<Boolean>());

        // use map to write out encoded bytes
        for (byte b : bytes) {
            List<Boolean> path = dict.get(b);
            for (boolean bit : path) {
                out.write(bit);
            }
        }
    }


    /**
     * Loads the given map with paths through this tree to each unique leaf-node byte
     * value. Paths are given as false and true booleans for 0/left and 1/right,
     * respectively.
     *
     * @param paths The map to load
     * @param node The current node to consider in a path from root to leaf
     * @param path The path so far from root to the current node; should be empty
     *     in the initial call to this recursive method.
     */
    private static void loadPaths(Map<Byte, List<Boolean>> paths, HuffmanNode<Byte> node,
                                  Deque<Boolean> path) {
        if (node == null) {
            assert false : "Fell off the tree, which should never happen.";
            return;
        } else if (node.getData() == null) { // this is an internal node
            // first, go left
            path.addLast(false); // 0
            loadPaths(paths, node.getLeft(), path);
            path.removeLast();

            // now go right
            path.addLast(true); // 1
            loadPaths(paths, node.getRight(), path);
            path.removeLast();
            return;
        } else {
            // a leaf node, so save copy of path into paths map
            paths.put(node.getData(), new ArrayList<Boolean>(path));
        }
    }


    /**
     * Writes this tree in byte-encoded form to the given bit writer.
     *
     * @param out the BitWriter.
     * @throws IOException if there is a problem.
     */
    public void write(BitWriter out) throws IOException {
        write(this.root, out);
    }


    private static void write(HuffmanNode<Byte> node, BitWriter out) throws IOException {
        if (node == null) {
            return;
        } else {
            if (node.getData() == null) {
                // internal node
                out.write(0);
            } else {
                // leaf node
                out.write(1);
                out.writeByte(node.getData());
            }
            write(node.getLeft(), out);
            write(node.getRight(), out);
        }
    }


    /**
     * Returns a multiline pre-order traversal this Huffman tree.
     */
    @Override
    public String toString() {
        return (this.root == null) ? "" : this.root.toFullString("|");
    }


    // --static methods--

    /**
     * Compresses the file named by the given filename. Produces the output filename by
     * appending ".huff" to the given filename.
     *
     * @param filename Name of the file to compress.
     * @throws IOException If cannot read/write files.
     *
     * @see #compress(InputStream, OutputStream)
     */
    public static void compress(String filename) throws IOException {
        String out = filename + HUFF_EXT;
        BufferedInputStream filein = new BufferedInputStream(new FileInputStream(filename));
        BufferedOutputStream fileout = new BufferedOutputStream(new FileOutputStream(out));
        try {
            compress(filein, fileout);
        } finally {
            // close streams, even if an IOException flies by
            filein.close();
            fileout.close();
        }
    }

    /**
     * Compresses the file named by the given filename. Produces the output filename by
     * appending ".huff" to the given filename.
     *
     * @param fileName File to compress.
     * @param filenameOut Name of the file to write the out to.
     * @throws IOException If cannot read/write files.
     *
     * @see #compress(InputStream, OutputStream)
     */
    public static void compress(String fileName, String filenameOut) throws IOException {
        String out = filenameOut + HUFF_EXT;
        BufferedInputStream filein = new BufferedInputStream(new FileInputStream(fileName));
        BufferedOutputStream fileout = new BufferedOutputStream(new FileOutputStream(out));
        try {
            compress(filein, fileout);
        } finally {
            // close streams, even if an IOException flies by
            filein.close();
            fileout.close();
        }
    }


    /**
     * Compresses the given input stream, writing to the given output stream.
     * <p>
     * Writes all required parts of the output file format: the byte count header,
     * the encoded tree, and then the encoded data.
     * </p>
     *
     * @param in the InputStrem.
     * @param out the OutputStream.
     * @throws IOException If there are any read/write error.
     */
    public static void compress(InputStream in, OutputStream out) throws IOException {

        // read the file, storing it in a byte array buffer
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        while (true) {
            int b = in.read();
            if (b == -1) {
                break; // end of input stream
            } else {
                buffer.write(b);
            }
        }

        // build a tree from the bytes
        byte[] bytes = buffer.toByteArray();
        Huffman tree = new Huffman(bytes);
        // debug check:
        // System.out.println(tree);

        // write output, starting with byte count as a 4-byte int
        for (int i = 3; i >= 0; i--) {
            out.write(bytes.length >> (i * 8));
        }
        BitWriter bitStream = new BitWriter(out);
        tree.write(bitStream);
        tree.encode(bytes, bitStream);
        bitStream.flush();
    }


    /**
     * Decompresses the file named by the given filename. Produces the output filename
     * by removing ".huff" from the given filename.
     *
     * @param filename the name of the file.
     * @throws IOException If cannot read/write the files.
     */
    public static void decompress(String filename) throws IOException {
        if (!filename.endsWith(HUFF_EXT)) {
            throw new IllegalArgumentException(filename + " does not end in " + HUFF_EXT);
        }
        String out = filename.substring(0, filename.lastIndexOf(HUFF_EXT));
        BufferedInputStream filein = new BufferedInputStream(new FileInputStream(filename));
        BufferedOutputStream fileout = new BufferedOutputStream(new FileOutputStream(out));
        try {
            decompress(filein, fileout);
        } finally {
            // close streams, even if an IOException flies by
            filein.close();
            fileout.close();
        }
    }

    /**
     * Decompresses the file named by the given filename. Produces the output filename
     * by removing ".huff" from the given filename.
     *
     * @param filename name of the file
     * @param newFileName the name of the out file.
     * @throws IOException If cannot read/write the files.
     * @return the new file location.
     */
    public static String decompress(String filename, String newFileName) throws IOException {
        if (!filename.endsWith(HUFF_EXT)) {
            throw new IllegalArgumentException(filename + " does not end in " + HUFF_EXT);
        }
        String out = newFileName;
        BufferedInputStream filein = new BufferedInputStream(new FileInputStream(filename));
        BufferedOutputStream fileout = new BufferedOutputStream(new FileOutputStream(out));
        try {
            decompress(filein, fileout);
        } finally {
            // close streams, even if an IOException flies by
            filein.close();
            fileout.close();
        }
        return out;
    }


    /**
     * Decompresses the given input stream, writing to the given output stream.
     *
     * @param in the InputStream.
     * @param out the OutputStream.
     * @throws IOException If there are any read/write error.
     */
    public static void decompress(InputStream in, OutputStream out) throws IOException {
        // wrap input stream in a BitReader
        BitReader br = new BitReader(in);
        // read in byte count from BitReader
        // build a tree = new Huffman(BitReader)
        Huffman h = new Huffman(br);
        h.decode(h.head, br, out);
    }

    public static String getFileAsBinary(File file) {
        StringBuilder sb = new StringBuilder();
        long l = file.length();
        try {
            BitReader br = new BitReader(new FileInputStream(file));
            for (long i = 0; i < l; i++) {
                sb.append(br.readAsInt());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}