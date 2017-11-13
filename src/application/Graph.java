/**
 * 
 */

package application;

import application.Constant;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;
import java.util.Stack;

public class Graph {

  private int node = 0; // 节点个数
  private int side = 0; // 边个数

  private HashMap<String, Integer> vexToInt; // 单词映射到对应的节点下标

  private Vertex[] vertexs; // 顶点数组
  private Edge[] edges; // 边数组

  private int[][] distance; // Floyd算法距离矩阵
  private String[][] path; // Floyd算法最短路径矩阵

  private Stack<String> stackP = new Stack<String>(); // 栈（字符串类型）

  // 顶点结构体
  class Vertex {
    String ver; // 单词
    LinkedList<Edge> edge; // 边链表

    public Vertex() {
      ver = "";
      edge = new LinkedList<Edge>();
    }
  }
  // 边结构体

  class Edge {
    String startEdge = "";
    String endEdge = "";
    int weight = 0; // 权值
  }

  /**.
   * @param 生成图算法
   */
  public void readGraph(String[] readin) {
    vexToInt = new HashMap<String, Integer>();
    edges = new Edge[readin.length];
    // 边初始化
    for (int i = 0; i < readin.length; i++) {
      edges[i] = new Edge();
    }
    // 读取边
    for (int i = 0; i + 1 < readin.length; i++) {
      String title = readin[i];
      String last = readin[i + 1];

      int flag;
      flag = 0;
      // 已有边加权值
      for (int j = 0; j < side; j++) {
        if (edges[j].endEdge.equals(last) && edges[j].startEdge.equals(title)) {
          edges[j].weight++;
          flag = 1;
          break;
        }
      }
      // 没有边添加
      if (flag == 0) {
        edges[side].endEdge = last;
        edges[side].startEdge = title;
        edges[side].weight = 1;
        side++;
      }

    }
    // 单词映射顶点下标Hashmap初始化
    for (int i = 0; i < readin.length; i++) {
      vexToInt.put(readin[i], -1);
    }
    int value = 0;
    // 为单词(键)顶映射下标值(值)
    for (String key : vexToInt.keySet()) {
      vexToInt.replace(key, value);
      value++;
    }
    node = value;
    vertexs = new Vertex[value];



    distance = new int[node][node];
    path = new String[node][node];


    // ************为了防止用户不调用Floyd算法就使用最短距离算法，
    // ************故在生成图时调用弗洛伊德算法，将矩阵生成
    // 距离矩阵D和路径矩阵P初始化
    for (String key : vexToInt.keySet()) {
      int i = vexToInt.get(key);
      vertexs[i] = new Vertex();
      vertexs[i].ver = key;

      distance[i] = new int[node];
      path[i] = new String[node];
      for (int j = 0; j < node; j++) {
        distance[i][j] = Constant.INfINITE;
        path[i][j] = "";
      }


    }

    // 将边加入对应顶点边链表
    for (int i = 0; i < side; i++) {
      int j = vexToInt.get(edges[i].startEdge);
      vertexs[j].ver = edges[i].startEdge;
      vertexs[j].edge.add(edges[i]);


      distance[vexToInt.get(edges[i].startEdge)][vexToInt.get(edges[i].endEdge)] = edges[i].weight;
    }
    // Floyd算法
    for (String k : vexToInt.keySet()) {
      for (String i : vexToInt.keySet()) {
        for (String j : vexToInt.keySet()) {
          int ii = vexToInt.get(i);
          int jj = vexToInt.get(j);
          int kk = vexToInt.get(k);
          if (distance[ii][kk] + distance[kk][jj] < distance[ii][jj]) {
            distance[ii][jj] = distance[ii][kk] + distance[kk][jj];
            path[ii][jj] = k;
          }
        }
      }
    }
  }

  /**
   * 展示图算法.
   */
  public void showDirectedGraph() {
    GraphViz graphViz = new GraphViz();
    String adj;

    graphViz.addln(graphViz.start_graph());

    for (int j = 0; j < side; j++) {
      adj = edges[j].startEdge + "->" + edges[j].endEdge + "[label=\"" + edges[j].weight + "\"]";
      graphViz.addln(adj);
    }
    graphViz.addln(graphViz.end_graph());


    File file = new File("GRAPH1.jpg"); // 生成.jpg
    graphViz.writeGraphToFile(graphViz.getGraph(graphViz.getDotSource(), "jpg"), file);


  }

  /**********************************************************
   * 由于课程要求的queryBridgeWords和generateNewText都需要桥接词 . 
   * * 所以将重复代码整合为bridgeWords旨在生成桥接词，便于桥接词的查询，生成新文本 *
   * ********************************************************
   */
  // 生成桥接词算法
  private String bridgeWords(String word1, String word2) {
    String brigewords = "";
    int wordIndex = vexToInt.get(word1);
    LinkedList<Edge> startLink = vertexs[wordIndex].edge;
    Stack<String> maybridge = new Stack<String>();
    Stack<String> wholebridge = new Stack<String>();
    for (Edge edge : startLink) {
      maybridge.push(edge.endEdge);
    }
    // 可能的桥接词栈不为空，则说明还有可能的桥接词
    while (!maybridge.empty()) {
      int maybrigeIndex = vexToInt.get(maybridge.pop()); // 将他出栈
      LinkedList<Edge> bridgeLink = vertexs[maybrigeIndex].edge;
      // 遍历可能桥接词的边，如果有endEdge为word2的，说明可能桥接词为真的桥接词，将其如wholebridge栈
      for (Edge end : bridgeLink) {
        if (end.endEdge.equals(word2)) {
          wholebridge.push(vertexs[maybrigeIndex].ver);
        }
      }
    }
    // 全部桥接词wholebridge不为空，出栈
    while (!wholebridge.empty()) {
      String temp = wholebridge.pop();
      if (!wholebridge.empty()) {
        brigewords = brigewords + temp + ",";
      } else {
        brigewords = brigewords + temp + ".";
      }
    }
    return brigewords; // 返回桥接词
  }

