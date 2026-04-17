package com.example.calculadorabasicaii;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.calculadorabasicaii.dao.CalcMemoDAO;
import com.example.calculadorabasicaii.model.CalcMemo;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView txtCalculo, txtDisplay;
    List<String> expressao;
    int posicao;
    private CalcMemoDAO calcMemoDAO;
    private int userId;
    private CalcMemo calcMemo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        txtCalculo = findViewById(R.id.txtCalculo);
        txtDisplay = findViewById(R.id.txtDisplay);
        userId = getIntent().getIntExtra("userId", -1);

        calcMemoDAO = new CalcMemoDAO(this);
        calcMemo = calcMemoDAO.findCalcMemo(userId);

        if(calcMemo != null){
            txtDisplay.setText(calcMemo.getDisplay());
            txtCalculo.setText(calcMemo.getCalculo());
            if(!calcMemo.getCalculo().equals("0") && !calcMemo.getDisplay().equals("0")) txtCalculo.setVisibility(TextView.VISIBLE);
        } else {
            txtDisplay.setText("0");
        }

        expressao = new ArrayList<>();
        expressao.add(txtDisplay.getText().toString().trim());
        posicao = 0;

        //operadores.
        Button btnSoma = findViewById(R.id.btnSoma);
        Button btnSubtracao = findViewById(R.id.btnSubtracao);
        Button btnMultiplicacao = findViewById(R.id.btnMultiplicacao);
        Button btnDivisao = findViewById(R.id.btnDivisao);

        //percents.
        Button btnPercent = findViewById(R.id.btnPercent);

        //numeros.
        Button btn1 = findViewById(R.id.btn1);
        Button btn2 = findViewById(R.id.btn2);
        Button btn3 = findViewById(R.id.btn3);
        Button btn4 = findViewById(R.id.btn4);
        Button btn5 = findViewById(R.id.btn5);
        Button btn6 = findViewById(R.id.btn6);
        Button btn7 = findViewById(R.id.btn7);
        Button btn8 = findViewById(R.id.btn8);
        Button btn9 = findViewById(R.id.btn9);
        Button btn0 = findViewById(R.id.btn0);

        //outros.
        Button btnVirgula = findViewById(R.id.btnVirgula);
        Button btnSinal = findViewById(R.id.btnSinal);
        Button btnDelete = findViewById(R.id.btnDelete);
        Button btnClear = findViewById(R.id.btnClear);

        //calcular.
        Button btnIgual = findViewById(R.id.btnIgual);

        //fechar.
        TextView btnFechar = findViewById(R.id.btnFechar);

        //cliques.

        //clicando clear.
        btnClear.setOnClickListener(v -> onClearClick());

        //clicando delete.
        btnDelete.setOnClickListener(v -> onDeleteClick());

        //clicando os numeros.
        btn1.setOnClickListener(v -> onNumberClick("1"));
        btn2.setOnClickListener(v -> onNumberClick("2"));
        btn3.setOnClickListener(v -> onNumberClick("3"));
        btn4.setOnClickListener(v -> onNumberClick("4"));
        btn5.setOnClickListener(v -> onNumberClick("5"));
        btn6.setOnClickListener(v -> onNumberClick("6"));
        btn7.setOnClickListener(v -> onNumberClick("7"));
        btn8.setOnClickListener(v -> onNumberClick("8"));
        btn9.setOnClickListener(v -> onNumberClick("9"));
        btn0.setOnClickListener(v -> onNumberClick("0"));

        //clicando virgula.
        btnVirgula.setOnClickListener(v -> onVirgulaClick());

        //clicando sinal.
        btnSinal.setOnClickListener(v -> onSinalClick());

        //clicando operadores.
        btnSoma.setOnClickListener(v -> onOperadorClick("+"));
        btnSubtracao.setOnClickListener(v -> onOperadorClick("-"));
        btnMultiplicacao.setOnClickListener(v -> onOperadorClick("*"));
        btnDivisao.setOnClickListener(v -> onOperadorClick("/"));
        btnPercent.setOnClickListener(v -> onOperadorClick("%"));

        //clicando para fechar o app.
        btnFechar.setOnClickListener(v -> UpdateMemo());

        //clicando igual.
        btnIgual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Obs.: versão atual permite apenas uma operação
                if (expressao.size() > 2) {
                    txtDisplay.setText("0");
                    txtCalculo.setText("possível só 1 operação.");
                    txtCalculo.setVisibility(TextView.VISIBLE);
                    expressao.clear();
                    expressao.add("0");
                    posicao = 0;
                    return;
                }

                String display = txtDisplay.getText().toString().trim();

                if (display.isEmpty() || display.equals("0") || display.matches(".*[+\\-/*,]$")) {
                    return;
                }

                if (txtCalculo.getVisibility() == View.VISIBLE) {
                    onClearClick();
                    return;
                }

                List<Double> valores = new ArrayList<>();
                String total = "";
                String op = "";
                int sequenciaTermos = 0;

                for (String termo : expressao) {

                    if (termo.isEmpty()) continue;

                    int qte_percent = termo.length() - termo.replace("%", "").length();
                    termo = termo.replaceAll("[()]", "").replace(",", ".");

                    if (termo.matches(".*[+\\-/*%]$")) {
                        op += termo.substring(termo.length() - 1);
                    }

                     if ((qte_percent > 1) && termo.endsWith("%")) {
                        termo = termo.substring(0, termo.indexOf("%"));
                        double num1 = Double.parseDouble(termo);
                        String depois = display.substring(display.lastIndexOf("%") + 1);
                        if (depois.isEmpty() || depois.matches("^[+\\-/*].*")) {
                            num1 = num1 * Math.pow(0.01, qte_percent);
                        } else {
                            num1 = num1 * Math.pow(0.01, qte_percent - 1);
                        }
                        valores.add(num1);
                        sequenciaTermos += 1;
                        continue;
                    }

                     termo = termo.replace("%", "").replaceAll("[+\\-/*]$", "");

                    if (!termo.isEmpty()) {
                        double num = Double.parseDouble(termo);
                        if (qte_percent == 1) {
                            String depois = display.substring(display.indexOf("%") + 1);
                            if (depois.isEmpty() || depois.matches("^[+\\-/*].*")) {
                                num = num * 0.01;
                            }
                        }
                        valores.add(sequenciaTermos, num);
                    }

                    sequenciaTermos += 1;
                }

                if(op.length() > 1) {
                    txtDisplay.setText("0");
                    txtCalculo.setText("possível só 1 operação.");
                    txtCalculo.setVisibility(TextView.VISIBLE);
                    expressao.clear();
                    expressao.add("0");
                    posicao = 0;
                    return;
                }

                if((valores.size() == 2) && !op.isEmpty()) {
                    total = Calcular(valores.get(0), valores.get(1), op.substring(0,1));
                } else if (!valores.isEmpty() && valores.size() < 2){
                    total = String.valueOf(valores.get(0));
                } else {
                    total = "indefinido 2.0";
                }

                try {
                    total = new java.math.BigDecimal(total).toPlainString();
                } catch (Exception e) {
                    System.out.println(e);
                }

                //total = new java.math.BigDecimal(total).toPlainString();

                txtDisplay.setText(total.replace(".", ","));
                //txtCalculo.setText(valores.toString());
                txtCalculo.setText(display);
                txtCalculo.setVisibility(TextView.VISIBLE);
                expressao.clear();
                expressao.add("0");
                //TODO
                //expressao.add(total.replace(".", ",")); // Permite continuar a conta com o resultado
                posicao = 0;
            }

        });

    }

    public void onNumberClick(String number) {
        if (txtCalculo.getVisibility() == View.VISIBLE) limpaTXTCalculo();
        String valorAtual = expressao.get(posicao);

        if (valorAtual.equals("0")) {
            valorAtual = number;
        } else {
            valorAtual += number;
        }

        expressao.set(posicao, valorAtual);
        txtDisplay.setText(String.join("", expressao));
    }

    public void onOperadorClick(String operador) {
        if (txtCalculo.getVisibility() == View.VISIBLE) limpaTXTCalculo();
        String valorAtual = expressao.get(posicao);

        if(operador.equals("-") && valorAtual.equals("-")) {
            valorAtual = "0";
            expressao.set(posicao, valorAtual);
            txtDisplay.setText(String.join("", expressao));
            return;
        }
        if(operador.equals("-") && valorAtual.equals("0") && posicao == 0){
            valorAtual = "-";
            expressao.set(posicao, valorAtual);
            txtDisplay.setText(String.join("", expressao));
            return;
        }

        if (valorAtual.isEmpty() && posicao > 0 ) {
            String valorAnterior = expressao.get(posicao - 1);
            if (valorAnterior.matches(".*[+\\-/*]$")) {
                // Troca o último caractere do anterior pelo novo operador
                valorAnterior = valorAnterior.substring(0, valorAnterior.length() - 1) + operador;
                expressao.set(posicao - 1, valorAnterior);
                txtDisplay.setText(String.join("", expressao));
                return;
            }
            if (operador.equals("%")){
                // agrega %
                valorAnterior = valorAnterior + operador;
                expressao.set(posicao - 1, valorAnterior);
                //expressao.remove(posicao);
                txtDisplay.setText(String.join("", expressao));
                return;
            }
        }

        valorAtual += operador;
        expressao.set(posicao, valorAtual);

        //if (!operador.equals("%")) {
            posicao++;
            expressao.add("");
       // }

        txtDisplay.setText(String.join("", expressao));

    }

    public void onVirgulaClick() {
        if (txtCalculo.getVisibility() == View.VISIBLE) limpaTXTCalculo();
        String valorAtual = expressao.get(posicao);

        if (valorAtual.isEmpty() || valorAtual.matches(".*[+\\-/*%]$") || valorAtual.contains(",")) {
            return;
        } else {
            valorAtual += ",";
        }
        expressao.set(posicao, valorAtual);
        txtDisplay.setText(String.join("", expressao));
    }

    public void onSinalClick() {
        if (txtCalculo.getVisibility() == View.VISIBLE) limpaTXTCalculo();
        String valorAtual = expressao.get(posicao);

        if (valorAtual.equals("0") || valorAtual.endsWith(",") || valorAtual.isEmpty()) return;

        if (valorAtual.startsWith("(-") && valorAtual.endsWith(")")) {
            valorAtual = valorAtual.replaceFirst("\\(-", "");
            valorAtual = valorAtual.replaceFirst("\\)$", "");
        } else {
            valorAtual = "(-" + valorAtual + ")";
        }
        if (valorAtual.isEmpty() && posicao == 0) valorAtual = "0";

        expressao.set(posicao, valorAtual);
        txtDisplay.setText(String.join("", expressao));

    }

    public void onDeleteClick() {
        if (txtCalculo.getVisibility() == View.VISIBLE) limpaTXTCalculo();
        String valorAtual = expressao.get(posicao);

        if ((valorAtual.length() == 1) && (posicao == 0)) {
            valorAtual = "0";
            expressao.set(posicao, valorAtual);
            txtDisplay.setText(String.join("", expressao));
            return;
        }

        if (valorAtual.endsWith(")")) {
            valorAtual = valorAtual.replaceFirst("\\(-", "");
            valorAtual = valorAtual.replaceFirst("\\)$", "");
            expressao.set(posicao, valorAtual);
            txtDisplay.setText(String.join("", expressao));
            return;
        }

        if (!valorAtual.isEmpty()) {
            valorAtual = valorAtual.substring(0, valorAtual.length() - 1);
        }
        if (valorAtual.isEmpty() && posicao > 0) {
            expressao.remove(posicao);
            posicao--;
            valorAtual = expressao.get(posicao);
            valorAtual = valorAtual.substring(0, valorAtual.length() - 1);
        }
        expressao.set(posicao, valorAtual);
        txtDisplay.setText(String.join("", expressao));
    }

    public void onClearClick() {
        if (txtCalculo.getVisibility() == View.VISIBLE) limpaTXTCalculo();
        txtDisplay.setText("0");
        expressao.clear();
        expressao.add(txtDisplay.getText().toString().trim());
        posicao = 0;
    }

    public void limpaTXTCalculo(){
        txtCalculo.setText("");
        txtCalculo.setVisibility(TextView.INVISIBLE);
    }

    public String Calcular(double num1, double num2, String op){

        String total = "";

        switch (op) {
            case "+":
                total = String.valueOf(num1 + num2);
                break;
            case "-":
                total = String.valueOf(num1 - num2);
                break;
            case "*":
                total = String.valueOf(num1 * num2);
                break;
            case "/":
                if (num2 == 0) {
                    txtCalculo.setText("Não é possível dividir por zero.");
                    txtCalculo.setVisibility(TextView.VISIBLE);
                    total = "Indeterminado";
                    break;
                }
                total = String.valueOf(num1 / num2);
                break;
            case "%":
                total = String.valueOf(num1 % num2);
                break;
            default:
                txtCalculo.setText("Operação inválida");
                txtCalculo.setVisibility(TextView.VISIBLE);
                total = "indefinido";
                break;
        }

        return total;

    }

    private void UpdateMemo() {
        calcMemo.setDisplay(txtDisplay.getText().toString());
        calcMemo.setCalculo(txtCalculo.getText().toString());
        boolean sucesso = calcMemoDAO.updateCalcMemo(calcMemo);
        if (!sucesso) {
            //System.out.println("Não foi possível atualizar o histórico do usuário!");
            Log.i("Update CalcMemo", "Não foi possível atualizar o histórico do usuário!");
            Toast.makeText(this, "Não foi possível atualizar o histórico do usuário!", Toast.LENGTH_SHORT).show();
        }
        Log.i("Update CalcMemo", "Atualizado o histórico do usuário!");
        finish();
    }

}
