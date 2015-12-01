package labgeo1;

/**
 * Class used for transforming point's and other geometrical objects
 * !!WARNING rotation is not implemented!!!
 * @author Dragos-Alexandru
 */
public class AffineTransformer {
    double[][] A;
    double[] B;
    int type;
    public static final int ZOOM = 0;
    public static final int TRANSLATE = 1;
    public static final int ROTATE = 2;
    public AffineTransformer(int type){
        A = new double[3][3];
        for(double[] a:A){
            a = new double[3];
            for(double aa:a){
                aa = 0;
            }
        }
        B = new double[3];
        for(double b:B){
            b = 0;
        }
        switch(type){
            case ZOOM:
                setAsZoom();
            break;
            case TRANSLATE:
                setAsTranslation();
            break;
            case ROTATE:
                setAsZoom();
            break;
        }
    }
    
    public void addZoom(double zoom){
        if(this.type == ZOOM){
            this.A[0][0] += zoom;
            this.A[1][1] += zoom;
            this.A[2][2] += zoom;
        }
    }
    
    private void setAsZoom(){
        this.B[0] = 0;
        this.B[1] = 0;
        this.B[2] = 0;
    }
    
    public void addTranslation(int x, int y, int z){
        if(this.type == TRANSLATE){
            if(B.length != 3) return;
            this.B[0] += x;
            this.B[1] -= y;
            this.B[2] += z;
        }
    }
    
    private void setAsTranslation(){
        this.A[0][0] += 1;
        this.A[1][1] += 1;
        this.A[2][2] += 1;
    }
    
    /**
     *  This function transforms a point's coordonates
     * !!WARNING rotation is not implemented!!!
     * @param X
     * @return
     */
    public Point applyToPoint(Point X){
        if(this.type != ROTATE){
            X.x = A[0][0]*X.x + A[0][1]*X.y+A[0][2]*X.z + B[0];
            X.y = A[1][0]*X.x + A[1][1]*X.y+A[1][2]*X.z + B[1];
            X.z = A[2][0]*X.x + A[2][1]*X.y+A[2][2]*X.z + B[2];
        }else{
            X.x = A[0][0]*X.x + A[0][1]*X.y+A[0][2]*X.z + B[0];
            X.y = A[1][0]*X.x + A[1][1]*X.y+A[1][2]*X.z + B[1];
            X.z = A[2][0]*X.x + A[2][1]*X.y+A[2][2]*X.z + B[2];
        }
        return X;
    }
    
    public VectorGeo applyToVector(VectorGeo d){
        d.value = applyToPoint(d.value);
        d.X = applyToPoint(d.X);
        d.Y = applyToPoint(d.Y);
        return d;
    }
    
}
