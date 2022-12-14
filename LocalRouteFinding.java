
import java.io.*;
import java.util.*;

class Graph
{
	private static int V; // No. of vertices
	private static LinkedList<Integer> adj[]; //Adjacency Lists
	private static LinkedList<Node> adj2[];
	int[] hValues = {366, 374, 380, 329, 253, 244, 241, 242, 193, 176, 100, 160, 0, 80, 90};

	// Constructor
	Graph(int v)
	{
		V = v;
		adj = new LinkedList[v];
		adj2 = new LinkedList[v];

		for (int i=0; i<v; ++i) {
			adj[i] = new LinkedList();
			adj2[i] = new LinkedList();
		}
	}

	// Function to add an edge into the graph

	void addEdge(int v,int w, int cost) {
		adj2[v].add(new Node(w, cost));
	}

	// prints BFS traversal from a given source s
	void BFS(int s, int d)
	{
		Queue<Integer> queue = new LinkedList<Integer>();
		ArrayList<Integer> seen = new ArrayList<Integer>();

		queue.add(s);

		while(!queue.isEmpty()) {
			int cur = queue.remove();
			if(!seen.contains(cur)) {
				seen.add(cur);
				System.out.print(cur + ", ");
				if(cur == d) {
					return;
				}
			}

			for(int adjacent : adj[cur]) {
				if(!seen.contains(adjacent)) {
					queue.add(adjacent);
				}
			}

		}
	}

	static void BFS2(int s, int d, int v, int[] pred, int[] dist) {
		Queue<Integer> queue = new LinkedList<Integer>();
		ArrayList<Integer> seen = new ArrayList<Integer>();
		boolean[] found = new boolean[19];
		for(int i = 0; i < found.length; i++) {
			found[i] = false;
		}

		queue.add(s);
		int count = 0;
		while(!queue.isEmpty()) {

			int cur = queue.remove();
			dist[cur] = count;
			if(!seen.contains(cur)) {
				seen.add(cur);
			}

			for(Node adjacent : adj2[cur]) {
				if(!seen.contains(adjacent.spot)) {
					queue.add(adjacent.spot);
					if(found[adjacent.spot] == false) {
						pred[adjacent.spot] = cur;
						found[adjacent.spot] = true;
					}
				}
			}
		}

		for(int i = 0; i < dist.length; i++) {
			//System.out.print(i);
			int cur = i;
			int len = 0;
			while(cur > 0) {
				cur = pred[cur];
				len++;

			}
			dist[i] = len;
		}

		for(int i = 0; i < dist.length; i++) {
			System.out.print(dist[i] + ", ");
		}
	}

	void DFS(int first, int last) {
		Stack<Integer> stack = new Stack<Integer>();
		ArrayList<Integer> seen = new ArrayList<Integer>();

		stack.push(first);

		while(!stack.isEmpty()) {
			int cur = stack.pop();
			if(!seen.contains(cur)) {
				seen.add(cur);
				System.out.print(cur + " ");
				if(cur == last) {
					return;
				}
			}

			for(int adjacent : adj[cur]) {
				if(!seen.contains(adjacent)) {
					stack.push(adjacent);				}
			}
		}
	}

	static void UCS(int s, int dest, int v, int[] pred, int[] dist) {
		PriorityQueue<Node> queue = new PriorityQueue<Node>(V, new NodeComparator());
		ArrayList<Node> seen = new ArrayList<Node>();
		boolean[] found = new boolean[19];
		for(int i = 0; i < found.length; i++) {
			found[i] = false;
		}
		for (int i = 0; i < v; i++) {
			dist[i] = 0;
			pred[i] = -1;
		}
		
		Node n = new Node(s, 0);
		n.dist = 0;
		n.prev = new Node(-1,0);
		queue.add(n);
		int count = 0;
		while(!queue.isEmpty()) {

			Node cur = queue.remove();
			System.out.println(cur.spot);
			//dist[cur.spot] = count;
			if(!seen.contains(cur)) {
				seen.add(cur);
				found[cur.spot] = true;
				if(dist[cur.spot] == 0) {
					dist[cur.spot] = cur.dist;
					pred[cur.spot] = cur.prev.spot;
					
				}
					

				//System.out.println(cur.dist + ",");
			}

			//System.out.print(cur.spot + ", ");

			for(Node adjacent : adj2[cur.spot]) {
				if(!seen.contains(adjacent)) {
					if(found[adjacent.spot] == false) {
						adjacent.prev = cur;
						adjacent.dist = cur.dist + adjacent.cost;
						if(adjacent.spot != 10) 
							found[adjacent.spot] = true;
						
						queue.add(adjacent);
					}
					
				}
			}
		}
		for(int i = 0; i < dist.length; i++) {
			System.out.print(dist[i] + ", ");
		}

	}



