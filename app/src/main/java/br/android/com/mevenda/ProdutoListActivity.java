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

import java.util.ArrayList;
import java.util.List;

import br.android.com.mevenda.Utils.Utils;
import br.android.com.mevenda.adapters.ProdutosCarrinhoRecyclerViewAdapter;
import br.android.com.mevenda.adapters.ProdutosRecyclerViewAdapter;
import br.android.com.mevenda.bean.Cliente;
import br.android.com.mevenda.bean.Produto;
import io.realm.Realm;

public class ProdutoListActivity extends AppCompatActivity {

    private final Class classCadastro = Produto.class;
    private RecyclerView mRecyclerView;
    private ProdutosRecyclerViewAdapter mAdapter;
    private ProdutosCarrinhoRecyclerViewAdapter mAdapterCarrinho;
    private boolean isFromPedido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produto_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        isFromPedido = (boolean) getIntent().getSerializableExtra(FPedidoActivity.IS_FROM_PEDIDO);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cadastrarProduto(view);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        atualizarLista();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.produto_carrinho, menu);
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
                List<Produto> produtoList =  mAdapterCarrinho.getItensCarrinho();

                intent.putParcelableArrayListExtra(FPedidoActivity.LIST_PRODUTO_CARRINHO_PARAMETER, (ArrayList<? extends Parcelable>) produtoList);
                setResult(3, intent);
                finish();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void cadastrarProduto(View view) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();

        Produto produto = new Produto();
        int id = Utils.getNextId(classCadastro);
        produto.setId(id);
        produto.setNome("Produto Teste " + id);
        produto.setQuantidadeEstoque(2 + id);
        produto.setPreco(50);

        realm.copyToRealm(produto);
        realm.commitTransaction();
        realm.close();
        Snackbar.make(view, "Produto inserido com sucesso!", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        atualizarLista();
    }

    private void atualizarLista() {
        Realm realm = Realm.getDefaultInstance();
        List<Produto> list = realm.where(classCadastro).findAll();
        if (isFromPedido) {
            mAdapterCarrinho = new ProdutosCarrinhoRecyclerViewAdapter(this, list, new ProdutosCarrinhoRecyclerViewAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {

                }
            });
        } else {
            mAdapter = new ProdutosRecyclerViewAdapter(this, list, new ProdutosRecyclerViewAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {

                }
            });
        }
        mRecyclerView = (RecyclerView) findViewById(R.id.produto_recycler_view);
        mRecyclerView.setAdapter(isFromPedido ? mAdapterCarrinho : mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
}
