package mathLib.sfg.numeric;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Stack;

import mathLib.numbers.Complex;


public class SFG {

	private ArrayList<Edge>[] graph;
	private ArrayList<Path> forwardPaths, individualLoops;
	private boolean[] visited;
	private ArrayList<ArrayList<Integer>> allLoops;
	private Hashtable<String, Boolean> orignal;
	private Complex[] deltaM;
	private ArrayList<String> nodesName;
	private boolean supressOutputs = true ;

	@SuppressWarnings("unchecked")
	public SFG(int nodes, ArrayList<String> names) {
		graph = new ArrayList[nodes];
		for (int i = 0; i < nodes; i++) {
			graph[i] = new ArrayList<>();
		}
		visited = new boolean[nodes];
		nodesName = names;
	}

	@SuppressWarnings("unchecked")
	public SFG(ArrayList<String> names) {
		if(names != null){
			int nodes = names.size() ;
			graph = new ArrayList[nodes];
			for (int i = 0; i < nodes; i++) {
				graph[i] = new ArrayList<>();
			}
			visited = new boolean[nodes];
			nodesName = names;
		}
		else{
			int nodes = 0 ;
			graph = new ArrayList[nodes];
			for (int i = 0; i < nodes; i++) {
				graph[i] = new ArrayList<>();
			}
			visited = new boolean[nodes];
			nodesName = new ArrayList<>();
		}
	}

	public ArrayList<String> getNodes(){
		return nodesName ;
	}

	public ArrayList<Edge>[] getEdges(){
		return graph ;
	}

	@SuppressWarnings("unchecked")
	public void append(SFG sfg){
		int M = nodesName.size() ;
		int N = sfg.getNodes().size() ;
		nodesName.addAll(sfg.getNodes()) ;
		ArrayList<Edge>[] temp = graph.clone() ;
		graph = new ArrayList[nodesName.size()] ;
		for (int i = 0; i < nodesName.size(); i++) {
			graph[i] = new ArrayList<>();
		}
		for(int i=0; i<M; i++){
			for(int j=0; j<temp[i].size(); j++){
				addArrow(i+1, temp[i].get(j).getTo()+1, temp[i].get(j).getGain());
			}
		}
		for(int i=0; i<N; i++){
			for(int j=0; j<sfg.getEdges()[i].size(); j++){
				addArrow(i+1+M, sfg.getEdges()[i].get(j).getTo()+1+M, sfg.getEdges()[i].get(j).getGain());
			}
		}
		this.visited = new boolean[nodesName.size()] ;
		this.forwardPaths = null ;
		this.individualLoops = null ;
		this.allLoops = null ;
		this.orignal = null ;
		this.deltaM = null ;
	}

	public SFG combine(SFG sfg){
		ArrayList<String> newNodes = new ArrayList<>() ;
		newNodes.addAll(nodesName) ;
		newNodes.addAll(sfg.getNodes()) ;
		int M = nodesName.size() ;
		int N = sfg.getNodes().size() ;
		SFG newSFG = new SFG(newNodes) ;
		for(int i=0; i<M; i++){
			for(int j=0; j<graph[i].size(); j++){
				newSFG.addArrow(i+1, graph[i].get(j).getTo()+1, graph[i].get(j).getGain());
			}
		}
		for(int i=0; i<N; i++){
			for(int j=0; j<sfg.getEdges()[i].size(); j++){
				newSFG.addArrow(i+1+M, sfg.getEdges()[i].get(j).getTo()+1+M, sfg.getEdges()[i].get(j).getGain());
			}
		}
		return newSFG ;
	}

	public String printAllNodes(){
		String st = "" ;
		for(int i=0; i<nodesName.size(); i++){
			st += nodesName.get(i) + "  " ;
		}
		return st ;
	}

	public Complex[] getDeltas(){
		return deltaM;
	}

	public void supressAll(boolean supress){
		this.supressOutputs = supress ;
	}

