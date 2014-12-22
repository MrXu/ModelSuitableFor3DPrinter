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

    public ArrayList<stlFace> stlFaces = new ArrayList<stlFace>();
    public ArrayList<VertexGeometric> outerVertexList = null;
    public ArrayList<VertexGeometric> innerVertexList = new ArrayList<VertexGeometric>();

    public void addStlFace(stlFace face){
        this.stlFaces.add(face);
    }
    public void addInnerVertex(VertexGeometric vertex){
        this.innerVertexList.add(vertex);
    }
    public void copyOuterVertexList(ArrayList<VertexGeometric> list){
        this.outerVertexList = list;
    }

    public void writeStl(String filename){

        int loopi = 0;
        int loopj = 0;

        if (stlFaces==null){
            System.err.println("ArrayList of stlFace is null.");
        }
        else {

            Writer writer = null;

            try {
                writer = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(filename), "utf-8"));

                //write stl head
                writer.write("solid shape\n");

                for(loopi=0;loopi<stlFaces.size();loopi++){
                    //write face normal
                    writer.write(" facet normal " + stlFaces.get(loopi).normal.toString() + "\n");
                    //write vertices
                    writer.write("  outer loop\n");
                    for(loopj=0;loopj<3;loopj++){
                        writer.write("   vertex " + stlFaces.get(loopi).VertexList.get(loopj).toString() + "\n");
                    }
                    writer.write("  endloop\n");
                    //write endfacet
                    writer.write(" endfacet\n");
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


    public void createInnerVertexBasedOnNeighbourFace(){
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

    public void createNeighbourFaces(){
        int loopi = 0;
        int loopj = 0;
        for (loopi=0;loopi<stlFaces.size();loopi++){
            for(loopj=0;loopj<stlFaces.get(loopi).VertexList.size();loopj++){
                stlFaces.get(loopi).VertexList.get(loopj).addNeighbourFace(stlFaces.get(loopi));
            }
        }
        for(VertexGeometric vertex:outerVertexList){
            vertex.addinNeighbourVertices();
        }
    }


}
