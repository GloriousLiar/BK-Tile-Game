import java.io.*;
import java.util.*;
public class TileGame
{
	public static Tile[][] tiles = new Tile[][]
	{
		{new Tile(3), new Tile(6), new Tile(1), new Tile(1)},
		{new Tile(4), new Tile(7), new Tile(8), new Tile(2)},
		{new Tile(5), new Tile(4), new Tile(3), new Tile(5)},
		{new Tile(7), new Tile(8), new Tile(2), new Tile(6)}
	};
	public static Map<Point,Tile> map;
	public static final Point start = new Point(1.5f,-1f);
	public static ArrayList<String> sols = new ArrayList<String>();
	public static void main(String[] sArgs) throws IOException
	{
		map = new HashMap<Point,Tile>();
		for(int i=0; i<4; ++i)
			for(int j=0; j<4; ++j)
				map.put(new Point(i,j),tiles[i][j]);
		solve("",start,0,false,null);
		Collections.sort(sols,new SolComparator());
		PrintWriter oPrint = new PrintWriter(new File("sols.txt"));
		for(int i=0; i<100; ++i)
			oPrint.println(sols.get(i));
		oPrint.close();
	}
	public static void solve(String s,Point pos,float dist, boolean pair, Tile prev)
	{
		if(check())
		{ 
			dist+=pos.dist(new Point(1.5f,1.5f)) + new Point(1.5f,1.5f).dist(start);
			sols.add(s+" "+dist); return; }
		if(pos.equals(start))
		{
			tiles[0][0].flipped=true;
			solve(s+3,new Point(0,0),dist+pos.dist(new Point(0,0)),false, tiles[0][0]);
			tiles[0][0].flipped=false;
			tiles[0][1].flipped=true;
			solve(s+6,new Point(0,1),dist+pos.dist(new Point(0,1)),false, tiles[0][1]);
			tiles[0][1].flipped=false;
			tiles[0][2].flipped=true;
			solve(s+1,new Point(0,2),dist+pos.dist(new Point(0,2)),false, tiles[0][2]);
			tiles[0][2].flipped=false;
			tiles[0][3].flipped=true;
			solve(s+1,new Point(0,3),dist+pos.dist(new Point(0,3)),false, tiles[0][3]);
			tiles[0][3].flipped=false;
		}
		else
		{
			if(!pair)
			{
				outer:
				for(int i=0; i<4; ++i)
				{
					for(int j=0; j<4; ++j)
					{
						Tile temp = tiles[i][j];
						if(temp.num == prev.num && !temp.equals(prev))
						{
							tiles[i][j].flipped=true;
							solve(s,new Point(i,j),dist+pos.dist(new Point(i,j)),true, null);
							tiles[i][j].flipped=false;
							break outer;
						}
					}
				}
			}
			else
			{
				for(int i=0; i<4; ++i)
				{
					for(int j=0; j<4; ++j)
					{
						if(tiles[i][j].flipped) continue;
						Tile temp = tiles[i][j];
						tiles[i][j].flipped=true;
						solve(s+temp.num,new Point(i,j),dist+pos.dist(new Point(i,j)),false, tiles[i][j]);
						tiles[i][j].flipped=false;
					}
				}
			}
		}
	}
	public static boolean check()
	{
		for(Tile[] tt: tiles)
		{
			for(Tile t: tt)
			{
				if(t.flipped == false) return false;
			}
		}
		return true;
	}
}

class Tile
{
	int num;
	boolean flipped;
	public Tile(int i)
	{ 
		num = i; 
	}
	
}

class Point
{
	float x,y;
	public Point(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	public boolean equals(Point other)
	{
		return this.x == other.x && this.y == other.y;
	}
	public float dist(Point other)
	{
		return (float)(Math.sqrt((Math.pow(x-other.x,2)+(Math.pow(y-other.y,2)))));
	}
}

class SolComparator implements Comparator<String>
{
	public int compare(String s1, String s2)
	{
		s1 = s1.substring(9);
		s2 = s2.substring(9);
		return (int)((Double.parseDouble(s1)-Double.parseDouble(s2))*100000);
	}
}