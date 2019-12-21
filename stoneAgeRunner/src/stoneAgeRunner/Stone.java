package stoneAgeRunner;

public class Stone {
	byte x ;
	byte y ;
byte id;
boolean vertical;
byte length;
public Stone(byte x,byte y,int id,boolean v, byte len) {
	this.x=x;
	this.y=y;
	
	this.vertical=v;
	this.length=len;
	switch(length) {
	case 2: if(vertical) {
		this.id = (byte) (-2 -id);
	}else {
		this.id = (byte) (2 + id);
	}break;
	case 3: if(vertical) {
		this.id = (byte) (-50 -id);
	}else {
		this.id = (byte) (50 + id);
	}break;
	}
}
public Stone(byte x,byte y,byte id,boolean v, byte len) {
	this.x=x;
	this.y=y;
	
	this.vertical=v;
	this.length=len;
	this.id=id;
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
	//s+=" "+id;
	
	return s;
	
}
	
}
