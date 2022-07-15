import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class Cliente {
	//Definimos los elementos de la ventana del cliente
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
	
	public Cliente() {
		Interfaz();
	}
	
	public void Interfaz() {
		chat_ventana = new JFrame("Cliente online");
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
		
		Thread principal = new Thread(new Runnable(){
			public void run() {
				try {
					socket = new Socket("localhost",9000);
					leer();
					escribir();
				} catch (IOException e) {
					// TODO Auto-generated catch block
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
							area_chat.append("Servidor:" + mensaje_recibido + "\n");
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
		new Cliente();
	}

}
