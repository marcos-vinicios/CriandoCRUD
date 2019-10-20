package marcosvinicios.cursoandroidapp.criandocrud;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<ViewHolder> {
    ListaActivity listaActivity;
    List<Model> modelList;


    public CustomAdapter(ListaActivity listaActivity, List<Model> modelList) {
        this.listaActivity = listaActivity;
        this.modelList = modelList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
        //inflater layout
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.model_layout, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(itemView);
        //lidar com item cliks aqui
        viewHolder.setOnClickListener(new ViewHolder.ClickListiner() {
            @Override
            public void onItemClick(View view, int position) {
                //isso será chamado quando o usuário clica no item

                //mostrar dados em um brinde ao clicar
                String  titulo1 = modelList.get(position).getTitulo();
                String descr = modelList.get(position).getDescricao();
                Toast.makeText(listaActivity, titulo1 + "\n" + descr, Toast.LENGTH_SHORT ).show();
            }

            @Override
            public void onItemLongClick(View view, final int position) {
                // será chamado quando o usuário da um click longo no item

                //criando AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(listaActivity);
                //opção para disply no dialog
                String[] opcao = {"Update", "Delete"};
                builder.setItems(opcao, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0){
                            //update e clicou
                            //(recuperar)get dados
                            String id = modelList.get(position).getId();
                            String titulo1 = modelList.get(position).getTitulo();
                            String descricao1 = modelList.get(position).getDescricao();

                            //intent para iniciar activity
                            Intent intent = new Intent(listaActivity, MainActivity.class);
                            //colocar dados na intent
                            intent.putExtra("pId", id);
                            intent.putExtra("pTitulo", titulo1);
                            intent.putExtra("pDescricao", descricao1);
                            //iniciando activity
                            listaActivity.startActivity(intent);
                        }
                        if (which == 1){
                            //delete e clicou
                        }
                    }
                }).create().show();


            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        //ligar views / set dados
        viewHolder.mTituloTv.setText(modelList.get(i).getTitulo());
        viewHolder.mDescricaoTv.setText(modelList.get(i).getDescricao());
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }
}
