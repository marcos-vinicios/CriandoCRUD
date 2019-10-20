package marcosvinicios.cursoandroidapp.criandocrud;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class ViewHolder extends RecyclerView.ViewHolder {

    TextView mTituloTv, mDescricaoTv;
    View mView;


    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        mView = itemView;

        //item click
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v, getAdapterPosition());
            }
        });
        //item long click listener

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mClickListener.onItemLongClick(v,getAdapterPosition());
                return true;
            }
        });
        //inicializando views com model_layout.xml
        mTituloTv = itemView.findViewById(R.id.rTituloTv);
        mDescricaoTv = itemView.findViewById(R.id.rDescricaoTv);
    }
    //criando manualmente
    private ViewHolder.ClickListiner mClickListener;
    //iterface for click listener
    public interface ClickListiner{
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }
    public void setOnClickListener(ViewHolder.ClickListiner clickListener){
        mClickListener = clickListener;
    }
}
