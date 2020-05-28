package model;

public class ElementoAresta {
	public Aresta aresta;
	public ElementoAresta proximo;
	public ElementoAresta anterior;

	public ElementoAresta(Aresta a) {
		this.aresta = a;
		this.proximo = null;
		this.anterior = null;
	}
}
