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
import br.android.com.mevenda.adapters.ClientesRecyclerViewAdapter;
import br.android.com.mevenda.adapters.ProdutosRecyclerViewAdapter;
import br.android.com.mevenda.bean.Cliente;
import br.android.com.mevenda.bean.Produto;
import io.realm.Realm;

public class ClienteListActivity extends AppCompatActivity {

    private final Class classCadastro = Cliente.class;
    private RecyclerView mRecyclerView;
    private ClientesRecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cadastrarCliente(view);
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

    private void cadastrarCliente(View view) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();

        Cliente cliente = new Cliente();
        int id = Utils.getNextId(classCadastro);

        cliente.setId(id);
        cliente.setNome("Cliente teste " +id);
        cliente.setEmail("email@email.com");
        cliente.setTelefone("91999999999");

        realm.copyToRealm(cliente);
        realm.commitTransaction();
        realm.close();
        Snackbar.make(view, "Cliente inserido com sucesso!", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        atualizarLista();
    }


    private void atualizarLista() {
        Realm realm = Realm.getDefaultInstance();
        List<Cliente> list = realm.where(classCadastro).findAll();

        mAdapter = new ClientesRecyclerViewAdapter(this, list, new ClientesRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent=new Intent();
                Cliente cliente = (Cliente) mAdapter.getItem(position);

                intent.putExtra(FPedidoActivity.CLIENTE_PARAMETER, cliente);
                setResult(2,intent);
                finish();
            }
        });
        mRecyclerView = (RecyclerView) findViewById(R.id.cliente_recycler_view);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

}
