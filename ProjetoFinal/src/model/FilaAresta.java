package model;

public class FilaAresta {

	private ElementoAresta inicio, atual, temp;
	int tamanho;

	public FilaAresta() {
		inicio = atual = temp = null;
	}

	public boolean vazia() {
		return inicio == null;
	}

	public void inserePrioridade(Aresta a) {

		ElementoAresta novo = new ElementoAresta(a);

		if (vazia()) {
			inicio = novo;
			temp = novo;
			atual = novo;
			tamanho++;
		} else {
			atual = inicio;
			for (int i = 1; i <= this.tamanho; i++) {
				if (novo.aresta.getValor() > atual.aresta.getValor()) {
					if (atual.proximo != null) {
						atual = atual.proximo;
					} else {
						atual.proximo = novo;
						novo.anterior = atual;
						break;
					}
				} else {
					if ((inicio.aresta.getOrigem().concat(inicio.aresta.getDestino())).equals((atual.aresta.getOrigem().concat(atual.aresta.getDestino())))) {
						inicio.anterior = novo;
						novo.proximo = inicio;
						inicio = novo;
					} else {
						temp = atual.anterior;
						atual.anterior = novo;
						novo.anterior = temp;
						novo.anterior.proximo = novo;
						novo.proximo = atual;
					}
					break;
				}
			}
			tamanho++;
		}
	}

	public Aresta remove() {

		Aresta a = inicio.aresta;
		inicio = inicio.proximo;
		tamanho--;

		return a;
	}

	public int tamanho() {
		return tamanho;
	}
}
