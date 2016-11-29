package compilador;

import java.util.ArrayList;
public class Scanner {
	String code="";
	Boolean finish=false;
	ArrayList <String> errors = new ArrayList<String>();
	ArrayList <Token> words = new ArrayList<Token>();
	int pointer=0;
	Scanner(String code){
		this.code=code+" ";
		words.add(new Token(1,"if"));
		words.add(new Token(2,"while"));
		words.add(new Token(3,"Int"));
		words.add(new Token(3,"Boolean"));
		words.add(new Token(4,"="));
		words.add(new Token(5,"=="));
		words.add(new Token(5,"+"));
		words.add(new Token(6,";"));
		words.add(new Token(7,"("));
		words.add(new Token(8,")"));		
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
		for (int x =0 ; x<words.size();x++)
			if (words.get(x).value.compareTo(v)==0)
				return words.get(x);
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
