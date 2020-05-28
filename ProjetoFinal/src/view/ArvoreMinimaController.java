package view;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Properties;

import core.Conexao;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import model.Aresta;
import model.Fila;
import model.FilaAresta;
import model.Vertice;

public class ArvoreMinimaController {

	public static final String ARESTA_TXT = "aresta.txt";
	public static final String CONF_PROPERTIES = "conf.properties";
	public static final String VERTICE_TXT = "vertice.txt";
	public static final String GRAY = "Cinza";
	public static final String WHITE = "Branco";
	public static final int INFINITO = 999;

	@FXML
	TextField txtCusto;
	@FXML
	TextField txtSource;

	@FXML
	TableView<Aresta> tbl;

	@FXML
	TableColumn<Aresta, String> colOrigem;
	@FXML
	TableColumn<Aresta, String> colDestino;
	@FXML
	TableColumn<Aresta, Number> colValor;

	ArrayList<Vertice> verticeLista = new ArrayList<Vertice>();
	ArrayList<Aresta> arestaLista = new ArrayList<Aresta>();
	boolean valorado, orientado;
	FilaAresta filaAresta = new FilaAresta();
	Fila fila = new Fila();
	Vertice source;

	@FXML
	public void initialize() {
		inicializaTbl();
		lerArquivoProperties();
		leVertice();
		leAresta();

	}

	private void inicializaTbl() {
		colOrigem.setCellValueFactory(cellData -> cellData.getValue().origemProperty());
		colDestino.setCellValueFactory(cellData -> cellData.getValue().destinoProperty());
		colValor.setCellValueFactory(cellData -> cellData.getValue().valorProperty());

	}

	public void prim() {

		ArrayList<Aresta> conjunto = new ArrayList<Aresta>();
		source();
		criaFilaVertice();

		while (!fila.vazia()) {
			Vertice atual = fila.remove();
			alteraDistanciaPrim(atual);
			fila = organizaFilaVertice(fila);
			adicionaArestaNoConjunto(atual, conjunto);
		}
		tbl.setItems(FXCollections.observableArrayList(conjunto));
		txtCusto.setText(Integer.toString(custoDistancia()));
	}

	public void alteraDistanciaPrim(Vertice atual) {

		for (int i = 0; i < atual.getAdj().size(); i++) {
			if (fila.verificaIgual(atual.getAdj().get(i).getNome())) {
				Aresta aresta = pegaAresta(atual, atual.getAdj().get(i));
				if (aresta.getValor() < atual.getAdj().get(i).getDistancia()) {
					atual.getAdj().get(i).setDistancia(aresta.getValor());
					atual.getAdj().get(i).setPath(atual.getNome());
				}
			}
		}
	}

	public void adicionaArestaNoConjunto(Vertice atual, ArrayList<Aresta> conjunto) {

		if (!fila.vazia()) {
			Aresta aresta = pegaAresta(atual, fila.getInicio());
			if (aresta.getOrigem().trim().isEmpty()) {
				conjunto.add(anteriorMenor());
			} else {
				conjunto.add(pegaAresta(atual, fila.getInicio()));
			}
		}
	}

	public int custoDistancia() {

		int custo = 0;
		for (Vertice vertice : verticeLista) {
			custo += vertice.getDistancia();
		}
		return custo;
	}

	public Aresta anteriorMenor() {
		Aresta aresta = new Aresta();
		aresta.setValor(INFINITO);
		Vertice adjacente = fila.getInicio();

		for (int i = 0; i < adjacente.getAdj().size(); i++) {
			if (!fila.verificaIgual(adjacente.getAdj().get(i).getNome())) {
				if (pegaAresta(adjacente.getAdj().get(i), adjacente).getValor() < aresta.getValor()) {
					aresta = pegaAresta(adjacente.getAdj().get(i), adjacente);
				}
			}
		}
		return aresta;
	}

	public Aresta pegaAresta(Vertice atual, Vertice adjacente) {

		Aresta aresta = new Aresta();
		for (Aresta busca : arestaLista) {
			if (atual.getNome().equals(busca.getOrigem()) && adjacente.getNome().equals(busca.getDestino())) {
				aresta = busca;
			} else if (atual.getNome().equals(busca.getDestino()) && adjacente.getNome().equals(busca.getOrigem())) {
				aresta.setValor(busca.getValor());
				aresta.setDestino(busca.getOrigem());
				aresta.setOrigem(busca.getDestino());
			}
		}
		return aresta;
	}

	public boolean arestaIgual(Vertice vertice) {

		boolean contem = false;

		if (vertice.getCor().equals(GRAY))
			contem = true;
		return contem;
	}

	public void criaFilaVertice() {

		for (Vertice vertice : verticeLista) {
			fila.inserePrioridade(vertice);
		}
	}

	public Fila organizaFilaVertice(Fila fila) {

		Fila novaFila = new Fila();
		int tamanho = fila.tamanho();
		for (int i = 0; i < tamanho; i++) {
			novaFila.inserePrioridade(fila.remove());
		}
		return novaFila;
	}

