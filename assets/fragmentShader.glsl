precision highp float;
uniform vec4 uAngles;
uniform vec2 uScreen;
uniform vec2 uAB;
uniform vec2 settings;
uniform vec3 color;
varying vec2 pos;
varying mat4 transform;
void main() {
    // Settings -> <radius, aura>
    // Scale based on the nearpoint of the frustum? - NOPE.
    //-6, 3
    float ratio = uScreen.x/uScreen.y;
    vec2 fragmentPosition = vec2(gl_PointCoord.x/(-6.0),gl_PointCoord.y/3.0);//Fragment position put on the same coordinate system as the game object.
    vec2 pos2 = vec2((pos.x-fragmentPosition.x)*(ratio*2.0),(pos.y-fragmentPosition.y));
    vec4 light = vec4(0.0,0.0,0.0,500.0);
    float M_PI = 3.14159265358979;
    float result = (uAngles.z)*pow(dot(uAngles.yx,pos2),2.0)+((uAngles.w)*pow(dot(vec2(pos2.x,-pos2.y),uAngles.xy),2.0));
    float lightDist = pow(length(fragmentPosition.xy-light.xy),1.0)/(5.5*result);
    if(result <= settings.x){
        gl_FragColor = vec4(color,((pow(result*24.0,2.0))/(450.0*settings.x*settings.x)));
        //gl_FragColor = vec4(1.0, 1.0, 1.0,i/10.0);
    }else{
        gl_FragColor = vec4(color,(pow(settings.x/.055,4.0)*.000004*settings.y)/pow(result,4.0));
    }
}