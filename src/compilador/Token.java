package compilador;

public class Token {
	int id=0;
	String value="";
	Token(int id,String value){
		this.id = id;
		this.value = value;
	}
	public String toString(){
		if (id==-1)
			return "id -- > "+ id +", value -->"+value+"-->Token invalido";
		else
			return "id -- > "+ id +", value -->"+value;
	}
}
