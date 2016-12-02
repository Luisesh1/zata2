package compilador1;

import java.util.ArrayList;

public class Tabla {
	ArrayList<renglon> lista = new ArrayList<renglon>(); 
	public void addRenglon(Token t, Token d){
		lista.add(new renglon(t.value,d.value));
	}
	public String getTipo(Token t1){
		for (int x=0;x<lista.size();x++)
			if (lista.get(x).declaracion.compareTo(t1.value)==0)
				return lista.get(x).tipo;
		return "fail";
	}
	public boolean match(Token v1,Token v2){
		if (!exist(v1)||!exist(v2))
			return false;
		  return getRow(v1).tipo.compareTo(getRow(v2).tipo)==0;
	}
	public boolean exist(Token v1){
		for (int x=0;x<lista.size();x++)
			if (lista.get(x).declaracion.compareTo(v1.value)==0)
				return true;
		System.out.println("no esta declarado");
		return false;
	}
	public renglon getRow(Token v1){
		for (int x=0;x<lista.size();x++)
			if (lista.get(x).declaracion.compareTo(v1.value)==0)
				return lista.get(x);
		return null;
	}
	public String toString(){
		String cad="";
		for (int x=0;x<lista.size();x++)
			cad=cad+lista.get(x)+"\n";
		return cad;
	}
	class renglon{
		String tipo;
		String declaracion;
		renglon(String tipo,String declaracion){
			this.tipo=tipo;
			this.declaracion=declaracion;
		}
		public String toString(){
			return "Tipo: "+tipo + "  identificador : "+declaracion;
		}
	}
	public static void main(String []sds){
		Tabla t= new Tabla();
		System.out.println(t.exist(new Token(0,"a")));
		t.addRenglon(new Token(0,"Int"),new Token(0,"a"));
		t.addRenglon(new Token(0,"Int"),new Token(0,"b"));
		t.addRenglon(new Token(0,"Boolean"),new Token(0,"c"));
		System.out.println(t);
		System.out.println(t.exist(new Token(0,"a")));
		System.out.println(t.match(new Token(0,"a"),new Token(0,"b")));
		System.out.println(t.match(new Token(0,"a"),new Token(0,"c")));
		
	}
	
}
