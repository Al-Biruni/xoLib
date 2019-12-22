package stoneAgeRunner;

import java.util.Arrays;

public abstract class State implements Comparable<State> {
byte [] [] state;
//public HashMap <String,State> nextStates;

public State(byte[][] map) {
	// TODO Auto-generated constructor stub
	this.state=map;
}


@Override
public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + Arrays.deepHashCode(state);
	return result;
}

@Override
public boolean equals(Object obj) {
	if (this == obj)
		return true;
	if (obj == null)
		return false;
	if (getClass() != obj.getClass())
		return false;
	State other = (State) obj;
	if (!Arrays.deepEquals(state, other.state))
		return false;
	return true;
}



}
