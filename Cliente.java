package server.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Cliente extends Thread {

	private static int PORTA_PADRAO = 10588;
	private Socket conexao;

	public Cliente(Socket socket) {
		this.conexao = socket;
	}

	public static void main(String[] args) {

		try {

			String mensagem;
			
			Socket socket = new Socket("127.0.0.1", PORTA_PADRAO);
			PrintStream saida = new PrintStream(socket.getOutputStream());
			BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));

			//Usuario Digita seu login. deve ser único
			System.out.print("Digite seu nome de usuario: ");
			String nomeUsuario = teclado.readLine();
			
			//Envia ao servidor o nome do usuário
			saida.println(nomeUsuario);

			Thread thread = new Cliente(socket);
			thread.start();

			// Envio de mensagem
			while (true) {
					mensagem = teclado.readLine();
					saida.println(mensagem);		
			}

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Thread Responsavel pelo recebimento das mensagens
	@Override
	public void run() {

		try {
			BufferedReader entrada = new BufferedReader(new InputStreamReader(this.conexao.getInputStream()));
			String mensagem;

			while (true) {

				// Mensagem enviada pelo servidor
				mensagem = entrada.readLine();

				// Caso mensagem seja nula, sai do programa
				if (mensagem == null) {
					System.out.println("Você saiu do chat");
					System.exit(0);
				}
				
				//Exibe mensagem na tela do cliente
				System.out.println(mensagem);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}