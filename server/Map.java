// class to handle the master copy of the map
// author: Chance Nelson
public class Map {
    private boolean[][] map;              // 2d array of booleans
    private int size;                     // size of the map

        
    // constructor
    // args:
    //      size: size to set the 2d boolean array (only need one since its always a square)
    public Map(int size) {
        map = new boolean[size][size];
        this.size = size;
            
    }
    
    
    // flips the boolean at andex [x, y]
    // args:
    //      x: x coordinate
    //      y: y coordinate
    public void flip(int x, int y) {
        map[x][y] = !map[x][y];
        
    }
    
    
    // gets the current state of the internal 2d array and returns it
    public boolean[][] get_map() {
        return map;
        
    }
    
    
    // gets the size of the map and returns it
    public int get_size() {
        return size;
        
    }
    
}
