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

  private int node = 0; // �ڵ����
  private int side = 0; // �߸���

  private HashMap<String, Integer> vexToInt; // ����ӳ�䵽��Ӧ�Ľڵ��±�

  private Vertex[] vertexs; // ��������
  private Edge[] edges; // ������

  private int[][] distance; // Floyd�㷨�������
  private String[][] path; // Floyd�㷨���·������

  private Stack<String> stackP = new Stack<String>(); // ջ���ַ������ͣ�

  // ����ṹ��
  class Vertex {
    String ver; // ����
    LinkedList<Edge> edge; // ������

    public Vertex() {
      ver = "";
      edge = new LinkedList<Edge>();
    }
  }
  // �߽ṹ��

  class Edge {
    String startEdge = "";
    String endEdge = "";
    int weight = 0; // Ȩֵ
  }

  /**.
   * @param ����ͼ�㷨
   */
  public void readGraph(String[] readin) {
    vexToInt = new HashMap<String, Integer>();
    edges = new Edge[readin.length];
    // �߳�ʼ��
    for (int i = 0; i < readin.length; i++) {
      edges[i] = new Edge();
    }
    // ��ȡ��
    for (int i = 0; i + 1 < readin.length; i++) {
      String title = readin[i];
      String last = readin[i + 1];

      int flag;
      flag = 0;
      // ���б߼�Ȩֵ
      for (int j = 0; j < side; j++) {
        if (edges[j].endEdge.equals(last) && edges[j].startEdge.equals(title)) {
          edges[j].weight++;
          flag = 1;
          break;
        }
      }
      // û�б����
      if (flag == 0) {
        edges[side].endEdge = last;
        edges[side].startEdge = title;
        edges[side].weight = 1;
        side++;
      }

    }
    // ����ӳ�䶥���±�Hashmap��ʼ��
    for (int i = 0; i < readin.length; i++) {
      vexToInt.put(readin[i], -1);
    }
    int value = 0;
    // Ϊ����(��)��ӳ���±�ֵ(ֵ)
    for (String key : vexToInt.keySet()) {
      vexToInt.replace(key, value);
      value++;
    }
    node = value;
    vertexs = new Vertex[value];



    distance = new int[node][node];
    path = new String[node][node];


    // ************Ϊ�˷�ֹ�û�������Floyd�㷨��ʹ����̾����㷨��
    // ************��������ͼʱ���ø��������㷨������������
    // �������D��·������P��ʼ��
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

    // ���߼����Ӧ���������
    for (int i = 0; i < side; i++) {
      int j = vexToInt.get(edges[i].startEdge);
      vertexs[j].ver = edges[i].startEdge;
      vertexs[j].edge.add(edges[i]);


      distance[vexToInt.get(edges[i].startEdge)][vexToInt.get(edges[i].endEdge)] = edges[i].weight;
    }
    // Floyd�㷨
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
   * չʾͼ�㷨.
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


    File file = new File("GRAPH1.jpg"); // ����.jpg
    graphViz.writeGraphToFile(graphViz.getGraph(graphViz.getDotSource(), "jpg"), file);


  }

  /**********************************************************
   * ���ڿγ�Ҫ���queryBridgeWords��generateNewText����Ҫ�ŽӴ� . 
   * * ���Խ��ظ���������ΪbridgeWordsּ�������ŽӴʣ������ŽӴʵĲ�ѯ���������ı� *
   * ********************************************************
   */
  // �����ŽӴ��㷨
  private String bridgeWords(String word1, String word2) {
    String brigewords = "";
    int wordIndex = vexToInt.get(word1);
    LinkedList<Edge> startLink = vertexs[wordIndex].edge;
    Stack<String> maybridge = new Stack<String>();
    Stack<String> wholebridge = new Stack<String>();
    for (Edge edge : startLink) {
      maybridge.push(edge.endEdge);
    }
    // ���ܵ��ŽӴ�ջ��Ϊ�գ���˵�����п��ܵ��ŽӴ�
    while (!maybridge.empty()) {
      int maybrigeIndex = vexToInt.get(maybridge.pop()); // ������ջ
      LinkedList<Edge> bridgeLink = vertexs[maybrigeIndex].edge;
      // ���������ŽӴʵıߣ������endEdgeΪword2�ģ�˵�������ŽӴ�Ϊ����ŽӴʣ�������wholebridgeջ
      for (Edge end : bridgeLink) {
        if (end.endEdge.equals(word2)) {
          wholebridge.push(vertexs[maybrigeIndex].ver);
        }
      }
    }
    // ȫ���ŽӴ�wholebridge��Ϊ�գ���ջ
    while (!wholebridge.empty()) {
      String temp = wholebridge.pop();
      if (!wholebridge.empty()) {
        brigewords = brigewords + temp + ",";
      } else {
        brigewords = brigewords + temp + ".";
      }
    }
    return brigewords; // �����ŽӴ�
  }

  /**
   * .
   * 
   * @param ��һ����
   * @param �ڶ�����
   * @return ��ѯ�ŽӴ�
   */
  public String queryBridgeWords(String word1, String word2) {
    String outputString = "";
    // word1word2������
    if (vexToInt.containsKey(word1) && vexToInt.containsKey(word2)) {
      String bridgeWord = bridgeWords(word1, word2); // ���������ŽӴ��㷨
      if (bridgeWord.equals("")) {
        outputString = "No bridge words from " + word1 + " to " + word2 + " !";
      } else {
        outputString = "The bridge words from " + word1 + " to " + word2 + " are: " + bridgeWord;
      }
      // word1��word2������
    } else {
      outputString = "No word1 or word2 in the graph!";
    }
    return outputString;
  }

  /**
   * .
   * 
   * @param �ı�
   * @return �������ı�
   */
  public String generateNewText(String inputText) {
    String outputText = "";
    String[] splitText = inputText.toLowerCase().split("[^a-zA-Z]+");// �ָ����ı�

    int i = 0;
    // �������ı�
    for (; i + 1 < splitText.length; i++) {
      outputText = outputText + splitText[i] + " ";
      // ���ڴ�֮������ŽӴ��㷨
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
   * ���·���㷨 shortestPath��������һ�����·��(�ݹ��㷨) . 
   * allToOne�ǵ�Դ���·�� calcshortestPath�ǲ�ѯ������֮������·��
   * �������·���㷨���ڵ�Դ��ָ���ĵ�����ظ����֣���дshortestPath�������򻯴���
   */

  // ����һ�����·��
  private String shortestPath(String start, String end) {
    String outt = "";
    int i = vexToInt.get(start);
    int j = vexToInt.get(end);

    if (path[i][j].equals("")) {
      return "";
    } else {
      stackP.push(path[i][j]);
      outt = outt + shortestPath(start, path[i][j]); // �ݹ�
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
   * @param ��ʼ��
   * @return ��Դ���·��
   */
  public String allToOne(String start) {
    String outt = "";
    // ����������
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
   * @param ��һ����
   * @param �ڶ�����
   * @return ���֮������·��
   */
  public String calcshortestPath(String word1, String word2) {
    String outt = "";
    // word1word2����
    if (vexToInt.containsKey(word1) && vexToInt.containsKey(word2)) {
      if (word1.equals(word2)
          || distance[vexToInt.get(word1)][vexToInt.get(word2)] == Constant.INfINITE) {
        outt = "No way!";
        // ��·��
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
   * @param �������
   * @return null
   */
  public String randomWalk() {
    String outt = "";

    HashMap<Edge, Boolean> visit = new HashMap<Edge, Boolean>();// ���Ƿ����������ʼ��false
    for (int j = 0; j < side; j++) {
      visit.put(edges[j], false);
    }


    Random r1 = new Random();
    int x = r1.nextInt(node);
    String start = vertexs[x].ver;
    stackP.push(start); // ���һ��������

    outt = start;
    LinkedList<Edge> p;
    while (!stackP.empty()) {
      p = vertexs[vexToInt.get(stackP.pop())].edge;
      // ��·����
      if (!p.isEmpty()) {
        int j = r1.nextInt(p.size());
        Edge edge = p.get(j);
        outt = outt + " " + edge.endEdge;
        // �ظ��ı�
        if (!visit.get(edge)) {
          visit.replace(edge, true);
          stackP.push(edge.endEdge);
        }


      }
    }
    return outt;
  }



}