	public static void ShortestDistance(int s, int dest, int v){
		// predecessor[i] array stores predecessor of
		// i and distance array stores distance of i
		// from s
		int pred[] = new int[v];
		int dist[] = new int[v];


		for (int i = 0; i < v; i++) {
			dist[i] = 0;
			pred[i] = -1;
		}


		//UCS(s, dest,v, pred, dist);
		BFS2(s, dest,v, pred, dist);

		// LinkedList to store path
		LinkedList<Integer> path = new LinkedList<Integer>();

		int crawl = dest;//crawl equals 12 at start
		path.add(crawl);//12 added to path
		int cur = 10;
		int len = 0;
		while(cur != 7) {
			cur = pred[cur];
			path.add(cur);
			len++;
		}
		dist[dest] = len;
		System.out.println("Shortest path length is: " + dist[dest]);

		// Print path
		System.out.println("Path is ::");
		for (int i = path.size() - 1; i >= 0; i--) {
			System.out.print(path.get(i) + " ");
		}
	}

	// Driver method to
	public static void main(String args[])
	{
		Graph g = new Graph(19);

		g.addEdge(0, 2, 8);
		g.addEdge(0, 1, 18);
		g.addEdge(0, 3, 15);
		g.addEdge(0, 4, 14);

		g.addEdge(1, 0, 18);
		g.addEdge(1, 4, 13);
		g.addEdge(1, 6, 8);
		g.addEdge(1, 8, 7);
		g.addEdge(1, 7, 10);

		g.addEdge(2, 0, 8);
		g.addEdge(2, 5, 8);
		g.addEdge(2, 6, 6);

		g.addEdge(3, 0, 15);
		g.addEdge(3, 5, 5);
		g.addEdge(3, 10, 12);

		g.addEdge(4, 0, 14);
		g.addEdge(4, 1, 13);
		g.addEdge(4, 7, 10);

		g.addEdge(5, 2, 8);
		g.addEdge(5, 3, 5);
		g.addEdge(5, 6, 6);
		g.addEdge(5, 11, 8);
		g.addEdge(5, 13, 11);

		g.addEdge(6, 1, 8);
		g.addEdge(6, 2, 6);
		g.addEdge(6, 5, 6);
		g.addEdge(6, 8, 5);
		g.addEdge(6, 11, 8);

		g.addEdge(7, 1, 10);
		g.addEdge(7, 4, 10);

		g.addEdge(8, 1, 7);
		g.addEdge(8, 6, 5);
		g.addEdge(8, 9, 9);
		g.addEdge(8, 12, 11);

		g.addEdge(9, 8, 9);
		g.addEdge(9, 11, 8);
		g.addEdge(9, 12, 5);
		g.addEdge(9, 15, 11);
		g.addEdge(9, 18, 12);

		g.addEdge(10, 3, 12);
		g.addEdge(10, 13, 9);

		g.addEdge(11, 5, 8);
		g.addEdge(11, 6, 8);
		g.addEdge(11, 9, 8);
		g.addEdge(11, 14, 7);

		g.addEdge(12, 8, 11);
		g.addEdge(12, 9, 5);
		g.addEdge(12, 18, 12);

		g.addEdge(13, 5, 11);
		g.addEdge(13, 10, 9);
		g.addEdge(13, 14, 5);
		g.addEdge(13, 16, 7);

		g.addEdge(14, 11, 7);
		g.addEdge(14, 13, 5);
		g.addEdge(14, 16, 5);
		g.addEdge(14, 15, 9);

		g.addEdge(15, 9, 11);
		g.addEdge(15, 14, 9);
		g.addEdge(15, 17, 9);

		g.addEdge(16, 13, 7);
		g.addEdge(16, 14, 5);

		g.addEdge(17, 15, 9);
		g.addEdge(17, 18, 8);

		g.addEdge(18, 12, 12);
		g.addEdge(18, 9, 12);
		g.addEdge(18, 17, 8);


		System.out.println("Following is Breadth First Traversal "+
				"(starting from vertex 0)");

		//g.BFS(0, 12);
		ShortestDistance(7,10,19);
	}
}
