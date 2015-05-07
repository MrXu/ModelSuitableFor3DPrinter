package stlLoader;

import ObjLoader.builder.VertexGeometric;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: xuwei
 * Date: 19/12/14
 * Time: 4:15 PM
 * To change this template use File | Settings | File Templates.
 */


public class stlFace {

    public stlFaceNorm normal;
    //use the same set of vertices in obj
    public ArrayList<VertexGeometric> VertexList;

    public stlFace(){

    }

    public stlFace(ArrayList<VertexGeometric> Verteices){
        this.VertexList = Verteices;
        this.normal = calculateNorm();
    }

    //face norm changes constantly when model is rotated or shifted
    public stlFaceNorm getFaceNorm(){
            stlFaceNorm faceNorm = calculateNorm();
            normal = faceNorm;
            return normal;
    }

    public stlFaceNorm calculateNorm(){
        float length;
        //counterclockwise
        VertexGeometric a = VertexList.get(0);
        VertexGeometric b = VertexList.get(1);
        VertexGeometric c = VertexList.get(2);
        //vector ab = b - a
        VertexGeometric ab = new VertexGeometric(b.x-a.x,b.y-a.y,b.z-a.z,-1);
        //vector ac = c - a
        VertexGeometric ac = new VertexGeometric(c.x-a.x,c.y-a.y,c.z-a.z,-1);
        //cross product to get the not-normalized normal
        //a.crossproduct(b) = (a2b3-a3b2,a3b1-a1b3,a1b2-a2b1)
        float x = ab.y*ac.z-ab.z*ac.y;
        float y = ab.z*ac.x-ab.x*ac.z;
        float z = ab.x*ac.y-ab.y*ac.x;

        //length for normalization
        length = (float)Math.sqrt((double)(x*x+y*y+z*z));
        //normalized face normal
        stlFaceNorm faceNorm = new stlFaceNorm(x/length,y/length,z/length);

        return faceNorm;
    }



    public float getFaceArea(){

        VertexGeometric v1 = this.VertexList.get(0);
        VertexGeometric v2 = this.VertexList.get(1);
        VertexGeometric v3 = this.VertexList.get(2);

        stlVector vA = new stlVector(v1.x-v2.x,v1.y-v2.y,v1.z-v2.z);
        stlVector vB = new stlVector(v3.x-v2.x,v3.y-v2.y,v3.z-v2.z);

        //cross product to get area
        float x1 = vA.x;
        float x2 = vA.y;
        float x3 = vA.z;
        float y1 = vB.x;
        float y2 = vB.y;
        float y3 = vB.z;

        float area = (float)Math.sqrt((double)((x2*y3-x3*y2)*(x2*y3-x3*y2) + (x3*y1-x1*y3)*(x3*y1-x1*y3) + (x1*y2-x2*y1)*(x1*y2-x2*y1)))/2;

        return area;
    }


    public stlFace backOfTheFace(){
        ArrayList<VertexGeometric> reversedArray = new ArrayList<VertexGeometric>();
        for (int i=this.VertexList.size()-1;i>=0;i--){
            reversedArray.add(this.VertexList.get(i));
        }
        return new stlFace(reversedArray);
    }


    public String toString(){
        return VertexList.get(0).toString()+"|"+VertexList.get(1).toString()+"|"+VertexList.get(2).toString();
    }

}
