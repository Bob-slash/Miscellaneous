
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.*;
import java.io.*;
import java.awt.Font;
import java.lang.Math;

public class WordProcessor {
	private JFrame frame;

	public WordProcessor() {
		frame = new JFrame("Bubbles Program");
		frame.setSize(800, 800);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setPreferredSize(frame.getSize());
		frame.add(new Word(frame.getSize()));
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String... argv) {
		new WordProcessor();
	}

	public static class Word extends JPanel implements Runnable, MouseListener  {


		private Thread animator;
		Dimension d;
		String str = "";
		int xPos = 0;
		int yPos = 0;
		int fontSize = 20;
		int[][] grid = {
				{0, 1, 2, 3,-1,-1, 4, 5},
				{6, 7, 8, 9,-1,10,11,12},
				{-1,-1,13,14,-1,15,16,17},
				{-1,-1,18,19,20,21,22,-1},
				{22,23,24,25,26,27,-1,-1},
				{28,-1,-1,29,30,31,32,-1}
		};
		Node[][] Nodes = new Node[6][8];
		private static LinkedList<Node> adj[];
		int start = 0;
		int dest = 4;
		int V; // No. of vertices
		LinkedList<Integer> FPath = new LinkedList<Integer>();
		int count = 0;
		int count2 = 0;
		int cur = 0;

		Color co = new Color(255,255,255);
		Color[] coArray = {
				new Color(255,255,255), new Color(0,255,255), new Color(255,255,0),new Color(255,0,255),new Color(0,0,255)	
		};




		public Word (Dimension dimension) {
			setSize(dimension);
			setPreferredSize(dimension);
			addMouseListener(this);
			addKeyListener(new TAdapter());
			setFocusable(true);
			d = getSize();
			adj = new LinkedList[48];

			for (int i=0; i<48; ++i) {
				adj[i] = new LinkedList();
			}

			for(int i = 0; i < grid.length; i++) {
				for(int j = 0; j < grid[i].length; j++) {
					if(grid[i][j] == 0) {
						Nodes[i][j] = new Node(grid[i][j], i, j, 0);
					}
					else{ 
						Nodes[i][j] = new Node(grid[i][j], i, j, 2);
					}
				}
			}
			start();

			//for animating the screen - you won't need to edit
			if (animator == null) {
				animator = new Thread(this);
				animator.start();
			}
			setDoubleBuffered(true);
		}

		@Override
		public void paintComponent(Graphics g) {
			int cubeW = 74;
			int cubeH = 74;
			Graphics2D g2 = (Graphics2D)g;
			g2.setColor(Color.black);
			g2.fillRect(0, 0,(int)d.getWidth() , (int)d.getHeight());
			
			if(count % 30 == 0) {
				if(cur == dest) {
					count = 0;
					count2 = 0;
					cur = 0;
				}
				cur = FPath.get(count2);
				count2++;
			}

			for(int i = 0; i < grid.length; i++) {
				for(int j = 0; j < grid[i].length; j++) {
					if(grid[i][j] != -1)
						g2.setColor(Color.white);
					else
						g2.setColor(new Color(150,150,150));

					if(grid[i][j] == dest) {
						g2.setColor(Color.RED);
					}
					if(grid[i][j] == cur) {
						g2.setColor(Color.GREEN);
					}
					g2.fillRect(100 + (j * 75), 100 + (i * 75), cubeW, cubeH);
					g2.setFont(new Font("TimesRoman", Font.PLAIN, fontSize));

					if(grid[i][j] == 0) {
						Nodes[i][j] = new Node(grid[i][j], j, i, 0);
					}
					else{ 
						Nodes[i][j] = new Node(grid[i][j], j, i, 2);
					}

					if(grid[i][j] != -1) {
						g2.setColor(Color.BLACK);
						g2.drawString(String.valueOf(grid[i][j]),130 + (j * 75),140 + (i * 75));
					}

					//System.out.println(Nodes[i][j].dist);


				}
			}
			count++;
			
			
		}

		public void ShortestDistance(int s, int dest, int v){
			// predecessor[i] array stores predecessor of
			// i and distance array stores distance of i
			// from s
			int pred[] = new int[v];
			double dist[] = new double[v];


			for (int i = 0; i < v; i++) {
				dist[i] = 0;
				pred[i] = -1;
			}


			aStar(s, dest,v, pred, dist);
			//BFS2(s, dest,v, pred, dist);

			// LinkedList to store path
			LinkedList<Integer> path = new LinkedList<Integer>();

			int crawl = dest;//crawl equals 12 at start
			path.add(crawl);//12 added to path
			int cur = 4;
			int len = 0;
			while(cur != 0) {
				cur = pred[cur];
				path.add(cur);
				len++;
			}
			dist[dest] = len;
			System.out.println("Shortest path length is: " + dist[dest]);

			// Print path
			System.out.println("Path is :");
			for (int i = path.size() - 1; i >= 0; i--) {
				System.out.print(path.get(i) + " ");
				FPath.add(path.get(i));
			}
		}

