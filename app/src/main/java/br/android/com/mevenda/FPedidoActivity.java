package br.android.com.mevenda;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.android.com.mevenda.Utils.LibraryClass;
import br.android.com.mevenda.Utils.Utils;
import br.android.com.mevenda.bean.Cliente;
import br.android.com.mevenda.bean.ItemPedido;
import br.android.com.mevenda.bean.Pedido;
import br.android.com.mevenda.bean.Produto;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmList;

public class FPedidoActivity extends AppCompatActivity {
    public final static String CLIENTE_PARAMETER = "cliente";
    public final static String IS_FROM_PEDIDO = "isFromPedido";
    public final static String LIST_PRODUTO_CARRINHO_PARAMETER = "Lista de produtos do carrinho";
    private Cliente clienteSelecionado;
    private DatabaseReference firebase;

    @BindView(R.id.buttonCliente)
    Button buttonCliente;

    @BindView(R.id.buttonProduto)
    Button buttonProduto;

    @BindView(R.id.buttonMostrarItens)
    Button buttonMostrarItens;

    @BindView(R.id.tv_quantidade_total_itens)
    TextView tvQuantidadeTotalItens;

    @BindView(R.id.tv_valor_total_itens)
    TextView tvValorTotalItens;

    private List<Produto> produtoList;

    private boolean mostrandoProdutos = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fpedido);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                showDialogSair();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick(R.id.buttonGerarPedido)
    public void gerarPedido(View view) {
        if (validarCampos(view)) {
            firebase = LibraryClass.getFirebase().child("pedidos");
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("pedidos");
            String id = mDatabase.push().getKey();

            Pedido pedido = new Pedido();
            pedido.setId(id);
            pedido.setCliente(clienteSelecionado);
            pedido.setDataEmissao(new Date());
            pedido.setItemPedidoList(criarItensPedidos());
            pedido.setValorTotal(getValorTotalPedido());

            try {
                firebase.child(pedido.getId()).setValue(pedido);

            } catch (Exception e) {
                e.printStackTrace();
            }

            finish();
        }
    }

    private double getValorTotalPedido() {
        double valorTotal = 0d;
        for (Produto p : produtoList) {
            valorTotal += p.getPreco() * p.getQuantidadePedido();
        }
        return valorTotal;
    }

    private List<ItemPedido> criarItensPedidos() {
        List<ItemPedido> itemPedidos = new ArrayList<ItemPedido>();
        for (Produto p : produtoList) {
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("itemPedidos");
            String id = mDatabase.push().getKey();

            ItemPedido itemPedido = new ItemPedido();

            itemPedido.setId(id);
            itemPedido.setProduto(p);
            itemPedido.setQuantidade(p.getQuantidadePedido());
            itemPedido.setValorUnitario(p.getPreco());
            itemPedido.setValorTotal(p.getPreco() * p.getQuantidadePedido());
            itemPedidos.add(itemPedido);
        }
        return itemPedidos;
    }

    private boolean validarCampos(View view) {
        if (clienteSelecionado == null) {
            Snackbar.make(view, "Selecione um cliente!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return false;
        } else if (produtoList == null || produtoList.isEmpty()) {
            Snackbar.make(view, "Adicione produtos ao pedido!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return false;
        }
        return true;
    }

    @OnClick(R.id.buttonCliente)
    public void getCliente() {
        Intent intent = new Intent(FPedidoActivity.this, ClienteListActivity.class);
        startActivityForResult(intent, 2);
    }

    @OnClick(R.id.buttonProduto)
    public void getProduto() {
        Intent intent = new Intent(FPedidoActivity.this, ProdutoListActivity.class);
        intent.putExtra(IS_FROM_PEDIDO, true);
        startActivityForResult(intent, 3);
    }

    @OnClick(R.id.buttonMostrarItens)
    public void mostrarItens() {
        TableLayout tabelaItens = (TableLayout) findViewById(R.id.tabelaItens);
        if (!mostrandoProdutos && produtoList != null && !produtoList.isEmpty()) {
            for (Produto p : produtoList) {
                TableRow row1 = (TableRow) LayoutInflater.from(FPedidoActivity.this).inflate(R.layout.attrib_row_itens, null);
                TableRow row2 = (TableRow) LayoutInflater.from(FPedidoActivity.this).inflate(R.layout.attrib_row_2_itens, null);

                ((TextView) row1.findViewById(R.id.attrib_name_produto)).setText(p.getNome());

                ((TextView) row2.findViewById(R.id.attrib_quantidade)).setText("Qtde: " + String.valueOf(p.getQuantidadePedido()));
                ((TextView) row2.findViewById(R.id.attrib_preco)).setText("R$ " + Utils.converterDoubleToMonetario(p.getPreco()));

                tabelaItens.addView(row1);
                tabelaItens.addView(row2);
            }
            mostrandoProdutos = true;
            buttonMostrarItens.setText(R.string.action_ocultar_itens);
        } else {
            if (produtoList != null && !produtoList.isEmpty()) {
                ocultarItens();
            }
        }
    }

    private void ocultarItens() {
        TableLayout tabelaItens = (TableLayout) findViewById(R.id.tabelaItens);
        mostrandoProdutos = false;
        buttonMostrarItens.setText(R.string.action_mostrar_itens);
        tabelaItens.removeAllViews();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 2) {
            clienteSelecionado = data.getParcelableExtra(CLIENTE_PARAMETER);
            buttonCliente.setText(clienteSelecionado.getNome());
        } else if (resultCode == 3) {
            produtoList = data.getParcelableArrayListExtra(LIST_PRODUTO_CARRINHO_PARAMETER);
            ocultarItens();
            atualizarTotais();

        }
    }

    public void showDialogSair() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Deseja realmente sair do pedido?")
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }

                        }
                ).setNegativeButton("Não", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }
        );
        builder.create().show();
    }

    private void atualizarTotais() {
        double valorTotal = 0d;
        valorTotal = getValorTotalPedido();
        tvQuantidadeTotalItens.setText(String.valueOf(produtoList.size()));
        tvValorTotalItens.setText("R$ " + Utils.converterDoubleToMonetario(valorTotal));
    }

    @Override
    public void onBackPressed() {
        showDialogSair();
    }

}
