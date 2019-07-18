package stoneAgeRunner;

public class Stone {
int x ;
int y ;
int id;
boolean vertical;
int length;
public Stone(int x,int y,int id,boolean v, int len) {
	this.x=x;
	this.y=y;
	this.id=id;
	this.vertical=v;
	this.length=len;
}

@Override
public String toString() {
	String s="R";
	if(vertical) {
		s+="V";
	}else {
		s+="H";
	}
	if(length==2) {
		s+="S";
	}else {
		s+="L";
	}
	s+=" "+id;
	
	return s;
	
}
	
}
