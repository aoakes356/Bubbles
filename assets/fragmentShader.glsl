precision highp float;
//uniform vec4 vColor;
uniform vec4 uAngles;
uniform vec2 uScreen;
uniform vec2 uAB;
varying vec2 pos;
varying mat4 transform;
void main() {
    // Scale based on the nearpoint of the frustum? - NOPE.
    //-6, 3
    float ratio = uScreen.x/uScreen.y;
    vec2 fragmentPosition = vec2(gl_PointCoord.x/(-6.0),gl_PointCoord.y/3.0);//Fragment position put on the same coordinate system as the game object.
    vec2 pos2 = vec2((pos.x-fragmentPosition.x)*(ratio*2.0),(pos.y-fragmentPosition.y));
    vec4 light = vec4(0.0,0.0,0.0,500.0);
    float M_PI = 3.14159265358979;
    float result = (uAngles.z)*pow(dot(uAngles.yx,pos2),2.0)+((uAngles.w)*pow(dot(vec2(pos2.x,-pos2.y),uAngles.xy),2.0));
    /*float zim = (pos2.y/(uAB.y+.5))*10.0;
    float zr = (pos2.x/(uAB.x+.5))*10.0;
    float cim = (pos2.x*(uAB.x+.5));
    float cr = (pos2.y*(uAB.y+.5));
    float i;
    for(i = 0.0; i < 10.0; i+=.5){
        zr = zr*zr+cr-(zim*zim);
        zim = 2.0*(zim*zr)+cim;
        if(length(vec2(zr,zim)) > 2.0){
            break;
        }
    }*/
    //float concave = pow(3.0,pow(length(pos2-uAB),2.0))/100.0;
    /*if(result > 0.0){
       result += concave*result;
    } */
        //   vec3 restricted = clamp(vec3(0,0,abs((fragmentPosition.y))),vec3(.0,.0,.0),vec3(1.0,1.0,1.0));
    //   gl_FragColor = vec4(restricted,1.0);
    float lightDist = pow(length(fragmentPosition.xy-light.xy),1.0)/(5.5*result);
    /*if(lightDist < .25){
        lightDist = lightDist/result;
    }*/
    //result += i/250.0;
    if(result <= .06){
        gl_FragColor = vec4(1.0, 1.0, 1.0,((pow(result*24.0,2.0))/lightDist)+i/50.0);
        //gl_FragColor = vec4(1.0, 1.0, 1.0,i/10.0);
    }else{
        gl_FragColor = vec4(1.0,1.0,1.0,0.0);
    }
}