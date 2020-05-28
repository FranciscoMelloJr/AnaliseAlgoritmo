package view;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Properties;

import core.Conexao;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import model.Aresta;
import model.Vertice;

public class PrincipalController {

	public static final String ARESTA_TXT = "aresta.txt";
	public static final String CONF_PROPERTIES = "conf.properties";
	public static final String VERTICE_TXT = "vertice.txt";

	@FXML
	TabPane tabPane;
	@FXML
	RadioButton ckOrientado;
	@FXML
	RadioButton ckValorado;
	@FXML
	Button arvoreButton;

	@FXML
	TextField txtVertice;
	@FXML
	TextField txtOrigem;
	@FXML
	TextField txtDestino;
	@FXML
	TextField txtValor;

	ArrayList<Vertice> verticeLista = new ArrayList<>();
	ArrayList<Aresta> arestaLista = new ArrayList<>();

	public void initialize() {
		leVertice();
		leAresta();
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

				arestaLista.add(a);
			}

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

	private void alteraArquivoProperties() {
		Properties propertie = new Properties();
		try (FileReader fr = new FileReader(CONF_PROPERTIES); BufferedReader br = new BufferedReader(fr)) {
			propertie.load(fr);
			propertie.setProperty("orientado", String.valueOf(ckOrientado.isSelected()));
			propertie.setProperty("valorado", String.valueOf(ckValorado.isSelected()));
			try (BufferedWriter bw = new BufferedWriter(new FileWriter("conf.properties"))) {
				propertie.store(bw, "Atualização de Configurações");
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
				ex.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void inserirAresta() {
		// try (BufferedWriter bufferedWriter = new BufferedWriter(new
		// FileWriter(ARESTA_TXT, true))) {
		// Aresta aresta = new Aresta();
		// aresta.setOrigem(txtOrigem.getText());
		// aresta.setDestino(txtDestino.getText());
		// if (ckValorado.isSelected())
		// aresta.setValor(Integer.parseInt(txtValor.getText()));
		// arestaLista.add(aresta);
		// limpaTelaE();
		// txtOrigem.requestFocus();
		// String origem = String.format("%-5.5s", aresta.getOrigem());
		// String destino = String.format("%-5.5s", aresta.getOrigem());
		// String valor = String.format("%03d", aresta.getValor()).substring(0, 3);
		// bufferedWriter.append(origem + destino + valor + "\n");
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

		Aresta aresta = new Aresta();
		aresta.setOrigem(txtOrigem.getText());
		aresta.setDestino(txtDestino.getText());
		if (ckValorado.isSelected())
			aresta.setValor(Integer.parseInt(txtValor.getText()));

		limpaTelaE();
		txtOrigem.requestFocus();

		try (Connection conn = Conexao.getConexao()) {

			String sql = "INSERT INTO aresta(origem,destino,valor) values(?,?,?)";
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setString(1, aresta.getOrigem());
			preparedStatement.setString(2, aresta.getDestino());
			preparedStatement.setInt(3, aresta.getValor());
			preparedStatement.executeUpdate();
			arestaLista.add(aresta);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void inserirVertice() {
		// try (BufferedWriter bufferedWriter = new BufferedWriter(new
		// FileWriter(VERTICE_TXT, true))) {
		// Vertice vertice = new Vertice();
		// vertice.setNome(txtVertice.getText());
		// verticeLista.add(vertice);
		// txtVertice.setText("");
		// String nome = String.format("%-5.5s", vertice.getNome());
		// bufferedWriter.append(nome + "\n");
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

		Vertice vertice = new Vertice();
		vertice.setNome(txtVertice.getText());
		txtVertice.setText("");

		try (Connection conn = Conexao.getConexao()) {

			String sql = "INSERT INTO vertice(nome) values(?)";
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setString(1, vertice.getNome());
			preparedStatement.executeUpdate();

			verticeLista.add(vertice);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@FXML
	public void deleteValorBanco() {
		try (Connection conn = Conexao.getConexao()) {

			String sql = "DELETE FROM vertice";
			String sqlaresta = "DELETE FROM aresta";

			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			preparedStatement.executeUpdate();

			PreparedStatement prepared = conn.prepareStatement(sqlaresta);
			prepared.executeUpdate();

			verticeLista.clear();
			arestaLista.clear();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void valoradoSN() {

		if (!ckOrientado.isSelected()) {
			if (ckValorado.isSelected()) {
				txtValor.setDisable(false);
				arvoreButton.setDisable(false);
			} else {
				txtValor.setDisable(true);
				arvoreButton.setDisable(true);
			}
		}
	}

	@FXML
	public void OrientadoSN() {

		if (ckValorado.isSelected()) {
			if (ckOrientado.isSelected()) {
				arvoreButton.setDisable(true);
			} else {
				arvoreButton.setDisable(false);
			}
		}
	}

	@FXML
	public void limpaTelaE() {

		txtOrigem.setText("");
		txtValor.setText("");
		txtDestino.setText("");
	}

	@FXML
	public void focusValor() {
		if (ckValorado.isSelected())
			txtValor.requestFocus();
		else {
			inserirAresta();
		}
	}

	@FXML
	public void focusDestino() {
		txtDestino.requestFocus();
	}

	@FXML
	public void abreAdjacenciaIncidencia() {
		alteraArquivoProperties();
		abreTab("Matriz/Lista", "ListaMatriz.fxml");
	}

	@FXML
	public void abreDistancia() {
		alteraArquivoProperties();
		abreTab("Matriz", "Distancia.fxml");
	}

	@FXML
	public void abreCaminhoMinimo() {
		alteraArquivoProperties();
		abreTab("Caminho Minímo", "CaminhoMinimo.fxml");
	}

	@FXML
	public void abreArvoreMinima() {
		alteraArquivoProperties();
		abreTab("Arvore Geradora Miníma", "ArvoreMinima.fxml");
	}

	private void abreTab(String titulo, String path) {
		try {
			Tab tab = tabAberta(titulo);
			if (tab == null) {
				tab = new Tab(titulo);
				tab.setClosable(true);
				tabPane.getTabs().add(tab);
				tab.setContent((Node) FXMLLoader.load(getClass().getResource(path)));
			}
			selecionaTab(tab);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Tab tabAberta(String titulo) {
		for (Tab tab : tabPane.getTabs()) {
			if (!(tab.getText() == null) && tab.getText().equals(titulo)) {
				return tab;
			}
		}
		return null;
	}

	private void selecionaTab(Tab tab) {
		tabPane.getSelectionModel().select(tab);
	}

}
