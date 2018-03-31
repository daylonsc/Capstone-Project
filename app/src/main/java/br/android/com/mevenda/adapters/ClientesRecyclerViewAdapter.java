package br.android.com.mevenda.adapters;

import android.content.Context;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.android.com.mevenda.R;
import br.android.com.mevenda.bean.Cliente;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by daylo on 17/03/2018.
 */
public class ClientesRecyclerViewAdapter extends
        RecyclerView.Adapter<ClientesRecyclerViewAdapter.ViewHolder> implements Filterable {

    private static final String TAG = ClientesRecyclerViewAdapter.class.getSimpleName();

    private Context context;
    private List<Cliente> list;
    private OnItemClickListener onItemClickListener;
    private Parcelable layoutManagerSavedState;
    private RecyclerView recyclerView;

    private List<Cliente> originalData;
    private List<Cliente> filteredData;

    public ClientesRecyclerViewAdapter(Context context, List<Cliente> list, Parcelable layoutManagerSavedState, RecyclerView recyclerView,
                                       OnItemClickListener onItemClickListener) {
        this.context = context;
        this.list = list;
        this.onItemClickListener = onItemClickListener;
        this.layoutManagerSavedState = layoutManagerSavedState;
        this.recyclerView = recyclerView;

        originalData = list;
        filteredData = list;
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

        restoreLayoutManagerPosition();

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

    private void restoreLayoutManagerPosition() {
        if (layoutManagerSavedState != null) {
            recyclerView.getLayoutManager().onRestoreInstanceState(layoutManagerSavedState);
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults results = new FilterResults();

                //If there's nothing to filter on, return the original data for your list
                if (charSequence == null || charSequence.length() == 0) {
                    results.values = originalData;
                    results.count = originalData.size();
                } else {
                    List<Cliente> filterResultsData = new ArrayList<Cliente>();

                    for (Cliente c : originalData) {
                        if (c.getNome().toUpperCase().contains(charSequence.toString().toUpperCase())) {
                            filterResultsData.add(c);
                        }
                    }

                    results.values = filterResultsData;
                    results.count = filterResultsData.size();
                }

                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                list = (List<Cliente>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}