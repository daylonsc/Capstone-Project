package br.android.com.mevenda.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.android.com.mevenda.R;
import br.android.com.mevenda.bean.Cliente;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by daylo on 17/03/2018.
 */
public class ClientesRecyclerViewAdapter extends
        RecyclerView.Adapter<ClientesRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = ClientesRecyclerViewAdapter.class.getSimpleName();

    private Context context;
    private List<Cliente> list;
    private OnItemClickListener onItemClickListener;

    public ClientesRecyclerViewAdapter(Context context, List<Cliente> list,
                                       OnItemClickListener onItemClickListener) {
        this.context = context;
        this.list = list;
        this.onItemClickListener = onItemClickListener;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_cliente_nome)
        public TextView textViewNomeCliente;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        public void bind(final Cliente model,
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

        View view = inflater.inflate(R.layout.recycler_view_cliente_item_layout, parent, false);
        ButterKnife.bind(this, view);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Cliente item = list.get(position);
        holder.textViewNomeCliente.setText(item.getNome());
        holder.bind(item, onItemClickListener);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

}