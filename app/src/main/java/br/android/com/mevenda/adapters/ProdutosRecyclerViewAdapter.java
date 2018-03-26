package br.android.com.mevenda.adapters;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.android.com.mevenda.R;
import br.android.com.mevenda.bean.Produto;
import br.android.com.mevenda.interfaces.RecyclerViewOnclickListener;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by daylo on 17/03/2018.
 */
public class ProdutosRecyclerViewAdapter extends
        RecyclerView.Adapter<ProdutosRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = ProdutosRecyclerViewAdapter.class.getSimpleName();

    private Context context;
    private List<Produto> list;
    private OnItemClickListener onItemClickListener;


    public ProdutosRecyclerViewAdapter(Context context, List<Produto> list,
                                       OnItemClickListener onItemClickListener) {
        this.context = context;
        this.list = list;
        this.onItemClickListener = onItemClickListener;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_produto_nome)
        public TextView textViewNome;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        public void bind(final Produto model,
                         final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(getLayoutPosition());

                }
            });
        }

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.recycler_view_produto_item_layout, parent, false);
        ButterKnife.bind(this, view);


        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Produto item = list.get(position);
        holder.textViewNome.setText(item.getNome());
        holder.bind(item, onItemClickListener);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

}