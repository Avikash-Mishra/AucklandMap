import java.awt.List;
import java.util.ArrayList;
/**A class which represents a Trie graph*/
public class Trie {

	private TrieNode root;

	public Trie() {
		root = new TrieNode();
	}

	public Trie(TrieNode root) {
		this.root = new TrieNode(root);
	}
	public TrieNode getRoot() {
		return root;
	}

	/**A method that takes in a string, and inserts that
	 * string into the Trie*/
	public void insertWord(String word) {
		//Start at root
		TrieNode curNode = root;
		//loop through each char in the string
		for (char c : word.toCharArray()) {
			//get the index of where the string should be stored in the children array
			int index = getIndex(c);
			//if -1 then the char is invalid
			if (index != -1) {
				/*
				*if the child at that index is null then assign a new TreeNode with
				*the current char and set its parents to the curNode
				*/
				if (curNode.children[index] == null) {
					curNode.children[index] = new TrieNode(c);
					curNode.children[index].parent = curNode;

				}
				/*If the child is not null, set isleaf to false and set curNode to the child*/
				if (curNode.children[index] != null) {
					curNode.isLeaf = false;
					curNode = curNode.children[index];
				}
			}
		}
		//set the curNode as a word = true
		curNode.isWord = true;
	}

	/**Method returns a subTrie given the prefix and gets the last char*/
	public Trie getTrieFromPrefix(String prefix) {
		TrieNode curNode = root;
		for (char c : prefix.toCharArray()) {
			int index = getIndex(c);
			if (curNode.children[index] == null) {
				return null;
			} else {
				curNode = curNode.children[index];
			}
		}
		return new Trie(curNode);
	}

	

	/**Inner class which represets a Node in the Trie*/
	 class TrieNode {
		TrieNode parent;
		TrieNode[] children;
		boolean isLeaf;
		boolean isWord;
		char character;
		boolean isRoot;

		public TrieNode() {
			children = new TrieNode[28];
			isLeaf = true;
			isWord = false;
			isRoot = true;
		}

		public TrieNode(char character) {
			this();
			this.character = character;
			isRoot = false;

		}

		public TrieNode(TrieNode node) {
			this.isLeaf = node.isLeaf;
			this.isWord = node.isWord;
			this.character = node.character;
			this.children = node.children;
			this.parent = node.parent;

		}

		/**Returns the path to the root as a string from this Node
		 * Uses string builder*/
		public String getPathWord() {
			StringBuilder string = new StringBuilder(100);
			char[] array = new char[100];
			TrieNode curNode = this;
			int count = 0;
			/*Loop up the trie up until loop
			 * while storing each charater in an array*/
			while (true) {
				array[count] = curNode.character;
				if (curNode.isRoot) {
					break;
				}
				curNode = curNode.parent;
				count++;
			}

			//builds the string
			for (int i = count - 1; i >= 0; i--) {
				string.append(array[i]);
			}
			return string.toString();

		}

		public ArrayList<String> getWords() {
			// Create a list to return
			ArrayList<String> list = new ArrayList<String>();
			//If this node represents a word, add it
			if (isWord) {
				list.add(getPathWord());
			}

			// If any children
			if (!isLeaf) {
				// Add any words belonging to any children
				for (int i = 0; i < children.length; i++) {
					if (children[i] != null) {
						list.addAll(children[i].getWords());

					}

				}

			}

			return list;

		}

	}

	 /**Method to quickly return the index where the word is suppose
	  * to be in the children array*/
	public int getIndex(char c) {
		switch (c) {
		case 'a':
			return 0;
		case 'b':
			return 1;
		case 'c':
			return 2;
		case 'd':
			return 3;
		case 'e':
			return 4;
		case 'f':
			return 5;
		case 'g':
			return 6;
		case 'h':
			return 7;
		case 'i':
			return 8;
		case 'j':
			return 9;
		case 'k':
			return 10;
		case 'l':
			return 11;
		case 'm':
			return 12;
		case 'n':
			return 13;
		case 'o':
			return 14;
		case 'p':
			return 15;
		case 'q':
			return 16;
		case 'r':
			return 17;
		case 's':
			return 18;
		case 't':
			return 19;
		case 'u':
			return 20;
		case 'v':
			return 21;
		case 'w':
			return 22;
		case 'x':
			return 23;
		case 'y':
			return 24;
		case 'z':
			return 25;
		case ' ': 
			return 26;
		case '-':
			return 27;
		default:
			return -1;
		}

	}
}
