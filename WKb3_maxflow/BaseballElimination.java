import java.util.ArrayList;

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

// The last six methods should throw an IllegalArgumentException if one (or both) of the input arguments are invalid teams.

public class BaseballElimination {
  private final Team[] teams;
  private final int firstVirtual;
  private final int lastVirtual;

  private int leaderId;
  private int expectedMaxFlow;
  private String cached;
  private boolean isEliminatedCached;
  private ArrayList<String> certificateCached;

  // create a baseball division from given filename in format specified below
  public BaseballElimination(String filename) {
    In in = new In(filename);
    int teamsNum = in.readInt();

    firstVirtual = teamsNum;
    lastVirtual = teamsNum + 1;
    teams = new Team[teamsNum];

    leaderId = 0;
    for (int id = 0; id < teamsNum; id++) {
      String name = in.readString();
      int wins = in.readInt();
      int losses = in.readInt();
      int left = in.readInt();

      int[] games = new int[teamsNum];
      for (int i = 0; i < teamsNum; i++) {
        games[i] = in.readInt();
      }

      teams[id] = new Team(id, name, wins, losses, left, games);

      if (teams[leaderId].wins < wins) leaderId = id;
    }
  }

  private class Team {
    private final int id;
    private final String name;
    private final int wins;
    private final int losses;
    private final int left;
    private final int[] games;

    public Team(int id, String name, int wins, int losses, int left, int[] games) {
      this.id = id;
      this.name = name;
      this.wins = wins;
      this.losses = losses;
      this.left = left;
      this.games = games;
    }
  }

  // number of teams
  public int numberOfTeams() {
    return teams.length;
  }

  // all teams
  public Iterable<String> teams() {
    ArrayList<String> names = new ArrayList<>();
    for (Team team : teams) {
      names.add(team.name);
    }
    return names;
  }

  private Team findTeam(String name) {
    for (Team team : teams) {
      if (team.name.equals(name)) {
        return team;
      }
    }
    throw new IllegalArgumentException("Team does not exist");
  }

  // number of wins for given team
  public int wins(String team) {
    return findTeam(team).wins;
  }

  // number of losses for given team
  public int losses(String team) {
    return findTeam(team).losses;
  }

  // number of remaining games for given team
  public int remaining(String team) {
    return findTeam(team).left;
  }

  // number of remaining games between team1 and team2
  public int against(String team1, String team2) {
    Team a = findTeam(team1);
    Team b = findTeam(team2);
    return a.games[b.id];
  }

  private FlowNetwork buildNetwork(Team current) {
    int n = teams.length;
    /* n: teams
     * (n*n - n) / 2: number of unique games
     * 2: virtual nodes
     * n-1: number of games /*/
    FlowNetwork network = new FlowNetwork(n + ((n*n - n)/2) + 2 - (n-1));
    // Games index start
    int g = lastVirtual + 1;
    int bestScore = current.wins + current.left;
    expectedMaxFlow = 0;

    for (int id = 0; id < teams.length; id++) {
      Team team = teams[id];
      if (team == current) continue;

      network.addEdge(new FlowEdge(team.id, lastVirtual, (double) bestScore - team.wins));

      // id+1 to disregard game with self and skip previously added nodes
      for (int opponent = id+1; opponent < teams.length; opponent++) {
        if (opponent == current.id) continue;
        int gameNode = g++;
        expectedMaxFlow += team.games[opponent];
        network.addEdge(new FlowEdge(firstVirtual, gameNode, team.games[opponent]));
        network.addEdge(new FlowEdge(gameNode, team.id, Double.POSITIVE_INFINITY));
        network.addEdge(new FlowEdge(gameNode, opponent, Double.POSITIVE_INFINITY));
      }
    }

    return network;
  }

  private class FordFulkerson {
    private FlowNetwork network;
    private boolean[] marked;
    private FlowEdge[] edgeTo;
    private int maxFlow = 0;

    public FordFulkerson(FlowNetwork network) {
      this.network = network;
      edgeTo = new FlowEdge[network.V()];

      while (findAugmentingPath()) {
        double bottleneck = Double.POSITIVE_INFINITY;
        for (int v = lastVirtual; v != firstVirtual; v = edgeTo[v].other(v)) {
          bottleneck = Math.min(bottleneck, edgeTo[v].residualCapacityTo(v));
        }

        for (int v = lastVirtual; v != firstVirtual; v = edgeTo[v].other(v)) {
          edgeTo[v].addResidualFlowTo(v, bottleneck);
        }

        this.maxFlow += bottleneck;
      }
    }

    private boolean findAugmentingPath() {
      marked = new boolean[network.V()];

      Stack<Integer> stack = new Stack<>();
      stack.push(firstVirtual);
      marked[firstVirtual] = true;

      while (!marked[lastVirtual] && !stack.isEmpty()) {
        int v = stack.pop();

        for (FlowEdge edge : network.adj(v)) {
          int w = edge.other(v);

          if (!marked[w] && edge.residualCapacityTo(w) > 0) {
              marked[w] = true;
              stack.push(w);
              edgeTo[w] = edge;
          }
        }
      }

      return marked[lastVirtual];
    }

    public ArrayList<String> getMinCut() {
      ArrayList<String> list = new ArrayList<>();
      // 1 team was not added to the network
      for (int id = 0; id < teams.length; id++) {
        if (marked[id]) {
          list.add(teams[id].name);
        }
      }
      return list;
    }
  }

  private void runMaxFlow(Team current) {
    FlowNetwork network = buildNetwork(current);
    FordFulkerson ff = new FordFulkerson(network);
    isEliminatedCached = ff.maxFlow != expectedMaxFlow;
    certificateCached = isEliminatedCached ? ff.getMinCut() : null;
    cached = current.name;
  }

  private boolean isTriviallyEliminated(Team current) {
    if (current.wins + current.left < teams[leaderId].wins) {
      cached = current.name;
      isEliminatedCached = true;

      certificateCached = new ArrayList<>();
      certificateCached.add(teams[leaderId].name);

      return true;
    }

    return false;
  }

  // is given team eliminated?
  public boolean isEliminated(String name) {
    if (name.equals(cached)) {
      return isEliminatedCached;
    }

    Team current = findTeam(name);

    if (current.id == leaderId) {
      return false;
    }

    // Eliminated by leader
    if (!isTriviallyEliminated(current)) {
      runMaxFlow(current);
    }

    return isEliminatedCached;
  }

  // subset R of teams that eliminates given team; null if not eliminated
  public Iterable<String> certificateOfElimination(String name) {
    if (name.equals(cached)) {
      return certificateCached;
    }

    Team current = findTeam(name);

    if (current.id == leaderId) {
      return null;
    }

    if (!isTriviallyEliminated(current)) {
      runMaxFlow(current);
    }

    return certificateCached;
  }

  public static void main(String[] args) {
    String file = "/Users/jpenna/Documents/princeton-algs/WK8_maxflow/samples/teams5c.txt";
    BaseballElimination division = new BaseballElimination(file);
    // BaseballElimination division = new BaseballElimination(args[0]);
    for (String team : division.teams()) {
      if (division.isEliminated(team)) {
        StdOut.print(team + " is eliminated by the subset R = { ");
        for (String t : division.certificateOfElimination(team)) {
          StdOut.print(t + " ");
        }
        StdOut.println("}");
      } else {
        StdOut.println(team + " is not eliminated");
      }
    }
  }
}
