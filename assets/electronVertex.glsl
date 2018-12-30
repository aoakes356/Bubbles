attribute vec4 vPosition;
uniform vec2 inputTextureCoordinate;
varying vec2 pos;
uniform mat4 uMVPMatrix;
varying mat4 transform;
void main() {
   gl_Position = uMVPMatrix*vPosition;
   pos = inputTextureCoordinate.xy;
   transform = uMVPMatrix;
}