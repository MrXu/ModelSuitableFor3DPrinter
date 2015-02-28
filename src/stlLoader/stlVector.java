package stlLoader;

import ObjLoader.builder.VertexGeometric;

/**
 * Created with IntelliJ IDEA.
 * User: xuwei
 * Date: 28/2/15
 * Time: 5:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class stlVector {

    public float x;
    public float y;
    public float z;


    public stlVector(VertexGeometric from, VertexGeometric to){
        this.x = to.x-from.x;
        this.y = to.y-from.y;
        this.z = to.z-from.z;
    }

    public stlVector(float x, float y, float z){
        this.x=x;
        this.y=y;
        this.z=z;
    }

    public float dotProduct(stlVector vec){
        float result;
        result = (x*vec.x)+(y*vec.y)+(z*vec.z);
        return result;
    }

    public float dotProductXZ(stlVector vec){
        float result;
        result = (x*vec.x)+(z*vec.z);
        return result;
    }

    public float getLengthXZ(){
        return (float)Math.sqrt((double)(this.x*this.x+this.z*this.z));
    }

    public float getLength(){
        return (float)Math.sqrt((double)(this.x*this.x+this.y*this.y+this.z*this.z));
    }

    public VertexGeometric moveVertxXZ(VertexGeometric v){
        float x = v.x+this.x;
        float y = 0.00f;
        float z = v.z+this.z;
        return new VertexGeometric(x,y,z,-1);
    }

    public void normalize(){
        float length = getLength();
        this.x = this.x/length;
        this.y = this.y/length;
        this.z = this.z/length;
    }

    public void scaleAsUnitVector(float length){
        normalize();
        this.x = this.x*length;
        this.y = this.y*length;
        this.z = this.z*length;
    }

    public String toString(){
        return ("("+x+","+y+","+z+")");
    }

}
