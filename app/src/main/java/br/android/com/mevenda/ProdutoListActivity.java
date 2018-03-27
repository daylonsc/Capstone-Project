package br.android.com.mevenda;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.android.com.mevenda.Utils.LibraryClass;
import br.android.com.mevenda.Utils.Utils;
import br.android.com.mevenda.adapters.ProdutosCarrinhoRecyclerViewAdapter;
import br.android.com.mevenda.adapters.ProdutosRecyclerViewAdapter;
import br.android.com.mevenda.bean.Cliente;
import br.android.com.mevenda.bean.Produto;
import io.realm.Realm;

public class ProdutoListActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ProdutosRecyclerViewAdapter mAdapter;
    private ProdutosCarrinhoRecyclerViewAdapter mAdapterCarrinho;
    private boolean isFromPedido;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListener;
    private List<Produto> produtos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produto_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        produtos = new ArrayList<Produto>();

        isFromPedido = (boolean) getIntent().getSerializableExtra(FPedidoActivity.IS_FROM_PEDIDO);

        atualizarLista();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cadastrarProduto(view);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.produto_carrinho, menu);

        MenuItem menuItem = menu.findItem(R.id.action_save);
        if (!isFromPedido) {
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_save:
                Intent intent = new Intent();
                List<Produto> produtoList = mAdapterCarrinho.getItensCarrinho();

                intent.putParcelableArrayListExtra(FPedidoActivity.LIST_PRODUTO_CARRINHO_PARAMETER, (ArrayList<? extends Parcelable>) produtoList);
                setResult(3, intent);
                finish();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void cadastrarProduto(View view) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("produtos");
        String id = mDatabase.push().getKey();
        Produto produto = new Produto();
        produto.setId(id);
        produto.setNome("Produto Teste " + id);
        produto.setQuantidadeEstoque(2);
        produto.setPreco(50);

        try {
            firebase.child(produto.getId()).setValue(produto);

        } catch (Exception e) {
            e.printStackTrace();
        }
        Snackbar.make(view, "Produto inserido com sucesso!", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        atualizarLista();
    }

    private void atualizarLista() {
        if (isFromPedido) {
            mAdapterCarrinho = new ProdutosCarrinhoRecyclerViewAdapter(this, produtos, new ProdutosCarrinhoRecyclerViewAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {

                }
            });
        } else {
            mAdapter = new ProdutosRecyclerViewAdapter(this, produtos, new ProdutosRecyclerViewAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {

                }
            });
        }
        mRecyclerView = findViewById(R.id.produto_recycler_view);
        mRecyclerView.setAdapter(isFromPedido ? mAdapterCarrinho : mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        firebase = LibraryClass.getFirebase().child("produtos");

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                produtos.clear();

                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Produto produto = dados.getValue(Produto.class);
                    produtos.add(produto);
                }
                if (isFromPedido) {
                    mAdapterCarrinho.notifyDataSetChanged();
                } else {
                    mAdapter.notifyDataSetChanged();
                }

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