	public String printAllLoops() {
		if (allLoops == null) {
			constructLoops();
		}
		int level = 1;
		StringBuilder output = new StringBuilder();
		for (ArrayList<Integer> loop : allLoops) {

			output.append("Level " + level).append(" Untouched Loops:\n");
			if (!loop.isEmpty()) {
				int cnt=1;
				for (int i = 0; i < loop.size(); i += level) {
					output.append("Loop #").append(cnt++).append(": ");
					output.append(individualLoops.get(loop.get(i)).getPath());
					Complex gain = individualLoops.get(loop.get(i)).getGain();
					for (int j = 1; j < level; j++) {
						output.append(" , ").append(individualLoops.get(loop.get(i + j)).getPath());
						gain = gain.times(individualLoops.get(loop.get(i+j)).getGain()) ;
					}
					output.append(" (total Gain = ").append(gain);
					output.append(")\n");
				}
			}
			level++;
			output.append("====================================\n");
		}
		return output.toString();
	}

	public String printAllLoops_noGains() {
		if (allLoops == null) {
			constructLoops();
		}
		int level = 1;
		StringBuilder output = new StringBuilder();
		for (ArrayList<Integer> loop : allLoops) {
			output.append("Level " + level).append(" Untouched Loops:\n");
			if (!loop.isEmpty()) {
				int cnt=1;
				for (int i = 0; i < loop.size(); i += level) {
					output.append("Loop #").append(cnt++).append(": ");
					output.append(individualLoops.get(loop.get(i)).getPath());
					for (int j = 1; j < level; j++) {
						output.append(" , ").append(individualLoops.get(loop.get(i + j)).getPath());
					}
					output.append("\n");
				}
			}
			level++;
			output.append("====================================\n");
		}
		return output.toString();
	}

	// this is for compact form. only level 1 loops are printed in a full format.
	public String printAllLoops_compactForm() {
		if (allLoops == null) {
			constructLoops();
		}
		Map<String, String> map = new HashMap<>() ;
		int level = 1;
		StringBuilder output = new StringBuilder();
		for (ArrayList<Integer> loop : allLoops) {
			output.append("Level " + level).append(" Untouched Loops:\n");
			if (!loop.isEmpty()) {
				int cnt=1;
				for (int i = 0; i < loop.size(); i += level) {
					if(level==1){
						output.append("L").append(cnt).append(": ");
						output.append(individualLoops.get(loop.get(i)).getPath());
						map.put(individualLoops.get(loop.get(i)).getPath(), "L"+cnt) ;
					}
					else{
						output.append(map.get(individualLoops.get(loop.get(i)).getPath())) ;
						for (int j = 1; j < level; j++) {
							output.append(" , ");
							output.append(map.get(individualLoops.get(loop.get(i + j)).getPath()));
						}
					}
					output.append("\n");
					cnt++ ;
				}
			}
			level++;
			output.append("====================================\n");
		}
		return output.toString();
	}

	private String getSign(int x) {
		if(x%2 ==0)
			return "+" ;
		else
			return "-" ;
	}

	public String printDelta_compactForm() {
		if (allLoops == null) {
			constructLoops();
		}
		Map<String, String> map = new HashMap<>() ;
		int level = 1;
		StringBuilder delta = new StringBuilder();
		delta.append("Delta = 1") ;
		for (ArrayList<Integer> loop : allLoops) {
			if (!loop.isEmpty()) {
				int cnt=1;
				for (int i = 0; i < loop.size(); i += level) {
					if(level==1){
						delta.append(getSign(level)+"L").append(cnt);
						map.put(individualLoops.get(loop.get(i)).getPath(), "L"+cnt) ;
					}
					else{
						delta.append(getSign(level)+map.get(individualLoops.get(loop.get(i)).getPath())) ;
						for (int j = 1; j < level; j++) {
							delta.append("*");
							delta.append(map.get(individualLoops.get(loop.get(i + j)).getPath()));

						}
					}
					cnt++ ;
				}
			}
			level++;
		}
		delta.append("\n====================================");
		return delta ;
	}

	public Complex computeForwardGain(int src, int dest) {
		if (deltaM == null) {
			deltaM = new Complex[forwardPaths.size()+1];
			int path = 0;

			Complex ans = Complex.ZERO ;
			for (Path p : forwardPaths) {
				deltaM[path+1] = computeDelta(path);
				ans = ans.plus(deltaM[path+1].times(p.getGain())) ;
				path++;
			}
			return ans;
		}else{
			Complex ans = Complex.ZERO ;
			int path = 0 ;
			for (Path p : forwardPaths) {
				ans = ans.plus(deltaM[++path].times(p.getGain())) ;
			}
			return ans;
		}
	}

