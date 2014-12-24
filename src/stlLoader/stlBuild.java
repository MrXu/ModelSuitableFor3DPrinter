package stlLoader;

import ObjLoader.builder.VertexGeometric;

import java.io.*;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: xuwei
 * Date: 19/12/14
 * Time: 4:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class stlBuild {

    public ArrayList<stlFace> outerStlFaces = new ArrayList<stlFace>();
    public ArrayList<stlFace> innerStlFaces = new ArrayList<stlFace>();
    public ArrayList<VertexGeometric> outerVertexList = null;
    public ArrayList<VertexGeometric> innerVertexList = new ArrayList<VertexGeometric>();

    public void addOuterStlFace(stlFace face){
        this.outerStlFaces.add(face);
    }
    public void addInnerStlFace(stlFace face){
        this.innerStlFaces.add(face);
    }
    public void addInnerVertex(VertexGeometric vertex){
        this.innerVertexList.add(vertex);
    }
    public void copyOuterVertexList(ArrayList<VertexGeometric> list){
        this.outerVertexList = list;
    }


    /**
     * write stl file
     * */
    public void writeStl(String filename){

        int loopi = 0;
        int loopj = 0;

        if (outerStlFaces ==null){
            System.err.println("ArrayList of outerStlFace is null.");
        }
        else {

            Writer writer = null;

            try {
                writer = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(filename), "utf-8"));

                //write stl head
                writer.write("solid shape\n");

                for(loopi=0;loopi< outerStlFaces.size();loopi++){
                    //write face normal
                    writer.write(" facet normal " + outerStlFaces.get(loopi).normal.toString() + "\n");
                    //write vertices
                    writer.write("  outer loop\n");
                    for(loopj=0;loopj<3;loopj++){
                        writer.write("   vertex " + outerStlFaces.get(loopi).VertexList.get(loopj).toString() + "\n");
                    }
                    writer.write("  endloop\n");
                    //write endfacet
                    writer.write(" endfacet\n");
                }

                if (innerStlFaces==null){
                    System.err.println("ArrayList of innerStlFace is null.");
                    constructInnerStructure();
                }
                else{
                    for(loopi=0;loopi< innerStlFaces.size();loopi++){
                        //write face normal
                        writer.write(" facet normal " + innerStlFaces.get(loopi).normal.toString() + "\n");
                        //write vertices
                        writer.write("  outer loop\n");
                        for(loopj=0;loopj<3;loopj++){
                            writer.write("   vertex " + innerStlFaces.get(loopi).VertexList.get(loopj).toString() + "\n");
                        }
                        writer.write("  endloop\n");
                        //write endfacet
                        writer.write(" endfacet\n");
                    }
                }



                //write stl endsolid
                writer.write("endsolid");



            } catch (IOException ex) {
                // report
            }
            finally {
                try {
                    writer.close();
                }
                catch (Exception ex)
                {

                }
            }
        }
    }

    /**
     * construct inner structure
     * */
    public void constructInnerStructure(){
        createInnerVertexBasedOnNeighbourFace();
        createInnerFaces();
    }


    /**
     * inner vertices creation based on neighbouring vertices
     * */
    public void createInnerVertexBasedOnNeighbourVertex(){
        int loopi = 0;
        //create a shrunk vertex for every outer vertex
        for (loopi=0;loopi<outerVertexList.size();loopi++){
            //using neighbours vertex
            VertexGeometric newVertex = new VertexGeometric(0,0,0,loopi);
            for(VertexGeometric vertex: outerVertexList.get(loopi).neighbourVertices){
                /**
                 * algorithm based on neighbouring vertices seems to be wrong, need to refer to notes
                 * Consider using neighbouring facets?
                 */

            }
        }
    }

    /**
     * inner vertices creation based on neighbouring faces
     * */
    public void createInnerVertexBasedOnNeighbourFace(){
        int loopi = 0;
        //create a shrunk vertex for every outer vertex
        for (loopi=0;loopi<outerVertexList.size();loopi++){
            //using neighbours vertex
            VertexGeometric newVertex = new VertexGeometric(0,0,0,loopi);
            //copy the outer vertex
            newVertex.copyThisVertex(outerVertexList.get(loopi));
            //new shifting vector
            stlFaceNorm shiftingVector = new stlFaceNorm(0,0,0);
            //construct shifting vector by the sum of neighbouring faces' reversed normal
            for(stlFace neighbourface: outerVertexList.get(loopi).neighbourFaces){
                /**
                 * algorithm based on neighbouring vertices could be wrong if angle is larger than 180
                 * here we use neighbouring facets normals
                 */
                shiftingVector.add(neighbourface.normal.reverse().x,neighbourface.normal.reverse().y,neighbourface.normal.reverse().z);
            }
            /**
             * shift the vertex
             */
            newVertex.shiftByVector(shiftingVector.x,shiftingVector.y,shiftingVector.z);
            //add the new shifted vertex into inner vertex list
            addInnerVertex(newVertex);
        }

    }

    /**
     *create inner faces using inner verteices and the index of outer vertices
     * */
    public void createInnerFaces(){
        for (stlFace outerface:outerStlFaces){
            int loopi = 2;
            ArrayList<VertexGeometric> vertexlist = new ArrayList<VertexGeometric>();
            //iterate the vertexlist of outer face in a REVERSED order so that the face normal changes direction
            for(loopi=(outerface.VertexList.size()-1);loopi>=0;loopi--){
                //get the index of outerface's vertex
                int index = outerface.VertexList.get(loopi).index;
                //add the inner vertex with the same index into the vertexlist
                vertexlist.add(innerVertexList.get(index));
            }
            //create a new facet
            stlFace newInnerFace = new stlFace(vertexlist);
            //add the face into the innerFaces
            addInnerStlFace(newInnerFace);
        }
    }





    /**
     create neighbouring facets for every vertex
     */
    public void createNeighbourFaces(){
        int loopi = 0;
        int loopj = 0;
        for (loopi=0;loopi< outerStlFaces.size();loopi++){
            for(loopj=0;loopj< outerStlFaces.get(loopi).VertexList.size();loopj++){
                outerStlFaces.get(loopi).VertexList.get(loopj).addNeighbourFace(outerStlFaces.get(loopi));
            }
        }
        for(VertexGeometric vertex:outerVertexList){
            vertex.addinNeighbourVertices();
        }
    }


}
