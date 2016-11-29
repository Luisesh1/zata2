package compilador;

import java.util.ArrayList;

public class Nodo {
	Token token;
	Nodo current=this;
	ArrayList <Nodo>hojas =new ArrayList<Nodo>(); 
	int nivel=0;
	public Nodo(Token token) {
		super();
		this.token = token;
	}
	
	public void addNodo(Nodo n){
		hojas.add(n);
	}
	public  Token getToken(){
		return token;
	}
	public void recorrido(){
		recorre(this);
	}
	private void recorre(Nodo n){
		if(n == null)
            return;
		for (int x=0;x<n.hojas.size();x++){
			recorre(n.hojas.get(x));
			System.out.println(n.hojas.get(x).getToken().value);
		}
	}

}

