package stlLoader;

import java.util.ArrayList;
import java.util.Scanner;
import ObjLoader.builder.VertexGeometric;

/**
 * Created with IntelliJ IDEA.
 * User: xuwei
 * Date: 13/1/15
 * Time: 8:48 PM
 * To change this template use File | Settings | File Templates.
 *
 * @ToDo: generic class independent of choice of coordinates
 *
 */
public class quickHull {


    public ArrayList<VertexGeometric> quickHull(ArrayList<VertexGeometric> points){

        //the arraylist of points in convex-hull
        ArrayList<VertexGeometric> convexHull = new ArrayList<VertexGeometric>();

        if (points.size()<3){
            return (ArrayList) points.clone();
        }

        int minPoint = -1;
        int maxPoint = -1;
        int minX = Integer.MIN_VALUE;
        int maxX = Integer.MAX_VALUE;

        for(int i=0;i<points.size();i++){
            if (points.get(i).x<minX){

            }
        }

        return convexHull;
    }


}
