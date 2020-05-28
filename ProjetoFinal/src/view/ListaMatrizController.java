package view;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Properties;

import core.Conexao;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import model.Aresta;
import model.Vertice;

public class ListaMatrizController {

	@FXML
	TextArea txtMatrizIncidencia;
	@FXML
	TextArea txtMatrizAdjacencia;
	@FXML
	TextArea txtListaAdjacencia;
	@FXML
	TextArea txtListaAresta;

	ArrayList<Vertice> verticeLista = new ArrayList<Vertice>();
	ArrayList<Aresta> arestaLista = new ArrayList<Aresta>();
	boolean valorado, orientado;

	@FXML
	public void initialize() {
		lerArquivoProperties();
		leVertice();
		leAresta();
		calculaMatrizIncidencia();
		calculaMatrizAdjacencia();
		calculaListaAdjacencia();
		calculaListaAresta();
	}

	private void leAresta() {
		// arestaLista.clear();
		// try (BufferedReader br = new BufferedReader(new FileReader("aresta.txt"))) {
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
				System.out.println(a.toString());
				arestaLista.add(a);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void leVertice() {
		// verticeLista.clear();
		// try (BufferedReader br = new BufferedReader(new FileReader("vertice.txt"))) {
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
				System.out.println(v.toString());
				verticeLista.add(v);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void lerArquivoProperties() {
		Properties propertie = new Properties();
		try (FileReader fr = new FileReader("conf.properties")) {
			propertie.load(fr);
			orientado = Boolean.valueOf(propertie.getProperty("orientado"));
			valorado = Boolean.valueOf(propertie.getProperty("valorado"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String centerString(int width, String s) {
		return String.format("%-" + width + "s", String.format("%" + (s.length() + (width - s.length()) / 2) + "s", s));
	}

	public void calculaMatrizIncidencia() {

		String matrizIncidencia[][] = new String[verticeLista.size() + 1][arestaLista.size() + 1];

		for (int i = 0; i < verticeLista.size(); i++) {
			matrizIncidencia[i + 1][0] = verticeLista.get(i).getNome();
		}

		for (int j = 0; j < arestaLista.size(); j++) {
			matrizIncidencia[0][j + 1] = centerString(4, arestaLista.get(j).nValorado());
		}

		for (int j = 0; j < verticeLista.size(); j++) {
			for (int i = 0; i < arestaLista.size(); i++) {
				if (arestaLista.get(i).getOrigem().equals(verticeLista.get(j).getNome())) {
					if (valorado) {
						matrizIncidencia[j + 1][i + 1] = centerString(6,
								String.valueOf(" " + arestaLista.get(i).getValor()));
					} else {
						matrizIncidencia[j + 1][i + 1] = centerString(6, " 1");
					}
				}
				if (arestaLista.get(i).getDestino().equals(verticeLista.get(j).getNome())) {
					if (!orientado) {
						matrizIncidencia[j + 1][i + 1] = centerString(6, " 1");
						if (arestaLista.get(i).getDestino().equals(arestaLista.get(i).getOrigem())) {
							matrizIncidencia[j + 1][i + 1] = centerString(6, " 2");
						}
						if (valorado) {
							matrizIncidencia[j + 1][i + 1] = centerString(6,
									String.valueOf(" " + arestaLista.get(i).getValor()));
						}
					} else {
						if (valorado) {
							matrizIncidencia[j + 1][i + 1] = centerString(6,
									String.valueOf("-" + arestaLista.get(i).getValor()));
						} else {
							matrizIncidencia[j + 1][i + 1] = centerString(6, "-1");
						}
					}
				}
			}
		}

		matrizIncidencia[0][0] = "    ";
		String stringMatrizIncidencia = "";
		for (int i = 0; i < verticeLista.size() + 1; i++) {
			for (int j = 0; j < arestaLista.size() + 1; j++) {
				if (matrizIncidencia[i][j] == null) {
					if (valorado) {
						matrizIncidencia[i][j] = centerString(6, " X");
					} else {
						matrizIncidencia[i][j] = centerString(6, " 0");
					}
				}
				stringMatrizIncidencia += (matrizIncidencia[i][j]);
			}
			stringMatrizIncidencia += "\n";
		}
		txtMatrizIncidencia.setText(stringMatrizIncidencia);
	}

	public void calculaMatrizAdjacencia() {

		String matrizAdjacencia[][] = new String[verticeLista.size() + 1][verticeLista.size() + 1];
		int indiceX = 1;
		int indiceY = 1;
		for (Aresta a : arestaLista) {
			for (int x = 0; x < verticeLista.size(); x++) {
				if (a.getOrigem().equals(verticeLista.get(x).getNome())) {
					indiceX = x + 1;
				}
			}
			for (int y = 0; y < verticeLista.size(); y++) {
				if (a.getDestino().equals(verticeLista.get(y).getNome())) {
					indiceY = y + 1;
				}
			}
			if ((matrizAdjacencia[indiceX][indiceY] != null)) {
				int arestas = Integer.parseInt(matrizAdjacencia[indiceX][indiceY]);
				arestas++;
				matrizAdjacencia[indiceX][indiceY] = String.valueOf(arestas);
			} else {
				matrizAdjacencia[indiceX][indiceY] = "1";
			}
			if (valorado) {
				matrizAdjacencia[indiceX][indiceY] = String.valueOf(a.getValor());
			}
			if (!orientado) {
				if (matrizAdjacencia[indiceY][indiceX] != null) {
					int aux = Integer.parseInt(matrizAdjacencia[indiceY][indiceX]);
					if (indiceX != indiceY) {
						aux++;
					}
					matrizAdjacencia[indiceY][indiceX] = String.valueOf(aux);
				} else {
					matrizAdjacencia[indiceY][indiceX] = "1";
				}
				if (valorado) {
					matrizAdjacencia[indiceY][indiceX] = String.valueOf(a.getValor());
				}
			}
		}

		for (int k = 0; k < verticeLista.size(); k++) {
			matrizAdjacencia[0][k + 1] = verticeLista.get(k).getNome();
			matrizAdjacencia[k + 1][0] = verticeLista.get(k).getNome();
		}

		matrizAdjacencia[0][0] = " ";
		String stringMatrizAdjacencia = "";
		for (int i = 0; i < matrizAdjacencia.length; i++) {
			for (int j = 0; j < matrizAdjacencia.length; j++) {
				if (matrizAdjacencia[i][j] == null) {
					if (valorado) {
						matrizAdjacencia[i][j] = "X";
					} else {
						matrizAdjacencia[i][j] = "0";
					}
				}
				stringMatrizAdjacencia += matrizAdjacencia[i][j] + "  ";
			}
			stringMatrizAdjacencia += "\n";
		}
		txtMatrizAdjacencia.setText(stringMatrizAdjacencia);
	}

	public void calculaListaAdjacencia() {

		ArrayList<Aresta> adjacenciaLista = new ArrayList<Aresta>();

		for (int i = 0; i < verticeLista.size(); i++) {
			Aresta a = new Aresta();
			a.setOrigem(verticeLista.get(i).getNome() + "->");
			adjacenciaLista.add(a);
			for (Aresta aresta : arestaLista) {
				if (verticeLista.get(i).getNome().equals(aresta.getOrigem())) {
					a.setDestino(a.getDestino() + " " + aresta.getDestino());
				}
			}
		}

		if (!orientado) {
			for (int j = 0; j < verticeLista.size(); j++) {
				for (Aresta aresta : arestaLista) {
					if (verticeLista.get(j).getNome().equals(aresta.getDestino())) {
						if (!aresta.getOrigem().equals(aresta.getDestino())) {
							adjacenciaLista.get(j)
									.setOrigem(adjacenciaLista.get(j).getOrigem() + " " + aresta.getOrigem());
						}
					}
				}
			}
		}

		String stringListaAdjacencia = "";
		for (int k = 0; k < verticeLista.size(); k++) {
			stringListaAdjacencia += adjacenciaLista.get(k).nValorado() + "\n";
		}
		txtListaAdjacencia.setText(stringListaAdjacencia);
	}

	public void calculaListaAresta() {

		ArrayList<Aresta> lista = arestaLista;

		if (!valorado) {
			for (int i = 0; i < arestaLista.size(); i++) {
				for (Aresta aresta : lista) {
					if (arestaLista.get(i).nValorado().equals(aresta.nValorado())) {
						aresta.setValor(aresta.getValor() + 1);
					}
				}
			}
			for (int j = 0; j < lista.size(); j++) {
				for (int l = 0; l < lista.size(); l++) {
					if (lista.get(j).nValorado().equals(lista.get(l).nValorado())) {
						if (lista.get(l).getValor() > 1) {
							lista.remove(l);
						}
					}
				}
			}
		}
		String stringListaAresta = "";
		for (int k = 0; k < lista.size(); k++) {
			stringListaAresta += lista.get(k).toString() + "\n";
		}
		txtListaAresta.setText(stringListaAresta);
	}

}
