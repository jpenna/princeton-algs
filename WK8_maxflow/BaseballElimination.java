import java.util.ArrayList;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.In;

// The last six methods should throw an IllegalArgumentException if one (or both) of the input arguments are invalid teams.

class BaseballElimination {
  private Team[] teams;
  private int firstVirtual;
  private int lastVirtual;
  private int leaderId;
  private int maxFlow;

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

      int[] games = new int[teamsNum];
      for (int i = 0; i < teamsNum; i++) {
        games[i] = in.readInt();
      }

      teams[id] = new Team(id, name, wins, in.readInt(), in.readInt(), games);

      if (teams[leaderId].wins < wins) leaderId = id;
    }
  }

  private class Team {
    private int id;
    private String name;
    private int wins;
    private int losses;
    private int left;
    private int[] games;

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
  public Iterable <String> teams() {
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

  private EdgeWeightedDigraph buildDigraph(Team current) {
    int n = teams.length;
    /* n: teams
     * (n*n - n) / 2: number of unique games
     * 2: virtual nodes */
    EdgeWeightedDigraph digraph = new EdgeWeightedDigraph(n + ((n*n - n)/2) + 2);
    // Games index start
    int g = lastVirtual + 1;
    int bestScore = current.wins + current.left;
    maxFlow = 0;

    for (int id = 0; id < teams.length; id++) {
      Team team = teams[id];
      if (team == current) continue;

      digraph.addEdge(new DirectedEdge(team.id, lastVirtual, bestScore));

      // id+1 to disregard game with self and skip previously added nodes
      for (int opponent = id+1; opponent < teams.length; opponent++) {
        int gameNode = g++;
        maxFlow += team.games[opponent];
        digraph.addEdge(new DirectedEdge(firstVirtual, gameNode, team.games[opponent]));
        digraph.addEdge(new DirectedEdge(gameNode, team.id, Double.POSITIVE_INFINITY));
        digraph.addEdge(new DirectedEdge(gameNode, opponent, Double.POSITIVE_INFINITY));
      }
    }

    return digraph;
  }

  // is given team eliminated?
  public boolean isEliminated(String name) {
    Team current = findTeam(name);
    // Leader is not eliminated
    if (current.id == leaderId) {
      return false;
    }

    // Trivial elimination
    if (current.wins + current.left < teams[leaderId].wins) {
      return true;
    }

    EdgeWeightedDigraph digraph = buildDigraph(current);

    return true;
  }

  // subset R of teams that eliminates given team; null if not eliminated
  public Iterable <String> certificateOfElimination(String team) {
    return new ArrayList<>();
  }

  public static void main(String[] args) {
    String file = "/Users/jpenna/Documents/princeton-algs/WK8_maxflow/samples/teams4.txt";
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
