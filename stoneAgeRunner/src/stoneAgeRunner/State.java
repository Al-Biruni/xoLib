package stoneAgeRunner;

import java.util.HashMap;



public abstract class State implements Comparable<State> {
String [] [] state;
public HashMap <String,State> nextStates;

public State(String[][] map) {
	// TODO Auto-generated constructor stub
	this.state=map;
}

protected abstract void genStateSpace();
public abstract int hashCode();



}
