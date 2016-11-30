package compilador;

import java.util.ArrayList;

public class Parser {
	ArrayList <String>errors= new ArrayList<String>();
	ArrayList <String>errorSemantico= new ArrayList<String>();
	ArrayList <String>Mensajes= new ArrayList<String>();
	Nodo root=new Nodo(new Token(-1,"program"));
	Nodo decl=new Nodo(new Token(-1,"declaraciones"));
	Nodo sent=new Nodo(new Token(-1,"sentencias"));
	boolean declaraciones=false;
	Tabla tabla_de_simbolos  = new Tabla();	
	Scanner scanner; 
	Boolean error=false;
	Token tok=new Token(-1,"inicializar");
	int
	 IF=1,
	 WHILE=2,
	 INT=3,
	 BOOLEAN=3,
	 equ=4,
	 comp=5,
	 mas=5,
	 pcom=6,
	 pa=7,
	 pc=8,
	 id=9,
	 iv=-1;
	Parser(String code){
		scanner= new Scanner(code);
		root.addNodo(decl);
		root.addNodo(sent);
		advance();
	}
	
	public void compilar(){
		Token token;
		System.out.println(tok);
		do{
			token=scanner.getToken();
			System.out.println(token);
		}while (!scanner.finish);
	}
	public void program(){
		if (tok==null){
			Mensajes.add("compilado finalizado Exito!!");
			return;
		}
		while(tok.id==3&&!error){
			varDeclaration();
			if(tok==null)
				break;
		}
		declaraciones=true;
		System.out.println("------------tabla de simbolos--------------");
		System.out.println(tabla_de_simbolos);
		while(tok!=null&&!error){
			sent.addNodo(statement());
		}
		
		Mensajes.add("compilado finalizado");
		if (!error)
			Mensajes.add("Exito!!");
	}
	public void varDeclaration(){
		if (tok==null)
			return;
		Nodo aux=decl;
		Nodo tipo = new Nodo(tok);
		decl.addNodo(tipo);
		compare(INT);
		tipo.addNodo(new Nodo(tok));
		compare(id);
		compare(pcom);
		tabla_de_simbolos.addRenglon(tipo.getToken(), tipo.hojas.get(0).getToken());
	}
	public Nodo statement(){
		Nodo padre=null; 
		Token t1=null,t2=null,t3=null;
		Nodo nt1=null,nt2=null;
		switch ( tok.id ) { 
		case 9: 
			t1=tok;
			compare(id);
			t2=tok;
			compare(equ);
			nt1=expresion();
			compare(pcom);
			padre=new Nodo(t2);
			padre.addNodo(nt1);
			padre.addNodo(new Nodo(t1));
			
		break; 
		case 1:
			t1=tok;
			compare(IF);
			compare(pa);
			nt1=expresion();
			compare(pc);
			nt2=statement();
			padre = new Nodo(t1);
			padre.addNodo(nt1);
			padre.addNodo(nt2);
		break; 
		case 2:
			t1=tok;
			compare(WHILE);
			compare(pa);
			nt1=expresion();
			compare(pc);
			nt2=statement();
			padre = new Nodo(t1);
			padre.addNodo(nt1);
			padre.addNodo(nt2);
		break;
		default: error ();
			return null;
		}
		return padre;
	}
	public Nodo expresion(){
		Nodo padre=null;
		Token temp1=null,temp2= null,temp3=null;
		if (tok==null||error)
			return null;
		switch ( tok.id ) { 
		case 9: 
			temp1=tok;
			compare(id);
			if(tok.id==pcom||tok.id==pc)
				return new Nodo(temp1);
			temp2=tok;
			compare(mas);
			temp3=tok;
			compare(id); 
			if (temp2.value.equals("+")&&(tabla_de_simbolos.getTipo(temp1).equals("Boolean")||tabla_de_simbolos.getTipo(temp3).equals("Boolean"))){
				errorSemantico.add("no se puede hacer la suma de booleanos: "+temp1.value +"+"+temp3.value);
				System.out.println("no se puede hacer la suma de booleanos: "+temp1.value +"+"+temp3.value);
			}else
				if (!tabla_de_simbolos.match(temp1, temp3)){
					errorSemantico.add("tipos de datos no compactibles: "+temp1.value +"+"+temp3.value);
					System.out.println("tipos de datos no compactibles: "+temp1.value +temp2.value+temp3.value);
				}	
		break; 
		default: error ();
		}
		padre = new Nodo(temp2);
		padre.addNodo(new Nodo(temp1));
		padre.addNodo(new Nodo(temp3));
		return padre;
	}
	public void advance(){
		if (tok==null)
			return;
		tok =scanner.getToken();
		System.out.println(tok);
		
	}
	public void compare ( int t) { 
		if (tok==null){
			error();
			return;
			}
		if(error)
			return;
		if ( tok.id == t){
			if (declaraciones&&tok.id==9)
				if (!tabla_de_simbolos.exist(tok)){
					System.out.println("Token inexitenete ---> "+tok);
					errorSemantico.add("Token inexitenete ---> "+tok);
				}
			advance (); 
		}			
		else 
			error (); 
	} 
	public void error(){
		errors.add((tok==null)?"sentencia imcompleta":"error cerca de: "+tok.value);
		error=true;
	}
	public static void main(String [] aa){
		Parser p = new Parser("Int x; Int y; Int b; Int a; Int r;  while(x==y)while(f)x=x+b; r=a==b;r=a==b; ");
		p.program();
		for(int x=0;x<p.scanner.errors.size();x++)
			System.out.println(p.scanner.errors.get(x));
		for(int x=0;x<p.errors.size();x++)
			System.out.println(p.errors.get(x));
		for(int x=0;x<p.Mensajes.size();x++)
			System.out.println(p.Mensajes.get(x));
		System.out.println(p.sent.getTriples());
	}

}
