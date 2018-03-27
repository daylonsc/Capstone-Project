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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.android.com.mevenda.Utils.LibraryClass;
import br.android.com.mevenda.Utils.Utils;
import br.android.com.mevenda.adapters.ClientesRecyclerViewAdapter;
import br.android.com.mevenda.adapters.ProdutosRecyclerViewAdapter;
import br.android.com.mevenda.bean.Cliente;
import br.android.com.mevenda.bean.Produto;
import io.realm.Realm;

public class ClienteListActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ClientesRecyclerViewAdapter mAdapter;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListener;
    private List<Cliente> clientes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        clientes = new ArrayList<Cliente>();

        atualizarLista();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cadastrarCliente(view);
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

    private void cadastrarCliente(View view) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("clientes");
        String id = mDatabase.push().getKey();
        Cliente cliente = new Cliente();
        cliente.setNome("Cliente teste ");
        cliente.setEmail("email@email.com");
        cliente.setTelefone("91999999999");

        cliente.setId(id);

        try {
            firebase.child(cliente.getId()).setValue(cliente);

        } catch (Exception e) {
            e.printStackTrace();
        }

        Snackbar.make(view, "Cliente inserido com sucesso!", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        atualizarLista();
    }


    private void atualizarLista() {

        mAdapter = new ClientesRecyclerViewAdapter(this, clientes, new ClientesRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent();
                Cliente cliente = (Cliente) mAdapter.getItem(position);

                intent.putExtra(FPedidoActivity.CLIENTE_PARAMETER, cliente);
                setResult(2, intent);
                finish();
            }
        });
        mRecyclerView = (RecyclerView) findViewById(R.id.cliente_recycler_view);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        firebase = LibraryClass.getFirebase().child("clientes");

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                clientes.clear();

                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Cliente cliente = dados.getValue(Cliente.class);
                    clientes.add(cliente);
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
