import java.util.ArrayList;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

// The last six methods should throw an IllegalArgumentException if one (or both) of the input arguments are invalid teams.

class BaseballElimination {
  private Team[] teams;
  private int[] games;
  private int firstVirtual;
  private int lastVirtual;
  private int leaderId;

  // create a baseball division from given filename in format specified below
  public BaseballElimination(String filename) {
    In in = new In(filename);
    int teamsNum = in.readInt();

    firstVirtual = teamsNum;
    lastVirtual = teamsNum + 1;
    teams = new Team[teamsNum];
    games = new int[(teamsNum * teamsNum - teamsNum) / 2];

    leaderId = 0;
    int id = 0;
    int gameN = 0;
    while (id < teamsNum) {
      String name = in.readString();
      int wins = in.readInt();

      teams[id] = new Team(id, name, wins, in.readInt(), in.readInt());

      if (teams[leaderId].wins < wins) leaderId = id;

      int[] gamesList = in.readAllInts();
      for (int i = id + 1; i < teamsNum; i++) {
        games[gameN++] = gamesList[i];
      }
    }
  }

  private class Team {
    private int id;
    private String name;
    private int wins;
    private int losses;
    private int left;

    public Team(int id, String name, int wins, int losses, int left) {
      this.id = id;
      this.name = name;
      this.wins = wins;
      this.losses = losses;
      this.left = left;
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
    return games[a.id * teams.length + b.id];
  }

  // is given team eliminated?
  public boolean isEliminated(String name) {
    Team current = findTeam(name);
    // Trivial elimination
    if (current.wins + current.left < teams[leaderId].wins) {
      return true;
    }

    int n = teams.length;
    // TODO Optimization: (n-1)-1 nodes won't be used because the current team is disregarded
    EdgeWeightedDigraph digraph = new EdgeWeightedDigraph(n + games.length + 2);

    for (int game = 0; game < n; game++) {
      games[game]
    }


    for (int teamId = 0; teamId < n; teamId++) {
      if (teams[teamId] == current) continue;

      int base = teamId * n;
      for (int i = base; i < n; i++) {
        DirectedEdge edge = new DirectedEdge(firstVirtual, base + i, games[i]);
      }
      DirectedEdge edge = new DirectedEdge(team, opponent, in.readInt());
      digraph.addEdge(edge);
    }
  }

  // subset R of teams that eliminates given team; null if not eliminated
  public Iterable <String> certificateOfElimination(String team) {
    return new ArrayList<>();
  }

  public static void main(String[] args) {
    BaseballElimination division = new BaseballElimination(args[0]);
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