  /**
   * .
   * 
   * @param 第一个词
   * @param 第二个词
   * @return 查询桥接词
   */
  public String queryBridgeWords(String word1, String word2) {
    String outputString = "";
    // word1word2都存在
    if (vexToInt.containsKey(word1) && vexToInt.containsKey(word2)) {
      String bridgeWord = bridgeWords(word1, word2); // 调用生成桥接词算法
      if (bridgeWord.equals("")) {
        outputString = "No bridge words from " + word1 + " to " + word2 + " !";
      } else {
        outputString = "The bridge words from " + word1 + " to " + word2 + " are: " + bridgeWord;
      }
      // word1或word2不存在
    } else {
      outputString = "No word1 or word2 in the graph!";
    }
    return outputString;
  }

  /**
   * .
   * 
   * @param 文本
   * @return 生成新文本
   */
  public String generateNewText(String inputText) {
    String outputText = "";
    String[] splitText = inputText.toLowerCase().split("[^a-zA-Z]+");// 分割新文本

    int i = 0;
    // 遍历新文本
    for (; i + 1 < splitText.length; i++) {
      outputText = outputText + splitText[i] + " ";
      // 相邻词之间调用桥接词算法
      if (vexToInt.containsKey(splitText[i]) && vexToInt.containsKey(splitText[i + 1])) {
        String bridgeWords = bridgeWords(splitText[i], splitText[i + 1]);
        if (!bridgeWords.equals("")) {
          String[] temp = bridgeWords.split("[^a-zA-Z]+");

          Random forInsert = new Random();
          int insertIndex = forInsert.nextInt(temp.length);


          outputText = outputText + temp[insertIndex] + " ";
        }
      }
    }
    outputText = outputText + splitText[i];

    return outputText;
  }

  /**********************************
   * 最短路径算法 shortestPath用于生成一条最短路径(递归算法) . 
   * allToOne是单源最短路径 calcshortestPath是查询两个点之间的最短路径
   * 由于最短路径算法对于单源和指定的点对是重复部分，故写shortestPath函数来简化代码
   */

  // 生成一条最短路径
  private String shortestPath(String start, String end) {
    String outt = "";
    int i = vexToInt.get(start);
    int j = vexToInt.get(end);

    if (path[i][j].equals("")) {
      return "";
    } else {
      stackP.push(path[i][j]);
      outt = outt + shortestPath(start, path[i][j]); // 递归
      if (!stackP.empty()) {
        outt = outt + stackP.pop() + "->";
      }
      outt = outt + shortestPath(path[i][j], end);
      if (!stackP.empty()) {
        outt = outt + stackP.pop() + "->";
      }
      return outt;
    }
  }

  /**
   * .
   * 
   * @param 起始点
   * @return 单源最短路径
   */
  public String allToOne(String start) {
    String outt = "";
    // 遍历其他点
    for (String end : vexToInt.keySet()) {
      if (start.equals(end)) {
        continue;
      } else {
        outt = outt + calcshortestPath(start, end);
      }
    }
    return outt;
  }

  /**
   * .
   * 
   * @param 第一个词
   * @param 第二个词
   * @return 点对之间的最短路径
   */
  public String calcshortestPath(String word1, String word2) {
    String outt = "";
    // word1word2存在
    if (vexToInt.containsKey(word1) && vexToInt.containsKey(word2)) {
      if (word1.equals(word2)
          || distance[vexToInt.get(word1)][vexToInt.get(word2)] == Constant.INfINITE) {
        outt = "No way!";
        // 有路径
      } else {
        String temp = shortestPath(word1, word2);
        outt = word1 + "->" + temp + word2 + "\nThe length of path is "
            + distance[vexToInt.get(word1)][vexToInt.get(word2)] + "\n";
      }
    } else {
      outt = word1 + " or " + word2 + " is not in the graph!";
    }
    return outt;
  }

  /**
   * .
   * 
   * @param 随机游走
   * @return null
   */
  public String randomWalk() {
    String outt = "";

    HashMap<Edge, Boolean> visit = new HashMap<Edge, Boolean>();// 边是否遍历过，初始化false
    for (int j = 0; j < side; j++) {
      visit.put(edges[j], false);
    }


    Random r1 = new Random();
    int x = r1.nextInt(node);
    String start = vertexs[x].ver;
    stackP.push(start); // 随机一个点起手

    outt = start;
    LinkedList<Edge> p;
    while (!stackP.empty()) {
      p = vertexs[vexToInt.get(stackP.pop())].edge;
      // 无路可走
      if (!p.isEmpty()) {
        int j = r1.nextInt(p.size());
        Edge edge = p.get(j);
        outt = outt + " " + edge.endEdge;
        // 重复的边
        if (!visit.get(edge)) {
          visit.replace(edge, true);
          stackP.push(edge.endEdge);
        }


      }
    }
    return outt;
  }



}
