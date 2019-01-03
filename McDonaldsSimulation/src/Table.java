import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

public class Table {
   
    private Point location;
    private Dimension dim;
    private Color color;
    private boolean[][] freePlaces;
    private Point[][] locatoinPlaces;
    private final int LATE = 30;
    
    public Table(Point location,Dimension dim, Color color){
        this.location = location;
        this.dim = dim;
        this.color = color;
        freePlaces  = new boolean[dim.height][dim.width/2];
        locatoinPlaces = new Point[dim.height][dim.width/2];
        for (int i = 0; i < freePlaces.length; i++) {
            for (int j = 0; j < freePlaces[i].length; j++) {
                freePlaces[i][j] = true;
                if(!(i%2 == 0))
                    locatoinPlaces[i][j] = new Point(location.x +(2*j), location.y - 2);
                else
                    locatoinPlaces[i][j] = new Point(location.x +(2*j), location.y +dim.height);
            }
        }
    }
    
    public void paint(Graphics g){
        g.setColor(color);
        g.fillRect(location.x*LATE, location.y*LATE, dim.width*LATE, dim.height*LATE);
        g.setColor(Color.BLACK);
        g.drawRect(location.x*LATE, location.y*LATE, dim.width*LATE, dim.height*LATE);
    }
    
    public Point getFirsttFreePlace(){
        for (int i = 0; i < freePlaces.length; i++) {
            for (int j = 0; j < freePlaces[i].length; j++) {
                if(freePlaces[i][j]){
                    freePlaces[i][j] = false;
                    return locatoinPlaces[i][j];
                }
            }
        }
        return null;
    }
    
    public void togglePlace(Point where){
        for (int i = 0; i < freePlaces.length; i++) {
            for (int j = 0; j < freePlaces[i].length; j++) {
                if(locatoinPlaces[i][j].x == where.x && locatoinPlaces[i][j].y == where.y)
                    freePlaces[i][j] = true;
            }
        }
    }
    
    public Point getLocation(){
        return location;
    }
}