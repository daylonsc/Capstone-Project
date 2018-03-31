package br.android.com.mevenda.adapters;

import android.content.Context;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.android.com.mevenda.R;
import br.android.com.mevenda.Utils.Utils;
import br.android.com.mevenda.bean.Produto;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by daylo on 23/03/2018.
 */
public class ProdutosCarrinhoRecyclerViewAdapter extends
        RecyclerView.Adapter<ProdutosCarrinhoRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = ProdutosCarrinhoRecyclerViewAdapter.class.getSimpleName();

    private Context context;
    private static List<Produto> list;
    private static List<Produto> listCarrinho;
    private OnItemClickListener onItemClickListener;
    private Parcelable layoutManagerSavedState;
    private RecyclerView recyclerView;

    public ProdutosCarrinhoRecyclerViewAdapter(Context context, List<Produto> list, Parcelable layoutManagerSavedState, RecyclerView recyclerView,
                                               OnItemClickListener onItemClickListener) {
        this.context = context;
        this.list = list;
        this.onItemClickListener = onItemClickListener;
        listCarrinho = new ArrayList<Produto>();
        this.layoutManagerSavedState = layoutManagerSavedState;
        this.recyclerView = recyclerView;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textViewQuantidade)
        public TextView textViewQuantidade;

        @BindView(R.id.textViewNomeProduto)
        public TextView textViewNomeProduto;

        @BindView(R.id.textViewPreco)
        public TextView textViewPreco;

        @BindView(R.id.imageViewMais)
        public ImageView imageViewMais;

        @BindView(R.id.imageViewMenos)
        public ImageView imageViewMenos;

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

        private boolean produtoJaEstanoCarrinho(Produto produto) {
            for (Produto p : listCarrinho) {
                if (p.getId() == produto.getId()) {
                    return true;
                }
            }
            return false;
        }

        @OnClick(R.id.imageViewMais)
        public void addItem(View view) {
            String quantidade = textViewQuantidade.getText().toString();
            Produto produto = (Produto) getItem(getLayoutPosition());
            produto.setQuantidadePedido(Integer.valueOf(quantidade) + 1);

            if (produtoJaEstanoCarrinho(produto)) {
                listCarrinho.remove(produto);
                listCarrinho.add(produto);
            } else {
                listCarrinho.add(produto);
            }

            textViewQuantidade.setText(String.valueOf(produto.getQuantidadePedido()));
        }

        @OnClick(R.id.imageViewMenos)
        public void removeItem(View view) {
            String quantidade = textViewQuantidade.getText().toString();
            Produto produto = (Produto) getItem(getLayoutPosition());
            if (!quantidade.equals("0")) {
                produto.setQuantidadePedido(Integer.valueOf(quantidade) - 1);

                if (produto.getQuantidadePedido() == 0) {
                    listCarrinho.remove(produto);
                } else {
                    if (produtoJaEstanoCarrinho(produto)) {
                        listCarrinho.remove(produto);
                        listCarrinho.add(produto);
                    }
                }

                textViewQuantidade.setText(String.valueOf(produto.getQuantidadePedido()));
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.recycler_view_carrinho_produto_item_layout, parent, false);
        ButterKnife.bind(this, view);

        ViewHolder viewHolder = new ViewHolder(view);

        restoreLayoutManagerPosition();

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Produto item = list.get(position);
        holder.textViewNomeProduto.setText(item.getNome());
        holder.textViewPreco.setText(R.string.label_cifrao + Utils.converterDoubleToMonetario(item.getPreco()));
        holder.bind(item, onItemClickListener);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static Object getItem(int position) {
        return list.get(position);
    }

    public List<Produto> getItensCarrinho() {
        return listCarrinho;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private void restoreLayoutManagerPosition() {
        if (layoutManagerSavedState != null) {
            recyclerView.getLayoutManager().onRestoreInstanceState(layoutManagerSavedState);
        }
    }

}