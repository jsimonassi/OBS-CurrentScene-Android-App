package com.joaosimonassi.obscurrentscene.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.joaosimonassi.obscurrentscene.ObsConfig;
import com.joaosimonassi.obscurrentscene.R;

public class SelectSceneActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_scene);
        getSupportActionBar().hide();

        ListView sceneList = findViewById(R.id.list_id);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ObsConfig.current_scenes);
        sceneList.setAdapter(adapter);

        sceneList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ObsConfig.myScene = ObsConfig.current_scenes.get(position);
                startActivity(new Intent(SelectSceneActivity.this, HomeActivity.class));
            }
        });
    }
}