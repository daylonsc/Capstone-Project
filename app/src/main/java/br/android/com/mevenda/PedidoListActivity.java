package br.android.com.mevenda;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

import br.android.com.mevenda.Utils.Utils;
import br.android.com.mevenda.adapters.PedidosRecyclerViewAdapter;
import br.android.com.mevenda.adapters.ProdutosRecyclerViewAdapter;
import br.android.com.mevenda.bean.Pedido;
import br.android.com.mevenda.bean.Produto;
import io.realm.Realm;

public class PedidoListActivity extends AppCompatActivity {

    private final Class classCadastro = Pedido.class;
    private RecyclerView mRecyclerView;
    private PedidosRecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                irParaCadastroPedido();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        atualizarLista();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void irParaCadastroPedido() {
        Intent intent = new Intent(this, FPedidoActivity.class);
        startActivity(intent);
    }

    private void atualizarLista() {
        Realm realm = Realm.getDefaultInstance();
        List<Pedido> list = realm.where(classCadastro).findAll();

        mAdapter = new PedidosRecyclerViewAdapter(this, list, new PedidosRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }
        });
        mRecyclerView = (RecyclerView) findViewById(R.id.pedido_recycler_view);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

}
