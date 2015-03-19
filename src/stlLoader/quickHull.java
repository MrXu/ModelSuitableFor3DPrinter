package stlLoader;

import java.util.ArrayList;
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
 * using x,z
 *
 */
public class quickHull {


    public ArrayList<VertexGeometric> getHullPolygon(ArrayList<VertexGeometric> points){

        //the arraylist of points in convex-hull
        ArrayList<VertexGeometric> convexHull = new ArrayList<VertexGeometric>();

        if (points.size()<3){
            return (ArrayList) points.clone();
        }

        int minPoint = -1;
        int maxPoint = -1;
        float minX = Float.MAX_VALUE;
        float maxX = Float.MIN_VALUE;

        //find the right-most and left-most
        for(int i=0;i<points.size();i++){
            if (points.get(i).x<minX){
                minX=points.get(i).x;
                minPoint = i;
            }
            if (points.get(i).x>maxX){
                maxX = points.get(i).x;
                maxPoint = i;
            }
        }

        //get the left-most and right-most vertices
        VertexGeometric A = points.get(minPoint);
        VertexGeometric B = points.get(maxPoint);

        //add A and B to convexHull
        convexHull.add(A);
        convexHull.add(B);

        //remove A and B from points
        points.remove(A);
        points.remove(B);

        //left set of points and right set of points

        ArrayList<VertexGeometric> leftSet = new ArrayList<VertexGeometric>();
        ArrayList<VertexGeometric> rightSet = new ArrayList<VertexGeometric>();

        //assign the points left to leftSet or rightSet
        for (int i=0;i<points.size();i++){
            VertexGeometric p = points.get(i);
            if (pointLocation(A,B,p) == -1){
                leftSet.add(p);
            }
            else{
                rightSet.add(p);
            }
        }

        //recursive call
        hullSet(A,B,rightSet,convexHull);
        hullSet(B,A,leftSet,convexHull);


        return convexHull;
    }


    /**
     * decide the location of point P
     * @param A
     * @param B
     * @param P
     * @return
     */
    public int pointLocation(VertexGeometric A, VertexGeometric B, VertexGeometric P){
        float cp1 = (B.x - A.x)*(P.z - A.z) - (B.z - A.z)*(P.x - A.x);
        return (cp1>0) ? 1:-1;
    }

    /**
     * recursive hullSet function
     * @param A
     * @param B
     * @param set
     * @param hull
     */
    public void hullSet(VertexGeometric A, VertexGeometric B, ArrayList<VertexGeometric> set, ArrayList<VertexGeometric> hull){

        //position of B in hull
        int insertPosition = hull.indexOf(B);
        //if empty set
        if (set.size()==0){
            return;
        }
        //if one element in set
        if (set.size()==1){
            VertexGeometric p = set.get(0);
            set.remove(p);
            hull.add(insertPosition,p);
            return;
        }

        float dist = -Float.MAX_VALUE;
        int furthestPoint = -1;

        //find the furthest point
        for (int i=0;i<set.size();i++){
            VertexGeometric p = set.get(i);
            float distance = distance(A,B,p);
//            System.err.println(distance);
            if (distance > dist){
                dist = distance;
                furthestPoint = i;
//                System.err.println(furthestPoint);
            }
        }

        //remove from set and add to hull
        VertexGeometric P = set.get(furthestPoint);
        set.remove(furthestPoint);
        hull.add(insertPosition,P);

        //determine who's to the left of AP
        ArrayList<VertexGeometric> leftSetAP = new ArrayList<VertexGeometric>();
        for (int i=0;i<set.size();i++){
            VertexGeometric M = set.get(i);
            if (pointLocation(A,P,M)==1){
                leftSetAP.add(M);
            }
        }

        //determine who's to the left of PB
        ArrayList<VertexGeometric> leftSetPB = new ArrayList<VertexGeometric>();
        for (int i=0;i<set.size();i++){
            VertexGeometric M = set.get(i);
            if (pointLocation(P,B,M)==1){
                leftSetPB.add(M);
            }
        }

        //recursive
        hullSet(A,P,leftSetAP,hull);
        hullSet(P,B,leftSetPB,hull);

    }

    /**
     * distance matrix
     * @param A
     * @param B
     * @param P
     * @return
     */
    public float distance(VertexGeometric A, VertexGeometric B, VertexGeometric P){
        float ABx = B.x - A.x;
        float ABz = B.z - A.z;
        float num = ABx*(A.z - P.z) - ABz*(A.x - P.x);
        if (num<0){
            num = -num;
        }
        return num;
    }



}
