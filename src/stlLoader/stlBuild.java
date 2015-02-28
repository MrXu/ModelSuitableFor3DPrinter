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
    public float thickness = 0.01f;
    public float minThicknessUnit = 0.005f;

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



    public void process(stlFace baseFace){
        /**
         * STEP 1:
         * create neighbouring facets for every vertex
         */
        createNeighbourFaces();

        /**
         * STEP 2:
         * construct inner structure
         */
        constructInnerStructure();

        /**
         * STEP 4:
         * rotate the model based on the selected basesurface
         */
        rotateModelSurface(baseFace);

        /**
         * STEP 5:
         * shift the model to positive region
         */
        shiftToPositiveRegion();

        /**
         * STEP 6:
         * judge whether mass center is inside the polygon
         * 1) if yes, do nothing
         * 2) if no, modify the distance of inner vertices shifting
         */
        if (!IsMassCenterInsideBaseFace(baseFace)){
            adjustThicknessContinuously(baseFace);
            System.out.println("Finish adjusting thickness.");
        }


    }




    /**
     * write stl file
     * */
    public void writeStl(String filename){

        int loopi = 0;
        int loopj = 0;

        if (outerStlFaces.isEmpty()){
            System.err.println("ArrayList of outerStlFace is null.");
        }
        else {
            /**
             * if innerstlfaces is empty, construct one
             * */
            if (innerStlFaces.isEmpty()){
                System.err.println("ArrayList of innerStlFace is null.");
                constructInnerStructure();
            }

            Writer writer = null;

            try {
                writer = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(filename), "utf-8"));

                //write stl head
                writer.write("solid shape\n");

                for(loopi=0;loopi< outerStlFaces.size();loopi++){
                    /**
                     * write outer face
                     * */
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
                    /**
                     * write co-responding inner face
                     * */
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
     * write stl file for inner model
     * */
    public void writeInnerStl(String filename){

        int loopi = 0;
        int loopj = 0;



        Writer writer = null;

        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(filename), "utf-8"));

            //write stl head
            writer.write("solid shape\n");

            if (innerStlFaces.isEmpty()){
                System.err.println("ArrayList of innerStlFace is null.");
                constructInnerStructureForTest();
            }

            System.err.println("Number of outer vertices (writer): "+outerVertexList.size());
            System.err.println(innerStlFaces);
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



    /**
     * construct inner structure
     * */
    public void constructInnerStructure(){
        createInnerVertexBasedOnNeighbourFace();
        createInnerFaces();
    }

    /**
     * construct inner structure for test
     * */
    public void constructInnerStructureForTest(){
        createInnerVertexBasedOnNeighbourFace();
        createInnerFacesForTest();
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
        //clear the innervertexlist
        ArrayList<VertexGeometric> newinnerVertexList = new ArrayList<VertexGeometric>();
        this.innerVertexList = newinnerVertexList;

        int loopi = 0;
        float length = 0;
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
            //normalize the vector
            length = (float)Math.sqrt((double)(shiftingVector.x*shiftingVector.x+shiftingVector.y*shiftingVector.y+shiftingVector.z*shiftingVector.z));
            shiftingVector.dividedBy(length);
            //assign thickness of the layer
            shiftingVector.multiple(thickness);
            /**
             * shift the vertex
             */
            newVertex.shiftByVector(shiftingVector.x,shiftingVector.y,shiftingVector.z);
            //add the new shifted vertex into inner vertex list
            addInnerVertex(newVertex);
        }
        System.out.println("Number of vertices in innerVertexList: "+innerVertexList.size());
        System.out.println("Number of vertices in outerVertexList: "+outerVertexList.size());
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
     *create inner faces using inner verteices and the index of outer vertices
     * */
    public void createInnerFacesForTest(){
        for (stlFace outerface:outerStlFaces){
            int loopi = 2;
            ArrayList<VertexGeometric> vertexlist = new ArrayList<VertexGeometric>();
            //iterate the vertexlist of outer face in a ORIGINAL order so that the face normal changes direction
            for(VertexGeometric v:outerface.VertexList){
                //get the index of outerface's vertex
                int index = v.index;
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


    /**
     * calculate the center of mass based on triangular pyramid
     */
    public VertexGeometric calculateMassCenterByPyramid(){

        //create a combined arraylist of stlfaces
        ArrayList<stlFace> stlFaces = new ArrayList<stlFace>(this.innerStlFaces);
        stlFaces.addAll(this.outerStlFaces);

        //initiate the center of mass
        VertexGeometric massCenter = new VertexGeometric(-1,-1,-1,-1);
        //initiate the total volume
        float totalVolume = 0;
        //initiate the current volume
        float currentVolume = 0;

        if (this.innerStlFaces.isEmpty()){
            System.err.println("Inner stlFaces has not been calculated.");
            return massCenter;
        }
        else{
            for(stlFace face:stlFaces){
                VertexGeometric p1 = face.VertexList.get(0);
                VertexGeometric p2 = face.VertexList.get(1);
                VertexGeometric p3 = face.VertexList.get(2);
                //calculate current volume based on Tetrahedron
                currentVolume = (p1.x*p2.y*p3.z-p1.x*p3.y*p2.z-p2.x*p1.y*p3.z+p2.x*p3.y+p1.z+p3.x*p1.y*p2.z-p3.x*p2.y*p1.z)/6;
                //accumulate total volume
                totalVolume += currentVolume;
                //accumulate mass center coordinates
                massCenter.x += ((p1.x+p2.x+p3.x)/4)*currentVolume;
                massCenter.y += ((p1.y+p2.y+p3.y)/4)*currentVolume;
                massCenter.z += ((p1.z+p2.z+p3.z)/4)*currentVolume;
            }
            massCenter.x = massCenter.x/totalVolume;
            massCenter.y = massCenter.y/totalVolume;
            massCenter.z = massCenter.z/totalVolume;
            return massCenter;
        }


    }


    /**
     * find vertices on the same plate
     * algo: lines on a plate is always perpendicular to the normal of the plate
     */
    public ArrayList<VertexGeometric> findVerticesOnPlate(stlFace baseFace){

        //the list of vertices to return
        ArrayList<VertexGeometric> verticesList = new ArrayList<VertexGeometric>();
        //the base point to construct vector
        VertexGeometric basePoint = baseFace.VertexList.get(0);
        //the base normal to compute dot product
        stlFaceNorm baseNorm = baseFace.calculateNorm();

        //iterate through the list of vertices to check
        float x=0;
        float y=0;
        float z=0;
        float dotproduct = 1;
        for(VertexGeometric vertex:this.outerVertexList){
            //the computed vector (vertex - baseVertex)
            x = vertex.x - basePoint.x;
            y = vertex.y - basePoint.y;
            z = vertex.z - basePoint.z;

            //check if cross dot product is zero
            dotproduct = x*baseNorm.x+y*baseNorm.y+z*baseNorm.z;
            if (dotproduct == 0){
                verticesList.add(vertex);
            }

        }

        return verticesList;

    }

    /**
     * rotate model based on the selected surface
     */
    public void rotateModelSurface(stlFace baseFace){

        float tempX;
        float tempY;
        float tempZ;
        float cosAroundX;
        float sinAroundX;
        float cosAroundZ;
        float sinAroundZ;

        stlFaceNorm baseNorm = baseFace.getFaceNorm();
        Rotation modelRotation = new Rotation(baseNorm);
        cosAroundX = modelRotation.cosAroundX;
        sinAroundX = modelRotation.sinAroundX;
        cosAroundZ = modelRotation.cosAroundZ;
        sinAroundZ = modelRotation.sinAroundZ;

        //testing
        System.out.println(baseNorm);
        System.out.println(cosAroundX);
        System.out.println(sinAroundX);
        System.out.println(cosAroundZ);
        System.out.println(sinAroundZ);


        if (baseNorm.x==0 & baseNorm.z==0){
            return;
        }
        else{
            tempX = tempY = tempZ = 0;

            //rotate outerVertexList
            for (VertexGeometric vertex:this.outerVertexList){

                //rotate around x axis
                tempX = vertex.x;
                tempY = vertex.y*cosAroundX - vertex.z*sinAroundX;
                tempZ = vertex.y*sinAroundX + vertex.z*cosAroundX;
                vertex.x = tempX;
                vertex.y = tempY;
                vertex.z = tempZ;

                //rotate around z axis
                tempX = vertex.x*cosAroundZ - vertex.y*sinAroundZ;
                tempY = vertex.x*sinAroundZ + vertex.y*cosAroundZ;
                tempZ = vertex.z;
                vertex.x = tempX;
                vertex.y = tempY;
                vertex.z = tempZ;

            }

            //rotate innerVertexList
            for (VertexGeometric vertex:this.innerVertexList){

                //rotate around x axis
                tempX = vertex.x;
                tempY = vertex.y*cosAroundX - vertex.z*sinAroundX;
                tempZ = vertex.y*sinAroundX + vertex.z*cosAroundX;
                vertex.x = tempX;
                vertex.y = tempY;
                vertex.z = tempZ;

                //rotate around z axis
                tempX = vertex.x*cosAroundZ - vertex.y*sinAroundZ;
                tempY = vertex.x*sinAroundZ + vertex.y*cosAroundZ;
                tempZ = vertex.z;
                vertex.x = tempX;
                vertex.y = tempY;
                vertex.z = tempZ;
            }

        }

    }

    /**
     * shift the model to the positive region
     */
    public void shiftToPositiveRegion(){

        VertexGeometric shiftingVector = new VertexGeometric(0,0,0,-1);

        //compute the vector to shift
        for (VertexGeometric vertex : this.outerVertexList){
            if (vertex.x<shiftingVector.x){
                shiftingVector.x = vertex.x;
            }
            if (vertex.y<shiftingVector.y){
                shiftingVector.y = vertex.y;
            }
            if (vertex.z<shiftingVector.z){
                shiftingVector.z = vertex.z;
            }
        }

        //print
        System.out.println(shiftingVector);

        //shift all vertices
        for (VertexGeometric outvertex:this.outerVertexList){
            outvertex.x = outvertex.x - shiftingVector.x;
            outvertex.y = outvertex.y - shiftingVector.y;
            outvertex.z = outvertex.z - shiftingVector.z;

            //testing
            System.out.println(outvertex);

        }
        for (VertexGeometric invertex:this.innerVertexList){
            invertex.x = invertex.x - shiftingVector.x;
            invertex.y = invertex.y - shiftingVector.y;
            invertex.z = invertex.z - shiftingVector.z;

            //testing
            System.out.println(invertex);
        }

    }


    /**
     * if vertice is inside the polygon by x,z
     * @param v
     * @param vlist
     * @return
     */
    public boolean IsInsidePolygon(VertexGeometric v,ArrayList<VertexGeometric> vlist){

        int i;
        int j;
        boolean result = false;

        //loop through vlist
        for(i=0,j=vlist.size()-1;i<vlist.size();j=i++){
            if ((vlist.get(i).z > v.z) != (vlist.get(j).z > v.z) &&
                    (v.x < (vlist.get(j).x - vlist.get(i).x) * (v.z - vlist.get(i).z) / (vlist.get(j).z-vlist.get(i).z) + vlist.get(i).x)) {
                result = !result;
            }
        }

        return result;
    }
    public boolean testIsInsidePolygon(){

        ArrayList<VertexGeometric> vlist = new ArrayList<VertexGeometric>();
        VertexGeometric v = new VertexGeometric(4,0,4,1);

        vlist.add(new VertexGeometric(0,0,0,2));
        vlist.add(new VertexGeometric(0,0,3,2));
        vlist.add(new VertexGeometric(3,0,3,2));
        vlist.add(new VertexGeometric(3,0,0,2));

        return IsInsidePolygon(v,vlist);

    }


    /**
     * check if mass center is inside the hull polygon of base surface
     * @param baseface
     * @return
     */
    public boolean IsMassCenterInsideBaseFace(stlFace baseface){

        //initiate the center of mass
        VertexGeometric massCenter = new VertexGeometric(-1,-1,-1,-1);
        massCenter = calculateMassCenterByPyramid();

        //base surface hull
        ArrayList<VertexGeometric> vlist = new ArrayList<VertexGeometric>();
        quickHull hullFunction = new quickHull();
        //call find vertices on the same plate to get the list of vertices
        vlist = hullFunction.getHullPolygon(findVerticesOnPlate(baseface));
//        System.err.println("-------------list of vertices in hull polygon-------------");
//        System.err.println(baseface.VertexList);
//        System.err.println(findVerticesOnPlate(baseface));
//        System.err.println(vlist);

        return IsInsidePolygon(massCenter,vlist);

    }







    /**
     * minimum distance of a point to a line segment, based on x and z.
     * return a vector pointing from the vertex to the nearest point on the line segment
     * @param v
     * @param head
     * @param tail
     * @return
     */
    private stlVector minimumDistance(VertexGeometric v,VertexGeometric head,VertexGeometric tail){
        float dist;

        //square of line segment length
        float lineLengthSqr = (head.x-tail.x)*(head.x-tail.x) + (head.z-tail.z)*(head.z-tail.z);
        //head and tail overlaps
        if(lineLengthSqr==0){
//            dist = (float)Math.sqrt((double)((v.x-tail.x)*(v.x-tail.x) + (v.z-tail.z)*(v.z-tail.z)));
            return new stlVector(v,head);
        }

        //head and tail does not overlap
        stlVector vecA = new stlVector(head,v);
        stlVector vecB = new stlVector(head,tail);
        float dotProduct = vecA.dotProductXZ(vecB);
        float projectionOverLength = dotProduct/lineLengthSqr;

        //beyond head end
        if (projectionOverLength<0.0){
            //distance in terms of x,z dimension
            return new stlVector(v,head);
            //return (float)Math.sqrt((double)((v.x-head.x)*(v.x-head.x) + (v.z-head.z)*(v.z-head.z)));
        }
        //beyond tail end
        else if (projectionOverLength>1.0){
            return new stlVector(v,tail);
//            return (float)Math.sqrt((double)((v.x-tail.x)*(v.x-tail.x) + (v.z-tail.z)*(v.z-tail.z)));
        }
        //falls in between
        else{
            //calculate projection point on the line segment
            VertexGeometric projectionPoint = new VertexGeometric(-1,-1,-1,-1);
            projectionPoint.x = projectionOverLength*vecB.x + head.x;
            projectionPoint.y = projectionOverLength*vecB.y + head.y;
            projectionPoint.z = projectionOverLength*vecB.z + head.z;

            return new stlVector(v,projectionPoint);
            //calculate distance in terms of x,z dimension
//            return (float)Math.sqrt((double)((v.x-projectionPoint.x)*(v.x-projectionPoint.x) + (v.z-projectionPoint.z)*(v.z-projectionPoint.z)));
        }

    }
    //solely for testing purpose, to be deleted
    private void  minimumDistanceTest(){
        VertexGeometric v = new VertexGeometric(-1,0,2,-1);
        VertexGeometric head = new VertexGeometric(0,0,0,-1);
        VertexGeometric tail = new VertexGeometric(4,0,0,-1);

        System.out.println("------ test minimum distance from a point to line segment ------");
        System.out.println(minimumDistance(v, head, tail).toString());
    }







    /**
     * continuously adjust the thickness of model
     * to ensure mass center is inside the hull polygon of base surface.
     * while maintaining the integrity of model (thickness cannot be smaller than minimum thickness unit)
     * @param baseface
     */
    public void adjustThicknessContinuously(stlFace baseface){
        boolean insidePolygon = IsMassCenterInsideBaseFace(baseface);
        // set the movingThickness
        float movingThickness = this.minThicknessUnit;
        // limit the number of adjustment to make sure the model is still safe
        int adjustLimit = (int)((this.thickness-this.minThicknessUnit)/this.minThicknessUnit);
        int adjustCount = 0;

        while(!insidePolygon){
            if (adjustCount>=adjustLimit){
                System.err.println("Unable to adjust! Please select another surface to be the base surface");
                break;
            }
            //adjust thickness
            adjustThickness(baseface,movingThickness);
            //increment adjust count
            adjustCount = adjustCount+1;
            //re-evaluate whether mass center is inside the hull polygon of base surface
            insidePolygon = IsMassCenterInsideBaseFace(baseface);
        }
    }
    /**
     * adjust the thickness of model
     * to ensure mass center is inside hull polygon
     * @param baseface
     */
    public void adjustThickness(stlFace baseface, float movingThickness){
        //get mass center
        VertexGeometric massCenter = calculateMassCenterByPyramid();

        //get list hull polygon
        quickHull hullFunction = new quickHull();
        ArrayList<VertexGeometric> vlist = hullFunction.getHullPolygon(findVerticesOnPlate(baseface));
        System.out.println(vlist);

        //initialize shifting vector with random value
        stlVector shiftingVector = new stlVector(vlist.get(0),vlist.get(1));
        float minimumDistance = Float.MAX_VALUE;
        stlVector tempVector;

        /**
         * find the shifting vector with shortest distance
         */
        //loop through to find the best vector to move
        //the sequence is assumed to be
        for(int i=0;i<vlist.size()-1;i++){
            tempVector = minimumDistance(massCenter,vlist.get(i),vlist.get(i+1));
            //find the vector with shortest length
            if (minimumDistance>tempVector.getLengthXZ()){
                minimumDistance = tempVector.getLengthXZ();
                shiftingVector = tempVector;
            }
        }
        //compare with last to first
        tempVector = minimumDistance(massCenter,vlist.get(vlist.size()-1),vlist.get(0));
        if (minimumDistance>tempVector.getLengthXZ()){
            shiftingVector = tempVector;
        }

        System.out.println("The shifting vector: "+shiftingVector.toString());

        /**
         * based on which side the vertices are,
         * shifting the inner vertices closer to outer vertices
         */
        //determine which side the inner vertices are relative to the plane
        VertexGeometric pointOnPlane = shiftingVector.moveVertxXZ(massCenter);
        stlVector vectorTemp;
        for (VertexGeometric v:this.innerVertexList){
            vectorTemp = new stlVector(pointOnPlane,v);
            float dotProduct = vectorTemp.dotProduct(shiftingVector);
            //dotproduct < 0 means v is on the side whose thickness needs to be reduces
            if (dotProduct<0){
                VertexGeometric outerV = this.outerVertexList.get(v.index);
                stlVector movingVector = new stlVector(v,outerV);
                //scale by the specified movingThickness
                movingVector.scaleAsUnitVector(movingThickness);
                //change the original inner vertex's x,y,z
                v.x = v.x+movingVector.x;
                v.y = v.y+movingVector.y;
                v.z = v.z+movingVector.z;
            }
        }

    }
    //solely for testing purpose, to be deleted
    private void adjustThicknessTest(){
        ArrayList<VertexGeometric> vexlist = new ArrayList<VertexGeometric>();
        vexlist.add(new VertexGeometric(0,0,0,-1));
        vexlist.add(new VertexGeometric(4,0,0,-1));
        vexlist.add(new VertexGeometric(4,0,4,-1));
        vexlist.add(new VertexGeometric(2,0,2,-1));
        vexlist.add(new VertexGeometric(0,0,4,-1));

        VertexGeometric center = new VertexGeometric(-1,0,3,-1);

        //get list hull polygon
        quickHull hullFunction = new quickHull();
        ArrayList<VertexGeometric> vlist = hullFunction.getHullPolygon(vexlist);
        System.out.println(vlist);

        //initialize shifting vector with random value
        stlVector shiftingVector = new stlVector(vlist.get(0),vlist.get(1));
        float minimumDistance = Float.MAX_VALUE;
        stlVector tempVector;

        //loop through to find the best vector to move
        //the sequence is assumed to be
        for(int i=0;i<vlist.size()-1;i++){
            tempVector = minimumDistance(center,vlist.get(i),vlist.get(i+1));
            //find the vector with shortest length
            if (minimumDistance>tempVector.getLengthXZ()){
                minimumDistance = tempVector.getLengthXZ();
                shiftingVector = tempVector;
            }
        }
        //compare with last to first
        tempVector = minimumDistance(center,vlist.get(vlist.size()-1),vlist.get(0));
        if (minimumDistance>tempVector.getLengthXZ()){
            shiftingVector = tempVector;
        }

        System.out.println(shiftingVector.toString());


    }


}