	public Vertice pegaVerticeOrigem(Aresta aresta) {

		Vertice origem = null;

		for (Vertice vertice : verticeLista) {
			if (aresta.getOrigem().equals(vertice.getNome())) {
				origem = vertice;
			}
		}
		return origem;
	}

	public Vertice pegaVerticeDestino(Aresta aresta) {

		Vertice destino = null;

		for (Vertice vertice : verticeLista) {
			if (aresta.getDestino().equals(vertice.getNome())) {
				destino = vertice;
			}
		}
		return destino;
	}

	public void kruskal() {

		int custo = 0;

		ArrayList<Aresta> listaConjuntoAresta = new ArrayList<>();
		for (Vertice vertice : verticeLista) {
			vertice.getConjunto().add(vertice);
		}

		insereValorPrioridade();

		do {
			Aresta aresta = filaAresta.remove();
			Vertice primeiroVertice = pegaVerticeOrigem(aresta);
			Vertice segundoVertice = pegaVerticeDestino(aresta);
			boolean unir = false;
			int tamanhoConjunto = segundoVertice.conjunto().length();

			for (int i = 0; i < tamanhoConjunto; i++) {
				if (!primeiroVertice.conjunto().contains(Character.toString(segundoVertice.conjunto().charAt(i)))) {
					for (int j = 0; j < segundoVertice.conjunto().length(); j++)
						primeiroVertice.getConjunto().add(segundoVertice.getConjunto().get(j));
					segundoVertice.setConjunto(primeiroVertice.getConjunto());
					unir = true;
					for (Vertice vertice : verticeLista) {
						for (int l = 0; l < segundoVertice.conjunto().length(); l++) {
							if (vertice.conjunto().contains(Character.toString(segundoVertice.conjunto().charAt(l)))) {
								vertice.setConjunto(segundoVertice.getConjunto());
							}
						}
					}
				}
			}
			if (unir)
				listaConjuntoAresta.add(aresta);
		} while (listaConjuntoAresta.size() != verticeLista.size() - 1);

		for (Aresta aresta : listaConjuntoAresta) {
			custo += aresta.getValor();
		}
		tbl.setItems(FXCollections.observableArrayList(listaConjuntoAresta));
		txtCusto.setText(Integer.toString(custo));
	}

	public void insereValorPrioridade() {

		for (Aresta aresta : arestaLista) {
			filaAresta.inserePrioridade(aresta);
		}
	}

	private void lerArquivoProperties() {
		Properties propertie = new Properties();
		try (FileReader fr = new FileReader(CONF_PROPERTIES)) {
			propertie.load(fr);
			orientado = Boolean.valueOf(propertie.getProperty("orientado"));
			valorado = Boolean.valueOf(propertie.getProperty("valorado"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void leVertice() {
		// verticeLista.clear();
		// try (BufferedReader br = new BufferedReader(new FileReader(VERTICE_TXT))) {
		// String linha = "";
		// while ((linha = br.readLine()) != null) {
		// Vertice v = new Vertice();
		// String nome = linha.substring(0, 5).trim();
		// v.setNome(nome);
		// verticeLista.add(v);
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

		verticeLista.clear();

		String sqlSelect = "SELECT * FROM vertice";
		try (Connection conn = Conexao.getConexao()) {
			PreparedStatement preparedStatement = conn.prepareStatement(sqlSelect);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				Vertice v = new Vertice();
				v.setNome(resultSet.getString("nome"));

				verticeLista.add(v);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void leAresta() {
		// arestaLista.clear();
		// try (BufferedReader br = new BufferedReader(new FileReader(ARESTA_TXT))) {
		// String linha = "";
		// while ((linha = br.readLine()) != null) {
		// String origem = linha.substring(0, 5).trim();
		// String destino = linha.substring(5, 10).trim();
		// int valor = Integer.parseInt(linha.substring(10, 13).trim());
		// Aresta a = new Aresta();
		// a.setOrigem(origem);
		// a.setDestino(destino);
		// a.setValor(valor);
		// arestaLista.add(a);
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

		arestaLista.clear();

		String sqlSelect = "SELECT * FROM aresta";
		try (Connection conn = Conexao.getConexao()) {
			PreparedStatement preparedStatement = conn.prepareStatement(sqlSelect);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				Aresta a = new Aresta();
				a.setOrigem(resultSet.getString("origem"));
				a.setDestino(resultSet.getString("destino"));
				a.setValor(Integer.parseInt(resultSet.getString("valor")));
				adicionaAdjacente(a);
				arestaLista.add(a);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void adicionaAdjacente(Aresta aresta) {

		for (Vertice vertice : verticeLista) {
			if (vertice.getNome().equals(aresta.getOrigem())) {
				for (Vertice adjacente : verticeLista) {
					if (adjacente.getNome().equals(aresta.getDestino())) {
						vertice.getAdj().add(adjacente);
						if (!orientado)
							adjacente.getAdj().add(vertice);
					}
				}
			}
		}
	}

	@FXML
	public void source() {

		for (Vertice vertice : verticeLista) {
			if (vertice.getNome().equalsIgnoreCase(txtSource.getText())) {
				source = vertice;
			}
		}
		source.setDistancia(0);
		txtSource.setText("");
	}
}
