package compilador;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import compilador.Intermedio.triple;

public class Nodo {
	Token token;
	Nodo current=this;
	ArrayList <Nodo>hojas =new ArrayList<Nodo>(); 
	ArrayList <triple>triples = new ArrayList<triple>(); 
	ArrayList <triple> saltos = new ArrayList <triple>();
	int nivel=0;
	int prof=0;
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
	public String getTriples(){
		String cad="";
		recorrido();
		for (int x = 0;x<triples.size();x++){
			cad=cad+(x+1) + "   "+triples.get(x)+"\n";
		}
		return cad;
	}
	public void recorrido(){
		triples = new ArrayList<triple>(); 
		saltos = new ArrayList <triple>();
		recorrer(this);
	}
	private void recorrer(Nodo n){
		for (int x=0;x<n.hojas.size();x++)
			recorre(n.hojas.get(x));
	}
	private void recorre(Nodo n){
		String t1,t2,t3;
		if(n == null||n.hojas.size()==0)
            return;
		prof++;
		for (int x =0;x<saltos.size();x++){
			if(prof <= saltos.get(x).profundidad){
				if(saltos.get(x).bucle)
					triples.add(new triple("jump"," ",saltos.get(x).pos+"",0,false,triples.size()+1));
				saltos.get(x).c3= (triples.size()+1)+"";
				saltos.remove(x);
				x--;
			}
		}
		recorre(n.hojas.get(0));
		nivel++;
		t1=n.getToken().value;
		t2=n.hojas.get(0).getToken().value;
		t3=n.hojas.get(1).getToken().value;
		if (n.getToken().id==1){
			t1="jz";
			t3="?";
			if(!(n.hojas.get(0).getToken().id==9)){
				t1="jnz";
				t2="("+ (triples.size()) +")";
			}
		}
		if((n.getToken().value.equals("=="))){
			t1="-";
		}
		if(n.getToken().id==4){
			t1="eq";
			t2="("+ (triples.size()) +")";
		}
		triple tri =new triple(t1,t2,t3,prof,n.getToken().id==2,triples.size()+1);
		if(n.getToken().id==1||n.getToken().id==2){
			saltos.add(tri);
			Collections.sort(saltos);
		}
		triples.add(tri);
		recorre(n.hojas.get(1));
		prof--;
	}
	class triple implements Comparable<triple> {
		String c1,c2,c3;
		int profundidad,pos;
		boolean bucle=false;
		public triple(String c1, String c2, String c3,int profundidad,boolean bucle,int pos) {
			super();
			this.c1 = c1;
			this.c2 = c2;
			this.c3 = c3;
			this.bucle=bucle;
			this.profundidad=profundidad;
			this.pos=pos;
		}

		public String getC1() {
			return c1;
		}

		public void setC1(String c1) {
			this.c1 = c1;
		}

		public String getC2() {
			return c2;
		}

		public void setC2(String c2) {
			this.c2 = c2;
		}

		public String getC3() {
			return c3;
		}

		public void setC3(String c3) {
			this.c3 = c3;
		}
		public String toString(){
			return c1+ "  "+ c2+"  "+ c3;
		}
		public int compareTo(triple other) {
	        return other.profundidad-profundidad;
	    }
		
	}
}

