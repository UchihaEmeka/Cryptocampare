package nnaemeka.nwakaudu.cryptocampare.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import nnaemeka.nwakaudu.cryptocampare.interfaces.CurrencyExchange;
import nnaemeka.nwakaudu.cryptocampare.R;
import nnaemeka.nwakaudu.cryptocampare.adapter.BTCAdapter;
import nnaemeka.nwakaudu.cryptocampare.interfaces.ExchangeService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/* Fragment used as page 1 */
public class BTCFragment extends Fragment implements Callback<CurrencyExchange> {
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private Button refreshButton;

    String fsym = "BTC";
    String tsyms = "NGN" + "," + "USD" +
            "," + "EUR" + "," + "JPY" + "," +
            "GBP" + "," + "AUD" + "," + "CAD" + "," +
            "CHF" + "," + "SEK" + "," + "NZD" + "," +
            "MXN" + "," + "SGD" + "," + "HKD" + "," +
            "NOK" + "," + "KRW" + "," + "TRY" + "," +
            "RUB" + "," + "INR" + "," + "BRL" + "," + "ZAR";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page1, null);
        recyclerView =  view.findViewById(R.id.recyclerview);
        progressBar =  view.findViewById(R.id.progress_bar);
        refreshButton = view.findViewById(R.id.refresh_button);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshButton.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                loadExchangeData();
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        progressBar.setVisibility(View.VISIBLE);
        loadExchangeData();
    }
    private void loadExchangeData(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://min-api.cryptocompare.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ExchangeService service = retrofit.create(ExchangeService.class);
        Call<CurrencyExchange> call = service.loadExchange(fsym,tsyms);
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<CurrencyExchange> call, Response<CurrencyExchange> response) {
        progressBar.setVisibility(View.INVISIBLE);
        CurrencyExchange currencyExchange = response.body();
        recyclerView.setAdapter(new BTCAdapter(recyclerView.getContext(), currencyExchange.getCurrencyList()));
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setHasFixedSize(true);
    }
    @Override
    public void onFailure(Call<CurrencyExchange> call, Throwable t) {
        progressBar.setVisibility(View.INVISIBLE);
        refreshButton.setVisibility(View.VISIBLE);
        Toast.makeText(recyclerView.getContext(), "Poor or no internet connection", Toast.LENGTH_SHORT).show();
    }


}

