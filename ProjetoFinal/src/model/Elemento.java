package model;

public class Elemento {
	public Vertice vertice;
	public Elemento proximo;
	public Elemento anterior;

	public Elemento(Vertice v) {
		this.vertice = v;
		this.proximo = null;
		this.anterior = null;
	}
}
