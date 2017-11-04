package nnaemeka.nwakaudu.cryptocampare.interfaces;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by official on 10/22/17.
 */

public interface ExchangeService {
    @GET("data/price?")
    Call<CurrencyExchange> loadExchange(@Query("fsym") String coins, @Query("tsyms") String currency);
}


