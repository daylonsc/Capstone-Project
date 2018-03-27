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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.android.com.mevenda.Utils.LibraryClass;
import br.android.com.mevenda.Utils.Utils;
import br.android.com.mevenda.adapters.PedidosRecyclerViewAdapter;
import br.android.com.mevenda.adapters.ProdutosRecyclerViewAdapter;
import br.android.com.mevenda.bean.Cliente;
import br.android.com.mevenda.bean.Pedido;
import br.android.com.mevenda.bean.Produto;
import io.realm.Realm;

public class PedidoListActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private PedidosRecyclerViewAdapter mAdapter;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListener;
    private List<Pedido> pedidos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        pedidos = new ArrayList<Pedido>();

        atualizarLista();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                irParaCadastroPedido();
            }
        });
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

        mAdapter = new PedidosRecyclerViewAdapter(this, pedidos, new PedidosRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }
        });
        mRecyclerView = (RecyclerView) findViewById(R.id.pedido_recycler_view);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        firebase = LibraryClass.getFirebase().child("pedidos");

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pedidos.clear();

                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Pedido pedido = dados.getValue(Pedido.class);
                    pedidos.add(pedido);
                }

                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

    }

    @Override
    public void onResume() {
        super.onResume();
        firebase.addValueEventListener(valueEventListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebase.removeEventListener(valueEventListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebase.addValueEventListener(valueEventListener);
    }

}
