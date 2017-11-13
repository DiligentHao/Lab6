package application;

import static org.junit.Assert.*;

import org.junit.Test;

public class GraphTest2 {
	public static Graph box = new Graph();
	@Test
	public void testCalcshortestPath() {
		String[] text = {"being", "mature", "doesn", "t", "mean", "the", "person", "is", "adult", "the", "age", "is", "not", "the", "standard", "to", "measure", "whether", "a", "person", "is", "mature", "or", "not", "some", "teenagers", "know", "their", "future", "duty", "and", "act", "in", "a", "mature", "way", "the", "symbol", "of", "being", "mature", "is", "not", "decided", "by", "age", "but", "the", "way", "they", "think"};
		box.readGraph(text);
		
		String result = box.calcshortestPath("wang", "zhou");
		String expect = new String("wang or zhou is not in the graph!");
		assertEquals(expect, result);
		
		result = box.calcshortestPath("mature", "t");
		expect = new String("mature->doesn->t\n" + 
				"The length of path is 2\n");
		assertEquals(expect, result);
		
		result = box.calcshortestPath("think", "being");
		expect = new String("No way!");
		assertEquals(expect, result);

	}

	
}