	public Complex computeForwardGain() {
		if (deltaM == null) {
			deltaM = new Complex[forwardPaths.size()+1];
			int path = 0;

			Complex ans = Complex.ZERO ;
			for (Path p : forwardPaths) {
				deltaM[path+1] = computeDelta(path);
				ans = ans.plus(deltaM[path+1].times(p.getGain())) ;
				path++;
			}
			return ans;
		}else{
			Complex ans = Complex.ZERO ;
			int path = 0 ;
			for (Path p : forwardPaths) {
				ans = ans.plus(deltaM[++path].times(p.getGain())) ;
			}
			return ans;
		}
	}

	public Complex getGain(String nodeSrc, String nodeDest){
		buildForwardPaths(nodeSrc, nodeDest);
		Complex gain = computeForwardGain().divides(computeDelta()) ;
		return gain ;
	}

	public Complex getGain(){
		Complex gain = computeForwardGain().divides(computeDelta()) ;
		return gain ;
	}

	private Complex computeDelta(int num) { // this calculates all the co-factors
		int sign = -1;
		Complex delta = Complex.ONE ;
		if(allLoops==null){
			printAllLoops();
		}
		orignal = new Hashtable<>(); // remove list
		String[] remove = forwardPaths.get(num).getPath().split(" ");
		for (String a : remove) {
			orignal.put(a, true);
		}
		int levels = allLoops.size();
		if(!supressOutputs){
			System.out.println("Delta "+(num+1));
			}
		for (int level = 0; level < levels; level++) {
			ArrayList<Integer> cur = allLoops.get(level);
			Complex brackerGain = Complex.ZERO ;
			for (int i = 0; i < cur.size(); i += (level + 1)) {
				Complex termGain = Complex.ONE ;
				for (int j = 0; j <= level; j++) {
					if (isTouched(individualLoops.get(cur.get(i + j)).getPath()
							.split(" "))) {
						termGain = Complex.ZERO;
						break;
					}else{
						if(!supressOutputs){
							System.out.println("UnTouched : "+individualLoops.get(cur.get(i+j)).getPath());
							}
					}
					termGain = termGain.times(individualLoops.get(cur.get(i + j)).getGain()) ;
				}
				brackerGain = brackerGain.plus(termGain) ;
			}
			delta = delta.plus(brackerGain.times(sign)) ;
			sign *= -1;
		}
		if(!supressOutputs){System.out.println("Delta "+(num+1)+" = "+delta);}
		return delta;
	}

	public Complex computeDelta() {
		constructLoops();
		int sign = -1;
		Complex delta = Complex.ONE ;
		int levels = allLoops.size();
		for (int level = 0; level < levels; level++) {
			ArrayList<Integer> cur = allLoops.get(level);
			Complex bracketGain = Complex.ZERO ;
			for (int i = 0; i < cur.size(); i += (level + 1)) {
				Complex termGain = individualLoops.get(cur.get(i)).getGain();
				for (int j = 1; j <= level; j++) {
					termGain = termGain.times(individualLoops.get(cur.get(i + j)).getGain()) ;
				}
				bracketGain = bracketGain.plus(termGain) ;
			}
			delta = delta.plus(bracketGain.times(sign)) ;
			sign *= -1;
		}
		return deltaM[0]=delta;
	}

	private void constructLoops() {
		if (allLoops == null) {
			allLoops = new ArrayList<>();
			// Start adding the first level of n'th non-touching loops
			ArrayList<Integer> individual;
			constructIndividualLoops();
			allLoops.add(individual = new ArrayList<Integer>());
			for (int i = 0; i < individualLoops.size(); i++) {
				individual.add(i);
			}
			combination(1);
		}

	}

	public void addArrow(int from, int to, Complex gain) { // from/to base 1
		double eps = 1e-8 ;
		if(gain.abs() > eps){
			graph[--from].add(new Edge(--to, gain));
		}
	}

