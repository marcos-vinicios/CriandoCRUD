package marcosvinicios.cursoandroidapp.criandocrud;


import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ListaActivity extends AppCompatActivity {

    List<Model> modelList = new ArrayList<>();
    RecyclerView mRecyclerView;
    //layout manager for recyclerView
    RecyclerView.LayoutManager layoutManager;
    FloatingActionButton mBotaoAdicionar;

    //firebase instance
    FirebaseFirestore db;

    CustomAdapter adapter;

    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        //acionado o titulo
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Lista dados");

        //iniciar fireStore
        db = FirebaseFirestore.getInstance();

        //inicializando views
        mRecyclerView = findViewById(R.id.recyclerView);
        mBotaoAdicionar = findViewById(R.id.botaoAdiconar);

        //set recyclerView properties

        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        //iniciando progress dialog
        pd = new ProgressDialog(this);
        //depois altere ordem das activity no android manifest

        //mostrar dados do recycllerView
        mostrarDados();

        mBotaoAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(ListaActivity.this, MainActivity.class));
                finish();
            }
        });
    }
    private void mostrarDados(){
        //set titulo  no progress dialog
        pd.setTitle("Carregando Dados...");
        //mostrando progress dialog
        pd.show();

        db.collection("Documentos")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        //chamado quando os dados são recuperados
                        pd.dismiss();
                        //mostrar dados
                        for (DocumentSnapshot doc: task.getResult()){
                            Model model = new Model(doc.getString("id"),
                                    doc.getString("titulo"),
                                    doc.getString("descricao"));
                            modelList.add(model);
                        }
                        //adpater
                        adapter = new CustomAdapter(ListaActivity.this, modelList);
                        //set adapter e recyclerView
                        mRecyclerView.setAdapter(adapter);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //chamado quando houver algum erro ao se aposentar
                        pd.dismiss();

                        Toast.makeText(ListaActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }
}
