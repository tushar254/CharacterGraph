import java.io.*;
import java.util.ArrayList;
import java.util.Vector;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Iterator;
import java.util.Map;

class Vertex{
	public int id;
	public String name;
	public Vertex(int id,String name){
		this.id = id;
		this.name = name;
	}
}

class Edge{
	public int src;
	public int tgt;
	public int weight;
	// same inputs for vertices as the mapping of vlist !
	public Edge(int s,int t,int w){
		this.src = s;
		this.tgt = t;
		this.weight = w;
	}
}

public class A4_2019EE10539{
	public int V;
	public LinkedList<Edge> adj[];
	public int co_occurence[];

	public A4_2019EE10539(int v){
		this.V = v;
		this.adj = new LinkedList[v];
		this.co_occurence = new int[v];
		for(int i=0;i<v;i++){
			this.adj[i] = new LinkedList<Edge>();
			this.co_occurence[i] = 0;
		}
	}
	public void addEdge(int s,int t,int w){
		this.adj[s].add(new Edge(s,t,w));
		co_occurence[s] += w;
		this.adj[t].add(new Edge(t,s,w));
		co_occurence[t] += w;
	}

	public void average(){
		long sum = 0;
		for(int i=0;i<this.V;i++){
			sum += this.adj[i].size();
		}
		float ans = (float)sum/(this.V);
		System.out.format("%.2f",ans);
		System.out.println();
	}

	public void rank(Vector<Vertex> vlist){
		sort(vlist,0,vlist.size()-1);
		for(int i=vlist.size()-1;i>0;i--){
			System.out.print(vlist.get(i).name + ",");
		}
		System.out.print(vlist.get(0).name);
		System.out.println();
	}
	
	public int partition(Vector<Vertex> vlist,int low,int high){
		int x = (int)(Math.random()*(high-low) + low);
		int pivot = this.co_occurence[vlist.get(x).id];
		String cmp = vlist.get(x).name;
		swap(vlist,x,high);
		int i = low-1;
		for(int j=low;j<high;j++){
			int tc = this.co_occurence[vlist.get(j).id]; 
			if((tc < pivot) || (tc == pivot && vlist.get(j).name.compareTo(cmp) < 0)){
				i++;
				swap(vlist,i,j);
			}
		}
		swap(vlist,i+1,high);
		return i+1;
	}
	public void sort(Vector<Vertex> vlist,int low,int high){
		if(low >= high)	return;
		int p = partition(vlist,low,high);		
		sort(vlist,low,p-1);
		sort(vlist,p+1,high);
	}
	public void swap(Vector<Vertex> vlist,int x,int y){
		int tid = vlist.get(x).id;
		vlist.get(x).id = vlist.get(y).id;
		vlist.get(y).id = tid;
		String tname = vlist.get(x).name;
		vlist.get(x).name = vlist.get(y).name;
		vlist.get(y).name = tname;
 	}
	//************* MAIN FUNCTION STARTS **************
	public static void main(String[] args) {
		Map<String,Integer> NametoId = new HashMap<String,Integer>();
		int num = 0;		//first row is header !
		Vector vlist = new Vector<Vertex>();
		//Assuming args.length = 3, as mentioned
		String nodefile = args[0];
		String edgefile = args[1];
		String func = args[2];
		try{
			BufferedReader b = new BufferedReader(new FileReader(nodefile));
			String line = b.readLine();
			while((line = b.readLine()) != null){
				String[] val = {"",""};
				int cnt = 0,idx = 0;
				for(int i=0;i<line.length();i++){
					if(line.charAt(i) == '"'){
						cnt++;
						continue;
					}
					if(line.charAt(i) == ','){
						if(cnt%2 == 1) val[idx] += line.charAt(i);
						else idx++;
						continue;
					}
					val[idx] += line.charAt(i);
				}
				NametoId.put(val[1],num);
				vlist.add(new Vertex(num,val[1]));
				//System.out.println(val[0]+" "+val[1]);
				num++;
			}
		}
		catch(FileNotFoundException e){
			e.printStackTrace();
		}
		catch(IOException e){
			e.printStackTrace();
		}
		A4_2019EE10539 g = new A4_2019EE10539(num);
		try{
			BufferedReader b = new BufferedReader(new FileReader(edgefile));
			String line = b.readLine();
			while((line = b.readLine()) != null){
				String[] val = {"","",""};
				int cnt = 0,idx = 0;
				for(int i=0;i<line.length();i++){
					if(line.charAt(i) == '"'){
						cnt++;
						continue;
					}
					if(line.charAt(i) == ','){
						if(cnt%2 == 1) val[idx] += line.charAt(i);
						else idx++;
						continue;
					}
					val[idx] += line.charAt(i);
				}	
				//System.out.println(val[0]+" "+val[1]+" "+val[2]);
				g.addEdge(NametoId.get(val[0]),NametoId.get(val[1]),Integer.parseInt(val[2]));
			}
		}
		catch(FileNotFoundException e){
			e.printStackTrace();
		}
		catch(IOException e){
			e.printStackTrace();
		}
		if(func.equals("average")){
			g.average();
		}
		if(func.equals("rank")){
			g.rank(vlist);
		}
	}
}