package br.android.com.mevenda.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by daylo on 17/03/2018.
 */


public class Cliente implements Parcelable{

    private String id;
    private String nome;
    private String email;
    private String telefone;

    public Cliente() {
    }

    protected Cliente(Parcel in) {
        id = in.readString();
        nome = in.readString();
        email = in.readString();
        telefone = in.readString();
    }

    public static final Creator<Cliente> CREATOR = new Creator<Cliente>() {
        @Override
        public Cliente createFromParcel(Parcel in) {
            return new Cliente(in);
        }

        @Override
        public Cliente[] newArray(int size) {
            return new Cliente[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(nome);
        parcel.writeString(email);
        parcel.writeString(telefone);
    }
}
