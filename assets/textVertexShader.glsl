attribute vec4 vPosition;
attribute vec2 texcoord;
uniform mat4 uMVPMatrix;
varying vec2 f_texcoord;
varying mat4 transform;
void main() {
   gl_Position = uMVPMatrix*vPosition;
   f_texcoord = texcoord;
   transform = uMVPMatrix;
}
