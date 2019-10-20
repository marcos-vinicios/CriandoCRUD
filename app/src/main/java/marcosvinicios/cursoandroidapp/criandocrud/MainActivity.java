package marcosvinicios.cursoandroidapp.criandocrud;


import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    //views
    EditText mTitulo, mDescricao;
    Button mSalva, mLista;

    //progress dialog
    ProgressDialog pd;

    //fireStore intancia
    FirebaseFirestore db;

    String pId, pTitulo, pDescricao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //acionado o titulo
        ActionBar actionBar = getSupportActionBar();

        //inicializando a view no XML
        mTitulo = findViewById(R.id.titulo);
        mDescricao = findViewById(R.id.descricao);
        mSalva = findViewById(R.id.salvarBtn);
        mLista = findViewById(R.id.listaBtn);

        final Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            //update dados
            actionBar.setTitle("Update Dados");
            mSalva.setText("Update");
            //get dados
            pId = bundle.getString("pId");
            pTitulo = bundle.getString("pTitulo");
            pDescricao = bundle.getString("pDescricao");
            //set dados
            mTitulo.setText(pTitulo);
            mDescricao.setText(pDescricao);
        }
        else {
            // new dados
            actionBar.setTitle("Add dados");
            mSalva.setText("Salvo");
        }

        //progress dialog
        pd = new ProgressDialog(this);

        //fireStore
        db = FirebaseFirestore.getInstance();

        //click botao de upload dados
        mSalva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = getIntent().getExtras();
                if (bundle != null){
                    //atualizando
                    //input dados
                    String id = pId;
                    String titulo = mTitulo.getText().toString().trim();
                    String descricao = mDescricao.getText().toString().trim();
                    //função ligar para update data
                    updateDados(id,titulo,descricao);
                }
                else {
                    //adicionando novo
                    //input dados
                    String titulo = mTitulo.getText().toString().trim();
                    String descricao = mDescricao.getText().toString().trim();
                    //funcao call to upload dados
                    uploadDados(titulo, descricao);
                }

            }
        });
        //click botão de inicio ListActivity
        mLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ListaActivity.class));
                finish();
            }
        });
    }

    private void updateDados(String id, String titulo, String descricao) {
        //setando titulo no progress bar
        pd.setTitle("updating Dados...");
        //mostrando(show) progress bar quando usuario click o botão salva
        pd.show();

        db.collection("Documentos")
                .document(id)
                .update("titulo", titulo, "descricao",descricao)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //chamado quando atualizado com sucesso
                        pd.dismiss();
                        Toast.makeText(MainActivity.this, "Update..", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //chamado quando ouver erro
                        pd.dismiss();
                        //get mostrar error messagem
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void uploadDados(String titulo, String descricao) {
        //setando titulo no progress bar
        pd.setTitle("Adcionando dados no FIRESTORE");
        //mostrando(show) progress bar quando usuario click o botão salva
        pd.show();
        //randon id para que cada dado seja armazenando STORED
        String id = UUID.randomUUID().toString();

        Map<String,Object > doc = new HashMap<>();
        doc.put("id", id); //id dados
        doc.put("titulo", titulo);
        doc.put("descricao", descricao);

        //adicionando dados
        db.collection("documentos").document(id).set(doc)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //isso será chamado quando os dados forem adicionados com sucesso
                        pd.dismiss();
                        Toast.makeText(MainActivity.this,
                                "Upload...", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //isso será chamado quando os dados forem adicionados derem algum erro no upload
                        pd.dismiss();
                        Toast.makeText(MainActivity.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
