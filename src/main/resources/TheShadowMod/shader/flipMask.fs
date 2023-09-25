#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif
varying LOWP vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
void main()
{
    vec4 color = texture2D(u_texture, v_texCoords);
  // float gray = (color.r* 0.264 +color.g* 0.617 +color.b* 0.149 );
  // float gray = dot(color.rgb, vec3(0.299,0.587,0.114));


    gl_FragColor = vec4(1-color.r, 1-color.g, 1-color.b,color.a);
  //  gl_FragColor = vec4(gray,gray, gray,color.a);
}
