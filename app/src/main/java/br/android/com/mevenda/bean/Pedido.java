package br.android.com.mevenda.bean;

import android.content.ClipData;
import android.support.v4.app.NotificationCompat;

import java.util.Date;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by daylo on 17/03/2018.
 */

public class Pedido extends RealmObject {
    @PrimaryKey
    private int id;
    private Cliente cliente;
    @Required
    private RealmList<ItemPedido> itemPedidoList;
    @Required
    private Date dataEmissao;
    private double valorTotal;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public RealmList<ItemPedido> getItemPedidoList() {
        return itemPedidoList;
    }

    public void setItemPedidoList(RealmList<ItemPedido> itemPedidoList) {
        this.itemPedidoList = itemPedidoList;
    }

    public Date getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(Date dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }
}
