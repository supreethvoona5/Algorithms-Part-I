import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;

import java.util.Scanner;

/**
 * Created by aditp on 5/24/2017.
 */
public class KdTreeDrawer {

    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);
        KdTree kdTree = new KdTree();
        while (in.hasNextLine()) {
            Scanner scan = new Scanner(in.readLine());
            double x = scan.nextDouble();
            double y = scan.nextDouble();
            kdTree.insert(new Point2D(x, y));
        }
        kdTree.draw();
    }

}
