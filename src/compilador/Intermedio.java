package compilador;

import java.util.ArrayList;
import java.util.Stack;

public class Intermedio {
	Nodo sent = null;
	
	Stack<Integer> pila = new Stack<Integer>();
	ArrayList <triple>tribles = new ArrayList<triple>(); 
	Intermedio (Nodo n){
		sent= n;
	}
	public void recorrido(){
		recorre(sent);
	}
	private void recorre(Nodo n){
		if(n == null)
            return;
		for (int x=0;x<n.hojas.size();x++){
			recorre(n.hojas.get(x));
			System.out.println(n.hojas.get(x).getToken().value);
		}
	}
	class triple {
		String c1,c2,c3;

		public triple(String c1, String c2, String c3) {
			super();
			this.c1 = c1;
			this.c2 = c2;
			this.c3 = c3;
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
		
	}
	
}
