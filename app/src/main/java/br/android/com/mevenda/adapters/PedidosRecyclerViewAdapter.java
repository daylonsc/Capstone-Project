package br.android.com.mevenda.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.android.com.mevenda.R;
import br.android.com.mevenda.Utils.Utils;
import br.android.com.mevenda.bean.Pedido;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.internal.Util;

/**
 * Created by daylo on 17/03/2018.
 */
public class PedidosRecyclerViewAdapter extends
        RecyclerView.Adapter<PedidosRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = PedidosRecyclerViewAdapter.class.getSimpleName();

    private Context context;
    private List<Pedido> list;
    private OnItemClickListener onItemClickListener;

    public PedidosRecyclerViewAdapter(Context context, List<Pedido> list,
                                      OnItemClickListener onItemClickListener) {
        this.context = context;
        this.list = list;
        this.onItemClickListener = onItemClickListener;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_pedido_cliente_nome)
        public TextView textViewPedidoClienteNome;

        @BindView(R.id.tv_pedido_data)
        public TextView textViewPedidoData;

        @BindView(R.id.tv_pedido_valor)
        public TextView textViewPedidoValor;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        public void bind(final Pedido model,
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

        View view = inflater.inflate(R.layout.recycler_view_pedido_item_layout, parent, false);
        ButterKnife.bind(this, view);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Pedido item = list.get(position);
        holder.textViewPedidoClienteNome.setText(item.getCliente().getNome());
        holder.textViewPedidoData.setText(Utils.format(item.getDataEmissao(), null));
        holder.textViewPedidoValor.setText("R$ " + Utils.converterDoubleToMonetario(item.getValorTotal()) );

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