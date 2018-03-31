package br.android.com.mevenda.services;

import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import br.android.com.mevenda.bean.Cliente;
import br.android.com.mevenda.interfaces.OnEventListener;

public class Task extends AsyncTask<Void, Void, String> {
    private OnEventListener mCallBack;
    private Context mContext;
    private List<Cliente> mClientes;
    private List<Cliente> result;
    private String mBusca;

    public Task(String busca, List<Cliente> clientes, Context context, OnEventListener callback) {
        mCallBack = callback;
        mContext = context;
        mClientes = clientes;
        mBusca = busca;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... voids) {
        result = new ArrayList<Cliente>();
        for (Cliente c : mClientes) {
            if (c.getNome().toUpperCase().contains(mBusca.toString().toUpperCase())) {
                result.add(c);
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {

        if (mCallBack != null) {
            mCallBack.onSuccess(result);
        }
    }

}