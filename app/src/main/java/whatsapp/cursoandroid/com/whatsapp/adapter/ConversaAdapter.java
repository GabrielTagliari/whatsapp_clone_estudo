package whatsapp.cursoandroid.com.whatsapp.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import whatsapp.cursoandroid.com.whatsapp.R;
import whatsapp.cursoandroid.com.whatsapp.model.Conversa;

/**
 * Created by gabriel on 8/3/17.
 */

public class ConversaAdapter extends ArrayAdapter {

    private ArrayList<Conversa> conversas;
    private Context context;

    public ConversaAdapter(@NonNull Context c, @NonNull ArrayList<Conversa> objects) {
        super(c, 0, objects);
        this.conversas = objects;
        this.context = c;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = null;

        if (conversas != null) {
            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.lista_conversa, parent, false);

            TextView nomeContato = (TextView) view.findViewById(R.id.tv_nome);
            TextView mensagemContato = (TextView) view.findViewById(R.id.tv_ultima_mensagem);

            Conversa conversa = conversas.get(position);
            nomeContato.setText(conversa.getNome());
            mensagemContato.setText(conversa.getMensagem());
        }

        return view;
    }
}
