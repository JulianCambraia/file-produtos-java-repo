package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import model.entities.Produto;

public class FileProdutosInOutMainProgram {

	public static void main(String[] args) {
		Scanner sc = null;
		List<Produto> produtos = null;
		String[] itens;
		try {
			Locale.setDefault(Locale.US);
			sc = new Scanner(System.in);
			produtos = new ArrayList<Produto>();
			System.out.print("Informe o número de produtos:");
			Integer pNumeroProd = sc.nextInt();
			itens = new String[pNumeroProd];
			for (int i = 1; i <= pNumeroProd; i++) {
				System.out.println("Produto #" + i + ":");
				System.out.print("Nome: ");
				sc.nextLine();
				String pNome = sc.nextLine();
				System.out.print("Preço: ");
				Double pPreco = sc.nextDouble();
				sc.nextLine();
				System.out.print("Quantidade: ");
				Integer pQuantidade = sc.nextInt();

				produtos.add(new Produto(pNome, pPreco, pQuantidade));
			}
			System.out.println("Entre com o caminho da pasta onde será gravado o arquivo:");
			String strPathIn = sc.next();
			System.out.println("Entre com o nome do arquivo a ser criado (in):");
			String strFileIn = sc.next();
			String path = strPathIn + "/" + strFileIn;
			File fileIn = new File(path);

			try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileIn))) {
				for (Produto produto : produtos) {

					bw.write(produto.getNome().concat(","));
					bw.write(String.valueOf(String.format("%.2f", produto.getPreco())).concat(","));
					bw.write(String.valueOf(produto.getQuantidade()));
					bw.newLine();
				}
			} catch (IOException e) {
				System.err.println("Error: " + e.getMessage());
			}

			System.out.println("Entre com o caminho da pasta para o arquivo de saída:");
			String strPathOut = sc.next();
			String strDirOutput = strPathOut + "/SubDirOut";
			Boolean sucesso = new File(strDirOutput).mkdir();

			System.out.println("Entre com o nome do arquivo de saída (out):");
			String strFileOut = sc.next();
			String pathOut = strDirOutput + "/" + strFileOut;
			File fileOut = new File(pathOut);

			try (BufferedReader br = new BufferedReader(new FileReader(fileIn))) {
				String linha = br.readLine();
				fileOut.delete();
				while (linha != null) {
					itens = linha.split(",");
					Double subTotal = 0.00;
					subTotal += Double.valueOf(itens[1]) * Integer.valueOf(itens[2]);
					itens[1] = String.valueOf(String.format("%.2f", subTotal));
					System.out.println(linha);
					try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileOut, true))) {
						bw.write(itens[0].concat(",").concat(itens[1]));
						bw.newLine();
					} catch (IOException e) {
						System.err.println("Erro de escrita no arquivo de saída: " + e.getMessage());
					}

					linha = br.readLine();
				}
			} catch (IOException e) {
				System.err.println("Error: " + e.getMessage());
			} finally {
				if (sc != null) {
					sc.close();
					System.out.println("Arquivo: " + fileOut.getName() + " foi gravado em: " + fileOut.getParent());
				}
			}
			sc.close();
		} catch (InputMismatchException e1) {
			System.err.println("Dados de entrada inválida!");
		}
	}
}
