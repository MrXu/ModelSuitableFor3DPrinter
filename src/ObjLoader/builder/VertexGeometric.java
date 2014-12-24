package ObjLoader.builder;

// Written by Sean R. Owens, sean at guild dot net, released to the
// public domain. Share and enjoy. Since some people argue that it is
// impossible to release software to the public domain, you are also free
// to use this code under any version of the GPL, LPGL, Apache, or BSD
// licenses, or contact me for use of another license.

import stlLoader.stlFace;

import java.util.*;

public class VertexGeometric {

    public float x = 0;
    public float y = 0;
    public float z = 0;
    //the array index in the vertex list
    public int index = 0;
    /**
     * LinkedHashSet of neighbouring facets
     * added by stlbuilder class
     **/
    public LinkedHashSet<stlFace> neighbourFaces = new LinkedHashSet<stlFace>();
    //the linkedhashset of neighbours
    public LinkedHashSet<VertexGeometric> neighbourVertices = new LinkedHashSet<VertexGeometric>();

    public VertexGeometric(float x, float y, float z, int ArrayIndex) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.index = ArrayIndex;
    }

    public void addNeighbourVertex(VertexGeometric vertex){
        neighbourVertices.add(vertex);
    }

    public void addNeighbourFace(stlFace face){
        neighbourFaces.add(face);
    }

    public void shiftByVector(float x,float y,float z){
        this.x += x;
        this.y += y;
        this.z += z;
    }

    public void copyThisVertex(VertexGeometric copiedVertex){
        this.x = copiedVertex.x;
        this.y = copiedVertex.y;
        this.z = copiedVertex.z;
        this.index = copiedVertex.index;
    }

    public String toString() {
        if (null == this) {
            return "null";
        } else {
            return x + " " + y + " " + z; //fixed stl format
        }
    }

    public void addinNeighbourVertices(){
        for(stlFace face:neighbourFaces){
            for(VertexGeometric vertex:face.VertexList){
                addNeighbourVertex(vertex);
            }
        }
        neighbourVertices.remove(this);
    }
}