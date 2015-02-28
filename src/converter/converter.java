package converter;

import ObjLoader.builder.Build;
import ObjLoader.builder.VertexGeometric;
import ObjLoader.parser.Parse;
import stlLoader.stlBuild;
import stlLoader.stlFace;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: xuwei.
 */
public class converter {

    public static void main(String[] args){

        String filename;
        String stlfilename;
        String innerStlFileName;
        int loopi=0;

        System.out.println("Welcome to the 3D converter!");

        filename = "/Users/xuwei/JavaProjects/modelconverter/objfiles/wheel/wheel.obj";

        stlfilename = "/Users/xuwei/JavaProjects/modelconverter/objfiles/wheel/wheel.stl";
        innerStlFileName = "/Users/xuwei/JavaProjects/modelconverter/objfiles/wheel/innerwheel.stl";

        System.err.println("LOADING FILE " + filename);
        try {
            Build builder = new Build();
            Parse obj = new Parse(builder, filename);

            //initialize stl builder
            stlBuild stlbuilder = new stlBuild();
            //copy vertexGeometrics list
            stlbuilder.copyOuterVertexList(builder.verticesG);

            // print object name
//            System.out.println(builder.objectName);


            /**
             * STEP 1:
             * shifting the obj objects to be positive
             * calculating the shifting vector
             * */
            VertexGeometric shiftingVector = new VertexGeometric(0,0,0,-1);
            for(loopi=0;loopi<builder.verticesG.size();loopi++){
                if (builder.verticesG.get(loopi).x<shiftingVector.x){
                    shiftingVector.x = builder.verticesG.get(loopi).x;
                }
                if (builder.verticesG.get(loopi).y<shiftingVector.y){
                    shiftingVector.y = builder.verticesG.get(loopi).y;
                }
                if (builder.verticesG.get(loopi).z<shiftingVector.z){
                    shiftingVector.z = builder.verticesG.get(loopi).z;
                }
            }
            System.out.println(shiftingVector.toString());

            // shift all vertices to be in the positive space
            System.out.println("*****"+builder.verticesG.size()+" vertices *****");
            for(loopi=0;loopi<builder.verticesG.size();loopi++){
//                System.out.println(builder.verticesG.get(loopi).toString());
                builder.verticesG.get(loopi).x = builder.verticesG.get(loopi).x - shiftingVector.x;
                builder.verticesG.get(loopi).y = builder.verticesG.get(loopi).y - shiftingVector.y;
                builder.verticesG.get(loopi).z = builder.verticesG.get(loopi).z - shiftingVector.z;

                //test index
                System.out.println(builder.verticesG.get(loopi).index);
            }
            // print vertices normals
            System.out.println("*****"+builder.verticesN.size()+" vertices normals *****");
//            for(loopi=0;loopi<builder.verticesN.size();loopi++){
//                System.out.println(builder.verticesN.get(loopi).toString());
//            }

            System.out.println("*****"+builder.faces.size()+" faces *****");


            /**
             * STEP 2:
             * convert obj objects to stl objects by creating stlFaces for outerStlFaces
             * */
            for(loopi=0;loopi<builder.faces.size();loopi++){

                //System.out.println(builder.faces.get(loopi).toString());

                // obj objects converted to stl objects
                int vertexLoopi = 0;
                ArrayList<VertexGeometric> VertexList = new ArrayList<VertexGeometric>();
                for(vertexLoopi=0;vertexLoopi<3;vertexLoopi++){
                    VertexList.add(builder.faces.get(loopi).vertices.get(vertexLoopi).v);
                }
                stlFace tempFace = new stlFace(VertexList);
//                System.out.println(tempFace.toString());
                stlbuilder.addOuterStlFace(tempFace);

            }

            /**
             * STEP 3:
             * select the base surface
             */
            stlFace baseFace = stlbuilder.outerStlFaces.get(0);

            /**
             * STEP 4:
             * stlbuilder process
             */
            stlbuilder.process(baseFace);

            /**
             * STEP 5:
             * write stl files
             */
            //write stl file
            stlbuilder.writeStl(stlfilename);
            //write inner stl file
            stlbuilder.writeInnerStl(innerStlFileName);



            /**
             * testing purpose, to be deleted
             */
//            System.out.println("------- center of mass -------");
//            System.out.println(stlbuilder.calculateMassCenterByPyramid());
//
//            System.out.println("------- list of vertices on the plate -------");
//            stlFace theFace = stlbuilder.outerStlFaces.get(0);
//            System.out.println(stlbuilder.findVerticesOnPlate(theFace));
//            System.out.println(theFace.VertexList);
//            System.out.println("------- rotateing model ---------");
//            stlbuilder.rotateModelSurface(theFace);
//            System.out.println("------- shifting model ---------");
//            stlbuilder.shiftToPositiveRegion();
//            System.out.println("------- base surface ---------");
//            System.out.println(theFace);
//            System.out.println("------- test IsInsidePolygon ---------");
//            System.out.println(stlbuilder.testIsInsidePolygon());
//            System.out.println(stlbuilder.IsMassCenterInsideBaseFace(theFace));
//            System.out.println("------- writing stl files ---------");
//            //write stl file
//            stlbuilder.writeStl(stlfilename);
//            //write inner stl file
//            stlbuilder.writeInnerStl(innerStlFileName);





        } catch (java.io.FileNotFoundException e) {
            System.err.println("Exception loading object!  e=" + e);
            e.printStackTrace();
        } catch (java.io.IOException e) {
            System.err.println("Exception loading object!  e=" + e);
            e.printStackTrace();
        }




    }

}
