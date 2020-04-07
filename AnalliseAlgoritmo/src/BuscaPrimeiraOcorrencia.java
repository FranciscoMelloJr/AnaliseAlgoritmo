
// @Francisco de Assis de Mello Junior - 01/04/202
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BuscaPrimeiraOcorrencia {

	static String tabela = "		 Vetor1 Vetor2 Vetor 3 \n"; // Tabela para verificar os resultados ao final.
	static String tempo = "		   Vetor1   Vetor2    Vetor 3 \n"; // Tabela para imprimir o tempo de busca.

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		int A1[] = new int[11]; // Inicializa o primeiro vetor com 11 posições
		int A2[] = new int[21]; // Inicializa o segundo vetor com 21 posições
		int A3[] = new int[42]; // Inicializa o terceiro vetor com 42 posições

		// Preenche os vetores chamando o metodo abaixo.
		A1 = preencherVetor(A1);
		A2 = preencherVetor(A2);
		A3 = preencherVetor(A3);

		System.out.println("----------TESTANDO COM O ELEMENTO NA SEGUNDA POSIÇÃO----------");
		tabela += "Posição 2       |";
		tempo += "Posição 2       |";
		// Define a posição de onde colocar o elemento X como um inteiro 50.
		A1 = alocaX(A1, 1, 50);
		A2 = alocaX(A2, 1, 50);
		A3 = alocaX(A3, 1, 50);

		// Executa a busca pelo valor X = 50 no vetor
		System.out.println("-----Vetor 1-----");
		executarBusca(A1, 50);
		System.out.println("-----Vetor 2-----");
		executarBusca(A2, 50);
		System.out.println("-----Vetor 3-----");
		executarBusca(A3, 50);
		System.out.println();
		tabela += "\n";// Quebra linha
		tempo += "\n"; // Quebra linha

		System.out.println("----------TESTANDO COM O ELEMENTO NA POSIÇÃO MEDIANA----------");
		tabela += "Posição Mediana |";
		tempo += "Posição Mediana |";
		// Define a posição de onde colocar o elemento X como um inteiro 70.
		A1 = alocaX(A1, 6, 70);
		A2 = alocaX(A2, 11, 70);
		A3 = alocaX(A3, 21, 70);

		// Executa a busca pelo valor X = 70 no vetor
		System.out.println("-----Vetor 1-----");
		executarBusca(A1, 70);
		System.out.println("-----Vetor 2-----");
		executarBusca(A2, 70);
		System.out.println("-----Vetor 3-----");
		executarBusca(A3, 70);
		System.out.println();
		tabela += "\n"; // Quebra linha
		tempo += "\n"; // Quebra linha

		System.out.println("----------TESTANDO COM O ELEMENTO COM X INEXISTENTE----------");
		tabela += "Sem posição     |";
		tempo += "Sem posição     |";
		// Executa a busca com um valor X = 99 inexistente no vetor
		System.out.println("-----Vetor 1-----");
		executarBusca(A1, 99);
		System.out.println("-----Vetor 2-----");
		executarBusca(A2, 99);
		System.out.println("-----Vetor 3-----");
		executarBusca(A3, 99);
		System.out.println();
		tabela += "\n"; // Quebra linha
		tempo += "\n"; // Quebra linha

		System.out.println("-----TABELA----- \n" + tabela); // Imprime a tabela com as comparações
		System.out.println();
		System.out.println("-----TEMPO----- \n" + tempo); // Imprime a tabela com os horários
		System.out.println();
		System.out.println("FIM DO PROGRAMA");

	}

	// Executa a busca;
	public static void executarBusca(int[] A, int x) { // Recebe por parametro o vetor e o valor de X

		int j = 0; // Contador auxiliar para impedir loop
		boolean sinal; // Número encontrado ou não
		int contador = 0; // Contador de repetições
		int local; // Indice de onde esta o X no vetor

		while ((A[j] != x) && (j <= A.length - 2)) { // Compara se a posição atual do vetor é igual a X ou não /
														// Verificar se
														// não está no limite do vetor.
			j = j + 1; // Contador anti-loop
			contador++; // Conta quantas vezes irá entrar na condição.
			thread(); // Delay
		}

		// Organizador de espaçãmento da tabela
		if (contador > 9) {
			tabela += "  " + contador + "   ";
		} else {
			tabela += "   " + contador + "   ";
		}

		// Data/Hora atual para comparação
		SimpleDateFormat horario = new SimpleDateFormat("HH:mm:ss");
		Date hora = Calendar.getInstance().getTime();
		tempo += " " + horario.format(hora) + " ";

		if (A[j] != x) { // Caso verdadeiro, não foi encontrado o número X no vetor
			sinal = false;
			System.out.println("Condição encontrou: " + sinal);
			System.out.println("Quantas vezes percorreu a busca: " + contador);
		} else { // Caso falso, o valor de X foi encontrado;
			sinal = true; // X encontrado
			local = j; // Indice de X
			System.out.println("Condição encontrou: " + sinal);
			System.out.println("Encontrou o X na posição " + (local - 1) + " do vetor");
			System.out.println("Vezes em que entrou na busca: " + contador + " vez(es)");
		}

	}

	// Define a posição a ser alocado o X.
	public static int[] alocaX(int[] A, int indice, int x) {

		A[indice] = x;
		return A;
	}

	// Metodo para preencher os vetores com inteiros.
	public static int[] preencherVetor(int[] A) {

		for (int i = 0; i <= 10; i++) {
			A[i] = i;
		}
		return A;
	}

	// Metodo sleep para gerar delay.
	public static void thread() {

		Thread t1 = new Thread() {
			public void run() {
				System.out.println("Procurando...");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// e.printStackTrace();
				}
				// System.out.println("Dormi ?");
			}
		};
		t1.start();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// e.printStackTrace();
		}
		t1.interrupt();

	}

}
