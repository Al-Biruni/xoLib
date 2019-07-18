package stoneAgeRunner;



public abstract class Problem {
String[] operators ;
State intialState;
State[] stateSpace;
State goalState;

abstract int  pathCost(String op);
}
