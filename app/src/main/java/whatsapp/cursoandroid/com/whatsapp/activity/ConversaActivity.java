package whatsapp.cursoandroid.com.whatsapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import whatsapp.cursoandroid.com.whatsapp.R;
import whatsapp.cursoandroid.com.whatsapp.adapter.MensagemAdapter;
import whatsapp.cursoandroid.com.whatsapp.config.ConfiguracaoFirebase;
import whatsapp.cursoandroid.com.whatsapp.helper.Base64Custom;
import whatsapp.cursoandroid.com.whatsapp.helper.Preferencias;
import whatsapp.cursoandroid.com.whatsapp.model.Conversa;
import whatsapp.cursoandroid.com.whatsapp.model.Mensagem;

public class ConversaActivity extends AppCompatActivity {

    public static final String DGT_MSG = "Digite uma mensagem";
    private Toolbar toolbar;

    private String nomeDestinatario;
    private String idUsuarioDestinatario;

    private EditText mEditMensagem;
    private ImageButton mBtMensagem;

    private String idUsuarioRemetente;
    private String nomeUsuarioRemetente;

    private ListView mListView;

    private ArrayList<Mensagem> mensagens;
    private MensagemAdapter adapter;

    private ValueEventListener valueEventListener;

    private DatabaseReference firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversa);

        toolbar = (Toolbar) findViewById(R.id.tb_conversa);
        mEditMensagem = (EditText) findViewById(R.id.et_mensagem);
        mBtMensagem = (ImageButton) findViewById(R.id.bt_enviar);
        mListView = (ListView) findViewById(R.id.lv_conversas);

        Preferencias preferencias = new Preferencias(ConversaActivity.this);
        idUsuarioRemetente = preferencias.getIdentificador();
        nomeUsuarioRemetente = preferencias.getNome();

        Bundle extra = getIntent().getExtras();

        if (extra != null) {
            nomeDestinatario = extra.getString("nome");
            String emailDestinatario = extra.getString("email");
            idUsuarioDestinatario = Base64Custom.encoderBase64(emailDestinatario);
        }

        toolbar.setTitle(nomeDestinatario);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbar);

        mensagens = new ArrayList<>();

        adapter = new MensagemAdapter(ConversaActivity.this, mensagens);

        mListView.setAdapter(adapter);

        firebase = ConfiguracaoFirebase.getFirebase()
                .child("mensagens")
                .child(idUsuarioRemetente)
                .child(idUsuarioDestinatario);

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mensagens.clear();

                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Mensagem mensagem = dados.getValue(Mensagem.class);
                    mensagens.add(mensagem);
                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        firebase.addValueEventListener(valueEventListener);

        mBtMensagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String textoMensagem = mEditMensagem.getText().toString();

                if (!textoMensagem.isEmpty()) {
                    Mensagem mensagem = new Mensagem();
                    mensagem.setIdUsuario(idUsuarioRemetente);
                    mensagem.setMensagem(textoMensagem);

                    Boolean retornoRemetente = salvarMensagem(idUsuarioRemetente, idUsuarioDestinatario, mensagem);

                    if (!retornoRemetente) {
                        Toast.makeText(
                                ConversaActivity.this,
                                "Problema ao salvar mensagem, tente novamente!",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Boolean retornoDestinatario = salvarMensagem(idUsuarioDestinatario, idUsuarioRemetente, mensagem);

                        if (!retornoRemetente) {
                            Toast.makeText(
                                    ConversaActivity.this,
                                    "Problema ao salvar mensagem do destinatário, tente novamente!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    Conversa conversa = new Conversa();
                    conversa.setIdUsuario(idUsuarioDestinatario);
                    conversa.setNome(nomeDestinatario);
                    conversa.setMensagem(textoMensagem);

                    Boolean retornoConversaRemetente =
                            salvarConversa(idUsuarioRemetente, idUsuarioDestinatario, conversa);

                    if(!retornoConversaRemetente) {
                        Toast.makeText(
                                ConversaActivity.this,
                                "Problema ao salvar a conversa, tente novamente",
                                Toast.LENGTH_SHORT).show();
                    } else {

                        conversa = new Conversa();
                        conversa.setIdUsuario(idUsuarioRemetente);
                        conversa.setNome(nomeUsuarioRemetente);
                        conversa.setMensagem(textoMensagem);

                        Boolean retornoConversaDestinatario =
                                salvarConversa(idUsuarioDestinatario, idUsuarioRemetente, conversa);

                        if(!retornoConversaDestinatario) {
                            Toast.makeText(
                                    ConversaActivity.this,
                                    "Problema ao salvar a conversa do destinatário, tente novamente",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    mEditMensagem.setText("");

                } else {
                    Toast.makeText(ConversaActivity.this, DGT_MSG, Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private boolean salvarMensagem(String idRemetente, String idDestinatario, Mensagem mensagem) {
        try {

            firebase = ConfiguracaoFirebase.getFirebase().child("mensagens");
            firebase.child(idRemetente).child(idDestinatario).push().setValue(mensagem);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean salvarConversa(String idRemetente, String idDestinatario, Conversa conversa) {
        try {

            firebase = ConfiguracaoFirebase.getFirebase().child("conversas");
            firebase.child(idRemetente).child(idDestinatario).setValue(conversa);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebase.removeEventListener(valueEventListener);
    }
}
