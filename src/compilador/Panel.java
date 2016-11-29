package compilador;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Panel extends JPanel implements ActionListener 
{
	private JTextArea  programa, resultado;
	private JLabel     etiq1, etiq2, etiq3;
	private JButton    scanner;
	private JTextField total;
	Parser p;
	public Panel()
	{
		 programa   = new JTextArea();
		 resultado  = new JTextArea();
		 etiq1      = new JLabel("Programa");
	     etiq2      = new JLabel("Resultado");
		 etiq3      = new JLabel("Total = ");
		 scanner    = new JButton("Escanear");
		 total      = new JTextField();
		
		 scanner.setMnemonic('S');
		 scanner.addActionListener(this);
		 
		 agregarComponentes();
	}

	private void agregarComponentes() 
	{
		JPanel superior   = new JPanel();
		JPanel central    = new JPanel();
		JPanel inferior   = new JPanel();	
		JPanel auxiliar   = new JPanel();
		JPanel auxiliar2  = new JPanel();
		JPanel auxiliar3  = new JPanel();
		JPanel auxiliar4  = new JPanel();
		 
		setLayout(new BorderLayout());
	    
	    /*-------------------------------------------------*
		 *                PANEL SUPERIOR                   *
	     *-------------------------------------------------*/
		add(superior, BorderLayout.NORTH);	 
	    superior.setPreferredSize(new Dimension(50, 50));
	    superior.setLayout(new GridLayout(1, 3)); //Dividir el panel superior en 3 columnas.
	    
	    //Primer columna.
	    superior.add(etiq1);
	    
	    //Segunda columna.
	    superior.add(new JPanel()); //Relleno.
	    
	    //Tercer columna.
		superior.add(etiq2);
		
		etiq1.setHorizontalAlignment(JLabel.CENTER);
		etiq2.setHorizontalAlignment(JLabel.CENTER);
	    
		/*-------------------------------------------------*
		 *                  PANEL CENTRAL                  *
	     *-------------------------------------------------*/
	    add(central);
	    central.setLayout(new GridLayout(1, 3, 15, 15));  //Dividir el panel central en 3 columnas.
	    
	    //Primer columna.
	    central.add(auxiliar);
	    auxiliar.setLayout(new BorderLayout());
	    auxiliar.add(new JPanel(), BorderLayout.WEST); //Relleno.	    
	    auxiliar.add(new JScrollPane(programa));
	    
	    //Segunda columna.
	    central.add(auxiliar2);
	    auxiliar2.setLayout(new GridLayout(5, 1));
	    auxiliar2.add(new JPanel()); //Relleno.
	    auxiliar2.add(new JPanel()); //Relleno.
	    auxiliar2.add(scanner);
	    
	    //Tercer columna.
	    central.add(auxiliar3);
	    auxiliar3.setLayout(new BorderLayout());
	    auxiliar3.add(new JPanel(), BorderLayout.EAST); //Relleno.
	    auxiliar3.add(new JScrollPane(resultado));
	   
	    /*-------------------------------------------------*
		 *                  PANEL INFERIOR                 *
	     *-------------------------------------------------*/
	    add(inferior, BorderLayout.SOUTH);
	    inferior.setLayout(new GridLayout(1, 3, 15, 15)); //Dividir el panel inferior en 3 columnas.
	    
	    //Primer columna.
	    inferior.add(new JPanel()); //Relleno.
	    
	    //Segunda columna.
	    inferior.add(new JPanel()); //Relleno.
	    
	    //Tercer columna.
	    inferior.add(auxiliar4);
	    auxiliar4.setPreferredSize(new Dimension(50, 50));
	    auxiliar4.setLayout(new BorderLayout());
	    auxiliar4.add(new JPanel(), BorderLayout.NORTH); //Relleno.
	    auxiliar4.add(etiq3, BorderLayout.WEST);
	    auxiliar4.add(new JPanel(), BorderLayout.SOUTH); //Relleno.
	    auxiliar4.add(new JPanel(), BorderLayout.EAST); //Relleno.
	    auxiliar4.add(total);
	    
	    etiq3.setHorizontalAlignment(JLabel.CENTER);
	}

	public void actionPerformed(ActionEvent e) 
	{
		if(programa.getText().equals(""))
			JOptionPane.showMessageDialog(this, "No hay codigo.", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
		else
		{
			 p=new Parser(programa.getText());
			 p.program();
			 String cat="";
			for(int x=0;x<p.scanner.errors.size();x++)
				cat=cat+(p.scanner.errors.get(x))+"\n";
			for(int x=0;x<p.errors.size();x++)
				cat=cat+(p.errors.get(x)+"\n");
			for(int x=0;x<p.Mensajes.size();x++)
				cat=cat+(p.Mensajes.get(x)+"\n");
			resultado.setText(cat);
		}
	}
	
	public void setPrograma (String t)
	{
		programa.setText(t);
	}
	
	public String getPrograma()
	{
		return programa.getText();
	}

	public void appendResultado(Token t)
	{
		resultado.append(t.toString());
	}
	
	public void appendResultado(String t)
	{
		resultado.append(t);
	}
	
	public void setTotal(String t)
	{
		total.setText(t);
	}
	
}
