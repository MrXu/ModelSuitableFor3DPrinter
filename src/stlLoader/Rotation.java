package stlLoader;

/**
 * Created with IntelliJ IDEA.
 * User: xuwei
 * Date: 15/1/15
 * Time: 2:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class Rotation {

    private stlFaceNorm baseNorm=null;
    public float cosAroundX;
    public float sinAroundX;
    public float cosAroundZ;
    public float sinAroundZ;

    public Rotation(stlFaceNorm baseNorm){

        this.baseNorm = baseNorm;
        calculateAngle();

    }

    private void calculateAngle(){

        float baseNormLength = (float)Math.sqrt(baseNorm.x*baseNorm.x+baseNorm.y*baseNorm.y+baseNorm.z*baseNorm.z);

        /**
         * special cases - x axis, y axis, z axis
         */
        //normal on z axis
        if (baseNorm.x==0 && baseNorm.y==0){
            System.err.println("######### Alert - x=0, y=0 ###########");
            cosAroundX = 0;
            sinAroundX = -1;
            cosAroundZ = 1;
            sinAroundZ = 0;
        }else if (baseNorm.z==0 && baseNorm.y==0){    //normal on x axis
            System.err.println("######### Alert - y=0, z=0 ###########");
            cosAroundX = 1;
            sinAroundX = 0;
            cosAroundZ = 0;
            sinAroundZ = 1;
        }else if (baseNorm.x==0 && baseNorm.z==0){    //normal on y axis
            System.err.println("######### Alert - x=0, z=0 ###########");
            cosAroundX = 1;
            sinAroundX = 0;
            cosAroundZ = 1;
            sinAroundZ = 0;
        }else{                                        //normal cases
            /**
             * normal cases - x
             */
            //first, rotate around x; cos and sin computed based on baseNormal
            cosAroundX = (float)baseNorm.y/(float)Math.sqrt(baseNorm.y*baseNorm.y+baseNorm.z*baseNorm.z);
//            sinAroundX = -(float)baseNorm.z/(float)Math.sqrt(baseNorm.y*baseNorm.y+baseNorm.z*baseNorm.z);
            sinAroundX = (float)baseNorm.z/(float)Math.sqrt(baseNorm.y*baseNorm.y+baseNorm.z*baseNorm.z);

            //second, rotate around z; cos and sin computed based on baseNormal
            sinAroundZ = (float)baseNorm.x/baseNormLength;
            if (baseNorm.y<0){
                cosAroundZ = -(float)Math.sqrt(1-sinAroundZ*sinAroundZ);
            }else{
                cosAroundZ = (float)Math.sqrt(1-sinAroundZ*sinAroundZ);
            }

            System.out.println("==================> normal vector: " + baseNorm.toString());
            System.out.println("==================> cosAroundX:    " + cosAroundX);
            System.out.println("==================> sinAroundX:    " + sinAroundX);
            System.out.println("==================> cosAroundZ:    " + cosAroundZ);
            System.out.println("==================> sinAroundZ:    " + sinAroundZ);

        }

    }

}
