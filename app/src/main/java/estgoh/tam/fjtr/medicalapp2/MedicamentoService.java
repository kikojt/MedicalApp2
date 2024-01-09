package estgoh.tam.fjtr.medicalapp2;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface MedicamentoService {

    @POST("/login")
    Call<LoginResponse> login(@Body Utilizador utilizador);

    @POST("/registar_utilizador")
    Call<ResponseCode> registar(@Body Utilizador utilizador);

    @PUT("/logout")
    Call<ResponseCode> logout(@Body Utilizador utilizador);

    @POST("/adicionar_medicamneto")
    //Call<AdicionaResponse> adicionaMedicamento(@Body Map<String, Object> request);

    @GET("/listar_medicamentos")
    Call<ArrayList<Medicamento>> listarMedicamentos(@Body Utilizador utilizador);
}