	public void addArrow(int from, int to, double gain_real, double gain_imag) { // from/to base 1
		double eps = 1e-8 ;
		Complex gain = new Complex(gain_real, gain_imag) ;
		if(gain.abs() > eps){
			graph[--from].add(new Edge(--to, gain));
		}
	}

	public void addArrow(String nodeFrom, String nodeTo, Complex gain){
		int from = nodesName.indexOf(nodeFrom) + 1 ;
		int to = nodesName.indexOf(nodeTo) + 1 ;
		double eps = 1e-8 ;
		if(gain != null)
			if(gain.abs() > eps){
				graph[--from].add(new Edge(--to, gain));
			}
	}

	public void addArrow(String nodeFrom, String nodeTo, double gain){
		int from = nodesName.indexOf(nodeFrom) + 1 ;
		int to = nodesName.indexOf(nodeTo) + 1 ;
		if(gain != 0D){
			graph[--from].add(new Edge(--to, new Complex(gain, 0)));
		}
	}

	public void addArrow(String nodeFrom, String nodeTo, double gain_real, double gain_imag){
		int from = nodesName.indexOf(nodeFrom) + 1 ;
		int to = nodesName.indexOf(nodeTo) + 1 ;
		Complex gain = new Complex(gain_real, gain_imag) ;
		double eps = 1e-8 ;
		if(gain.abs() > eps){
			graph[--from].add(new Edge(--to, gain));
		}
	}

	public String printForwardPaths(int src, int dest) {
		if (forwardPaths == null)
			buildForwardPaths(src, dest);
		int i = 1;
		StringBuilder output = new StringBuilder();
		for (Path path : forwardPaths) {
			output.append("Forward Path #").append(i++).append(": ").append(path.getPath());
			output.append(" , Gain = ").append(path.getGain()).append("\n");
		}
		return output.toString();
	}

	public String printForwardPaths() {
		int i = 1;
		StringBuilder output = new StringBuilder();
		for (Path path : forwardPaths) {
			output.append("Forward Path #").append(i++).append(": ").append(path.getPath());
			output.append(" , Gain = ").append(path.getGain()).append("\n");
		}
		return output.toString();
	}

	public String printForwardPaths_noGains() {
		int i = 1;
		StringBuilder output = new StringBuilder();
		for (Path path : forwardPaths) {
			output.append("Forward Path #").append(i++).append(": ").append(path.getPath());
			output.append("\n");
		}
		output.append("====================================");
		return output.toString();
	}

	public void buildForwardPaths(int src, int dest) {
		forwardPaths = new ArrayList<>();
		visited[src-1]=true;
		DFS(src - 1, dest - 1, Complex.ONE, nodesName.get(src-1));
		visited[src-1]=false;
		deltaM = null;
	}

	public void buildForwardPaths(String nodeSrc, String nodeDest) {
		int src = nodesName.indexOf(nodeSrc) + 1 ;
		int dest = nodesName.indexOf(nodeDest) + 1 ;
		forwardPaths = new ArrayList<>();
		visited[src-1] = true;
		DFS(src - 1, dest - 1, Complex.ONE, nodesName.get(src-1));
		visited[src-1] = false;
		deltaM = null;
	}

	private void constructIndividualLoops() {
		if (individualLoops == null) {
			ArrayList<Path> temp = forwardPaths;
			int prev;
			forwardPaths = new ArrayList<>();
			for (int src = 0; src < graph.length; src++) {
				visited[src] = true;
				for (Edge node : graph[src]) {
					visited[node.getTo()] = true;
					prev = forwardPaths.size();
					DFS(node.getTo(),
							src,
							node.getGain(),
							nodesName.get(src) + " "
									+ nodesName.get(node.getTo()));

					visited[node.getTo()] = false;

					if (prev < forwardPaths.size()) // new paths were added
						checkRepeated(prev); // remove repeated paths

				}
				visited[src] = false;
			}

			individualLoops = forwardPaths;
			forwardPaths = temp;

		}
	}

