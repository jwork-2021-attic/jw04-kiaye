package world;

import asciiPanel.AsciiPanel;

public class Dfs {
    boolean found;
    World world;
    int startx;
    int starty;
    int endx;
    int endy;
    point end;
    boolean[][] visited;

    public Dfs(World world, int startx, int starty, int endx, int endy){
        this.world = world;
        this.startx = startx;
        this.starty = starty;
        this.endx = endx;
        this.endy = endy;
        this.visited = new boolean[world.width() + 1][world.height() + 1];
        for (boolean[] vis:visited){
            for (boolean vi : vis){
                vi = false;
            }
        }
    }
    public class point{
        int x;
        int y; 
        boolean visited;
        point pre;
        public point(int x, int y){
            this.x = x;
            this.y = y;
            this.visited = false;
        }
        public void setpre(point i){
            this.pre = i;
        }

        public int getx(){
            return this.x;
        }

        public int gety(){
            return this.y;
        }
    }

    public void dfs(){
        found = false;
        point start = new point(startx, starty);
        start.setpre(null);
        redfs(start);
        //findpath();
    }

    public void redfs(point i){
        if (found) return;
        visited[i.x][i.y] = true;
        if (i.x == endx && i.y == endy){
            this.end = i;
            found = true;
            return;
        }
        if (i.x > 0){
            point j = new point(i.x-1, i.y);
            j.setpre(i);
            if (world.tile(i.x-1,i.y).isGround() && !visited[j.x][j.y]){
                redfs(j);
            }
        }
        if (i.x < world.width() - 1){
            point k = new point(i.x+1, i.y);
            k.setpre(i);
            if (world.tile(i.x+1,i.y).isGround() && !visited[k.x][k.y]){
                redfs(k);
            }
        }
        if (i.y > 0){
            point s = new point(i.x, i.y-1);
            s.setpre(i);
            if (world.tile(i.x,i.y-1).isGround() && !visited[s.x][s.y]){
                redfs(s);
            }
        }
        if (i.y < world.height() - 1){
            point t = new point(i.x, i.y+1);
            t.setpre(i);
            if (world.tile(i.x,i.y+1).isGround() && !visited[t.x][t.y]){
                redfs(t);
            }
        }
    }

    public point findpath(){
        point k = this.end;
        point next = k;
        while (k.pre != null){
            Path path = new Path(world, (char)25, AsciiPanel.green);
            path.setX(k.x);
            path.setY(k.y);
            this.world.addpath2(path);
            next = k;
            k = k.pre;
        }
        world.removePath2(world.path2(endx,endy));
        return next;
    }
}