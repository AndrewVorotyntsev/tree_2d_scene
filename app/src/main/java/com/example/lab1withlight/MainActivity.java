package com.example.lab1withlight;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
        // создадим ссылку на экземпляр нашего класса MyClassSurfaceView
        private MyClassSurfaceView mGLSurfaceView;

        // переопределим метод
        // onCreate
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

//создадим экземпляр нашего класса MyClassSurfaceView
            mGLSurfaceView = new MyClassSurfaceView(this);

//вызовем экземпляр нашего класса MyClassSurfaceView
            setContentView(mGLSurfaceView);
// на экране появится поверхность для рисования в OpenGl ES
        }
        @Override
        protected void onPause() {
            super.onPause();
            mGLSurfaceView.onPause();
        }

        @Override
        protected void onResume() {
            super.onResume();
            mGLSurfaceView.onResume();
        }
    }