	private void checkRepeated(int prev) {
		Path recent, tPath;
		int PREV = prev;
		Stack<Integer> removeList = new Stack<>(); // repeated paths to be
													// removed
		for (; prev < forwardPaths.size(); prev++) {

			String[] recentPath = (recent = forwardPaths.get(prev)).getPath()
					.split(" ");
			orignal = new Hashtable<>();
			for (int i = 0; i < recentPath.length; i++) {
				orignal.put(recentPath[i], true);
			}

			String[] tempStrings;
			for (int i = 0; i < PREV; i++) {

				tempStrings = (tPath = forwardPaths.get(i)).getPath()
						.split(" ");

//				if (recent.getGain().equals(tPath.getGain()) && isEqual(tempStrings)) {
//					removeList.push(prev);
//					break;
//				}
				double eps = 1e-8 ; // error for declaring two complex numbers to be equal
				if (recent.getGain().divides(tPath.getGain()).minus(1).abs() < eps && isEqual(tempStrings)) {
					removeList.push(prev);
					break;
				}
			}
		}

		while (!removeList.isEmpty()) {
			forwardPaths.remove((int) removeList.pop());
		}
	}

	private boolean isEqual(String[] b) {
		if (orignal.size() != (b.length - 1)) // because "b" has the src node
												// twice
			return false;

		for (int i = 0; i < b.length; i++) {
			if (!orignal.containsKey(b[i]))
				return false;
		}
		return true;
	}

	private void DFS(int cur, int dest, Complex gain, String path) {
		if (cur == dest) {
			if (!path.equals(Integer.toString(dest + 1)))
				forwardPaths.add(new Path(gain, path));
			return;
		}

		int to;
		for (Edge node : graph[cur]) {
			to = node.getTo();

			if (to == dest || !visited[to]) {
				visited[to] = true;
				DFS(to, dest, gain.times(node.getGain()),
						path + " " + nodesName.get(to));
				visited[to] = false;
			}
		}
	}

	private void combination(int level) { // level is in base "1"
		// starting building the (level) non-touching loops
		// from the (level-1) non-touching loops

		if (level > allLoops.size())
			return;

		ArrayList<Integer> currentLevel = allLoops.get(level - 1);
		ArrayList<Integer> zeroLevel = allLoops.get(0);

		int length = allLoops.get(level - 1).size(); // length of the (level)
														// list
		int zeroLenght = zeroLevel.size();
		for (int i = 0; i < length; i += level) {
			// find the index of the last individual loop in the ith combination of the current level
			int index = 0 ;
			if(i>0){
				Path lastElem = individualLoops.get(currentLevel.get(i+level-1)) ;
				for(int m = 0; m<zeroLenght; m++){
					if(individualLoops.get(zeroLevel.get(m)).getPath().equals(lastElem.getPath())){
						index = m ;
						break ;
					}
				}
			}

			for (int k = index+1; k < zeroLenght; k++) {

				boolean touched = false;
				for (int j = 0; j < level; j++) {
					if (isTouched(individualLoops.get(zeroLevel.get(k)), individualLoops.get(currentLevel.get(i + j)))) {
						touched = true;
						break;
					}
				}
				if (!touched) { // if not touched then add this combination
					ArrayList<Integer> nextLevel = new ArrayList<>();
					if (allLoops.size() == level)
						allLoops.add(nextLevel = new ArrayList<Integer>());
					else
						nextLevel = allLoops.get(level);
					// check if this combination of paths were already taken

					for (int j = 0; j < level; j++) {
						nextLevel.add(currentLevel.get(i + j));
					}
					nextLevel.add(zeroLevel.get(k));
				}
			}
		}
		combination(level + 1);
	}

	private boolean isTouched(Path a, Path b) {
		String[] aa = a.getPath().split(" ");
		String[] bb = b.getPath().split(" ");

		orignal = new Hashtable<>();
		for (int i = 0; i < aa.length; i++) {
			orignal.put(aa[i], true);
		}

		for (int i = 0; i < bb.length; i++) {
			if (orignal.containsKey(bb[i]))
				return true;
		}
		return false;
	}

	private boolean isTouched(String[] a) {
		for (int i = 0; i < a.length; i++) {
			if (orignal.containsKey(a[i]))
				return true;
		}
		return false;
	}

}
