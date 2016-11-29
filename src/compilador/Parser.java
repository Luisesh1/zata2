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
	Nodo current=root;
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
			current=root;
			if(tok==null)
				break;
		}
		declaraciones=true;
		System.out.println("------------tabla de simbolos--------------");
		System.out.println(tabla_de_simbolos);
		while(tok!=null&&!error){
			statement();
			current=root;
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
	public void statement(){
		Nodo aux=current;
		switch ( tok.id ) { 
		case 9: 
			compare(id);
			compare(equ);
			expresion();
			compare(pcom); 
		break; 
		case 1: 
			compare(IF);
			compare(pa);
			expresion();
			compare(pc);
			statement();
		break; 
		case 2:
			compare(WHILE);
			compare(pa);
			expresion();
			compare(pc);
			statement();
		break;
		default: error ();
		}	
		current=aux;
	}
	public void expresion(){
		if (tok==null||error)
			return;
		Nodo aux=current;
		switch ( tok.id ) { 
		case 9: 
			Token temp1=tok;
			compare(id);
			if(tok.id==pcom||tok.id==pc)
				return;
			Token temp2=tok;
			compare(mas);
			Token temp3=tok;
			compare(id); 
			if (temp2.value.equals("+")&&(tabla_de_simbolos.getTipo(temp1).equals("Boolean")||tabla_de_simbolos.getTipo(temp3).equals("Boolean"))){
				errorSemantico.add("no se puede hacer la suma de booleanos: "+temp1.value +"+"+temp3.value);
				System.out.println("no se puede hacer la suma de booleanos: "+temp1.value +"+"+temp3.value);
			}else
				if (!tabla_de_simbolos.match(temp1, temp3)){
					errorSemantico.add("tipos de datos no compactibles: "+temp1.value +"+"+temp3.value);
					System.out.println("tipos de datos no compactibles: "+temp1.value +"+"+temp3.value);
				}
					
		break; 
		
		default: error ();
		}
		current=aux;
	}
	public void advance(){
		if (tok==null)
			return;
		if (!(tok.id==pcom||tok.id==pa||tok.id==pc||tok.id==iv)){
			Nodo n=new Nodo(tok);
			current.addNodo(n); 
			current = n;
		}
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
		Parser p = new Parser("Int i; Boolean b1; Boolean b2; if(i==i)i=i; i=i==b1; ");
		p.program();
		for(int x=0;x<p.scanner.errors.size();x++)
			System.out.println(p.scanner.errors.get(x));
		for(int x=0;x<p.errors.size();x++)
			System.out.println(p.errors.get(x));
		for(int x=0;x<p.Mensajes.size();x++)
			System.out.println(p.Mensajes.get(x));
	}

}
