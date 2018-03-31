package br.android.com.mevenda.interfaces;

import java.util.List;

import br.android.com.mevenda.bean.Cliente;

public interface OnEventListener<T> {
    public void onSuccess(List<Cliente> list);
    public void onFailure(Exception e);
}
