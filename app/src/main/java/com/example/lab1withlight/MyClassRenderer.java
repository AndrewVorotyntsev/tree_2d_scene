package com.example.lab1withlight;
import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


public class MyClassRenderer implements GLSurfaceView.Renderer{



    // интерфейс GLSurfaceView.Renderer содержит
    // три метода onDrawFrame, onSurfaceChanged, onSurfaceCreated
    // которые должны быть переопределены
    // текущий контекст
    private Context context;
    //координаты камеры
    private float xСamera, yCamera, zCamera;
    //координаты источника света
    private float xLightPosition, yLightPosition, zLightPosition;
    //матрицы

    private float[] modelMatrix= new float[16];
    private float[] viewMatrix= new float[16];
    private float[] modelViewMatrix= new float[16];;
    private float[] projectionMatrix= new float[16];;
    private float[] modelViewProjectionMatrix= new float[16];;
    //буфер для координат вершин
    private final FloatBuffer vertexBuffer;
    private final FloatBuffer vertexBuffer1;
    private final FloatBuffer vertexBuffer2;
    private final FloatBuffer vertexBuffer3;
    private final FloatBuffer vertexBuffer4;

    private final FloatBuffer vertexBuffer5;
    //буфер для нормалей вершин
    private FloatBuffer normalBuffer;
    private FloatBuffer normalBuffer1;
    //буфер для цветов вершин
    private final FloatBuffer colorBuffer;
    private final FloatBuffer colorBuffer1;
    private final FloatBuffer colorBuffer2;
    private final FloatBuffer colorBuffer4;
    private final FloatBuffer colorBuffer5;
    //шейдерный объект
    private Shader mShader;
    private Shader mShader1;
    private Shader mShader2;
    private Shader mShader3;
    private Shader mShader4;

    private Shader mShader5;

    private Texture mTexture0, mTexture1;

