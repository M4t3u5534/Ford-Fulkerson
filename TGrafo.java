import java.util.ArrayList;

public class TGrafo {
	// Atributos Privados
	private int n; // quantidade de vértices
	private int m; // quantidade de arestas
	private int adj[][]; // matriz de adjacência: adj[v][w] = capacidade c(e) (0 = sem aresta)
	private int fluxo[][]; // fluxo f(e) em cada aresta
	private int gf[][]; // grafo residual G^f: gf[v][w] = folga disponível de v para w

	// Métodos Públicos
	public TGrafo(int n) { // construtor
		this.n = n;
		this.m = 0;
		this.adj = new int[n][n];
		this.fluxo = new int[n][n];
		this.gf = new int[n][n];

		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++) {
				this.adj[i][j] = 0;
				this.fluxo[i][j] = 0;
				this.gf[i][j] = 0;
			}
	}

	// insere uma aresta v->w com capacidade c
	public void insereA(int v, int w, int c) {
		if (adj[v][w] == 0 && c > 0) {
			m++; // atualiza qtd arestas
		}
		adj[v][w] = c;
	}

	// remove uma aresta v->w do grafo
	public void removeA(int v, int w) {
		if (adj[v][w] != 0) {
			adj[v][w] = 0;
			fluxo[v][w] = 0;
			m--; // atualiza qtd arestas
		}
	}

	// apresenta o grafo: número de vértices, arestas e a matriz de adjacência
	public void show() {
		System.out.println("n: " + n);
		System.out.println("m: " + m);
		for (int i = 0; i < n; i++) {
			System.out.print("\n");
			for (int w = 0; w < n; w++)
				System.out.print("Adj[" + i + "," + w + "]= " + adj[i][w] + " ");
		}
		System.out.println("\n\nfim da impressao do grafo.");
	}

	// <rotina para construir G^f> a partir de c(e) e f(e)
	private void construirGf() {
		for (int v = 0; v < n; v++)
			for (int w = 0; w < n; w++)
				// folga direta (c - f) + possibilidade de cancelar fluxo contrário (f de w->v)
				gf[v][w] = (adj[v][w] - fluxo[v][w]) + fluxo[w][v];
	}

	// <rotina de busca>: DFS em G^f, vizinhos em ordem alfabética (índice crescente)
	// marca em vis[] todos os vértices atingíveis a partir de v e guarda o caminho em pai[]
	private void buscaDFS(int v, boolean vis[], int pai[]) {
		vis[v] = true;
		for (int w = 0; w < n; w++)
			if (gf[v][w] > 0 && !vis[w]) {
				pai[w] = v;
				buscaDFS(w, vis, pai);
			}
	}

	// Algoritmo de Ford-Fulkerson Resumido
	// retorna o valor do fluxo máximo f0 de s a t
	public int fordFulkerson(int s, int t) {
		// Início <dados G = (V, E, f); c(e) em E; f inicial dado; Capcorte <- infinito>
		int f0 = 0; // f inicial: fluxo zero
		int Capcorte = Integer.MAX_VALUE;

		// enquanto Capcorte != f0
		while (Capcorte != f0) {
			// construir G^f;
			construirGf();

			boolean vis[] = new boolean[n];
			int pai[] = new int[n];

			// enquanto existir caminho Pst de s a t em G^f   <rotina de busca>
			vis = new boolean[n];
			pai = new int[n];
			buscaDFS(s, vis, pai);
			while (vis[t]) {
				// determinar folga est de Pst;   <rotina para achar folga>
				int est = Integer.MAX_VALUE;
				for (int v = t; v != s; v = pai[v])
					est = Math.min(est, gf[pai[v]][v]);

				// introduzir fluxo em Pst igual a est;
				for (int v = t; v != s; v = pai[v]) {
					int u = pai[v];
					int cancela = Math.min(est, fluxo[v][u]); // primeiro cancela fluxo contrário
					fluxo[v][u] -= cancela;
					fluxo[u][v] += est - cancela;
				}

				// f0 <- f0 + est;   <rotina para somar fluxo>
				f0 = f0 + est;

				// construir G^f;
				construirGf();

				vis = new boolean[n];
				pai = new int[n];
				buscaDFS(s, vis, pai);
			}
			// fim-enquanto

			// X <- { vértices atingíveis de s em G^f }   <rotina para achar corte>
			boolean X[] = vis;

			// se t em X então fim.
			if (X[t])
				return f0;

			// senão achar Capcorte c(X, X barra) (= f0)   <o fluxo é máximo>
			Capcorte = 0;
			for (int v = 0; v < n; v++)
				if (X[v])
					for (int w = 0; w < n; w++)
						if (!X[w])
							Capcorte += adj[v][w];
		}
		// fim-enquanto

		return f0;
		// fim
	}
}