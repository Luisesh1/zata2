package compilador1;

import java.util.ArrayList;

public class Parser {
	public class Scanner {
		String code="";
		Boolean finish=false;
		ArrayList <String> errors = new ArrayList<String>();
		ArrayList <Token> reservados = new ArrayList<Token>();
		int pointer=0;
		
		Scanner(String code){
			this.code=code+" ";
			reservados.add(new Token(1,"if"));
			reservados.add(new Token(2,"while"));
			reservados.add(new Token(3,"Int"));
			reservados.add(new Token(3,"Boolean"));
			reservados.add(new Token(4,"="));
			reservados.add(new Token(5,"=="));
			reservados.add(new Token(5,"+"));
			reservados.add(new Token(6,";"));
			reservados.add(new Token(7,"("));
			reservados.add(new Token(8,")"));		
		}
		public Token getToken(){
			String cat="";
			Token aux = null;
			while (!finish)
			{	
				finish=(pointer==code.length()-1)
						||(code.substring(pointer,code.length()-1).trim().length()==0)?true:false;
				if((" ".charAt(0)==code.charAt(pointer)&&cat.length()==0)||"\n".charAt(0)==code.charAt(pointer)){
					pointer++;
					continue;
				}
				cat=cat+code.charAt(pointer);
				if (generateToken(cat)!=null){
					aux=generateToken(cat);
					pointer++;
				}
				else{
					if (generateToken(cat.trim())!=null){
						cat=cat.trim();
						pointer++;
						break;
					}
					if (generateToken(code.charAt(pointer)+"")==null){
						pointer++;
						errors.add("Error lexico: "+cat);
						return new Token(-1,cat);
					}
					break;
				}
			}
			
			return aux;
		}
		public Token generateToken(String v){
			for (int x =0 ; x<reservados.size();x++)
				if (reservados.get(x).value.compareTo(v)==0)
					return reservados.get(x);
			if (isIdentifier(v))
				return new Token(9,v);
			return null ;
		}
		public boolean isIdentifier(String v){
			if (!isLetter(v.charAt(0)))
				return false;
			for(int x = 1; x<v.length();x++)
			{
				if(!(isLetter(v.charAt(x))||isNumber(v.charAt(x))))
					return false;
			}
			return true;
		}	
		public boolean isLetter(char v){
			return ((v>=97&&v<=122)||(v>=65&&v<=90));
		}
		public boolean isNumber(char v){
			return (v>=48&&v<=57);
		}
	}
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
			}else
				if (!tabla_de_simbolos.match(temp1, temp3)){
					errorSemantico.add("no compactibles: "+temp1.value +temp2.value+temp3.value);
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
					errorSemantico.add("no declarado: "+tok);
				}
			advance (); 
		}			
		else 
			error (); 
	} 
	public void error(){
		errors.add((tok==null)?"sentencia incompleta":"error cerca de: "+tok.value);
		error=true;
	}

}