    //конструктор
    public MyClassRenderer(Context context) {
        // запомним контекст
        // он нам понадобится в будущем для загрузки текстур
        this.context=context;

        //координаты точечного источника света
        xLightPosition=0.7f;
        yLightPosition=1.2f;
        zLightPosition=0.5f;


        //мы не будем двигать объекты
        //поэтому сбрасываем модельную матрицу на единичную
        Matrix.setIdentityM(modelMatrix, 0);
        //координаты камеры
        xСamera=0.0f;
        yCamera=0.0f;
        zCamera=3.0f;
        //пусть камера смотрит на начало координат
        //и верх у камеры будет вдоль оси Y
        //зная координаты камеры получаем матрицу вида
        Matrix.setLookAtM(
                viewMatrix, 0, xСamera, yCamera, zCamera, 0, 0, 0, 0, 1, 0);
        // умножая матрицу вида на матрицу модели
        // получаем матрицу модели-вида
        Matrix.multiplyMM(modelViewMatrix, 0, viewMatrix, 0, modelMatrix, 0);
        //координаты вершины 1
        float x1=-1;
        float y1=-0.35f;
        float z1=0.0f;
        //координаты вершины 2
        float x2=-1;
        float y2=-1.5f;
        float z2=0.0f;
        //координаты вершины 3
        float x3=1;
        float y3=-0.35f;
        float z3=0.0f;
        //координаты вершины 4
        float x4=1;
        float y4=-1.5f;
        float z4=0.0f;
        //запишем координаты всех вершин в единый массив
        float vertexArray [] = {x1,y1,z1, x2,y2,z2, x3,y3,z3, x4,y4,z4};

        //coordinates for sky
        float skyVertexArray1 [] = {
                -1.0f,1.5f,0.0f,
                -1.0f,-0.35f,0.0f,
                1.0f,1.5f,0.0f,
                1.0f,-0.35f,0
        };

        // Верхняя ветка
        float topBranchVertexArray2 [] = {
                0.0f, 0.9f, 0.4f,
                -0.25f, 0.5f, 0.4f,
                0.25f, 0.5f, 0.4f
        };

        // Средняя ветка
        float middleVertexArray3 [] = {
                0f, 0.5f, 0.4f,
                -0.5f, 0.25f, 0.4f,
                0.5f, 0.25f,0.4f
        };

        // Нижняя ветка
        float bottomVertexArray4 [] = {
                0f, 0.25f, 0.4f,
                -0.5f, 0.0f, 0.4f,
                0.5f, 0.0f,0.4f
        };

        // Ствол
        float trunkVertexArray5 [] = {
                0f, 0.8f, 0.4f,
                -0.2f, -0.5f, 0.4f,
                0.2f, -0.5f,0.4f
        };

        //создадим буфер для хранения координат вершин
        ByteBuffer bvertex = ByteBuffer.allocateDirect(vertexArray.length*4);
        bvertex.order(ByteOrder.nativeOrder());
        vertexBuffer = bvertex.asFloatBuffer();
        vertexBuffer.position(0);

        ByteBuffer bvertex1 = ByteBuffer.allocateDirect(skyVertexArray1.length*4);
        bvertex1.order(ByteOrder.nativeOrder());
        vertexBuffer1 = bvertex1.asFloatBuffer();
        vertexBuffer1.position(0);

        ByteBuffer bvertex2 = ByteBuffer.allocateDirect(topBranchVertexArray2.length*4);
        bvertex2.order(ByteOrder.nativeOrder());
        vertexBuffer2 = bvertex2.asFloatBuffer();
        vertexBuffer2.position(0);

        ByteBuffer bvertex3 = ByteBuffer.allocateDirect(middleVertexArray3.length*4);
        bvertex3.order(ByteOrder.nativeOrder());
        vertexBuffer3 = bvertex3.asFloatBuffer();
        vertexBuffer3.position(0);

        ByteBuffer bvertex4 = ByteBuffer.allocateDirect(bottomVertexArray4.length*4);
        bvertex4.order(ByteOrder.nativeOrder());
        vertexBuffer4 = bvertex4.asFloatBuffer();
        vertexBuffer4.position(0);

        ByteBuffer bvertex5 = ByteBuffer.allocateDirect(trunkVertexArray5.length*4);
        bvertex5.order(ByteOrder.nativeOrder());
        vertexBuffer5 = bvertex5.asFloatBuffer();
        vertexBuffer5.position(0);

        //перепишем координаты вершин из массива в буфер
        vertexBuffer.put(vertexArray);
        vertexBuffer.position(0);
        vertexBuffer1.put(skyVertexArray1);
        vertexBuffer1.position(0);
        vertexBuffer2.put(topBranchVertexArray2);
        vertexBuffer2.position(0);
        vertexBuffer3.put(middleVertexArray3);
        vertexBuffer3.position(0);
        vertexBuffer4.put(bottomVertexArray4);
        vertexBuffer4.position(0);
        vertexBuffer5.put(trunkVertexArray5);
        vertexBuffer5.position(0);
        //вектор нормали перпендикулярен плоскости квадрата
        //и направлен вдоль оси Z
        float nx=0;
        float ny=0;
        float nz=1;
        //нормаль одинакова для всех вершин квадрата,
        //поэтому переписываем координаты вектора нормали в массив 4 раза
        float normalArray [] ={nx, ny, nz,   nx, ny, nz,   nx, ny, nz,   nx, ny, nz};
        float normalArray1 [] ={0, 0, 1,   0, 0, 1,   0, 0, 1,   0, 0, 1};
        //создадим буфер для хранения координат векторов нормали
        ByteBuffer bnormal = ByteBuffer.allocateDirect(normalArray.length*4);
        bnormal.order(ByteOrder.nativeOrder());
        normalBuffer = bnormal.asFloatBuffer();
        normalBuffer.position(0);

        //перепишем координаты нормалей из массива в буфер
        normalBuffer.put(normalArray);
        normalBuffer.position(0);

        //разукрасим вершины квадрата, зададим цвета для вершин
        float red1=2.04f;
        float green1=2.50f;
        float blue1=2.50f;
        //цвет второй вершины
        float red2=1.27f;
        float green2=2.50f;
        float blue2=2.50f;
        //цвет третьей вершины
        float red3=1;
        float green3=1;
        float blue3=1.5f;
        //цвет четвертой вершины
        float red4=1;
        float green4=1;
        float blue4=1;
        //перепишем цвета вершин в массив
        //четвертый компонент цвета (альфу) примем равным единице
        float colorArray [] = {
                red1, green1, blue1, 1,
                red2, green2, blue2, 1,
                red3, green3, blue3, 1,
                red4, green4, blue4, 1,
        };
        float skyColorArray1[] = {
                0f, 1f, 0.8f, 1,
                0.5f, 0.5f, 1, 1,
                0.2f, 0.2f, 0.8f, 1,
                0.5f, 0.5f, 1, 1,
        };
        float colorArray2[] = {
                1, 1f, 1f, 1,
                0, 0.8f, 0, 1,
                0, 1f, 0f, 1,
        };

        float colorArray4[] = {
                1, 1f, 1f, 1,
                0, 0.8f, 0, 1,
                0, 1f, 0f, 1,
        };

        float colorArray5[] = {
                1, 1, 1, 1,
                1.42f, 0.64f, 0.42f, 1,
                1.17f, 0.51f, 0.19f, 1,
        };

        //создадим буфер для хранения цветов вершин
        ByteBuffer bcolor = ByteBuffer.allocateDirect(colorArray.length*4);
        bcolor.order(ByteOrder.nativeOrder());
        colorBuffer = bcolor.asFloatBuffer();
        colorBuffer.position(0);
        //перепишем цвета вершин из массива в буфер
        colorBuffer.put(colorArray);
        colorBuffer.position(0);

        ByteBuffer bcolor1 = ByteBuffer.allocateDirect(skyColorArray1.length*4);
        bcolor1.order(ByteOrder.nativeOrder());
        colorBuffer1 = bcolor1.asFloatBuffer();
        colorBuffer1.position(0);
        colorBuffer1.put(skyColorArray1);
        colorBuffer1.position(0);

        ByteBuffer bcolor2 = ByteBuffer.allocateDirect(skyColorArray1.length*4);
        bcolor2.order(ByteOrder.nativeOrder());
        colorBuffer2 = bcolor2.asFloatBuffer();
        colorBuffer2.position(0);
        colorBuffer2.put(colorArray2);
        colorBuffer2.position(0);

        ByteBuffer bcolor4 = ByteBuffer.allocateDirect(colorArray4.length*4);
        bcolor4.order(ByteOrder.nativeOrder());
        colorBuffer4 = bcolor4.asFloatBuffer();
        colorBuffer4.position(0);
        colorBuffer4.put(colorArray4);
        colorBuffer4.position(0);

        ByteBuffer bcolor5 = ByteBuffer.allocateDirect(colorArray5.length*4);
        bcolor5.order(ByteOrder.nativeOrder());
        colorBuffer5 = bcolor5.asFloatBuffer();
        colorBuffer5.position(0);
        colorBuffer5.put(colorArray5);
        colorBuffer5.position(0);

    }//конец конструктора

