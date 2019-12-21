package stoneAgeRunner;

import java.util.HashMap;



public abstract class State implements Comparable<State> {
byte [] [] state;
public HashMap <String,State> nextStates;

public State(byte[][] map) {
	// TODO Auto-generated constructor stub
	this.state=map;
}

protected abstract void genStateSpace();
public abstract String hash();



}
