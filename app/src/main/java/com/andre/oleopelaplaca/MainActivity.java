package com.andre.oleopelaplaca;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends Activity {
    private EditText placa;
    private TextView resultado;
    private final Map<String,String> salvos = new HashMap<>();

    @Override public void onCreate(Bundle b) {
        super.onCreate(b);
        getWindow().setStatusBarColor(Color.rgb(8,16,24));
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL); root.setPadding(42,52,42,42);
        root.setBackgroundColor(Color.rgb(8,16,24));

        TextView titulo = text("ÓLEO PELA PLACA",28,Color.WHITE); titulo.setTypeface(null,1); root.addView(titulo);
        TextView sub = text("Consulte e salve a especificação de óleo do veículo",15,Color.rgb(160,174,192)); sub.setPadding(0,8,0,34); root.addView(sub);

        placa = new EditText(this); placa.setHint("ABC1D23"); placa.setTextColor(Color.WHITE); placa.setHintTextColor(Color.GRAY); placa.setTextSize(24); placa.setGravity(Gravity.CENTER); placa.setSingleLine(); placa.setAllCaps(true); placa.setFilters(new InputFilter[]{new InputFilter.LengthFilter(7)}); root.addView(placa,new LinearLayout.LayoutParams(-1,150));

        Button consultar = button("CONSULTAR PLACA"); root.addView(consultar,new LinearLayout.LayoutParams(-1,140));
        resultado = text("Digite uma placa para começar.",17,Color.WHITE); resultado.setPadding(12,32,12,28); root.addView(resultado);

        TextView aviso = text("Versão gratuita: o app guarda localmente as placas cadastradas. A identificação automática de qualquer placa depende de uma fonte/API autorizada.",13,Color.rgb(148,163,184)); aviso.setPadding(0,24,0,10); root.addView(aviso);
        consultar.setOnClickListener(v -> consultar());
        setContentView(root);
    }

    private void consultar(){
        String p=placa.getText().toString().replaceAll("[^A-Za-z0-9]","").toUpperCase(Locale.ROOT);
        if(p.length()!=7){ resultado.setText("Placa inválida. Digite os 7 caracteres."); return; }
        if(salvos.containsKey(p)){ resultado.setText("PLACA: "+p+"\n\n"+salvos.get(p)); return; }
        abrirCadastro(p);
    }

    private void abrirCadastro(String p){
        LinearLayout box=new LinearLayout(this); box.setOrientation(LinearLayout.VERTICAL); box.setPadding(30,10,30,0);
        EditText veiculo=field("Veículo / motor (ex.: Celta 1.0 Flex)");
        EditText oleo=field("Óleo (ex.: SAE 5W-30)");
        EditText norma=field("Norma (ex.: API SN / Dexos 1)");
        EditText litros=field("Capacidade (ex.: 3,5 L com filtro)");
        box.addView(veiculo); box.addView(oleo); box.addView(norma); box.addView(litros);
        new android.app.AlertDialog.Builder(this).setTitle("Cadastrar "+p).setView(box)
          .setMessage("Placa ainda não cadastrada. Salve a ficha técnica para consultas futuras.")
          .setNegativeButton("Cancelar",null)
          .setPositiveButton("Salvar",(d,w)->{
             String ficha="VEÍCULO: "+veiculo.getText()+"\nÓLEO: "+oleo.getText()+"\nNORMA: "+norma.getText()+"\nCAPACIDADE: "+litros.getText();
             salvos.put(p,ficha); resultado.setText("PLACA: "+p+"\n\n"+ficha+"\n\n✓ Salvo nesta sessão.");
          }).show();
    }

    private EditText field(String h){ EditText e=new EditText(this); e.setHint(h); e.setSingleLine(); return e; }
    private TextView text(String s,int z,int c){ TextView t=new TextView(this); t.setText(s); t.setTextSize(z); t.setTextColor(c); return t; }
    private Button button(String s){ Button b=new Button(this); b.setText(s); b.setTextSize(17); b.setTextColor(Color.rgb(20,20,20)); return b; }
}
