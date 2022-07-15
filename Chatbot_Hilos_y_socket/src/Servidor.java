import javax.swing.*;
import java.awt.*;
import java.net.*;
import java.io.*;
import java.awt.event.*;



public class Servidor {
	//Definimos los elementos de la ventana del servidor
	JFrame chat_ventana=null;
	JTextArea area_chat =null;
	JButton send = null;
	JTextField mensaje = null;

	JPanel contenedor_areachat = null;
	JPanel contenedor_sendmensaje = null;
	
	JScrollPane scroll = null;
	
	ServerSocket servidor = null;
	Socket socket = null;
	BufferedReader Reader = null;
	PrintWriter Writer = null;

	public Servidor () {
		Interfaz();
	}

	public void Interfaz() {
		chat_ventana = new JFrame("Servidor online");
		area_chat = new JTextArea(10,12);
		send = new JButton("enviar");
		mensaje = new JTextField(4);
		
		scroll = new JScrollPane(area_chat);
		contenedor_areachat = new JPanel();
		contenedor_areachat.setLayout(new GridLayout(1,1));
		contenedor_areachat.add(scroll);
		
		contenedor_sendmensaje = new JPanel();
		contenedor_sendmensaje.setLayout(new GridLayout(1,2));
		contenedor_sendmensaje.add(mensaje);
		contenedor_sendmensaje.add(send);
		
		chat_ventana.setLayout(new BorderLayout());
		chat_ventana.add(contenedor_areachat,BorderLayout.NORTH);
		chat_ventana.add(contenedor_sendmensaje,BorderLayout.SOUTH);
		chat_ventana.setSize(300,220);
		chat_ventana.setVisible(true);
		chat_ventana.setResizable(false);
		chat_ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Thread principal = new Thread(new Runnable() {
			public void run(){
				try {
					servidor = new ServerSocket(9000);
					while(true) {
						socket = servidor.accept();
						leer();
						escribir();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		principal.start();
	}
	
	public void leer() {
		Thread leer_hilo = new Thread(new Runnable() {
			public void run(){
				try {
					Reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
						while(true) {
							String mensaje_recibido = Reader.readLine();
							area_chat.append("Cliente:" + mensaje_recibido + "\n");
						}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		});
		leer_hilo.start();
		
	}
	
	public void escribir() {
		Thread escribir_hilo = new Thread(new Runnable() {
			public void run(){
				try {
					Writer = new PrintWriter(socket.getOutputStream(),true);
					send.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e) {
							String enviar_mensaje = mensaje.getText();
							Writer.println(enviar_mensaje);
						}
					});
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		escribir_hilo.start();
	}

	public static void main(String[] args) {
		new Servidor();
	}

}
