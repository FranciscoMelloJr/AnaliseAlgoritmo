package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Aresta {

	private StringProperty origem = new SimpleStringProperty("");
	private StringProperty destino = new SimpleStringProperty("");
	private IntegerProperty valor = new SimpleIntegerProperty(0);

	public String nValorado() {

		return this.getOrigem() + this.getDestino();
	}

	@Override
	public String toString() {
		return this.getOrigem() + " " + this.getDestino() + " " + this.getValor();
	}

	public final StringProperty origemProperty() {
		return this.origem;
	}

	public final String getOrigem() {
		return this.origemProperty().get();
	}

	public final void setOrigem(final String origem) {
		this.origemProperty().set(origem);
	}

	public final StringProperty destinoProperty() {
		return this.destino;
	}

	public final String getDestino() {
		return this.destinoProperty().get();
	}

	public final void setDestino(final String destino) {
		this.destinoProperty().set(destino);
	}

	public final IntegerProperty valorProperty() {
		return this.valor;
	}

	public final int getValor() {
		return this.valorProperty().get();
	}

	public final void setValor(final int valor) {
		this.valorProperty().set(valor);
	}

}