		// Driver method to
		public void start()
		{

			for(int i = 0; i < Nodes.length; i++) {
				for(int j = 0; j < Nodes[i].length; j++) {
					//System.out.println(i+ " : " + j);
					if(Nodes[i][j].spot != -1) {
						if(i > 0) {
							double h = getH(Nodes[i - 1][j], Nodes[0][7]);
							addEdge(grid[i][j], grid[i - 1][j], 2, h);
							Nodes[i][j].h = h;
							//System.out.println(Nodes[i][j].spot + " : " + h + "  h" + Nodes[i][j].x + " : " + Nodes[i][j].y);
						}
						if(j > 0) {
							double h = getH(Nodes[i][j - 1], Nodes[0][7]);
							addEdge(grid[i][j], grid[i][j - 1], 2, h);
							Nodes[i][j].h = h;
							//System.out.println(Nodes[i][j].spot + " : " + h + "  h" + Nodes[i][j].x + " : " + Nodes[i][j].y);
						}
						if(i < 5) {
							double h = getH(Nodes[i + 1][j], Nodes[0][7]);
							addEdge(grid[i][j], grid[i + 1][j], 2, h);
							Nodes[i][j].h = h;
							//System.out.println(Nodes[i][j].spot + " : " + h + "  h" + Nodes[i][j].x + " : " + Nodes[i][j].y);
						}
						if(j < 7) {
							double h = getH(Nodes[i][j + 1], Nodes[0][7]);
							addEdge(grid[i][j], grid[i][j + 1], 2, h);
							Nodes[i][j].h = h;
							//System.out.println(Nodes[i][j].spot + " : " + h + "  h" + Nodes[i][j].x + " : " + Nodes[i][j].y);
						}
					}
				}
			}



			//g.BFS(0, 12);

			ShortestDistance(0,4,48);
		}

		void addEdge(int v,int w, int cost, double h) {
			//System.out.println(w);
			adj[v].add(new Node(w, cost, h));
		}

		public double getH(Node current, Node goal) {
			double h = Math.sqrt((goal.x - current.x)*(goal.x - current.x) + (goal.y - current.y)*(goal.y - current.y));
			return h;
		}

		void aStar(int s, int dest, int v, int[] pred, double[] dist) {
			PriorityQueue<Node> queue = new PriorityQueue<Node>(48, new HNodeComparator());
			ArrayList<Node> seen = new ArrayList<Node>();
			boolean[] found = new boolean[48];
			for(int i = 0; i < found.length; i++) {
				found[i] = false;
			}
			for (int i = 0; i < v; i++) {
				dist[i] = 0;
				pred[i] = -1;
			}
			
			for(int i = 0; i < Nodes.length; i++) {
				for(int j = 0; j < Nodes[i].length; j++) {
					System.out.print(Nodes[i][j].h + ", ");
				}
				System.out.println("");
			}
			
			Node n = new Node(s, 0, 7);
			n.dist = 0;
			n.prev = new Node(-1,0,0);
			queue.add(n);
			int count = 0;
			while(!queue.isEmpty()) {
				System.out.println(queue);
				
				Node cur = queue.remove();
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

				for(Node adjacent : adj[cur.spot]) {
					if(!seen.contains(adjacent)) {
						if(adjacent.spot != -1 && found[adjacent.spot] == false) {
							adjacent.prev = cur;
							if(adjacent.dist == 0) {
								if(adjacent.dist > cur.h + adjacent.cost)
									adjacent.dist = cur.h + adjacent.cost;
							}
							if(adjacent.spot != dest) 
								found[adjacent.spot] = true;
							queue.add(adjacent);
						}
						
					}
				}
			}
			for(int i = 0; i < dist.length; i++) {
				System.out.print(pred[i] + ", ");
			}

		}


		public void mousePressed(MouseEvent e) {
			xPos = e.getX();
			yPos = e.getY();



		}

		public void mouseReleased(MouseEvent e) {
		}

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}

		public void mouseClicked(MouseEvent e) {
		}

		private class TAdapter extends KeyAdapter {

			public void keyReleased(KeyEvent e) {
				int keyr = e.getKeyCode();

			}

			public void keyPressed(KeyEvent e) {

				int kkey = e.getKeyChar();
				String   cc = Character.toString((char) kkey);
				str = " " + kkey;

				//key events related to strings below. You should NOT need
				// int key = e.getKeyCode();
				//String c = KeyEvent.getKeyText(e.getKeyCode());
				// String   c = Character.toString((char) key);

			}
		}//end of adapter

		public void run() {
			long beforeTime, timeDiff, sleep;
			beforeTime = System.currentTimeMillis();
			int animationDelay = 37;
			long time = System.currentTimeMillis();
			while (true) {// infinite loop
				// spriteManager.update();
				repaint();
				try {
					time += animationDelay;
					Thread.sleep(Math.max(0, time - System.currentTimeMillis()));
				} catch (InterruptedException e) {
					System.out.println(e);
				} // end catch
			} // end while loop
		}// end of run




	}//end of class
}