    //метод, который срабатывает при изменении размеров экрана
    //в нем мы получим матрицу проекции и матрицу модели-вида-проекции
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        // устанавливаем glViewport
        GLES20.glViewport(0, 0, width, height);
        float ratio = (float) width / height;
        float k=0.055f;
        float left = -k*ratio;
        float right = k*ratio;
        float bottom = -k;
        float top = k;
        float near = 0.1f;
        float far = 10.0f;
        // получаем матрицу проекции
        Matrix.frustumM(projectionMatrix, 0, left, right, bottom, top, near, far);
        // матрица проекции изменилась,
        // поэтому нужно пересчитать матрицу модели-вида-проекции
        Matrix.multiplyMM(
                modelViewProjectionMatrix, 0, projectionMatrix, 0, modelViewMatrix, 0);
    }

    //метод, который срабатывает при создании экрана
    //здесь мы создаем шейдерный объект
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        //включаем тест глубины
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        //включаем отсечение невидимых граней
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        //включаем сглаживание текстур, это пригодится в будущем
        GLES20.glHint(
                GLES20.GL_GENERATE_MIPMAP_HINT, GLES20.GL_NICEST);
        //записываем код вершинного шейдера в виде строки
        String vertexShaderCode=
                "uniform mat4 u_modelViewProjectionMatrix;"+
                        "attribute vec3 a_vertex;"+
                        "attribute vec3 a_normal;"+
                        "attribute vec4 a_color;"+
                        "varying vec3 v_vertex;"+
                        "varying vec3 v_normal;"+
                        "varying vec4 v_color;"+
                        "void main() {"+
                            "v_vertex=a_vertex;"+
                            "vec3 n_normal=normalize(a_normal);"+
                            "v_normal=n_normal;"+
                            "v_color=a_color;"+
                            "gl_Position = u_modelViewProjectionMatrix * vec4(a_vertex,1.0);"+
                        "}";
        //записываем код фрагментного шейдера в виде строки
        String fragmentShaderCode=
                "precision mediump float;"+
                        "uniform vec3 u_camera;"+
                        "uniform vec3 u_lightPosition;"+
                        "varying vec3 v_vertex;"+
                        "varying vec3 v_normal;"+
                        "varying vec4 v_color;"+
                        "void main() {"+
                        "vec3 n_normal=normalize(v_normal);"+
                        "vec3 lightvector = normalize(u_lightPosition - v_vertex);"+
                        "vec3 lookvector = normalize(u_camera - v_vertex);"+
                        "float ambient=0.2;"+
                        "float k_diffuse=0.3;"+
                        "float k_specular=0.5;"+
                        "float diffuse = k_diffuse * max(dot(n_normal, lightvector), 0.0);"+
                        "vec3 reflectvector = reflect(-lightvector, n_normal);"+
                        "float specular = k_specular * pow( max(dot(lookvector,reflectvector),0.0), 40.0 );"+
                        "vec4 one=vec4(1.0,1.0,1.0,1.0);"+
                        "vec4 lightColor = (ambient+diffuse+specular)*one;"+
                        "gl_FragColor = mix(lightColor,v_color,0.6);"+
                        "}";

        String fragmentShaderCode2 = "precision mediump float; " +
                "       uniform vec3 u_camera; " +
                "       uniform vec3 u_lightPosition;" +
                "        uniform sampler2D u_texture;    " +
                "    varying vec3 v_vertex;   " +
                "     varying vec3 v_normal;     " +
                "   varying vec4 v_color;" +
                "// принимаем координат текстур после интерполяции" +
                "        varying vec2 v_texcoord;" +
                "        void main()    " +
                "    {            vec3 n_normal = normalize(v_normal);" +
                "            vec3 lightvector = normalize(u_lightPosition - v_vertex); " +
                "           vec3 lookvector = normalize(u_camera - v_vertex);       " +
                "     float ambient = 0.2;            float k_diffuse = 0.8;      " +
                "      float k_specular = 0.4;  " +
                "          float diffuse = k_diffuse * max(dot(n_normal, lightvector), 0.0); " +
                "           vec3 reflectvector = reflect(-lightvector, n_normal);     " +
                "       float specular = k_specular * pow(max(dot(lookvector, reflectvector), 0.0), 40.0);" +
                "            vec4 one = vec4(1.0, 1.0, 1.0, 1.0);    " +
                "        // оставим пока квадрат временно без освещения и выполним смешивание текстуры    " +
                "        // вычисляем координаты первой текстуры//  " +
                "  float r = v_vertex.x * v_vertex.x + v_vertex.z * v_vertex.z;" +
                "//    vec2 texcoord = 0.3 * r * v_vertex.xz;   " +
                "        // вычисляем цвет пикселя для текстуры   " +
                "         vec4 textureColor = texture2D(u_texture, v_texcoord);  " +
                "          gl_FragColor = 2.0 * (ambient + diffuse) * textureColor + specular * one;}";
        String vertexShaderCode2 =                "uniform mat4 u_modelViewProjectionMatrix;        attribute vec3 a_vertex;        attribute vec3 a_normal;        attribute vec4 a_color;        varying vec3 v_vertex;        varying vec3 v_normal;        varying vec4 v_color;// определяем переменные для передачи// координат текстур на интерполяцию        varying vec2 v_texcoord;        void main() {            v_vertex = a_vertex;            vec3 n_normal = normalize(a_normal);            v_normal = n_normal;            v_color = a_color;            // вычисляем координаты первой текстуры и отравляем их на интерполяцию            // пусть координата текстуры S будет равна координате вершины X            // v_texcoord.s=a_vertex.x;            // а координата текстуры T будет равна координате вершины Z            //v_texcoord0.t=a_vertex.z;            float r = a_vertex.x * a_vertex.x + a_vertex.y * a_vertex.y;            // TODO: В этом случае получается странный эфект растягивания текстуры//    v_texcoord = 0.1 * r * a_vertex.xy;            v_texcoord = a_vertex.xy;            gl_Position = u_modelViewProjectionMatrix * vec4(a_vertex, 1.0);        }";


        String vertexShaderCode3 =

                "layout (location = 0) in vec3 aPos;        layout (location = 1) in vec3 aColor;        layout (location = 2) in vec2 aTex;        out vec3 color;        out vec2 texCoord;        uniform float scale;        void main()        {            gl_Position = vec4(aPos.x + aPos.x * scale, aPos.y + aPos.y * scale, aPos.z + aPos.z * scale, 1.0);            color = aColor;            texCoord = aTex;        }";

        String fragmentShaderCode3 = "out vec4 FragColor;// Inputs the color from the Vertex Shader        in vec3 color;// Inputs the texture coordinates from the Vertex Shader        in vec2 texCoord;// Gets the Texture Unit from the main function        uniform sampler2D tex0;        void main()        {            FragColor = texture(tex0, texCoord);        }";

        //создадим шейдерный объект
        mShader = new Shader(vertexShaderCode, fragmentShaderCode);
        //свяжем буфер вершин с атрибутом a_vertex в вершинном шейдере
        mShader.linkVertexBuffer(vertexBuffer);

        //свяжем буфер нормалей с атрибутом a_normal в вершинном шейдере
        mShader.linkNormalBuffer(normalBuffer);

        //свяжем буфер цветов с атрибутом a_color в вершинном шейдере
        mShader.linkColorBuffer(colorBuffer);
        //связь атрибутов с буферами сохраняется до тех пор,
        //пока не будет уничтожен шейдерный объект

        //создаем текстурные объекты из картинок
        mTexture0=new Texture(context,R.drawable.snow2);
        mTexture1=new Texture(context,R.drawable.snow2);

        mShader.linkTexture(mTexture0, mTexture0);

        mShader1 = new Shader(vertexShaderCode, fragmentShaderCode);
        mShader1.linkVertexBuffer(vertexBuffer1);
        mShader1.linkNormalBuffer(normalBuffer);
        mShader1.linkColorBuffer(colorBuffer1);

        mShader2 = new Shader(vertexShaderCode, fragmentShaderCode);
        mShader2.linkVertexBuffer(vertexBuffer2);
        mShader2.linkNormalBuffer(normalBuffer);
        mShader2.linkColorBuffer(colorBuffer2);

        mShader3 = new Shader(vertexShaderCode, fragmentShaderCode);
        mShader3.linkVertexBuffer(vertexBuffer3);
        mShader3.linkNormalBuffer(normalBuffer);
        mShader3.linkColorBuffer(colorBuffer2);

        mShader4 = new Shader(vertexShaderCode, fragmentShaderCode);
        mShader4.linkVertexBuffer(vertexBuffer4);
        mShader4.linkNormalBuffer(normalBuffer);
        mShader4.linkColorBuffer(colorBuffer4);

        mShader5 = new Shader(vertexShaderCode, fragmentShaderCode);
        mShader5.linkVertexBuffer(vertexBuffer5);
        mShader5.linkNormalBuffer(normalBuffer);
        mShader5.linkColorBuffer(colorBuffer5);

    }

    //метод, в котором выполняется рисование кадра
    public void onDrawFrame(GL10 unused) {
        //очищаем кадр
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        //передаем в шейдерный объект матрицу модели-вида-проекции
        mShader.useProgram();
        mShader.linkVertexBuffer(vertexBuffer);
        mShader.linkColorBuffer(colorBuffer);
        mShader.linkModelViewProjectionMatrix(modelViewProjectionMatrix);
        mShader.linkCamera(xСamera, yCamera, zCamera);
        mShader.linkLightSource(xLightPosition, yLightPosition, zLightPosition);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

        mShader1.useProgram();
        mShader1.linkVertexBuffer(vertexBuffer1);
        mShader1.linkColorBuffer(colorBuffer1);
        mShader1.linkModelViewProjectionMatrix(modelViewProjectionMatrix);
        mShader1.linkCamera(xСamera, yCamera, zCamera);
        mShader1.linkLightSource(xLightPosition, yLightPosition, zLightPosition);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

        mShader2.useProgram();
        mShader2.linkVertexBuffer(vertexBuffer2);
        mShader2.linkColorBuffer(colorBuffer2);
        mShader2.linkModelViewProjectionMatrix(modelViewProjectionMatrix);
        mShader2.linkCamera(xСamera, yCamera, zCamera);
        mShader2.linkLightSource(xLightPosition, yLightPosition, zLightPosition);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);

        mShader3.useProgram();
        mShader3.linkVertexBuffer(vertexBuffer3);
        mShader3.linkColorBuffer(colorBuffer2);
        mShader3.linkModelViewProjectionMatrix(modelViewProjectionMatrix);
        mShader3.linkCamera(xСamera, yCamera, zCamera);
        mShader3.linkLightSource(xLightPosition, yLightPosition, zLightPosition);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);

        mShader4.useProgram();
        mShader4.linkVertexBuffer(vertexBuffer4);
        mShader4.linkColorBuffer(colorBuffer4);
        mShader4.linkModelViewProjectionMatrix(modelViewProjectionMatrix);
        mShader4.linkCamera(xСamera, yCamera, zCamera);
        mShader4.linkLightSource(xLightPosition, yLightPosition, zLightPosition);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);

        mShader5.useProgram();
        mShader5.linkVertexBuffer(vertexBuffer5);
        mShader5.linkColorBuffer(colorBuffer5);
        mShader5.linkModelViewProjectionMatrix(modelViewProjectionMatrix);
        mShader5.linkCamera(xСamera, yCamera, zCamera);
        mShader5.linkLightSource(xLightPosition, yLightPosition, zLightPosition);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
    }

}