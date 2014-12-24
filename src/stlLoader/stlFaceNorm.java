package stlLoader;

/**
 * Created with IntelliJ IDEA.
 * User: xuwei
 * Date: 19/12/14
 * Time: 4:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class stlFaceNorm {

    public float x = 0;
    public float y = 0;
    public float z = 0;

    public void add(float x, float y, float z) {
        this.x += x;
        this.y += y;
        this.z += z;
    }

    public stlFaceNorm(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public stlFaceNorm reverse(){
        return new stlFaceNorm(-this.x,-this.y,-this.z);
    }

    public void dividedBy(float div){
        this.x = this.x/div;
        this.y = this.y/div;
        this.z = this.z/div;
    }

    public void multiple(float mul){
        this.x = this.x*mul;
        this.y = this.y*mul;
        this.z = this.z*mul;
    }

    public String toString() {
        if(null == this)
            return "null";
        else
            return x+" "+y+" "+z;   //fixed stl format
    }


}
