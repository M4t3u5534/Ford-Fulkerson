/* 
Alunos:
Mateus Ribeiro Cerqueira - 10443901
Pedro Henrique Carvalho Pereira - 10418861

Compilação e Execução
javac *.java
java TesteGrafoMatriz
*/
public class TesteGrafoMatriz {
	public static void main(String args[]) {

		// g1: Grafo de fluxo clássico (Cormen et al.) - possui arestas antiparalelas
		// vértices s=0, a=1, b=2, c=3, d=4, e=5, t=6
		TGrafo g1 = new TGrafo(7);
		g1.insereA(0, 1, 12); // s-a
		g1.insereA(0, 2, 8);  // s-b
		g1.insereA(0, 3, 3);  // s-c
		g1.insereA(1, 4, 12); // a-d
		g1.insereA(2, 1, 7);  // b-a
		g1.insereA(2, 4, 3);  // b-d
		g1.insereA(3, 2, 1);  // c-b
		g1.insereA(3, 5, 6);  // c-e
		g1.insereA(4, 5, 4);  // d-e
		g1.insereA(4, 6, 8);  // d-t
		g1.insereA(5, 6, 3);  // e-t

		// mostra o grafo e o fluxo máximo
		System.out.println("\n\n------- g1 ----------");
		g1.show();
		System.out.println("Fluxo maximo de 0 a 6: " + g1.fordFulkerson(0, 6)); // esperado: 11
	}
}
