package compilador;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;


public class Interface extends JFrame implements ActionListener
{
	private File      file;
	private Panel     panel;
	private JMenuBar  barrademenu;
	private JMenu     archivo;
	private JMenuItem nuevo, abrir, guardar, guardarcomo, salir;
	
	public Interface()
	{
		super("Compilador: <sin titulo>");
		
		panel       = new Panel();
		barrademenu = new JMenuBar();
		archivo     = new JMenu("Archivo");
		nuevo       = new JMenuItem("Nuevo");
		abrir       = new JMenuItem("Abrir");
		guardar     = new JMenuItem("Guardar");
		guardarcomo = new JMenuItem("Guardar como...");
		salir       = new JMenuItem("Salir");
		
		
		asignarOyentes();
		agregarComponentes();
	}
	
	private void agregarComponentes() 
	{
		barrademenu.add(archivo);
		    archivo.add(nuevo);
		    archivo.add(abrir);
		    archivo.add(guardar);
		    archivo.add(guardarcomo);
		    archivo.add(salir);
		    
		    archivo.setMnemonic('A');
		      nuevo.setMnemonic('N');
		      abrir.setMnemonic('A');
		    guardar.setMnemonic('G');
		guardarcomo.setMnemonic('O');
		      salir.setMnemonic('S');
		    		
		add(barrademenu, BorderLayout.NORTH);
		add(panel);
	}
	
	private void asignarOyentes() 
	{		
		      nuevo.addActionListener(this);
		      abrir.addActionListener(this);
	        guardar.addActionListener(this);
	    guardarcomo.addActionListener(this);
		      salir.addActionListener(this); 
		      
		Oyente oyente = new Oyente();
		addWindowListener(oyente);	    
	}

	public void setVisible()
	{
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setSize(800, 600);
		setVisible(true);
		setLocationRelativeTo(null);		
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getSource() == nuevo)
		{
			Interface ventana = new Interface();
			ventana.setVisible();
		}
		else if (e.getSource() == abrir)
		{
			JFileChooser abrirArchivo =  new JFileChooser();
			FileNameExtensionFilter filtro = new FileNameExtensionFilter("Documentos de texto (*.txt)","txt");
			abrirArchivo.setFileSelectionMode(JFileChooser.FILES_ONLY);
			abrirArchivo.setFileFilter(filtro);
			abrirArchivo.setAcceptAllFileFilterUsed(false);
			int opcion = abrirArchivo.showOpenDialog(this);
			
			if (opcion == JFileChooser.APPROVE_OPTION)
			{
				File fcopia = file;
				file = abrirArchivo.getSelectedFile();
				FileReader fr = null;
		        BufferedReader br = null;
		        String texto = "";
		       
		    	if(!file.getAbsolutePath().endsWith(".txt"))
				{
					File tmp = new File (file.getAbsolutePath()+".txt");
					file = tmp;
				}
				
		        if(file.exists())
		        {
		        	try
		        	{
		        		fr = new FileReader(file);
				        br = new BufferedReader(fr);
				 
				        String linea;
			         
    		            while((linea = br.readLine()) != null)
    		            	texto += linea + "\n";
    		            
    		            br.close();
				        setTitle("Compilador: <"+file.getName()+">");
				        panel.setPrograma(texto);
				    } 
				    catch(Exception ex)
				    {  	
				    	JOptionPane.showMessageDialog(this, "No se pudo leer el archivo. Int�ntelo de nuevo.", "Mensaje", JOptionPane.ERROR_MESSAGE);
				    }
		        }
		        else
		        {
		        	JOptionPane.showMessageDialog(this, "Archivo no encontrado. Int�ntelo de nuevo.", "Mensaje", JOptionPane.ERROR_MESSAGE);
		        	file = fcopia;
		        }	
			}		
		}
		else if (e.getSource() == guardar)
		{					
			if(file == null)
				guardarcomo();
			else
			{
				try
				{
					FileWriter fw = new FileWriter(file);
					BufferedWriter bw = new BufferedWriter(fw);
					PrintWriter pw = new PrintWriter(bw); 
						
					pw.write(panel.getPrograma());
						
					pw.close();
					bw.close();
					
					JOptionPane.showMessageDialog(this, "Archivo guardado.", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
				} 
				catch (Exception ex) 
				{
					JOptionPane.showMessageDialog(this, "Error al guardar el archivo. Int�ntelo de nuevo.", "Mensaje", JOptionPane.ERROR_MESSAGE);
				}
			}
		}			
		else if (e.getSource() == salir) 
		{
			int respuesta = JOptionPane.showConfirmDialog(null, "�Deseas salir?", "Confirmar", JOptionPane.YES_NO_OPTION);

			if (respuesta == JOptionPane.YES_OPTION)
				dispose();
		}
		else //Guardar como...
		{
			guardarcomo();
		}	
	}

	private void guardarcomo() 
	{
		try
		{
			File fcopia = file;
			JFileChooser guardarArchivo = new JFileChooser();
			int opcion = guardarArchivo.showSaveDialog(this);
			file = guardarArchivo.getSelectedFile();
			FileWriter fw = null;
			
			if(opcion == JFileChooser.APPROVE_OPTION)
			{
				fw = new FileWriter(file); 
				fw.write(panel.getPrograma());
		        fw.close();
		        
		        if(!file.getAbsolutePath().endsWith(".txt"))
				{
					File tmp = new File (file.getAbsolutePath()+".txt");
					file.renameTo(tmp);
					file = tmp;
				}
					
		        JOptionPane.showMessageDialog(this, "Archivo guardado.", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
		        setTitle("Compilador: <"+file.getName()+">");
		   }
		   else
			   file = fcopia;
		}
		catch(Exception ex)
		{
			JOptionPane.showMessageDialog(this, "Error al guardar el archivo. Int�ntelo de nuevo.", "Mensaje", JOptionPane.ERROR_MESSAGE);
		} 
	}
}

class Oyente extends WindowAdapter
{
	public void windowClosing (WindowEvent e)
	{
		int respuesta = JOptionPane.showConfirmDialog(null, "�Deseas salir?", "Confirmar", JOptionPane.YES_NO_OPTION);

		if (respuesta == JOptionPane.YES_OPTION)
		{
			JFrame ventana = (JFrame) e.getSource();
			ventana.dispose();
		}	
	}
}
