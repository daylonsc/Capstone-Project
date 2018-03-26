package br.android.com.mevenda.bean;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by daylo on 17/03/2018.
 */

public class Produto extends RealmObject implements Parcelable {
    @PrimaryKey
    private int id;
    private String nome;
    private int quantidadeEstoque;
    private double preco;

    //Atributo virtual para facilitar escolha das quantidades do pedido
    @Ignore
    private int quantidadePedido;

    public Produto() {

    }

    protected Produto(Parcel in) {
        id = in.readInt();
        nome = in.readString();
        quantidadeEstoque = in.readInt();
        preco = in.readDouble();
        quantidadePedido = in.readInt();
    }

    public static final Creator<Produto> CREATOR = new Creator<Produto>() {
        @Override
        public Produto createFromParcel(Parcel in) {
            return new Produto(in);
        }

        @Override
        public Produto[] newArray(int size) {
            return new Produto[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void setQuantidadeEstoque(int quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public int getQuantidadePedido() {
        return quantidadePedido;
    }

    public void setQuantidadePedido(int quantidadePedido) {
        this.quantidadePedido = quantidadePedido;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(nome);
        parcel.writeInt(quantidadeEstoque);
        parcel.writeDouble(preco);
        parcel.writeInt(quantidadePedido);
    }
}
