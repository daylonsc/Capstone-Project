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
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.android.com.mevenda.Utils.Constantes;
import br.android.com.mevenda.Utils.LibraryClass;
import br.android.com.mevenda.adapters.ClientesRecyclerViewAdapter;
import br.android.com.mevenda.bean.Cliente;
import br.android.com.mevenda.interfaces.OnEventListener;
import br.android.com.mevenda.services.Task;
import butterknife.BindView;

public class ClienteListActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ClientesRecyclerViewAdapter mAdapter;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListener;
    private List<Cliente> clientes;

    private final String KEY_RECYCLER_STATE = "recycler_state";
    private static Bundle mBundleRecyclerViewState;
    private Parcelable layoutManagerSavedState;

    @BindView(R.id.pb_loading_indicator)
    ProgressBar progressBar;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.cliente_list, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Task task = new Task(s, clientes,ClienteListActivity.this, new OnEventListener() {
                    @Override
                    public void onFailure(Exception e) {

                    }

                    @Override
                    public void onSuccess(List list) {
                        clientes = list;
                        atualizarLista();
                    }
                });
                task.execute();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void cadastrarCliente(View view) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(Constantes.PARAMETER_FIREBASE_CLIENTE);
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

        Snackbar.make(view, R.string.mensagem_sucesso_cliente, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        atualizarLista();
    }

    private void atualizarLista() {

        setmAdapter();

        mRecyclerView = (RecyclerView) findViewById(R.id.cliente_recycler_view);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        firebase = LibraryClass.getFirebase().child(Constantes.PARAMETER_FIREBASE_CLIENTE);

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

    private void showListDataView() {
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();

        // restore RecyclerView state
        if (mBundleRecyclerViewState != null) {
            layoutManagerSavedState = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
            mRecyclerView.getLayoutManager().onRestoreInstanceState(layoutManagerSavedState);

            setmAdapter();

            mRecyclerView.setAdapter(mAdapter);
        }


        firebase.addValueEventListener(valueEventListener);
    }

    private void setmAdapter() {
        mAdapter = new ClientesRecyclerViewAdapter(this, clientes, layoutManagerSavedState, mRecyclerView, new ClientesRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent();
                Cliente cliente = (Cliente) mAdapter.getItem(position);

                intent.putExtra(FPedidoActivity.CLIENTE_PARAMETER, cliente);
                setResult(2, intent);
                finish();
            }
        });
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

    @Override
    protected void onPause() {
        super.onPause();
        // save RecyclerView state
        mBundleRecyclerViewState = new Bundle();
        Parcelable listState = mRecyclerView.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);
    }

}
