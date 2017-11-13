package application;

import static org.junit.Assert.*;

import org.junit.Test;

public class GraphTest {
	public static Graph aaa = new Graph();
	@Test
	public void testShowDirectedGraph(){
		String[] s = {"being", "mature", "doesn", "t", "mean", "the", "person", "is", "adult", "the", "age", "is", "not", "the", "standard", "to", "measure", "whether", "a", "person", "is", "mature", "or", "not", "some", "teenagers", "know", "their", "future", "duty", "and", "act", "in", "a", "mature", "way", "the", "symbol", "of", "being", "mature", "is", "not", "decided", "by", "age", "but", "the", "way", "they", "think"};
		//System.out.println(s[1]);
		aaa.readGraph(s);
		String result = aaa.queryBridgeWords("wang", "zhou");
		String expect = new String("No word1 or word2 in the graph!");
		assertEquals(expect, result);
		
		result = aaa.queryBridgeWords("mature", "t");
		expect = new String("The bridge words from mature to t are: doesn.");
		assertEquals(expect, result);
		
		result = aaa.queryBridgeWords("111", "222");
		expect = new String("No word1 or word2 in the graph!");
		assertEquals(expect, result);
		
		
		
		result = aaa.queryBridgeWords("is", "being");
		expect = new String("No bridge words from is to being !");
		assertEquals(expect, result);
		
	}

}
