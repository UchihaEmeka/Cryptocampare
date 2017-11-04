package nnaemeka.nwakaudu.cryptocampare;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.text.DecimalFormat;
import nnaemeka.nwakaudu.cryptocampare.adapter.BTCAdapter;
import nnaemeka.nwakaudu.cryptocampare.interfaces.CurrencyExchange;
import nnaemeka.nwakaudu.cryptocampare.interfaces.ExchangeService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import static android.view.View.INVISIBLE;


public class Conversion extends Activity implements Callback<CurrencyExchange> {
    public static final String EXTRA_POSITION = "position";
    Context context;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private Button refreshButton;
    double rate;
   private String fsy="ETH";
    String tsyms = "NGN" + "," + "USD" +
            "," + "EUR" + "," + "JPY" + "," +
            "GBP" + "," + "AUD" + "," + "CAD" + "," +
            "CHF" + "," + "SEK" + "," + "NZD" + "," +
            "MXN" + "," + "SGD" + "," + "HKD" + "," +
            "NOK" + "," + "KRW" + "," + "TRY" + "," +
            "RUB" + "," + "INR" + "," + "BRL" + "," + "ZAR";
    TextView rateTextView,resultsTextView;
    EditText currencyInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ethconversion);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview1);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar1);
        refreshButton = (Button) findViewById(R.id.refresh_button1);
        loadExchangeData(fsy);
        Intent myIntent = getIntent();
        rateTextView = (TextView) findViewById(R.id.rate);
        resultsTextView = (TextView) findViewById(R.id.converted);
        currencyInput = (EditText) findViewById(R.id.currencyInput);
        rate = myIntent.getDoubleExtra("currency_rate", 0);
        rateTextView.setText(String.valueOf("Rate 1: " + rate));

        int postion = getIntent().getIntExtra(EXTRA_POSITION, 0);
        Resources resources = getResources();

        String[] currencyNames = resources.getStringArray(R.array.currencyNames);
        TypedArray countryPictures = resources.obtainTypedArray(R.array.country_flags);
        ImageView secondImage = findViewById(R.id.flag_image);
        TextView currencyName =(TextView) findViewById(R.id.currency_name);
        secondImage.setImageDrawable(countryPictures.getDrawable(postion % countryPictures.length()));
        currencyName.setText(currencyNames[postion % currencyNames.length]);
        countryPictures.recycle();

        currencyInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (currencyInput.getText().toString().length() == 0 || currencyInput.getText().toString().isEmpty()) {
                    resultsTextView.setText("");
                } else
                    calculate();}

            @Override
            public void afterTextChanged(Editable s) {}
        });}
    @Override
    public void onStart() {
        super.onStart();
        }


    private void loadExchangeData(String fsym){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://min-api.cryptocompare.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ExchangeService service = retrofit.create(ExchangeService.class);
      Call<CurrencyExchange> call = service.loadExchange(fsym,tsyms);
      call.enqueue(this);}

   @Override
    public void onResponse(Call<CurrencyExchange> call, Response<CurrencyExchange> response) {
        progressBar.setVisibility(INVISIBLE);
        CurrencyExchange currencyExchange = response.body();
        recyclerView.setAdapter(new BTCAdapter(recyclerView.getContext(), currencyExchange.getCurrencyList()));
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setHasFixedSize(true);
    }
    @Override
    public void onFailure(Call<CurrencyExchange> call, Throwable t) {
        progressBar.setVisibility(INVISIBLE);
      //  refreshButton.setVisibility(View.VISIBLE);
        Toast.makeText(recyclerView.getContext(), "Poor or no internet connection", Toast.LENGTH_SHORT).show();
    }


    private  void calculate(){
        if(currencyInput.getText().toString().length() == 0){
            return;
        }

        double input;
        input = Double.parseDouble(currencyInput.getText().toString());

        double output;
        output = input * rate;

        DecimalFormat decimalFormat = new DecimalFormat("#,###,###");
        resultsTextView.setText(decimalFormat.format(output));
    }
}

